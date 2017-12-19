package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.OilCompany;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.domain.service.FuelTypeService;
import org.egov.swm.domain.service.OilCompanyService;
import org.egov.swm.persistence.entity.RefillingPumpStationEntity;
import org.egov.swm.web.repository.BoundaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RefillingPumpStationJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_refillingpumpstation";

    @Autowired
    private OilCompanyService oilCompanyService;

    @Autowired
    private FuelTypeService fuelTypeService;

    @Autowired
    private BoundaryRepository boundaryRepository;

    public Boolean checkForUniqueRecords(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {
        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<RefillingPumpStation> search(final RefillingPumpStationSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), RefillingPumpStationSearch.class);
        }

        String orderBy = "order by name";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getCodes() != null) {
            addAnd(params);
            params.append("code in (:codes)");
            paramValues.put("codes", new ArrayList<>(Arrays.asList(searchRequest.getCodes().split(","))));
        }

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getCode() != null) {
            addAnd(params);
            params.append("code =:code");
            paramValues.put("code", searchRequest.getCode());
        }

        if (searchRequest.getName() != null) {
            addAnd(params);
            params.append("name =:name");
            paramValues.put("name", searchRequest.getName());
        }

        if (searchRequest.getQuantity() != null) {
            addAnd(params);
            params.append("quantity =:quantity");
            paramValues.put("quantity", searchRequest.getQuantity());
        }

        if (searchRequest.getTypeOfFuelCode() != null) {
            addAnd(params);
            params.append("typeoffuel =:typeoffuel");
            paramValues.put("typeoffuel", searchRequest.getTypeOfFuelCode());
        }

        if (searchRequest.getLocationCode() != null) {
            addAnd(params);
            params.append("location =:location");
            paramValues.put("location", searchRequest.getLocationCode());
        }

        if (searchRequest.getTypeOfPumpCode() != null) {
            addAnd(params);
            params.append("typeofpump =:typeofpump");
            paramValues.put("typeofpump", searchRequest.getTypeOfPumpCode());
        }

        Pagination<RefillingPumpStation> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<RefillingPumpStation>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(RefillingPumpStationEntity.class);

        final List<RefillingPumpStation> refillingPumpStationList = new ArrayList<>();

        final List<RefillingPumpStationEntity> refillingPumpStationEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final RefillingPumpStationEntity refillingPumpStationEntity : refillingPumpStationEntities) {

            refillingPumpStationList.add(refillingPumpStationEntity.toDomain());
        }

        if (refillingPumpStationList != null && !refillingPumpStationList.isEmpty()) {
         
            populateBoundarys(refillingPumpStationList);

            populateFuelTypes(refillingPumpStationList);

            populateTypeOfPumps(refillingPumpStationList);

        }
        page.setTotalResults(refillingPumpStationList.size());

        page.setPagedData(refillingPumpStationList);

        return page;
    }

    private void populateFuelTypes(List<RefillingPumpStation> refillingPumpStationList) {
        Map<String, FuelType> fuelTypeMap = new HashMap<>();
        String tenantId = null;

        if (refillingPumpStationList != null && !refillingPumpStationList.isEmpty())
            tenantId = refillingPumpStationList.get(0).getTenantId();

        List<FuelType> fuelTypes = fuelTypeService.getAll(tenantId, new RequestInfo());

        for (FuelType ft : fuelTypes) {
            fuelTypeMap.put(ft.getCode(), ft);
        }

        for (RefillingPumpStation refillingPumpStation : refillingPumpStationList) {

            if (refillingPumpStation.getTypeOfFuel() != null && refillingPumpStation.getTypeOfFuel().getCode() != null
                    && !refillingPumpStation.getTypeOfFuel().getCode().isEmpty()) {

                refillingPumpStation.setTypeOfFuel(fuelTypeMap.get(refillingPumpStation.getTypeOfFuel().getCode()));
            }

        }
    }

    private void populateTypeOfPumps(List<RefillingPumpStation> refillingPumpStationList) {
        Map<String, OilCompany> typeOfPumpMap = new HashMap<>();
        String tenantId = null;

        if (refillingPumpStationList != null && !refillingPumpStationList.isEmpty())
            tenantId = refillingPumpStationList.get(0).getTenantId();

        List<OilCompany> typeOfPumps = oilCompanyService.getAll(tenantId, new RequestInfo());

        for (OilCompany top : typeOfPumps) {
            typeOfPumpMap.put(top.getCode(), top);
        }

        for (RefillingPumpStation refillingPumpStation : refillingPumpStationList) {

            if (refillingPumpStation.getTypeOfPump() != null && refillingPumpStation.getTypeOfPump().getCode() != null
                    && !refillingPumpStation.getTypeOfPump().getCode().isEmpty()) {

                refillingPumpStation.setTypeOfPump(typeOfPumpMap.get(refillingPumpStation.getTypeOfPump().getCode()));
            }

        }
    }

    private void populateBoundarys(List<RefillingPumpStation> refillingPumpStationList) {

        StringBuffer boundaryCodes = new StringBuffer();
        Set<String> boundaryCodesSet = new HashSet<>();

        for (RefillingPumpStation rps : refillingPumpStationList) {

            if (rps.getLocation() != null && rps.getLocation().getCode() != null
                    && !rps.getLocation().getCode().isEmpty()) {

                boundaryCodesSet.add(rps.getLocation().getCode());

            }

        }

        List<String> locationCodes = new ArrayList(boundaryCodesSet);

        for (String code : locationCodes) {

            if (boundaryCodes.length() >= 1)
                boundaryCodes.append(",");

            boundaryCodes.append(code);

        }

        String tenantId = null;
        Map<String, Boundary> boundaryMap = new HashMap<>();

        if (refillingPumpStationList != null && !refillingPumpStationList.isEmpty())
            tenantId = refillingPumpStationList.get(0).getTenantId();

        if (boundaryCodes != null && boundaryCodes.length() > 0) {

            List<Boundary> boundarys = boundaryRepository.fetchBoundaryByCodes(boundaryCodes.toString(), tenantId);

            for (Boundary bd : boundarys) {

                boundaryMap.put(bd.getCode(), bd);

            }

            for (RefillingPumpStation refillingPumpStation : refillingPumpStationList) {

                if (refillingPumpStation.getLocation() != null && refillingPumpStation.getLocation().getCode() != null
                        && !refillingPumpStation.getLocation().getCode().isEmpty()) {

                    refillingPumpStation.setLocation(boundaryMap.get(refillingPumpStation.getLocation().getCode()));
                }

            }

        }

    }
}
