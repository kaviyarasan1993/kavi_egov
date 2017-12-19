/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.demand.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.BillDetail;
import org.egov.demand.model.CollectedReceipt;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.DemandDetailCriteria;
import org.egov.demand.model.DemandUpdateMisRequest;
import org.egov.demand.repository.querybuilder.DemandQueryBuilder;
import org.egov.demand.repository.rowmapper.CollectedReceiptsRowMapper;
import org.egov.demand.repository.rowmapper.DemandDetailRowMapper;
import org.egov.demand.repository.rowmapper.DemandRowMapper;
import org.egov.demand.util.SequenceGenService;
import org.egov.demand.web.contract.DemandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DemandRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DemandQueryBuilder demandQueryBuilder;
	
	@Autowired
	private SequenceGenService sequenceGenService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public List<Demand> getDemands(DemandCriteria demandCriteria, Set<String> ownerIds) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String searchDemandQuery = demandQueryBuilder.getDemandQuery(demandCriteria, ownerIds, preparedStatementValues);
		return jdbcTemplate.query(searchDemandQuery, preparedStatementValues.toArray(), new DemandRowMapper());
	}
	
	public List<Demand> getDemandsForConsumerCodes(Map<String,Set<String>> businessConsumercodeMap,String tenantId){
		
		String sql = demandQueryBuilder.getDemandQueryForConsumerCodes(businessConsumercodeMap, tenantId);
		return jdbcTemplate.query(sql,new DemandRowMapper());
	}

	public List<DemandDetail> getDemandDetails(DemandDetailCriteria demandDetailCriteria) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String searchDemandDetailQuery = DemandQueryBuilder.getDemandDetailQuery(demandDetailCriteria,preparedStatementValues);
		return jdbcTemplate.query(searchDemandDetailQuery, preparedStatementValues.toArray(),new DemandDetailRowMapper());
	}

	public void save(DemandRequest demandRequest) {

		log.info("DemandRepository save, the request object : " + demandRequest);
		List<Demand> demands = demandRequest.getDemands();
		List<DemandDetail> demandDetails = new ArrayList<>();
		for (Demand demand : demands) {
			demandDetails.addAll(demand.getDemandDetails());
		}
		log.info("DemandRepository save, demands ---->> "+demands+" \n demanddetails ---->> "+demandDetails);
		insertBatch(demands, demandDetails);
		log.info("Demands saved >>>> ");
	}
	
	public void update(DemandRequest demandRequest) {

		List<Demand> demands = demandRequest.getDemands();
		List<Demand> oldDemands = new ArrayList<>();
		List<DemandDetail> oldDemandDetails = new ArrayList<>();
		List<Demand> newDemands = new ArrayList<>();
		List<DemandDetail> newDemandDetails = new ArrayList<>();

		DemandCriteria demandCriteria = DemandCriteria.builder().demandId(demands
						.stream().map(demand -> demand.getId()).collect(Collectors.toSet()))
						.tenantId(demands.get(0).getTenantId()).build();
		List<Demand> existingDemands = getDemands(demandCriteria, null);
		System.err.println("repository demands "+existingDemands);
		Map<String, String> existingDemandMap = existingDemands.stream().collect(
						Collectors.toMap(Demand::getId, Demand::getId));
		Map<String, String> existingDemandDetailMap = new HashMap<>();
		for (Demand demand : existingDemands) {
			for (DemandDetail demandDetail : demand.getDemandDetails())
				existingDemandDetailMap.put(demandDetail.getId(), demandDetail.getId());
		}

		for (Demand demand : demands) {
			if (existingDemandMap.get(demand.getId()) == null)
				newDemands.add(demand);
			else
				oldDemands.add(demand);
			for (DemandDetail demandDetail : demand.getDemandDetails()) {
				if (existingDemandDetailMap.get(demandDetail.getId()) == null)
					newDemandDetails.add(demandDetail);
				else
					oldDemandDetails.add(demandDetail);
			}
		}
		updateBatch(oldDemands, oldDemandDetails);
		if(!newDemands.isEmpty() || !newDemandDetails.isEmpty())
		insertBatch(newDemands, newDemandDetails);
	}

	@Transactional
	private void insertBatch(List<Demand> newDemands, List<DemandDetail> newDemandDetails) {

		jdbcTemplate.batchUpdate(DemandQueryBuilder.DEMAND_INSERT_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int rowNum) throws SQLException {
				Demand demand = newDemands.get(rowNum);
				AuditDetail auditDetail = demand.getAuditDetail();
				ps.setString(1, demand.getId());
				ps.setString(2, demand.getConsumerCode());
				ps.setString(3, demand.getConsumerType());
				ps.setString(4, demand.getBusinessService());
				ps.setLong(5, demand.getOwner().getId());
				ps.setLong(6, demand.getTaxPeriodFrom());
				ps.setLong(7, demand.getTaxPeriodTo());
				ps.setBigDecimal(8, demand.getMinimumAmountPayable());
				ps.setString(9, auditDetail.getCreatedBy());
				ps.setString(10, auditDetail.getLastModifiedBy());
				ps.setLong(11, auditDetail.getCreatedTime());
				ps.setLong(12, auditDetail.getLastModifiedTime());
				ps.setString(13, demand.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return newDemands.size();
			}
		});

		jdbcTemplate.batchUpdate(DemandQueryBuilder.DEMAND_DETAIL_INSERT_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int rowNum) throws SQLException {
				DemandDetail demandDetail = newDemandDetails.get(rowNum);
				AuditDetail auditDetail = demandDetail.getAuditDetail();
				ps.setString(1, demandDetail.getId());
				ps.setString(2, demandDetail.getDemandId());
				ps.setString(3, demandDetail.getTaxHeadMasterCode());
				ps.setBigDecimal(4, demandDetail.getTaxAmount());
				ps.setBigDecimal(5, demandDetail.getCollectionAmount());
				ps.setString(6, auditDetail.getCreatedBy());
				ps.setString(7, auditDetail.getLastModifiedBy());
				ps.setLong(8, auditDetail.getCreatedTime());
				ps.setLong(9, auditDetail.getLastModifiedTime());
				ps.setString(10, demandDetail.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return newDemandDetails.size();
			}
		});
	}
	
	@Transactional
	private void updateBatch(List<Demand> oldDemands, List<DemandDetail> oldDemandDetails) {

		jdbcTemplate.batchUpdate(DemandQueryBuilder.DEMAND_UPDATE_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int rowNum) throws SQLException {
				Demand demand = oldDemands.get(rowNum);
				AuditDetail auditDetail = demand.getAuditDetail();
				ps.setString(1, demand.getId());
				ps.setString(2, demand.getConsumerCode());
				ps.setString(3, demand.getConsumerType());
				ps.setString(4, demand.getBusinessService());
				ps.setLong(5, demand.getOwner().getId());
				ps.setLong(6, demand.getTaxPeriodFrom());
				ps.setLong(7, demand.getTaxPeriodTo());
				ps.setBigDecimal(8, demand.getMinimumAmountPayable());
				ps.setString(9, auditDetail.getLastModifiedBy());
				ps.setLong(10, auditDetail.getLastModifiedTime());
				ps.setString(11, demand.getTenantId());
				ps.setString(12, demand.getId());
				ps.setString(13, demand.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return oldDemands.size();
			}
		});

		jdbcTemplate.batchUpdate(DemandQueryBuilder.DEMAND_DETAIL_UPDATE_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int rowNum) throws SQLException {
				DemandDetail demandDetail = oldDemandDetails.get(rowNum);
				AuditDetail auditDetail = demandDetail.getAuditDetail();
				ps.setString(1, demandDetail.getId());
				ps.setString(2, demandDetail.getDemandId());
				ps.setString(3, demandDetail.getTaxHeadMasterCode());
				ps.setBigDecimal(4, demandDetail.getTaxAmount());
				ps.setBigDecimal(5, demandDetail.getCollectionAmount());
				ps.setString(6, auditDetail.getLastModifiedBy());
				ps.setLong(7, auditDetail.getLastModifiedTime());
				ps.setString(8, demandDetail.getTenantId());
				ps.setString(9, demandDetail.getId());
				ps.setString(10, demandDetail.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return oldDemandDetails.size();
			}
		});
	}
	//update mis method for updating consumer code
	public void updateMIS(DemandUpdateMisRequest demandRequest) {

		jdbcTemplate.update(demandQueryBuilder.getDemandUpdateMisQuery(demandRequest), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, demandRequest.getConsumerCode());
				ps.setString(2, demandRequest.getRequestInfo().getDid());
				ps.setLong(3, new Date().getTime());
				ps.setString(4, demandRequest.getTenantId());
			}
		});
	}
	
	public void saveCollectedReceipts(List<BillDetail> billDetails,RequestInfo requestInfo) {
		List<String> ids=sequenceGenService.getIds(billDetails.size(), applicationProperties.getCollectedReceiptSequence());
		
		jdbcTemplate.batchUpdate(DemandQueryBuilder.COLLECTED_RECEIPT_INSERT_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int rowNum) throws SQLException {
				BillDetail billDetail = billDetails.get(rowNum);
				ps.setString(1, ids.get(rowNum));
				ps.setString(2, billDetail.getBusinessService());
				ps.setString(3, billDetail.getConsumerCode());
				ps.setString(4, billDetail.getReceiptNumber());
				ps.setBigDecimal(5, billDetail.getTotalAmount());
				ps.setLong(6, billDetail.getReceiptDate());
				ps.setString(7, billDetail.getStatus().toString());
				ps.setString(8, billDetail.getTenantId());
				ps.setString(9, requestInfo.getUserInfo().getId().toString());
				ps.setLong(10, new Date().getTime());
				ps.setString(11, requestInfo.getUserInfo().getId().toString());
				ps.setLong(12, new Date().getTime());
			}

			@Override
			public int getBatchSize() {
				return billDetails.size();
			}
		});
	}
	
	public List<CollectedReceipt> getCollectedReceipts(DemandCriteria demandCriteria){
		return jdbcTemplate.query(demandQueryBuilder.getCollectedReceiptsQuery(demandCriteria), new CollectedReceiptsRowMapper());
	}
}
