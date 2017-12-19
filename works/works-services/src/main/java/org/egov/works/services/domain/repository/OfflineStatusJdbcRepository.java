package org.egov.works.services.domain.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.services.web.contract.OfflineStatus;
import org.egov.works.services.web.contract.OfflineStatusSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class OfflineStatusJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_offlinestatus";

    public List<OfflineStatus> search(OfflineStatusSearchContract offlineStatusSearchContract) {
        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (offlineStatusSearchContract.getSortBy() != null && !offlineStatusSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(offlineStatusSearchContract.getSortBy());
            validateEntityFieldName(offlineStatusSearchContract.getSortBy(), OfflineStatus.class);
        }

        String orderBy = "order by objectnumber";
        if (offlineStatusSearchContract.getSortBy() != null && !offlineStatusSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by " + offlineStatusSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (offlineStatusSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", offlineStatusSearchContract.getTenantId());
        }
        if (offlineStatusSearchContract.getDetailedEstimateNumbers() != null) {
            addAnd(params);
            params.append("objectnumber in (:detailedEstimateNumbers)");
            paramValues.put("detailedEstimateNumbers", offlineStatusSearchContract.getDetailedEstimateNumbers());
        }

        if (offlineStatusSearchContract.getIds() != null) {
            addAnd(params);
            params.append("id in(:ids) ");
            paramValues.put("ids", offlineStatusSearchContract.getIds());
        }

        if (offlineStatusSearchContract.getWorkOrderNumbers() != null) {
            addAnd(params);
            params.append("objectnumber in (:workOrderNumbers)");
            paramValues.put("workOrderNumbers", offlineStatusSearchContract.getWorkOrderNumbers());
        }

        if (offlineStatusSearchContract.getLoaNumbers() != null) {
            addAnd(params);
            params.append("objectnumber in (:loaNumbers)");
            paramValues.put("loaNumbers", offlineStatusSearchContract.getLoaNumbers());
        }

        if (offlineStatusSearchContract.getStatuses() != null) {
            addAnd(params);
            params.append("status in (:statuses)");
            paramValues.put("statuses", offlineStatusSearchContract.getStatuses());
        }

        if (offlineStatusSearchContract.getObjectType() != null) {
            addAnd(params);
            params.append("lower(objectType) = :objecttype");
            paramValues.put("objecttype", offlineStatusSearchContract.getObjectType().toLowerCase());
        }

        params.append(" and deleted = false");
        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(OfflineStatus.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
    }

}
