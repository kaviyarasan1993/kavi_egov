package org.egov.works.workorder.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.works.workorder.domain.service.LetterOfAcceptanceService;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T13:18:24.260Z")

@Controller
public class LetterofacceptancesApiController implements LetterofacceptancesApi {

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResponseEntity<LetterOfAcceptanceResponse> letterofacceptancesCreatePost(
            @ApiParam(value = "Details of new Letter Of Acceptance(s) + RequestInfo meta data.", required = true) @Valid @RequestBody LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            @RequestParam(required = false) Boolean isRevision) {
        LetterOfAcceptanceResponse letterOfAcceptanceResponse = letterOfAcceptanceService
                .create(letterOfAcceptanceRequest, isRevision);
        return new ResponseEntity(letterOfAcceptanceResponse, HttpStatus.OK);
    }

    public ResponseEntity<LetterOfAcceptanceResponse> letterofacceptancesSearchPost(
            @NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
            @ApiParam(value = "Parameter to carry Request metadata in the request body") @RequestBody RequestInfo requestInfo,
            @Min(0) @Max(100) @ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @Size(max = 50) @ApiParam(value = "Comma separated list of LOA Number to get Letter Of Acceptance(s)") @RequestParam(value = "loaNumbers", required = false) List<String> loaNumbers,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Ids of Letter Of Acceptance to get the Letter Of Acceptance(s)") @RequestParam(value = "ids", required = false) List<String> ids,
            @Size(max = 50) @ApiParam(value = "Comma seperated list of Detailed Estimate Numbers to get the Letter Of Acceptance(s)") @RequestParam(value = "detailedEstimateNumbers", required = false) List<String> detailedEstimateNumbers,
            @ApiParam(value = "The file number for a Letter Of Acceptance") @RequestParam(value = "fileNumber", required = false) String fileNumber,
            @ApiParam(value = "Epoch time for Letter Of Acceptance when it is created in the system") @RequestParam(value = "fromDate", required = false) Long fromDate,
            @ApiParam(value = "Epoch time for Letter Of Acceptance when it is created in the system") @RequestParam(value = "toDate", required = false) Long toDate,
            @Size(max = 50) @ApiParam(value = "Comma separated list of the Department for which Letter Of Acceptance belongs to.") @RequestParam(value = "department", required = false) List<String> department,
            @Size(max = 50) @ApiParam(value = "Comma separated list of the LOA Status") @RequestParam(value = "statuses", required = false) List<String> statuses,
            @Size(max = 50) @ApiParam(value = "Comma separated list of Names of the contractor to which Letter Of Acceptance belongs to.") @RequestParam(value = "contractorNames", required = false) List<String> contractorNames,
            @Size(max = 50) @ApiParam(value = "Comma separated list of codes of the contractor to which Letter Of Acceptance belongs to.") @RequestParam(value = "contractorCodes", required = false) List<String> contractorCodes) {

        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria = LetterOfAcceptanceSearchContract.builder()
                .tenantId(tenantId).contractorCodes(contractorCodes).contractorNames(contractorNames)
                .pageNumber(pageNumber).pageSize(pageSize).department(department).detailedEstimateNumbers(detailedEstimateNumbers)
                .fileNumber(fileNumber).fromDate(fromDate).ids(ids).loaNumbers(loaNumbers).fromDate(fromDate).toDate(toDate)
                .build();
        LetterOfAcceptanceResponse letterOfAcceptanceResponse = letterOfAcceptanceService.search(letterOfAcceptanceSearchCriteria,
                requestInfo);
        return new ResponseEntity<>(letterOfAcceptanceResponse, HttpStatus.OK);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResponseEntity<LetterOfAcceptanceResponse> letterofacceptancesUpdatePost(
            @ApiParam(value = "Details of Letter Of Acceptance(s) + RequestInfo meta data.", required = true) @Valid @RequestBody LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            @RequestParam(required = false) Boolean isRevision) {
        LetterOfAcceptanceResponse letterOfAcceptanceResponse = letterOfAcceptanceService
                .update(letterOfAcceptanceRequest, isRevision);
        return new ResponseEntity(letterOfAcceptanceResponse, HttpStatus.OK);
    }

}
