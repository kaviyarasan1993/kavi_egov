package org.egov.search.utils;

import org.egov.search.model.Params;
import org.egov.search.model.Query;
import org.egov.search.model.SearchParams;
import org.egov.search.model.SearchRequest;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Component
public class SearchUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(SearchUtils.class);

	public String buildQuery(SearchRequest searchRequest, SearchParams searchParam, Query query){
		StringBuilder queryString = new StringBuilder();
		StringBuilder where = new StringBuilder();
		queryString.append(query.getBaseQuery());
		String whereClause = null;
		try{
			whereClause = buildWhereClause(searchRequest, searchParam);
		}catch(CustomException e){
			throw e;
		}
		if(null == whereClause){
			return whereClause;
		}
		where.append(" where ( ")
		           .append(whereClause.toString()+ " ) ");	
		
		if(null != query.getGroupBy()){
			queryString.append(" group by ")
						.append(query.getGroupBy());
		}
		
		if(null != query.getOrderBy()){
			where.append(" order by ")
						.append(query.getOrderBy().split(",")[0])
						.append(" ")
						.append(query.getOrderBy().split(",")[1]);
		}
		
		if(null != query.getSort()){
			queryString.append(" "+query.getSort());
		}
		
		String finalQuery = queryString.toString().replace("$where", where.toString());
		logger.info("Final Query: "+finalQuery);
		
		return finalQuery;
	}
	
	public 	String buildWhereClause(SearchRequest searchRequest, SearchParams searchParam){
		StringBuilder whereClause = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		if(null != searchParam){
			String condition = searchParam.getCondition();
			for(Params param: searchParam.getParams()){
				Object paramValue = null ;
				try{
					paramValue = JsonPath.read(mapper.writeValueAsString(searchRequest), param.getJsonPath());
				}catch(Exception e){
					logger.error("param: "+param.getName()+" is not provided");
					logger.error("Exception: ",e);
					if(param.getIsMandatory()){
						throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
								"Mandatory param for search not provided. Param: "+param.getName());					
					}else
						continue;
				}
				if(paramValue instanceof net.minidev.json.JSONArray){
					whereClause.append(param.getName())
							   .append(" IN ")
							   .append(paramValue.toString().replace("[", "(")
									   .replace("]", ")")
									   .replace("\"", "\'"));
				}else{
					whereClause.append(param.getName())
					   .append(" = ")
					   .append("'"+paramValue.toString()+"'");
				}
			    whereClause.append(" "+condition+" ");
			}
		}
		Integer index = whereClause.toString().lastIndexOf(searchParam.getCondition());
		String where = whereClause.toString().substring(0, index);

		logger.info("WHERE clause: "+where);
		return where;
	}
	
	
}
