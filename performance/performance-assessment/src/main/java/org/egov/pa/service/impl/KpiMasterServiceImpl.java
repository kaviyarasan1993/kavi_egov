package org.egov.pa.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.egov.pa.model.AuditDetails;
import org.egov.pa.model.Document;
import org.egov.pa.model.DocumentTypeContract;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiTarget;
import org.egov.pa.model.KpiTargetList;
import org.egov.pa.repository.KpiMasterRepository;
import org.egov.pa.service.KpiMasterService;
import org.egov.pa.validator.RestCallService;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("kpiMasterServ")
@Slf4j
public class KpiMasterServiceImpl implements KpiMasterService {
	
	@Autowired 
	@Qualifier("kpiMasterRepo")
	private KpiMasterRepository kpiMasterRepository;
	
	@Autowired
	private RestCallService restCallService; 
	
	@Override
	public KPIRequest createNewKpi(KPIRequest kpiRequest) {
		int numberOfIds = kpiRequest.getKpIs().size(); 
    	log.info("KPI Message Received at Service Level : " + kpiRequest);
    	List<Long> kpiIdList = kpiMasterRepository.getNewKpiIds(numberOfIds);
    	log.info("KPI Master Next ID Generated is : " + kpiIdList);
    	if(kpiIdList.size() == kpiRequest.getKpIs().size()) { 
    		for(int i = 0 ; i < kpiRequest.getKpIs().size() ; i++) { 
    			kpiRequest.getKpIs().get(i).setId(String.valueOf(kpiIdList.get(i)));
    		}
    	}
    	setCreatedDateAndUpdatedDate(kpiRequest);
    	prepareDocumentObjects(kpiRequest);
    	kpiMasterRepository.persistKpi(kpiRequest);
    	return kpiRequest;
	}
	
	@Override
	public KPIRequest updateNewKpi(KPIRequest kpiRequest) {
		log.info("KPI Message Received at Service Level : " + kpiRequest);
    	List<KpiTarget> updateList = new ArrayList<>(); 
    	List<KpiTarget> insertList = new ArrayList<>();
    	searchKpiTarget(kpiRequest, updateList, insertList);
    	log.info("KPI Targets to be updated : " + updateList);
    	log.info("KPI Targets to be inserted : " + insertList);
    	setCreatedDateAndUpdatedDate(kpiRequest);
    	prepareDocumentObjects(kpiRequest);
    	kpiMasterRepository.updateKpi(kpiRequest);
    	if(updateList.size() > 0) { 
    		KpiTargetList targetList = new KpiTargetList(); 
    		targetList.setTargetList(updateList);
    		kpiMasterRepository.updateKpiTarget(targetList);
    	} 
    	if(insertList.size() > 0) { 
    		KpiTargetList targetList = new KpiTargetList(); 
    		targetList.setTargetList(insertList);
    		kpiMasterRepository.persistKpiTarget(targetList);
    	}
    	return kpiRequest;
	}
	
	@Override
	public KPIRequest deleteNewKpi(KPIRequest kpiRequest) {
		log.info("KPI Message Received at Service Level : " + kpiRequest);
    	setCreatedDateAndUpdatedDate(kpiRequest);
    	kpiMasterRepository.deleteKpi(kpiRequest);
    	return kpiRequest;
	}
	
	@Override
	public List<KPI> searchKpi(KPIGetRequest kpiGetRequest) {
		log.info("KPI Get Request Received at Service Level : " + kpiGetRequest); 
    	return kpiMasterRepository.searchKpi(kpiGetRequest);
	}
	
	
	private void searchKpiTarget(KPIRequest kpiRequest, List<KpiTarget> updateList, List<KpiTarget> insertList) { 
    	/*for(KPI kpi : kpiRequest.getKpIs()) { 
    		if(null != kpi.getKpiTarget()) {
    			KpiTarget kpiTarget = kpi.getKpiTarget();
    			if(null != kpiTarget.getId()) { 
    				updateList.add(kpiTarget); 
    			} else { 
    				kpiTarget.setCreatedBy(kpiRequest.getRequestInfo().getUserInfo().getId());
    				insertList.add(kpiTarget);
    			}
    		}
    	}*/
    }
	
	@Override
	public Boolean getKpiType(String kpiCode, String tenantId) {
		return kpiMasterRepository.getKpiType(kpiCode, tenantId);
	}
	
	private void prepareDocumentObjects(KPIRequest kpiRequest) { 
    	List<KPI> kpiList = kpiRequest.getKpIs(); 
    	for(KPI kpi : kpiList) {
    		if(null != kpi.getDocuments() && kpi.getDocuments().size() > 0) { 
    			for(Document doc : kpi.getDocuments()) { 
    				doc.setKpiCode(kpi.getCode());
    			}
    		}
    	}
    }
    
    private void setCreatedDateAndUpdatedDate(KPIRequest kpiRequest) { 
    	List<KPI> kpiList = kpiRequest.getKpIs();
    	for(KPI kpi : kpiList) { 
    		AuditDetails audit = new AuditDetails(); 
    		audit.setCreatedTime(new java.util.Date().getTime());
    		audit.setLastModifiedTime(new java.util.Date().getTime());
    		kpi.setAuditDetails(audit);
    	}
    }
    
    public boolean checkNameOrCodeExists(KPIRequest kpiRequest, Boolean createOrUpdate) { 
    	List<KPI> kpiList = kpiMasterRepository.checkNameOrCodeExists(kpiRequest);
    	if(!createOrUpdate) { 
    		for(KPI kpi : kpiList) { 
    			for(int i=0 ; i<kpiRequest.getKpIs().size() ; i++) {
    				if(kpiRequest.getKpIs().get(i).getId().equals(kpi.getId())) {
        				return false; 
        			}
    			}
    		}
    	}
    	if(kpiList.size() > 0) 
    		return true;
    	else 
    		return false;
    }
    
    public String targetAlreadyAvailable(String kpiCode) { 
    	return kpiMasterRepository.targetExistsForKPI(kpiCode); 
    }

	@Override
	public List<DocumentTypeContract> getDocumentForKpi(String kpiCode) {
		return kpiMasterRepository.getDocumentForKpi(kpiCode);
	}

	
}
