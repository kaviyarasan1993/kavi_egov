package org.egov.swm.domain.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.domain.repository.RefillingPumpStationRepository;
import org.egov.swm.web.repository.BoundaryRepository;
import org.egov.swm.web.requests.RefillingPumpStationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefillingPumpStationService {

    @Autowired
    private RefillingPumpStationRepository refillingPumpStationRepository;

    @Autowired
    private OilCompanyService oilCompanyService;

    @Autowired
    private FuelTypeService fuelTypeService;

    @Autowired
    private BoundaryRepository boundaryRepository;

    public RefillingPumpStationRequest create(final RefillingPumpStationRequest refillingPumpStationRequest) {

        validate(refillingPumpStationRequest);
        Long userId = null;
        if (refillingPumpStationRequest.getRequestInfo() != null
                && refillingPumpStationRequest.getRequestInfo().getUserInfo() != null
                && null != refillingPumpStationRequest.getRequestInfo().getUserInfo().getId())
            userId = refillingPumpStationRequest.getRequestInfo().getUserInfo().getId();

        for (final RefillingPumpStation refillingPumpStation : refillingPumpStationRequest.getRefillingPumpStations()) {
            setAuditDetails(refillingPumpStation, userId);
            refillingPumpStation.setCode(UUID.randomUUID().toString().replace("-", ""));
        }

        return refillingPumpStationRepository.save(refillingPumpStationRequest);
    }

    public RefillingPumpStationRequest update(final RefillingPumpStationRequest refillingPumpStationRequest) {
        Long userId = null;
        if (refillingPumpStationRequest.getRequestInfo() != null
                && refillingPumpStationRequest.getRequestInfo().getUserInfo() != null
                && null != refillingPumpStationRequest.getRequestInfo().getUserInfo().getId())
            userId = refillingPumpStationRequest.getRequestInfo().getUserInfo().getId();

        for (final RefillingPumpStation refillingPumpStation : refillingPumpStationRequest.getRefillingPumpStations())
            setAuditDetails(refillingPumpStation, userId);

        validateForUniqueCodesInRequest(refillingPumpStationRequest);
        validate(refillingPumpStationRequest);

        refillingPumpStationRepository.update(refillingPumpStationRequest);

        return refillingPumpStationRequest;
    }

    public Pagination<RefillingPumpStation> search(final RefillingPumpStationSearch refillingPumpStationSearch) {

        return refillingPumpStationRepository.search(refillingPumpStationSearch);
    }

    private void validateForUniqueCodesInRequest(final RefillingPumpStationRequest refillingPumpStationRequest) {

        final List<String> codesList = refillingPumpStationRequest.getRefillingPumpStations().stream()
                .map(RefillingPumpStation::getCode).collect(Collectors.toList());

        if (codesList.size() != codesList.stream().distinct().count())
            throw new CustomException("Code", "Duplicate codes in given Refilling Pump Stations:");
    }

    private void validate(final RefillingPumpStationRequest refillingPumpStationRequest) {

        for (final RefillingPumpStation refillingPumpStation : refillingPumpStationRequest.getRefillingPumpStations()) {

            // Validate Fuel Type
            if (refillingPumpStation.getTypeOfFuel() != null && (refillingPumpStation.getTypeOfFuel().getCode() == null
                    || refillingPumpStation.getTypeOfFuel().getCode().isEmpty()))
                throw new CustomException("FuelType", "typeOfFuel code is mandatory: ");

            if (refillingPumpStation.getTypeOfFuel() != null)
                refillingPumpStation.setTypeOfFuel(fuelTypeService.getFuelType(refillingPumpStation.getTenantId(),
                        refillingPumpStation.getTypeOfFuel().getCode(), refillingPumpStationRequest.getRequestInfo()));

            // validate Oil Company
            if (refillingPumpStation.getTypeOfPump() != null && (refillingPumpStation.getTypeOfPump().getCode() == null
                    || refillingPumpStation.getTypeOfPump().getCode().isEmpty()))
                throw new CustomException("OilCompany", "typeOfPump code is mandatory ");

            if (refillingPumpStation.getTypeOfPump() != null)
                refillingPumpStation.setTypeOfPump(oilCompanyService.getOilCompany(refillingPumpStation.getTenantId(),
                        refillingPumpStation.getTypeOfPump().getCode(), refillingPumpStationRequest.getRequestInfo()));

            // Validate Boundary
            if (refillingPumpStation.getLocation() != null && (refillingPumpStation.getLocation().getCode() == null
                    || refillingPumpStation.getLocation().getCode().isEmpty()))
                throw new CustomException("Boundary", "Boundary code is Mandatory");

            if (refillingPumpStation.getLocation() != null && refillingPumpStation.getLocation().getCode() != null) {

                final Boundary boundary = boundaryRepository.fetchBoundaryByCode(refillingPumpStation.getLocation().getCode(),
                        refillingPumpStation.getTenantId());

                if (boundary != null)
                    refillingPumpStation.setLocation(boundary);
                else
                    throw new CustomException("Boundary",
                            "Given Boundary is Invalid: " + refillingPumpStation.getLocation().getCode());
            }

        }

    }

    private void setAuditDetails(final RefillingPumpStation contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getCode() || contract.getCode().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }
}
