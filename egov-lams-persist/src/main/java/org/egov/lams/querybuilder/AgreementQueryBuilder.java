package org.egov.lams.querybuilder;

public class AgreementQueryBuilder {

		public static String insertAgreementQuery() {
			String query = "INSERT INTO eglams_agreement values "
					+ "(nextval('seq_lams_agreement'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
			return query;
		}
}


