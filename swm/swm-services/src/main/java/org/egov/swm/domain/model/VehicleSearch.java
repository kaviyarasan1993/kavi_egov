package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleSearch extends Vehicle {
    private String regNumbers;
    private String vehicleTypeCode;
    private String vendorName;
    private String purchaseYear;
    private String fuelTypeCode;
    private String driverCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}