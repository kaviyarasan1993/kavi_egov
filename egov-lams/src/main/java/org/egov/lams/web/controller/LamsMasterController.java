package org.egov.lams.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.enums.NatureOfAllotmentEnum;
import org.egov.lams.model.enums.PaymentCycleEnum;
import org.egov.lams.model.enums.StatusEnum;
import org.egov.lams.web.service.GetRentIncrementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LamsMasterController {

	@Autowired
	GetRentIncrementService getRentIncrementService;

	@RequestMapping(value = "/getstatus")
	public Map<StatusEnum, StatusEnum> getSatusEnum() {
		Map<StatusEnum, StatusEnum> status = new HashMap<>();
		for (StatusEnum key : StatusEnum.values()) {
			status.put(key, StatusEnum.valueOf(key.toString()));
		}
		return status;
	}

	@RequestMapping(value = "/getpaymentcycle")
	public Map<PaymentCycleEnum, PaymentCycleEnum> getPayementCycleEnum() {
		Map<PaymentCycleEnum, PaymentCycleEnum> payementCycle = new HashMap<>();
		for (PaymentCycleEnum key : PaymentCycleEnum.values()) {
			payementCycle.put(key, PaymentCycleEnum.valueOf(key.toString()));
		}
		return payementCycle;
	}

	@RequestMapping(value = "/getnatureofallotment")
	public Map<NatureOfAllotmentEnum, NatureOfAllotmentEnum> getNatureOfAllotmentEnum() {
		Map<NatureOfAllotmentEnum, NatureOfAllotmentEnum> natureOfAllotment = new HashMap<>();
		for (NatureOfAllotmentEnum key : NatureOfAllotmentEnum.values()) {
			natureOfAllotment.put(key, NatureOfAllotmentEnum.valueOf(key.toString()));
		}
		return natureOfAllotment;
	}

	@RequestMapping(value = "/getrentincrements")
	public List<RentIncrementType> rentIncrementService() {
		return getRentIncrementService.getRentIncrements();
	}
}
