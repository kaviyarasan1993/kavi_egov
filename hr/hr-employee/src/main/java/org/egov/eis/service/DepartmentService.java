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

package org.egov.eis.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.bulk.Department;
import org.egov.eis.web.contract.DepartmentResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class DepartmentService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public Department getDepartment(String code, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		DepartmentResponse departmentResponse = null;
		try {
			url = new URI(propertiesManager.getCommonMastersServiceHostName()
					+ propertiesManager.getCommonMastersServiceBasePath()
					+ propertiesManager.getCommonMastersServiceDepartmentsSearchPath()
					+ "?tenantId=" + tenantId + "&code=" + code);
			log.debug(url.toString());
			departmentResponse = restTemplate.postForObject(url, requestInfoWrapper, DepartmentResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Following exception occurred while accessing Department API : " + e.getMessage());
			return null;
		}
		return isEmpty(departmentResponse.getDepartment()) ? null : departmentResponse.getDepartment().get(0);
	}

    public List<Department> getDepartments(List<Long> ids, String tenantId, RequestInfoWrapper requestInfoWrapper) {
        URI url = null;
        if(null == ids.get(0) || ids.isEmpty())
        {
        	  log.error("Following exception occurred while accessing Department id is null");
        	  return null;
        }
        String idsAsCSV = getIdsAsCSV(ids);
        DepartmentResponse departmentResponse = null;
        try {
            url = new URI(propertiesManager.getCommonMastersServiceHostName()
                    + propertiesManager.getCommonMastersServiceBasePath()
                    + propertiesManager.getCommonMastersServiceDepartmentsSearchPath()
                    + "?tenantId=" + tenantId + "&id=" + idsAsCSV);
            log.debug(url.toString());
            departmentResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
                    DepartmentResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Following exception occurred while accessing Department API : " + e.getMessage());
            return null;
        }
        return departmentResponse.getDepartment();
    }
    
    private String getIdsAsCSV(List<Long> ids) {
        return String.join(",", ids.stream().map(Object::toString).collect(Collectors.toList()));
    }
    private HttpEntity<RequestInfoWrapper> getRequestInfoAsHttpEntity(RequestInfoWrapper requestInfoWrapper) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestInfoWrapper, headers);
    }

}