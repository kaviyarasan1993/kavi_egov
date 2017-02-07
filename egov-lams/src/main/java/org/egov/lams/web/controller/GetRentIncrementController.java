package org.egov.lams.web.controller;

import java.util.List;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.web.service.GetRentIncrementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetRentIncrementController {
	
	@Autowired
	GetRentIncrementService getRentIncrementService;
	
	@RequestMapping(value = "/getrentincrements")
	public List<RentIncrementType> rentIncrementService() {
		
		return getRentIncrementService.getRentIncrements();
	}

}
