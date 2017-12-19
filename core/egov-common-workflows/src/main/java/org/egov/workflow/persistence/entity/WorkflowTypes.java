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

package org.egov.workflow.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EG_WF_TYPES")
@SequenceGenerator(name = WorkflowTypes.SEQ_WORKFLOWTYPES, sequenceName = WorkflowTypes.SEQ_WORKFLOWTYPES, allocationSize = 1)
public class WorkflowTypes extends AbstractAuditable {

	static final String SEQ_WORKFLOWTYPES = "SEQ_EG_WF_TYPES";
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = SEQ_WORKFLOWTYPES, strategy = GenerationType.SEQUENCE)
	private Long id;

	private String type;

	private String typeFQN;

	private String link;

	private String displayName;

	private boolean enabled;

	private boolean enabledInMs;

	private boolean grouped;

	private String tenantId;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public String getTypeFQN() {
		return typeFQN;
	}

	public void setTypeFQN(final String typeFQN) {
		this.typeFQN = typeFQN;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isGrouped() {
		return grouped;
	}

	public void setGrouped(final boolean grouped) {
		this.grouped = grouped;
	}

	public boolean isEnabledInMs() {
		return enabledInMs;
	}

	public void setEnabledInMs(boolean enabledInMs) {
		this.enabledInMs = enabledInMs;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
