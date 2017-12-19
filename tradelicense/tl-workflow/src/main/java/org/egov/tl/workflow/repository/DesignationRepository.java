/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.workflow.repository;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.workflow.repository.contract.DesignationResponse;
import org.egov.tl.workflow.repository.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DesignationRepository {

	private final RestTemplate restTemplate;

	private final String designationByNameUrl;

	@Autowired
	public DesignationRepository(final RestTemplate restTemplate,
			@Value("${egov.services.hr_masters.hostname}") final String designationServiceHostname,
			@Value("${egov.services.hr_masters.designations.by.name}") final String designationByNameUrl) {

		this.restTemplate = restTemplate;
		this.designationByNameUrl = designationServiceHostname + designationByNameUrl;
	}

	public DesignationResponse getDesignationByName(final String designationName, final String tenantId,
			RequestInfo requestInfo) {

		final RequestInfoWrapper wrapper = new RequestInfoWrapper();
		wrapper.setRequestInfo(requestInfo);

		return restTemplate.postForObject(designationByNameUrl, wrapper, DesignationResponse.class, designationName,
				tenantId);

	}
}
