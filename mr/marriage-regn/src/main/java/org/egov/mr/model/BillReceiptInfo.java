/*
 * Marriage Registration APIs
 * APIs for Marriage registration for citizens are listed here.
 *
 * OpenAPI spec version: 1.0.0
 * Contact: swaminathan.s@riflexions.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package org.egov.mr.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class BillReceiptInfo {

	private String billReferenceNo;

	private String event;

	private String receiptNo;

	private Long receiptDate;

	private String payeeName;

	private String payeeAddress;

	private List<ReceiptAccountDetail> accountDetails = new ArrayList<ReceiptAccountDetail>();

	private String serviceName;

	private String paidBy;

	private String receiptDescription;

	private Long totalAmount;

	private String receiptURL;

	private String collectionType;

	private Boolean isLegacy;

	private String additionalInfo;

	private String source;

	private String receiptInstrumentType;

	private String receiptStatus;

	private String tenantId;

}
