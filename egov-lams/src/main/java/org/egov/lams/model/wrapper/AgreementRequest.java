package org.egov.lams.model.wrapper;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.RequestInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AgreementRequest {

	private RequestInfo requestInfo = null;

	private Agreement agreement = null;

}
