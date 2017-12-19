package org.egov.dataupload.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.egov.dataupload.model.Definition;
import org.egov.dataupload.model.UploadDefinition;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;


@Component
public class DataUploadUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadUtils.class);
	
	@Value("${result.file.path}")
	private String resultFilePath;
	
	@SuppressWarnings({ "deprecation", "static-access" })
	public List<List<Object>> readExcelFile(HSSFSheet sheet, List<Object> coloumnHeaders){
        List<List<Object>> excelData = new ArrayList<>(); 
        Iterator<Row> iterator = sheet.iterator();
        while(iterator.hasNext()){
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            List<Object> dataList = new ArrayList<>();
            while(cellIterator.hasNext()){
	            Cell cell = cellIterator.next();
	            if(0 == cell.getRowIndex())
	            	coloumnHeaders.add(cell.getStringCellValue());
	            else{
	            	if(cell.CELL_TYPE_NUMERIC == cell.getCellType())
	            	dataList.add(cell.getNumericCellValue());
	            	if(cell.CELL_TYPE_STRING == cell.getCellType())
	            	dataList.add(cell.getStringCellValue());
	            }
            }
            excelData.add(dataList);
        }
	    logger.info("coloumnHeaders: "+coloumnHeaders);
	    logger.info("excelData: "+excelData);

        
        return excelData;

		
	}
	
	public Definition getUploadDefinition(Map<String, UploadDefinition> searchDefinitionMap,
			String moduleName, String defName){
		logger.info("Fetching Definitions for module: "+moduleName+" and upload feature: "+defName);
		List<Definition> definitions = null;
		try{
			definitions = searchDefinitionMap.get(moduleName).getDefinitions().parallelStream()
											.filter(def -> (def.getDefName().equals(defName)))
		                                 .collect(Collectors.toList());
		}catch(Exception e){
			logger.error("There's no Upload Definition provided for this upload feature", e);
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Upload Definition provided for this upload feature");
		}
		if(0 == definitions.size()){
			logger.error("There's no Upload Definition provided for this upload feature");
			throw new CustomException(HttpStatus.BAD_REQUEST.toString(), 
					"There's no Upload Definition provided for this upload feature");
		}
		logger.info("Definition to be used: "+definitions.get(0));

		return definitions.get(0);
		
	}
	
	public String getJsonPathKey(String jsonPath, StringBuilder expression){
        String[] expressionArray = (jsonPath).split("[.]");
    	for(int j = 0; j < (expressionArray.length - 1) ; j++ ){
    		expression.append(expressionArray[j]);
    		if(j != expressionArray.length - 2)
    			expression.append(".");
    	}
    	return expressionArray[expressionArray.length - 1];
	}
	
	public MultipartFile getExcelFile(String path) throws Exception{
		File file = new File(path);
	    FileInputStream input = new FileInputStream(file);
	    MultipartFile multipartFile = new MockMultipartFile("ReadFile",
	            file.getName(), "text/plain", IOUtils.toByteArray(input));
	    
	    return multipartFile;
	}
	
	
	public void writeToexcelSheet(List<Object> exisitingFields) throws Exception{
		logger.info("Writing to file: "+resultFilePath);
	    MultipartFile file = getExcelFile(resultFilePath);
		HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();
        logger.info("MxRowCount of sheet: "+rowCount);
        Row row = sheet.createRow(++rowCount);
        for(int i = 0; i < exisitingFields.size(); i++)
        {
            Cell cell = row.createCell(i);
            if(exisitingFields.get(i) instanceof String){
            	cell.setCellType(CellType.STRING);
            	cell.setCellValue(exisitingFields.get(i).toString());
            }else if(exisitingFields.get(i) instanceof Double){
            	cell.setCellType(CellType.NUMERIC);
            	cell.setCellValue(Double.parseDouble(exisitingFields.get(i).toString()));
            }
            
        }

        try (FileOutputStream outputStream = new FileOutputStream(resultFilePath)) {
            workbook.write(outputStream);
        }
        
        workbook.close();
	}
	
	public void clearExceFile(String filePath){
		logger.info("Clearing the file....: "+filePath);
		try{
		    MultipartFile file = getExcelFile(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
	        HSSFSheet sheet = workbook.getSheetAt(0);
	        int index = 0;
	        if(sheet != null)   {
	            index = workbook.getSheetIndex(sheet);
	            workbook.removeSheetAt(index);
	        }
	        FileOutputStream output = new FileOutputStream(filePath);
	        workbook.write(output);
	        output.close();
	        workbook.close();
		}catch(Exception e){
			logger.error("Couldn't delete all the contents of file: "+filePath, e);
		}
        
	}
	
	
	public List<Object> getResJsonPathList(Map<String, String> resFieldsMap, List<Object> coloumnHeaders){
		List<Object> jsonpathList = new ArrayList<>();
		for(Entry<String, String> entry: resFieldsMap.entrySet()){
			coloumnHeaders.add(entry.getValue());
			jsonpathList.add(entry.getKey());
		}
		
		return jsonpathList;
		
	}
	
	public void addAdditionalFields(Object response, List<Object> row, List<Object> jsonPathList) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		response = mapper.writeValueAsString(response);
		for(Object path: jsonPathList){
			try{
				Object value = JsonPath.read(response, path.toString());
				row.add(value);
			}catch(Exception e){
				row.add(null);
				
				continue;
			}
		}
	}
	
	public String mockIdGen(String module){
		StringBuilder id = new StringBuilder();
		id.append("DU-").append(module+"-").append(new Date().getTime());
		
		logger.info("JOB CODE: "+id.toString());
		return id.toString();
	}

}


