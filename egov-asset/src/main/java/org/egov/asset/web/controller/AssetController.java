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

package org.egov.asset.web.controller;

import java.util.List;
import org.egov.asset.model.Asset;
import org.egov.asset.model.SearchAsset;
import org.egov.asset.model.wrapper.AssetResponse;
import org.egov.asset.model.wrapper.ResponseInfo;
import org.egov.asset.web.service.AssetService;
import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetController {

	private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

	@Autowired
	private AssetService assetService;

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute SearchAsset searchAsset,BindingResult bindingResult) {
		logger.info(searchAsset.toString());
		if(bindingResult.hasErrors()){
			ErrorResponse errorResponse=populateErrors(bindingResult);
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		AssetResponse AssetResponse = null;
		List<Asset> Assets = null;
		
			Assets = assetService.getAssets(searchAsset);
			AssetResponse = new AssetResponse();
			AssetResponse.setAssets(Assets);
			AssetResponse.setResposneInfo(
					new ResponseInfo());
		return new ResponseEntity<AssetResponse>(AssetResponse, HttpStatus.OK);
	}

	
	
	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		//ResponseInfo responseInfo = new ResponseInfo();
		/*responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		responseInfo.setApi_id("");
		errRes.setResponseInfo(responseInfo);*/
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().put(errs.getField(), errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}


}