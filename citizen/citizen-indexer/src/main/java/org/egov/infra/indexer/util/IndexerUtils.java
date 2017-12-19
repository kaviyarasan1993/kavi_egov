package org.egov.infra.indexer.util;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.consumer.KafkaConsumerConfig;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.infra.indexer.web.contract.UriMapping;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class IndexerUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(IndexerUtils.class);

	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private KafkaConsumerConfig kafkaConsumerConfig;
	
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;
	
	@Value("${elasticsearch.poll.interval.seconds}")
	private String pollInterval;
	
	@Autowired
	private BulkIndexer bulkIndexer;
	
    private final ScheduledExecutorService scheduler =
    	       Executors.newScheduledThreadPool(1);
	
	
	public void orchestrateListenerOnESHealth(){
		kafkaConsumerConfig.pauseContainer();
		logger.info("Polling ES....");
        final Runnable esPoller = new Runnable() {
    		boolean threadRun = true;
                public void run() {
                	if(threadRun){
        	        Object response = null;
        			try{
        				StringBuilder url = new StringBuilder();
        				url.append(esHostUrl)
        					.append("/_search");
        				response = restTemplate.getForObject(url.toString(), Map.class);
        			}catch(Exception e){
        				logger.error("ES is DOWN..");
        			}
        			if(response != null){
        				logger.info("ES is UP!");
        				kafkaConsumerConfig.startContainer();
        				threadRun = false;
        			}
                  }
                }
            };
         scheduler.scheduleAtFixedRate(esPoller, 0, Long.valueOf(pollInterval), TimeUnit.SECONDS);
	}
	
	public String pullArrayOutOfString(String jsonString){
		String[] array = jsonString.split(":");
		StringBuilder jsonArray = new StringBuilder(); 
		for(int i = 1; i < array.length ; i++ ){
			jsonArray.append(array[i]);
			if(i != array.length - 1)
				jsonArray.append(":");
		}
		jsonArray.deleteCharAt(jsonArray.length() - 1);
		logger.info("string for jsonArray: "+jsonArray.toString());
		
		return jsonArray.toString();
	}
	
	public String buildString(Object object){
		//JsonPath cannot be applied on the type JSONObject. String has to be built of it and then used.
		String[] array = object.toString().split(":");
		StringBuilder jsonArray = new StringBuilder(); 
		for(int i = 0; i < array.length ; i++ ){
			jsonArray.append(array[i]);
			if(i != array.length - 1)
				jsonArray.append(":");
		}
		logger.info("string constructed from JSONObject: "+jsonArray.toString());
		return jsonArray.toString();		
	}
	
	public String buildUri(UriMapping uriMapping, String kafkaJson){
		StringBuilder serviceCallUri = new StringBuilder();	
		String uriWithPathParam = null;
		if(null != uriMapping.getPath()){
			uriWithPathParam = uriMapping.getPath();
			uriWithPathParam = uriWithPathParam.replace("$", 
					JsonPath.read(kafkaJson, uriMapping.getPathParam()).toString());
		}else if(null != uriMapping.getQueryParam()){
			String[] queryParamsArray = uriMapping.getQueryParam().split(",");
			if(queryParamsArray.length == 0){
				queryParamsArray[0] = uriMapping.getQueryParam();
			}
			for(int i = 0; i < queryParamsArray.length; i++){
				String[] queryParamExpression = queryParamsArray[i].split("=");
				logger.info("queryparam: "+queryParamExpression[1]);
				String queryParam = JsonPath.read(kafkaJson, queryParamExpression[1]);
				queryParamExpression[1] = queryParam;
				StringBuilder resolvedParam = new StringBuilder();
				resolvedParam.append(queryParamExpression[0]).append("=").append(queryParamExpression[1]);
				queryParamsArray[i] = resolvedParam.toString();
			}
			StringBuilder queryParams = new StringBuilder();
			if(queryParamsArray.length >  1){
				for(int i = 0; i < queryParamsArray.length; i++){
					queryParams.append(queryParamsArray[i]);
					if(i != queryParamsArray.length - 1)
						queryParams.append("&");
				}
	
			}else{
				queryParams.append(queryParamsArray[0]);
			}
			serviceCallUri.append(uriWithPathParam).append("?").append(queryParams.toString());
			logger.info("uri prepared for inter service call: "+serviceCallUri.toString());
		}else{
			serviceCallUri.append(uriMapping.getPath());
			logger.info("The uri has no path params or query params, using the direct path: "+serviceCallUri.toString());
		}
		return serviceCallUri.toString();
	}
	
	public String buildIndexId(Index index, String stringifiedObject){
		String[] idFormat = index.getId().split("[,]");
		StringBuilder id = new StringBuilder();
		try{
			if(0 == idFormat.length){
				id.append(JsonPath.read(stringifiedObject, index.getId()).toString());
			}else{
				for(int j = 0; j < idFormat.length; j++){
					logger.info("path: "+idFormat[j]);
					id.append(JsonPath.read(stringifiedObject, idFormat[j]).toString());
				} 
			}
		}catch(Exception e){
			logger.error("No id found at the given jsonpath: ", e);
			throw e;
		}
		return id.toString();
	}
	
	public JSONArray validateAndConstructJsonArray(String kafkaJson, Index index, boolean isBulk) throws Exception{
        String jsonArray = null;
        JSONArray kafkaJsonArray = null;
        ObjectMapper mapper = new ObjectMapper();
        try{
	    	if(isBulk){
	    		//Validating if the request is a valid json array.
				jsonArray = pullArrayOutOfString(kafkaJson);   
				if(null != index.getJsonPath()){
		    		if(JsonPath.read(kafkaJson, index.getJsonPath()) instanceof net.minidev.json.JSONArray){
		    			String inputArray = mapper.writeValueAsString(JsonPath.read(kafkaJson, index.getJsonPath()));
		    			kafkaJsonArray = new JSONArray(inputArray);
		    		}
	    		}else if((jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
	    			kafkaJsonArray = new JSONArray(jsonArray);
		        }else{
					logger.info("Invalid request for a json array!");
					return null;
		        }
	        }else{
	        	if(null != index.getJsonPath()){
	        		kafkaJson = mapper.writeValueAsString(JsonPath.read(kafkaJson, index.getJsonPath()));
		        	jsonArray = "[" + kafkaJson + "]";
	        	}else{
		        	jsonArray = "[" + kafkaJson + "]";
		        	logger.info("constructed json array out of input json object: "+jsonArray);
	        	}
				kafkaJsonArray = new JSONArray(jsonArray);
	        }
        }catch(Exception e){
        	logger.error("Exception while constructing json array for bulk index: ", e);
        	throw e;
        }
    	
    	return kafkaJsonArray;
	}
	
	public void validateAndIndex(String finalJson, String url, Index index) throws Exception{
		if(null == finalJson){
			logger.info("Indexing will not be done, please modify the data and retry.");
		    logger.info("Advice: Looks like isBulk = true in the config yaml but the record sent on the queue is a json object and not an array of objects. In that case, change either of them.");
		}else{
			doIndexing(finalJson, url.toString(), index);
		}
	}
	
	public void doIndexing(String finalJson, String url, Index index) throws Exception{
		if(finalJson.startsWith("{ \"index\""))
			bulkIndexer.indexJsonOntoES(url.toString(), finalJson);
		else{
			indexWithESId(index, finalJson);
		}
	}
	
	public void indexWithESId(Index index, String finalJson) throws Exception{
		logger.info("Non bulk indexing...");
		StringBuilder urlForNonBulk = new StringBuilder();
		urlForNonBulk.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/").append("_index");
		bulkIndexer.indexJsonOntoES(urlForNonBulk.toString(), finalJson);
	}

}
