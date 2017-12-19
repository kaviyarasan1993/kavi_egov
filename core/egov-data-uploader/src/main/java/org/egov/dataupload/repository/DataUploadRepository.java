package org.egov.dataupload.repository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;


@Repository
public class DataUploadRepository {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${write.file.path}")
	private String writeFilePath;
	
	@Value("${filestore.post.endpoint}")
	private String postFilePath;
	
	@Value("${filestore.host}")
	private String fileStoreHost;
			
	public static final Logger LOGGER = LoggerFactory.getLogger(DataUploadRepository.class);
		
	public Object doApiCall(Object request, String url) {
		Object response = null;
		try{
			response = restTemplate.postForObject(url, request, Map.class);
		}catch(Exception e){
			LOGGER.error("Exception while hitting url: "+url);
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), 
					"Exception while hitting url: "+url);
		}
		return response;
	}
	
	public String getFileContents(String filePath) throws Exception{
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ByteArrayHttpMessageConverter());
		RestTemplate restTemplate = new RestTemplate(messageConverters);
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

	    HttpEntity<String> entity = new HttpEntity<String>(headers);
	    try{
		    ResponseEntity<byte[]> response = restTemplate.exchange(
		    		filePath, HttpMethod.GET, entity, byte[].class, "1");
	
		    if (response.getStatusCode() == HttpStatus.OK) {
		        Files.write(Paths.get(writeFilePath), response.getBody());
		    }
	    }catch(Exception e){
			LOGGER.error("Exception while fetching file from: "+filePath, e);
			throw new CustomException("400", "Exception while fetching file from");
	    }
	    
	    return writeFilePath;
	}
	
	public Map<String, Object> postFileContents(String tenantId, String moduleName, String filePath) throws Exception{
		StringBuilder uri = new StringBuilder();
		Map<String, Object> result = new HashMap<>();
		uri.append(fileStoreHost).append(postFilePath)
		   .append("?tenantId="+tenantId).append("&module="+moduleName);
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new FileSystemResource(filePath));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new  
				HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);
		LOGGER.info("URI: "+uri.toString());
		try{
			ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.POST, requestEntity,
			                    Map.class);
			result = resultMap.getBody();
		}catch(Exception e){
			LOGGER.error("Couldn't post the response excel: "+filePath, e);
		}
		LOGGER.info("POST FILE response: "+result);
		
		return result;
	}

}