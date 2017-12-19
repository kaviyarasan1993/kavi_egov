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

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.*;
import org.egov.eis.model.bulk.Department;
import org.egov.eis.model.enums.BloodGroup;
import org.egov.eis.repository.*;
import org.egov.eis.service.exception.EmployeeIdNotFoundException;
import org.egov.eis.service.exception.IdGenerationException;
import org.egov.eis.service.exception.UserException;
import org.egov.eis.service.helper.EmployeeHelper;
import org.egov.eis.service.helper.EmployeeUserMapper;
import org.egov.eis.web.contract.*;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.Error;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.errorhandler.ErrorResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private ServiceHistoryService serviceHistoryService;

    @Autowired
    private ProbationService probationService;

    @Autowired
    private RegularisationService regularisationService;

    @Autowired
    private TechnicalQualificationService technicalQualificationService;

    @Autowired
    private EducationalQualificationService educationalQualificationService;

    @Autowired
    private DepartmentalTestService departmentalTestService;

    @Autowired
    private APRDetailService aprDetailService;

    @Autowired
    private EmployeeJurisdictionService employeeJurisdictionService;

    @Autowired
    private EmployeeLanguageService employeeLanguageService;

    @Autowired
    private EmployeeDocumentsService employeeDocumentsService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private HODDepartmentRepository hodDepartmentRepository;

    @Autowired
    private ServiceHistoryRepository serviceHistoryRepository;

    @Autowired
    private ProbationRepository probationRepository;

    @Autowired
    private RegularisationRepository regularisationRepository;

    @Autowired
    private TechnicalQualificationRepository technicalQualificationRepository;

    @Autowired
    private EducationalQualificationRepository educationalQualificationRepository;

    @Autowired
    private DepartmentalTestRepository departmentalTestRepository;

    @Autowired
    private APRDetailRepository aprDetailRepository;

    @Autowired
    private EmployeeJurisdictionRepository employeeJurisdictionRepository;

    @Autowired
    private EmployeeLanguageRepository employeeLanguageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeHelper employeeHelper;

    @Autowired
    private EmployeeUserMapper employeeUserMapper;

    @Autowired
    private EmployeeDataSyncService employeeDataSyncService;

    @Autowired
    private HRMastersService hrMastersService;

    @Autowired
    private IdGenService idGenService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private VacantPositionsService vacantPositionsService;

    @Autowired
    private EmployeeCreateService employeeCreateService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ErrorHandler errorHandler;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private ApplicationProperties applicationProperties;

    public List<EmployeeInfo> getEmployees(EmployeeCriteria empCriteria, RequestInfo requestInfo) throws CloneNotSupportedException {
        List<User> usersList = null;
        List<Long> ids = null;
        List<Long> idSearchCriteria = isEmpty(empCriteria.getId()) ? null : empCriteria.getId();

        // If roleCodes or userName is present, get users first, as there will be very limited results
        if (!isEmpty(empCriteria.getRoleCodes()) || !isEmpty(empCriteria.getUserName())) {
            usersList = userService.getUsers(empCriteria, requestInfo);
            log.debug("usersList returned by UsersService is :: " + usersList);
            if (isEmpty(usersList))
                return Collections.EMPTY_LIST;
            ids = usersList.stream().map(user -> user.getId()).collect(Collectors.toList());
            log.debug("User ids are :: " + ids);
            empCriteria.setId(ids);
        }

        List<EmployeeInfo> empInfoList = employeeRepository.findForCriteria(empCriteria);

        if (isEmpty(empInfoList)) {
            return Collections.EMPTY_LIST;
        }

        // If roleCodes or userName is not present, get employees first
        if (isEmpty(empCriteria.getRoleCodes()) || isEmpty(empCriteria.getUserName())) {
            ids = empInfoList.stream().map(employeeInfo -> employeeInfo.getId()).collect(Collectors.toList());
            log.debug("Employee ids are :: " + ids);
            empCriteria.setId(ids);
            usersList = userService.getUsers(empCriteria, requestInfo);
            log.debug("usersList returned by UsersService is :: " + usersList);
        }
        empInfoList = employeeUserMapper.mapUsersWithEmployees(empInfoList, usersList);

        if (!isEmpty(ids)) {
            List<EmployeeDocument> employeeDocuments = employeeRepository.getDocumentsForListOfEmployeeIds(ids,
                    empCriteria.getTenantId());
            employeeHelper.mapDocumentsWithEmployees(empInfoList, employeeDocuments);
        }

        empCriteria.setId(idSearchCriteria);
        return empInfoList;
    }

    public List<EmployeeInfo> getEmployeeWithoutAssignmentInfo(EmployeeCriteria employeeCriteria, RequestInfo requestInfo) throws CloneNotSupportedException {

        List<EmployeeInfo> empInfoList = employeeRepository.getEmployeesWithoutAssignment(employeeCriteria);

        if (isEmpty(empInfoList)) {
            return Collections.EMPTY_LIST;
        }
        List<Long> ids = empInfoList.stream().map(employeeInfo -> employeeInfo.getId()).collect(Collectors.toList());
        log.debug("Employee ids are :: " + ids);
        employeeCriteria.setId(ids);
        employeeCriteria.setActive(true);
        List<User> usersList = userService.getUsers(employeeCriteria, requestInfo);
        log.debug("usersList returned by UsersService is :: " + usersList);
        empInfoList = employeeUserMapper.mapUsersWithEmployeesForReport(empInfoList, usersList);

        return empInfoList;
    }

    public List<EmployeeInfo> getEmployeeUserInfo(BaseRegisterReportRequest baseRegisterReportRequest, RequestInfo requestInfo) throws CloneNotSupportedException {
        List<User> usersList = null;
        List<Long> ids = null;
        EmployeeCriteria empCriteria = new EmployeeCriteria();
        empCriteria.setTenantId(baseRegisterReportRequest.getTenantId());

        List<EmployeeInfo> empInfoList = employeeRepository.findForReportRequest(baseRegisterReportRequest);

        if (isEmpty(empInfoList)) {
            return Collections.EMPTY_LIST;
        }

        ids = empInfoList.stream().map(employeeInfo -> employeeInfo.getId()).collect(Collectors.toList());
        log.debug("Employee ids are :: " + ids);
        empCriteria.setId(ids);
        usersList = userService.getUsers(empCriteria, requestInfo);
        log.debug("usersList returned by UsersService is :: " + usersList);
        empInfoList = employeeUserMapper.mapUsersWithEmployeesForReport(empInfoList, usersList);

        return empInfoList;
    }

    public Map<String, Object> getPaginatedEmployees(EmployeeCriteria empCriteria, RequestInfo requestInfo)
            throws CloneNotSupportedException {
        List<EmployeeInfo> employeeInfos = getEmployees(empCriteria, requestInfo);

        if (isEmpty(employeeInfos))
            return getResponseForNoRecords(empCriteria.getPageSize(), empCriteria.getPageNumber());

        return getResponseForExistingRecords(empCriteria.getPageSize(), empCriteria.getPageNumber(),
                employeeInfos.size(), empCriteria, employeeInfos);
    }

    private Map<String, Object> getResponseForExistingRecords(Integer pageSize, Integer pageNumber, Integer recordsFetched,
                                                              EmployeeCriteria empCriteria, List<EmployeeInfo> empInfoList) {
        pageSize = isEmpty(pageSize) ? Integer.parseInt(applicationProperties.empSearchPageSizeDefault()) : pageSize;
        pageNumber = isEmpty(pageNumber) ? 1 : pageNumber;

        Integer totalDBRecords = employeeRepository.getTotalDBRecords(empCriteria);
        Integer totalpages = (int) Math.ceil((double) totalDBRecords / pageSize);

        Pagination page = Pagination.builder().totalResults(recordsFetched).totalPages(totalpages).currentPage(pageNumber)
                .pageNumber(pageNumber).pageSize(pageSize).build();

        return new LinkedHashMap<String, Object>() {{
            put("Employee", empInfoList);
            put("Page", page);
        }};
    }

    private Map<String, Object> getResponseForNoRecords(Integer pageSize, Integer pageNumber) {
        Pagination page = Pagination.builder().totalResults(0).totalPages(0).currentPage(0)
                .pageNumber(pageNumber).pageSize(pageSize).build();
        return new LinkedHashMap<String, Object>() {{
            put("Employee", Collections.EMPTY_LIST);
            put("Page", page);
        }};
    }

    public Employee getEmployee(Long employeeId, String tenantId, RequestInfo requestInfo) {
        Employee employee = employeeRepository.findById(employeeId, tenantId);
        List<Long> ids = new ArrayList<>();
        ids.add(employeeId);
        if (isEmpty(employee))
            throw new EmployeeIdNotFoundException(employeeId);

        EmployeeCriteria employeeCriteria = EmployeeCriteria.builder().id(ids).tenantId(tenantId).build();
        User user = userService.getUsers(employeeCriteria, requestInfo).get(0);

        user.setBloodGroup(isEmpty(user.getBloodGroup()) ? null : BloodGroup.fromValue(user.getBloodGroup()).toString());
        employee.setUser(user);

        employee.setLanguagesKnown(employeeLanguageRepository.findByEmployeeId(employeeId, tenantId));
        employee.setAssignments(assignmentRepository.findByEmployeeId(employeeId, tenantId));
        employee.getAssignments().forEach(assignment -> {
            assignment.setHod(hodDepartmentRepository.findByAssignmentId(assignment.getId(), tenantId));
        });
        employee.setServiceHistory(serviceHistoryRepository.findByEmployeeId(employeeId, tenantId));
        employee.setProbation(probationRepository.findByEmployeeId(employeeId, tenantId));
        employee.setJurisdictions(employeeJurisdictionRepository.findByEmployeeId(employeeId, tenantId));
        employee.setRegularisation(regularisationRepository.findByEmployeeId(employeeId, tenantId));
        employee.setTechnical(technicalQualificationRepository.findByEmployeeId(employeeId, tenantId));
        employee.setEducation(educationalQualificationRepository.findByEmployeeId(employeeId, tenantId));
        employee.setTest(departmentalTestRepository.findByEmployeeId(employeeId, tenantId));
        employee.setAprDetails(aprDetailRepository.findByEmployeeId(employeeId, tenantId));
        employeeDocumentsService.populateDocumentsInRespectiveObjects(employee);

        log.debug("After Employee Search: " + employee);

        return employee;
    }

    private String getEmployeeCode(String tenantId, RequestInfo requestInfo) throws IdGenerationException {
        String idGenEmpCodeName = propertiesManager.getIdGenServiceEmpCodeName();
        String idGenEmpCodeFormat = propertiesManager.getIdGenServiceEmpCodeFormat();
        return idGenService.generate(tenantId, idGenEmpCodeName, idGenEmpCodeFormat, requestInfo);
    }

    public Employee createAsync(EmployeeRequest employeeRequest) throws UserException, IdGenerationException, JsonProcessingException {
        Employee employee = employeeRequest.getEmployee();
        RequestInfo requestInfo = employeeRequest.getRequestInfo();
        // FIXME : Setting ts as null in RequestInfo as hr is following common-contracts with ts as Date
        // & ID Generation Service is following ts as epoch
        requestInfo.setTs(null);
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);

        Map<String, List<String>> hrConfigurations = hrMastersService.getHRConfigurations(employee.getTenantId(), requestInfoWrapper);

        if (hrConfigurations.get("Autogenerate_employeecode").get(0).equalsIgnoreCase("Y")) {
            employee.setCode(getEmployeeCode(employee.getTenantId(), requestInfo));
        }

        UserRequest userRequest = employeeHelper.getUserRequest(employeeRequest);
        userRequest.getUser().setBloodGroup(isEmpty(userRequest.getUser().getBloodGroup()) ? null
                : userRequest.getUser().getBloodGroup());
        if (hrConfigurations.get("Autogenerate_username").get(0).equalsIgnoreCase("Y")) {
            userRequest.getUser().setUserName(employee.getCode());
        }

        // FIXME : Fix a common standard for date formats in User Service.
        UserResponse userResponse = userService.createUser(userRequest);
        User user = userResponse.getUser().get(0);

        employee.setId(user.getId());
        employee.setUser(user);

        employeeHelper.populateDefaultDataForCreate(employeeRequest);

        log.info("employeeRequest before sending to kafka :: " + employeeRequest);
        kafkaTemplate.send(propertiesManager.getSaveEmployeeTopic(), employeeRequest);

        return employee;
    }

    @Transactional
    public void create(EmployeeRequest employeeRequest) {
        Employee employee = employeeRequest.getEmployee();
        employeeRepository.save(employee);
        employeeJurisdictionRepository.save(employee);
        if (!isEmpty(employee.getLanguagesKnown())) {
            employeeLanguageRepository.save(employee);
        }
        assignmentRepository.save(employeeRequest);
        employee.getAssignments().forEach((assignment) -> {
            if (!isEmpty(assignment.getHod())) {
                hodDepartmentRepository.save(assignment, employee.getTenantId());
            }
        });
        if (!isEmpty(employee.getServiceHistory())) {
            serviceHistoryRepository.save(employeeRequest);
        }
        if (!isEmpty(employee.getProbation())) {
            probationRepository.save(employeeRequest);
        }
        if (!isEmpty(employee.getRegularisation())) {
            regularisationRepository.save(employeeRequest);
        }
        if (!isEmpty(employee.getTechnical())) {
            technicalQualificationRepository.save(employeeRequest);
        }
        if (!isEmpty(employee.getEducation())) {
            educationalQualificationRepository.save(employeeRequest);
        }
        if (!isEmpty(employee.getTest())) {
            departmentalTestRepository.save(employeeRequest);
        }
        if (!isEmpty(employee.getAprDetails())) {
            aprDetailRepository.save(employeeRequest);
        }

        EmployeeSync employeeSync = EmployeeSync.builder().code(employee.getCode()).tenantId(employee.getTenantId()).signature(employee.getUser().getSignature()).userName(employee.getUser().getUserName()).build();
        EmployeeSyncRequest employeeSyncRequest = EmployeeSyncRequest.builder().employeeSync(employeeSync).build();
        if (propertiesManager.getDataSyncEmployeeRequired())
            employeeDataSyncService.createDataSync(employeeSyncRequest);
    }

    public Employee updateAsync(EmployeeRequest employeeRequest) throws UserException, JsonProcessingException {

        Employee employee = employeeRequest.getEmployee();

        UserRequest userRequest = employeeHelper.getUserRequest(employeeRequest);
        userRequest.getUser().setBloodGroup(isEmpty(userRequest.getUser().getBloodGroup()) ? null
                : userRequest.getUser().getBloodGroup());

        UserResponse userResponse = userService.updateUser(userRequest.getUser().getId(), userRequest);
        User user = userResponse.getUser().get(0);
        employee.setUser(user);

        employeeHelper.populateDefaultDataForUpdate(employeeRequest);

        AssignmentGetRequest assignmentGetRequest = AssignmentGetRequest.builder().tenantId(employee.getTenantId()).build();
        List<Assignment> assignments = assignmentService.getAssignments(employee.getId(), assignmentGetRequest);

        List<Long> positionfromDB = assignments.stream().map(assignment -> assignment.getPosition()).collect(Collectors.toList());
        List<Long> positionFromRequest = employeeRequest.getEmployee().getAssignments().stream()
                .map(assignment -> assignment.getPosition()).collect(Collectors.toList());
        boolean isPositionModified = !(ArrayUtils.isEquals(positionfromDB, positionFromRequest));

        List<Date> fromDateFromDB = assignments.stream().map(assignment -> assignment.getFromDate()).collect(Collectors.toList());
        List<Date> fromDateFromRequest = employeeRequest.getEmployee().getAssignments().stream().map(assignment -> assignment.getFromDate()).collect(Collectors.toList());
        boolean isFromDateModified = !(ArrayUtils.isEquals(fromDateFromDB, fromDateFromRequest));

        List<Date> toDateFromDB = assignments.stream().map(assignment -> assignment.getToDate()).collect(Collectors.toList());
        List<Date> toDateFromRequest = employeeRequest.getEmployee().getAssignments().stream()
                .map(assignment -> assignment.getToDate()).collect(Collectors.toList());
        boolean isToDateModified = !(ArrayUtils.isEquals(toDateFromDB, toDateFromRequest));

        boolean isAssignmentDeleted = assignments.size() != employeeRequest.getEmployee().getAssignments().size();

        if (isPositionModified || isFromDateModified || isToDateModified || isAssignmentDeleted) {
            log.info("employeeRequest before sending to kafka for updating assignments :: " + employeeRequest);
            kafkaTemplate.send(propertiesManager.getUpdateAssignmentTopic(), employeeRequest);
        }

        log.info("employeeRequest before sending to kafka for updating employee :: " + employeeRequest);
        kafkaTemplate.send(propertiesManager.getUpdateEmployeeTopic(), employeeRequest);
        return employee;
    }

    @Transactional
    public void update(EmployeeRequest employeeRequest) {
        Employee employee = employeeRequest.getEmployee();
        employeeRepository.update(employee);
        employeeLanguageService.update(employee);
        employeeJurisdictionService.update(employee);
        assignmentService.update(employee);
        departmentalTestService.update(employee);
        serviceHistoryService.update(employee);
        probationService.update(employee);
        regularisationService.update(employee);
        technicalQualificationService.update(employee);
        educationalQualificationService.update(employee);
        aprDetailService.update(employee);
        employeeDocumentsService.update(employee);

        EmployeeSync employeeSync = EmployeeSync.builder().code(employee.getCode()).tenantId(employee.getTenantId()).signature(employee.getUser().getSignature()).userName(employee.getUser().getUserName()).build();
        EmployeeSyncRequest employeeSyncRequest = EmployeeSyncRequest.builder().employeeSync(employeeSync).build();
        if (propertiesManager.getDataSyncEmployeeRequired())
            employeeDataSyncService.createDataSync(employeeSyncRequest);
    }

    public List<EmployeeInfo> getLoggedInEmployee(RequestInfo requestInfo) throws CloneNotSupportedException {
        org.egov.common.contract.request.User userInfo = requestInfo.getUserInfo();
        EmployeeCriteria employeeCriteria = new EmployeeCriteria();
        employeeCriteria.setUserName(userInfo.getUserName());
        employeeCriteria.setTenantId(userInfo.getTenantId());

        List<User> users = userService.getUsers(employeeCriteria, requestInfo);
        List<Long> userIds = users.stream().map(user -> user.getId()).collect(Collectors.toList());
        employeeCriteria.setId(userIds);

        return getEmployees(employeeCriteria, requestInfo);
    }

    public ResponseEntity<?> bulkCreate(EmployeeBulkRequest employeeBulkRequest) {
        RequestInfo requestInfo = employeeBulkRequest.getRequestInfo();
        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        List<org.egov.eis.model.bulk.Employee> bulkEmployees = employeeBulkRequest.getEmployees();

        List<Employee> employees = new ArrayList<>();

        for (int empIndex = 0; empIndex < bulkEmployees.size(); empIndex++) {
            org.egov.eis.model.bulk.Employee bulkEmployee = bulkEmployees.get(empIndex);
            List<org.egov.eis.model.bulk.Assignment> assignments = new ArrayList<>();
            for (int assignIndex = 0; assignIndex < bulkEmployee.getAssignments().size(); assignIndex++) {
                org.egov.eis.model.bulk.Assignment assignment = bulkEmployee.getAssignments().get(assignIndex);
                Department department = departmentService.getDepartment(
                        assignment.getDepartment().getCode(), bulkEmployee.getTenantId(), requestInfoWrapper);
                org.egov.eis.model.bulk.Designation designation = designationService.getDesignation(
                        assignment.getDesignation().getCode(), bulkEmployee.getTenantId(), requestInfoWrapper);

                assignment.setDepartment(department);
                assignment.setDesignation(designation);

                List<Position> position = vacantPositionsService.getVacantPositions(department.getId(), designation.getId(),
                        assignment.getFromDate(), bulkEmployee.getTenantId(), requestInfoWrapper);

                if (isEmpty(position) || (position.size() < assignIndex + 1))
                    return errorHandler.getErrorResponseEntityForNoVacantPositionAvailable(empIndex, department.getCode(),
                            designation.getCode(), requestInfo);
                assignment.setPosition(position.get(assignIndex).getId());

                assignments.add(assignment);
            }

            bulkEmployee.setAssignments(assignments);

            Employee employee = bulkEmployee.toDomain();
            EmployeeRequest employeeRequest = EmployeeRequest.builder().employee(employee).requestInfo(requestInfo).build();

            EmployeeEntityResponse employeeEntityResponse = employeeCreateService.createEmployee(employeeRequest);

            if (isEmpty(employeeEntityResponse.getEmployee()) && !isEmpty(employeeEntityResponse.getError())) {
                return getErrorResponseForBulkCreate(employeeEntityResponse);
            }

            employees.add(employeeEntityResponse.getEmployee());
        }
        return getSuccessResponseForBulkCreate(employees, requestInfo);
    }

    public ResponseEntity<?> getErrorResponseForBulkCreate(EmployeeEntityResponse employeeEntityResponse) {
        ResponseInfo responseInfo = employeeEntityResponse.getResponseInfo();
        Error error = employeeEntityResponse.getError();
        ErrorResponse errorResponse = ErrorResponse.builder().error(error).responseInfo(responseInfo).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getSuccessResponseForBulkCreate(List<Employee> employees, RequestInfo requestInfo) {
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        EmployeeBulkResponse employeeBulkResponse = EmployeeBulkResponse.builder()
                .employees(employees).responseInfo(responseInfo).build();
        return new ResponseEntity<>(employeeBulkResponse, HttpStatus.OK);
    }

}