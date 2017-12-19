package org.egov.swm.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.BinDetails;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.repository.BinDetailsRepository;
import org.egov.swm.domain.repository.CollectionPointRepository;
import org.egov.swm.web.repository.BoundaryRepository;
import org.egov.swm.web.requests.CollectionPointRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CollectionPointService {

    @Autowired
    private CollectionPointRepository collectionPointRepository;

    @Autowired
    private BinDetailsRepository binDetailsRepository;

    @Autowired
    private BoundaryRepository boundaryRepository;

    @Autowired
    private CollectionTypeService collectionTypeService;

    @Transactional
    public CollectionPointRequest create(final CollectionPointRequest collectionPointRequest) {

        validate(collectionPointRequest);

        Long userId = null;

        if (collectionPointRequest.getRequestInfo() != null
                && collectionPointRequest.getRequestInfo().getUserInfo() != null
                && null != collectionPointRequest.getRequestInfo().getUserInfo().getId())
            userId = collectionPointRequest.getRequestInfo().getUserInfo().getId();

        for (final CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

            setAuditDetails(cp, userId);

            cp.setCode(UUID.randomUUID().toString().replace("-", ""));

            populateBinDetailsIds(cp);

            populateCollectionPointDetails(cp);

        }

        return collectionPointRepository.save(collectionPointRequest);

    }

    @Transactional
    public CollectionPointRequest update(final CollectionPointRequest collectionPointRequest) {

        Long userId = null;

        if (collectionPointRequest.getRequestInfo() != null
                && collectionPointRequest.getRequestInfo().getUserInfo() != null
                && null != collectionPointRequest.getRequestInfo().getUserInfo().getId())
            userId = collectionPointRequest.getRequestInfo().getUserInfo().getId();

        for (final CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

            setAuditDetails(cp, userId);

            populateBinDetailsIds(cp);

            populateCollectionPointDetails(cp);
        }

        validate(collectionPointRequest);

        return collectionPointRepository.update(collectionPointRequest);

    }

    private void populateBinDetailsIds(final CollectionPoint cp) {
        if (cp != null && cp.getBinDetails() != null)
            for (final BinDetails bid : cp.getBinDetails()) {
                bid.setId(UUID.randomUUID().toString().replace("-", ""));
                bid.setTenantId(cp.getTenantId());
            }
    }

    private void populateCollectionPointDetails(final CollectionPoint cp) {
        if (cp != null && cp.getCollectionPointDetails() != null)
            for (final CollectionPointDetails cpd : cp.getCollectionPointDetails()) {
                cpd.setId(UUID.randomUUID().toString().replace("-", ""));
                cpd.setTenantId(cp.getTenantId());
            }
    }

    private void validate(final CollectionPointRequest collectionPointRequest) {

        findDuplicatesInUniqueFields(collectionPointRequest);

        for (final CollectionPoint collectionPoint : collectionPointRequest.getCollectionPoints()) {

            // Validate Boundary

            if (collectionPoint.getLocation() != null && (collectionPoint.getLocation().getCode() == null
                    || collectionPoint.getLocation().getCode().isEmpty()))
                throw new CustomException("Location",
                        "The field Location Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            if (collectionPoint.getLocation() != null && collectionPoint.getLocation().getCode() != null) {

                final Boundary boundary = boundaryRepository.fetchBoundaryByCode(collectionPoint.getLocation().getCode(),
                        collectionPoint.getTenantId());

                if (boundary != null)
                    collectionPoint.setLocation(boundary);
                else
                    throw new CustomException("Location",
                            "Given Location is Invalid: " + collectionPoint.getLocation().getCode());
            }

            if (collectionPoint.getCollectionPointDetails() != null)
                for (final CollectionPointDetails cpd : collectionPoint.getCollectionPointDetails()) {

                    if (cpd.getCollectionType() != null && (cpd.getCollectionType().getCode() == null
                            || cpd.getCollectionType().getCode().isEmpty()))
                        throw new CustomException("CollectionType",
                                "The field CollectionType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                    // Validate Collection Type
                    if (cpd.getCollectionType() != null && cpd.getCollectionType().getCode() != null)
                        cpd.setCollectionType(collectionTypeService.getCollectionType(collectionPoint.getTenantId(),
                                cpd.getCollectionType().getCode(), collectionPointRequest.getRequestInfo()));
                    else
                        throw new CustomException("CollectionType", "CollectionType is required");
                }

            validateUniqueFields(collectionPoint);

        }

    }

    private void findDuplicatesInUniqueFields(final CollectionPointRequest collectionPointRequest) {

        final Map<String, String> assetOrBinIdsMap = new HashMap<>();
        final Map<String, String> rfidsMap = new HashMap<>();
        final Map<String, String> nameMap = new HashMap<>();
        final Map<String, String> codeMap = new HashMap<>();

        for (final CollectionPoint collectionPoint : collectionPointRequest.getCollectionPoints()) {
            if (collectionPoint.getName() != null) {

                if (nameMap.get(collectionPoint.getName()) != null)
                    throw new CustomException("Name",
                            "Duplicate names in given collection Points: " + collectionPoint.getName());

                nameMap.put(collectionPoint.getName(), collectionPoint.getName());

            }

            for(CollectionPointDetails collectionPointDetails : collectionPoint.getCollectionPointDetails()){

                if (codeMap.get(collectionPointDetails.getCollectionType().getCode()) != null)
                    throw new CustomException("Collection Type",
                            "Collection types shall be unique per record.: " + collectionPointDetails.getCollectionType().getCode());

                codeMap.put(collectionPointDetails.getCollectionType().getCode(), collectionPointDetails.getCollectionType().getCode());
            }

            for (final BinDetails bd : collectionPoint.getBinDetails()) {

                if (bd.getAssetOrBinId() != null) {

                    if (assetOrBinIdsMap.get(bd.getAssetOrBinId()) != null)
                        throw new CustomException("BinId",
                                "Duplicate BinIds in given Bin details: " + bd.getAssetOrBinId());

                    assetOrBinIdsMap.put(bd.getAssetOrBinId(), bd.getAssetOrBinId());

                }

                if (bd.getRfid() != null) {
                    if (rfidsMap.get(bd.getRfid()) != null)
                        throw new CustomException("Rfid", "Duplicate RFIDs in given Bin details: " + bd.getRfid());

                    rfidsMap.put(bd.getRfid(), bd.getRfid());

                }

            }
        }

    }

    private void validateUniqueFields(final CollectionPoint collectionPoint) {

        if (collectionPoint.getName() != null)
            if (!collectionPointRepository.uniqueCheck(collectionPoint.getTenantId(), "name", collectionPoint.getName(),
                    "code", collectionPoint.getCode()))
                throw new CustomException("Name",
                        "The field name must be unique in the system The  value " + collectionPoint.getName()
                                + " for the field name already exists in the system. Please provide different value ");

        for (final BinDetails bd : collectionPoint.getBinDetails()) {

            if (bd.getAssetOrBinId() != null)
                if (!binDetailsRepository.uniqueCheck(collectionPoint.getTenantId(), "assetOrBinId",
                        bd.getAssetOrBinId(), "collectionPoint", collectionPoint.getCode()))
                    throw new CustomException("BinId", "The field BinId must be unique in the system The  value "
                            + bd.getAssetOrBinId()
                            + " for the field BinId already exists in the system. Please provide different value ");

            if (bd.getRfidAssigned() != null && bd.getRfidAssigned())
                if (bd.getRfid() == null || bd.getRfid().isEmpty())
                    throw new CustomException("RFID",
                            "The field RFID Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            if (bd.getRfid() != null)
                if (bd.getRfidAssigned() && bd.getRfidAssigned() != null)
                    if (!binDetailsRepository.uniqueCheck(collectionPoint.getTenantId(), "rfid", bd.getRfid(),
                            "collectionPoint", collectionPoint.getCode()))
                        throw new CustomException("RFID", "The field RFID must be unique in the system The  value "
                                + bd.getRfid()
                                + " for the field RFID already exists in the system. Please provide different value ");

        }
    }

    public Pagination<CollectionPoint> search(final CollectionPointSearch collectionPointSearch) {

        return collectionPointRepository.search(collectionPointSearch);
    }

    private void setAuditDetails(final CollectionPoint contract, final Long userId) {

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