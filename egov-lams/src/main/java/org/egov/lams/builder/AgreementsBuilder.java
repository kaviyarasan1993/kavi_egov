package org.egov.lams.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;

public class AgreementsBuilder {

	public static List<Agreement> mapAgreements(List<Agreement> agreements, List<Allottee> allottees,
			List<Asset> assets) {
		List<Agreement> newAgreements = new ArrayList<>();
		Map<Long, Asset> assetMap = new HashMap<>();
		//Map<Long, Allottee> allotteeMap = new HashMap<>();

		/*for (Allottee allottee : allottees) {
			allotteeMap.put(allottee.getId(), allottee);

		}*/
		if(agreements==null) throw new RuntimeException("no values for agrement criteria");
		
		if(assets==null) throw new RuntimeException("no values for asset criteria");
		for (Asset asset : assets) {
			assetMap.put(asset.getId(), asset);
		}

		for (Agreement agreement : agreements) {
			//Long allotteeId = agreement.getAllottee().getId();
			Long assetId = agreement.getAsset().getId();
			if (/*allotteeMap.containsKey(allotteeId) && */ assetMap.containsKey(assetId)) {
				//agreement.setAllottee(allotteeMap.get(allotteeId));
				agreement.setAsset(assetMap.get(assetId));
				newAgreements.add(agreement);
			}
		}
		return newAgreements;
	}

}
