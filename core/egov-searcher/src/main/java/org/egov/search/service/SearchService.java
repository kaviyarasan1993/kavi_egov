package org.egov.search.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.SearchApplicationRunnerImpl;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.search.model.Definition;
import org.egov.search.model.ResponseInfoFactory;
import org.egov.search.model.SearchDefinition;
import org.egov.search.model.SearchRequest;
import org.egov.search.repository.SearchRepository;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Service
public class SearchService {

	@Autowired
	private SearchRepository searchRepository;
	
	@Autowired
	private SearchApplicationRunnerImpl runner;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	public static final Logger logger = LoggerFactory.getLogger(SearchService.class);


	public Object searchData(SearchRequest searchRequest, String moduleName, String searchName) {
		Map<String, SearchDefinition> searchDefinitionMap = runner.getSearchDefinitionMap();
		Definition searchDefinition = null;
		try{
			searchDefinition = getSearchDefinition(searchDefinitionMap, moduleName, searchName);
		}catch(CustomException e){
			throw e;
		}
		logger.info("Definition being used for process: "+searchDefinition);
		List<String> maps = new ArrayList<>();
		try{
			maps = searchRepository.searchData(searchRequest, searchDefinition);
		}catch(CustomException e){
			throw e;
		}catch(Exception e){
			logger.error("Exception: ",e);
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), 
					"There was an error encountered at the Db");
		}
		Object data = null;
		try{
			data = formatResult(maps, searchDefinition, searchRequest);
		}catch(Exception e){
			logger.error("Exception: ",e);
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There was an error encountered while formatting the result, Verify output config from the yaml file.");
		}
		
		return data;
	}
	
	private Definition getSearchDefinition(Map<String, SearchDefinition> searchDefinitionMap,
			String moduleName, String searchName){
		logger.info("Fetching Definitions for module: "+moduleName+" and search feature: "+searchName);
		List<Definition> definitions = null;
		try{
			definitions = searchDefinitionMap.get(moduleName).getDefinitions().parallelStream()
											.filter(def -> (def.getName().equals(searchName)))
		                                 .collect(Collectors.toList());
		}catch(Exception e){
			logger.error("There's no Search Definition provided for this search feature");
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Search Definition provided for this search feature");
		}
		if(0 == definitions.size()){
			logger.error("There's no Search Definition provided for this search feature");
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Search Definition provided for this search feature");
		}
		return definitions.get(0);
		
	}
	
	private String formatResult(List<String> maps, Definition searchDefinition, SearchRequest searchRequest){
		String result = null;
	    Type type = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
		Gson gson = new Gson();
		List<Map<String, Object>> data = gson.fromJson(maps.toString(), type);
		
    	DocumentContext documentContext = JsonPath.parse(searchDefinition.getOutput().getJsonFormat());
		String[] expressionArray = (searchDefinition.getOutput().getOutJsonPath()).split("[.]");
		StringBuilder expression = new StringBuilder();
		for(int i = 0; i < (expressionArray.length - 1) ; i++ ){
			expression.append(expressionArray[i]);
			if(i != expressionArray.length - 2)
				expression.append(".");
		}
		documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1], data);
		
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(searchRequest.getRequestInfo(), true);
		String[] resInfoExpArray = (searchDefinition.getOutput().getResponseInfoPath()).split("[.]");
		StringBuilder resInfoExp = new StringBuilder();
		for(int i = 0; i < (resInfoExpArray.length - 1) ; i++ ){
			resInfoExp.append(resInfoExpArray[i]);
			if(i != resInfoExpArray.length - 2)
				resInfoExp.append(".");
		}
		documentContext.put(resInfoExp.toString(), resInfoExpArray[resInfoExpArray.length - 1], responseInfo);
		
		result = documentContext.jsonString().toString();
		logger.info("Final Result: "+result);
		return result;
		
	}
}
