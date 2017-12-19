package org.egov.asset.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.TestConfiguration;
import org.egov.asset.contract.AssetStatusResponse;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AssetStatusCriteria;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.StatusValue;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.Status;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetMasterService;
import org.egov.asset.util.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetMasterController.class)
@Import(TestConfiguration.class)
public class AssetMasterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetMasterService assetMasterService;

    @MockBean
    private AssetCommonService assetCommonService;

    @Test
    public void test_Should_Return_Status() throws Exception {
        final List<AssetStatus> assetStatus = new ArrayList<>();
        assetStatus.add(getAssetStatus());

        final AssetStatusResponse assetStatusResponse = new AssetStatusResponse();
        assetStatusResponse.setAssetStatus(assetStatus);
        assetStatusResponse.setResponseInfo(new ResponseInfo());

        when(assetMasterService.search(Matchers.any(AssetStatusCriteria.class), Matchers.any(RequestInfo.class)))
                .thenReturn(assetStatusResponse);

        mockMvc.perform(post("/assetstatuses/_search").param("objectName", AssetStatusObjectName.ASSETMASTER.toString())
                .param("code", Status.CAPITALIZED.toString()).param("tenantId", "default")
                .contentType(MediaType.APPLICATION_JSON).content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("status.json")));
    }

    @Test
    public void test_Should_Return_AssetCategoryType() throws Exception {
        mockMvc.perform(post("/GET_ASSET_CATEGORY_TYPE").param("tenantId", "default")
                .contentType(MediaType.APPLICATION_JSON).content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("assetcategorytype.json")));

    }

    @Test
    public void test_Should_Return_DepreciationMethod() throws Exception {
        mockMvc.perform(post("/GET_DEPRECIATION_METHOD").param("tenantId", "default")
                .contentType(MediaType.APPLICATION_JSON).content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("depreciationmethod.json")));

    }

    @Test
    public void test_Should_Return_ModeOfAcquisition() throws Exception {
        mockMvc.perform(post("/GET_MODE_OF_ACQUISITION").param("tenantId", "default")
                .contentType(MediaType.APPLICATION_JSON).content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("modeofacquisition.json")));

    }

    @Test
    public void test_error_assetStatusSearch() throws IOException, Exception {
        final ErrorResponse errorResponse = getErrorResponse();

        when(assetCommonService.populateErrors(any(BindingResult.class))).thenReturn(errorResponse);

        mockMvc.perform(post("/assetstatuses/_search").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("requestinfowrapper.json"))).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("errorresponse.json")));
    }

    private ErrorResponse getErrorResponse() {
        final ErrorResponse errorResponse = new ErrorResponse();
        final org.egov.asset.exception.Error error = new org.egov.asset.exception.Error();
        error.setCode(400);
        error.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.setDescription(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResponseInfo(new ResponseInfo());
        errorResponse.setError(error);
        return errorResponse;
    }

    private AssetStatus getAssetStatus() {
        final AssetStatus assetStatus = new AssetStatus();
        assetStatus.setObjectName(AssetStatusObjectName.ASSETMASTER.toString());

        final List<StatusValue> statusValues = new ArrayList<>();

        final StatusValue statusValue = new StatusValue();
        statusValue.setName(Status.CAPITALIZED.toString());
        statusValue.setCode(Status.CAPITALIZED.toString());
        statusValue.setDescription("Asset status is Capitalized");

        statusValues.add(statusValue);

        assetStatus.setStatusValues(statusValues);

        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("1");
        auditDetails.setCreatedDate(Long.valueOf("1499853291695"));
        auditDetails.setLastModifiedBy("1");
        auditDetails.setLastModifiedDate(Long.valueOf("1499853291695"));

        assetStatus.setAuditDetails(auditDetails);

        assetStatus.setTenantId("default");
        return assetStatus;
    }

    private String getFileContents(final String fileName) throws IOException {
        return new FileUtils().getFileContents(fileName);
    }
}