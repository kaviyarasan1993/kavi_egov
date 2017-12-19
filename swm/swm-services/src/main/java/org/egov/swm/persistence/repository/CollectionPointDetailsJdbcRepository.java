package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionPointDetailsSearch;
import org.egov.swm.persistence.entity.CollectionPointDetailsEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectionPointDetailsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_collectionpointdetails";

    @Transactional
    public void delete(final String tenantId, final String collectionPoint) {
        delete(TABLE_NAME, tenantId, "collectionPoint", collectionPoint);
    }

    public List<CollectionPointDetails> search(final CollectionPointDetailsSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), CollectionPointDetailsSearch.class);
        }

        if (searchRequest.getIds() != null) {
            addAnd(params);
            params.append("id in (:ids)");
            paramValues.put("ids", new ArrayList<>(Arrays.asList(searchRequest.getIds().split(","))));
        }
        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getId() != null) {
            addAnd(params);
            params.append("id =:id");
            paramValues.put("id", searchRequest.getId());
        }

        if (searchRequest.getCollectionPoint() != null) {
            addAnd(params);
            params.append("collectionPoint =:collectionPoint");
            paramValues.put("collectionPoint", searchRequest.getCollectionPoint());
        }

        if (searchRequest.getCollectionTypeCode() != null) {
            addAnd(params);
            params.append("collectionType =:collectionType");
            paramValues.put("collectionType", searchRequest.getCollectionTypeCode());
        }

        if (searchRequest.getGarbageEstimate() != null) {
            addAnd(params);
            params.append("garbageEstimate =:garbageEstimate");
            paramValues.put("garbageEstimate", searchRequest.getGarbageEstimate());
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(CollectionPointDetailsEntity.class);

        final List<CollectionPointDetails> collectionPointDetailsList = new ArrayList<>();

        final List<CollectionPointDetailsEntity> collectionPointDetailsEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final CollectionPointDetailsEntity collectionPointDetailsEntity : collectionPointDetailsEntities) {

            collectionPointDetailsList.add(collectionPointDetailsEntity.toDomain());
        }

        return collectionPointDetailsList;
    }

}