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
package org.egov.collection.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.validation.ValidationException;

import org.apache.log4j.Logger;
import org.egov.collection.web.contract.BillAccountDetail;
import org.springframework.stereotype.Service;

import lombok.ToString;

@Service
public class CollectionApportionerService {
	private static final Logger LOGGER = Logger
			.getLogger(CollectionApportionerService.class);

	public List<BillAccountDetail> apportionPaidAmount(
			final BigDecimal actualAmountPaid,
			final List<BillAccountDetail> billAccountDetails) {
		LOGGER.info("receiptDetails before apportioning amount "
				+ actualAmountPaid + ": " + billAccountDetails);
		Collections.sort(billAccountDetails, (
				BillAccountDetail billAccountDetail1,
				BillAccountDetail billAccountDetail2) -> billAccountDetail1
				.getOrder().compareTo(billAccountDetail2.getOrder()));
		BigDecimal totalCrAmountToBePaid = BigDecimal.ZERO;
		for (final BillAccountDetail billAccountDetail : billAccountDetails) {
			totalCrAmountToBePaid = totalCrAmountToBePaid.add(billAccountDetail
					.getCrAmountToBePaid());
		}
		Amount balance = new Amount(actualAmountPaid);
		BigDecimal crAmountToBePaid;
		for (final BillAccountDetail billAcctDetail : billAccountDetails) {

			if (balance.isZero()) {
				// nothing left to apportion
				billAcctDetail.setCreditAmount(BigDecimal.ZERO);
				billAcctDetail.setDebitAmount(BigDecimal.ZERO);
				continue;
			}
			crAmountToBePaid = billAcctDetail.getCrAmountToBePaid();

			if (balance.isLessThanOrEqualTo(crAmountToBePaid)) {
				// partial or exact payment
				billAcctDetail.setCreditAmount(balance.crAmount);
				balance = Amount.ZERO;
			} else { // excess payment
				billAcctDetail.setCreditAmount(crAmountToBePaid);
				balance = balance.minus(crAmountToBePaid);
			}
		}
		LOGGER.info("balance: "+balance);
		if (balance.isGreaterThanZero()) {
			LOGGER.error("Apportioning failed: excess payment!");
			throw new ValidationException(
					"Paid Amount is greater than Total Amount to be paid");
		}

		LOGGER.info("receiptDetails after apportioning: " + billAccountDetails);
		return billAccountDetails;
	}

	@ToString
	private static class Amount {
		BigDecimal crAmount;
		static Amount ZERO = new Amount(BigDecimal.ZERO);

		Amount(BigDecimal amount) {
			this.crAmount = amount;
		}

		boolean isZero() {
			return crAmount.compareTo(BigDecimal.ZERO) == 0;
		}

		private boolean isGreaterThan(BigDecimal bd) {
			return crAmount.compareTo(bd) > 0;
		}

		private boolean isGreaterThanZero() {
			return isGreaterThan(BigDecimal.ZERO);
		}

		boolean isLessThanOrEqualTo(BigDecimal bd) {
			return crAmount.compareTo(bd) <= 0;
		}

		Amount minus(BigDecimal bd) {
			return new Amount(crAmount.subtract(bd));
		}
	}
}
