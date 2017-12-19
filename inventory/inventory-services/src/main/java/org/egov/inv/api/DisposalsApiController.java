package org.egov.inv.api;

import org.egov.inv.model.DisposalRequest;
import org.egov.inv.model.DisposalResponse;
import org.egov.inv.model.ErrorRes;
import org.egov.inv.model.RequestInfo;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

@Controller
public class DisposalsApiController implements DisposalsApi {



    public ResponseEntity<DisposalResponse> disposalsCreatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Create  new"  )  @Valid @RequestBody DisposalRequest disposalRequest) {
        // do some magic!
        return new ResponseEntity<DisposalResponse>(HttpStatus.OK);
    }

    public ResponseEntity<DisposalResponse> disposalsSearchPost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo,
         @Size(max=50)@ApiParam(value = "comma seperated list of Ids") @RequestParam(value = "ids", required = false) List<String> ids,
        @ApiParam(value = "store of the Disposal ") @RequestParam(value = "store", required = false) Long store,
        @ApiParam(value = "disposal number of the Disposal ") @RequestParam(value = "disposalNumber", required = false) String disposalNumber,
        @ApiParam(value = "disposal date of the Disposal ") @RequestParam(value = "disposalDate", required = false) Long disposalDate,
        @ApiParam(value = "hand over to of the Disposal ") @RequestParam(value = "handOverTo", required = false) String handOverTo,
        @ApiParam(value = "auction number of the Disposal ") @RequestParam(value = "auctionNumber", required = false) String auctionNumber,
        @ApiParam(value = "disposal status of the Disposal ", allowableValues = "CREATED, APPROVED, REJECTED, CANCELED") @RequestParam(value = "disposalStatus", required = false) String disposalStatus,
        @ApiParam(value = "state id of the Disposal ") @RequestParam(value = "stateId", required = false) String stateId,
        @ApiParam(value = "totalDisposalValue  denormalized value from Disposal Details ") @RequestParam(value = "totalDisposalValue", required = false) Double totalDisposalValue,
         @Min(0) @Max(100)@ApiParam(value = "Number of records returned.", defaultValue = "20") @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
        @ApiParam(value = "Page number", defaultValue = "1") @RequestParam(value = "pageNumber", required = false, defaultValue="1") Integer pageNumber,
        @ApiParam(value = "This takes any field from the Object seperated by comma and asc,desc keywords. example name asc,code desc or name,code or name,code desc", defaultValue = "id") @RequestParam(value = "sortBy", required = false, defaultValue="id") String sortBy) {
        // do some magic!
        return new ResponseEntity<DisposalResponse>(HttpStatus.OK);
    }

    public ResponseEntity<DisposalResponse> disposalsUpdatePost( @NotNull@ApiParam(value = "Unique id for a tenant.", required = true) @RequestParam(value = "tenantId", required = true) String tenantId,
        @ApiParam(value = "common Request info"  )  @Valid @RequestBody DisposalRequest disposalRequest) {
        // do some magic!
        return new ResponseEntity<DisposalResponse>(HttpStatus.OK);
    }

}
