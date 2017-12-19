package org.egov.demand.repository.querybuilder;

import java.util.List;
import java.util.Set;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.BillSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillQueryBuilder {
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public final String INSERT_BILL_QUERY = "INSERT into egbs_bill "
			+"(id, tenantid, payeename, payeeaddress, payeeemail, isactive, iscancelled, createdby, createddate, lastmodifiedby, lastmodifieddate)"
			+"values(?,?,?,?,?,?,?,?,?,?,?)";
	
	public final String INSERT_BILLDETAILS_QUERY = "INSERT into egbs_billdetail "
			+"(id, tenantid, billid, businessservice, billno, billdate, consumercode, consumertype, billdescription, displaymessage, "
			+ "minimumamount, totalamount, callbackforapportioning, partpaymentallowed, collectionmodesnotallowed, "
			+ "createdby, createddate, lastmodifiedby, lastmodifieddate)"
			+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public final String INSERT_BILLACCOUNTDETAILS_QUERY = "INSERT into egbs_billaccountdetail "
			+"(id, tenantid, billdetail, glcode, orderno, accountdescription, cramounttobepaid, creditamount, debitamount, isactualdemand, purpose, "
			+ "createdby, createddate, lastmodifiedby, lastmodifieddate)"
			+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String BILL_BASE_QUERY = "SELECT b.id AS b_id, b.tenantid AS b_tenantid,"
			+ " b.payeename AS b_payeename, b.payeeaddress AS b_payeeaddress, b.payeeemail AS b_payeeemail,"
			+ " b.isactive AS b_isactive, b.iscancelled AS b_iscancelled, b.createdby AS b_createdby,"
			+ " b.createddate AS b_createddate, b.lastmodifiedby AS b_lastmodifiedby, b.lastmodifieddate AS b_lastmodifieddate,"
			+ " bd.id AS bd_id, bd.billid AS bd_billid, bd.tenantid AS bd_tenantid, bd.businessservice AS bd_businessservice,"
			+ " bd.billno AS bd_billno, bd.billdate AS bd_billdate, bd.consumercode AS bd_consumercode,bd.consumertype AS bd_consumertype,"
			+ " bd.billdescription AS bd_billdescription, bd.displaymessage AS bd_displaymessage, bd.minimumamount AS bd_minimumamount,"
			+ " bd.totalamount AS bd_totalamount, bd.callbackforapportioning AS bd_callbackforapportioning,"
			+ " bd.partpaymentallowed AS bd_partpaymentallowed, bd.collectionmodesnotallowed AS bd_collectionmodesnotallowed,"
			+ " ad.id AS ad_id, ad.tenantid AS ad_tenantid, ad.billdetail AS ad_billdetail, ad.glcode AS ad_glcode,"
			+ " ad.orderno AS ad_orderno, ad.accountdescription AS ad_accountdescription, ad.creditamount AS ad_creditamount,"
			+ " ad.debitamount AS ad_debitamount, ad.isactualdemand AS ad_isactualdemand, ad.purpose AS ad_purpose,"
			+ " ad.cramounttobepaid AS ad_cramounttobepaid"
			+ " FROM egbs_bill b"
			+ " LEFT OUTER JOIN egbs_billdetail bd ON b.id = bd.billid AND b.tenantid = bd.tenantid"
			+ " LEFT OUTER JOIN egbs_billaccountdetail ad ON bd.id = ad.billdetail AND bd.tenantid = ad.tenantid"
			+ " WHERE b.tenantid = ?"; 
	
	public String getBillQuery(BillSearchCriteria billSearchCriteria, List<Object> preparedStatementValues){
		
		StringBuilder billQuery = new StringBuilder(BILL_BASE_QUERY);
		preparedStatementValues.add(billSearchCriteria.getTenantId());
		addWhereClause(billQuery, preparedStatementValues, billSearchCriteria);
		addPagingClause(billQuery, preparedStatementValues, billSearchCriteria);
		
		return billQuery.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final BillSearchCriteria searchBill) {
		
		if( searchBill.getConsumerCode() == null 
				&& searchBill.getBillType() == null && searchBill.getBillId() == null
				&& searchBill.getIsActive() == null && searchBill.getIsCancelled() == null)
			return;
		
		if(searchBill.getBillId() != null && !searchBill.getBillId().isEmpty())
			selectQuery.append(" AND b.id in (" + getIdQuery(searchBill.getBillId()));
		
		if(searchBill.getIsActive() != null){
			selectQuery.append(" AND b.isactive = ?");
			preparedStatementValues.add(searchBill.getIsActive());
		}
		if(searchBill.getIsCancelled() != null){
			selectQuery.append(" AND b.iscancelled = ?");
			preparedStatementValues.add(searchBill.getIsCancelled());
		}
		
		if(searchBill.getService()!= null){
			selectQuery.append(" AND bd.businessservice = ?");
			preparedStatementValues.add(searchBill.getService());
		}
		if(searchBill.getConsumerCode() != null){
			selectQuery.append(" AND bd.consumercode = ?");
			preparedStatementValues.add(searchBill.getConsumerCode());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final BillSearchCriteria searchBillCriteria) {
		
		selectQuery.append(" ORDER BY b.payeename");

		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.commonsSearchPageSizeDefault());
		if (searchBillCriteria.getSize() != null)
			pageSize = searchBillCriteria.getSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (searchBillCriteria.getOffset() != null)
			pageNumber = searchBillCriteria.getOffset() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
															// pageNo * pageSize
	}
	
	private static String getIdQuery(Set<String> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {
			String[] list = idList.toArray(new String[idList.size()]);
			query.append("'" + list[0] + "'");
			for (int i = 1; i < idList.size(); i++)
				query.append("," + "'" + list[i] + "'");
		}
		return query.append(")").toString();
	}

}
