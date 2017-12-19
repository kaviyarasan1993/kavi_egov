package org.egov.works.workorder.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class LOAMeasurementSheetSearchContract {

    private List<String> ids;

    private String tenantId;

    private List<String> loaActivity;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;
}
