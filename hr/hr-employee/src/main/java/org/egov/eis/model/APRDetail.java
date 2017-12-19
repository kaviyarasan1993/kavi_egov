/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.egov.eis.model.enums.Gender;
import org.egov.eis.model.enums.MaritalStatus;
import org.egov.eis.model.enums.Relationship;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
public class APRDetail {

	private Long id;

	@NotNull
	private Integer yearOfSubmission;

	@NotNull
	private Boolean detailsSubmitted;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfSubmission;

	@Size(max=1024)
	private String remarks;

	private List<String> documents;

	private Long createdBy;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;

	private Long lastModifiedBy;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date lastModifiedDate;

	private String tenantId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof APRDetail)) return false;

		APRDetail aprDetail = (APRDetail) o;

		if (!isEmpty(yearOfSubmission) && !yearOfSubmission.equals(aprDetail.yearOfSubmission)) return false;
		if (!isEmpty(detailsSubmitted) && !detailsSubmitted.equals(aprDetail.detailsSubmitted)) return false;
		if (!isEmpty(dateOfSubmission) && !dateOfSubmission.equals(aprDetail.dateOfSubmission)) return false;
		return (!isEmpty(remarks) && remarks.equals(aprDetail.remarks));
	}
}