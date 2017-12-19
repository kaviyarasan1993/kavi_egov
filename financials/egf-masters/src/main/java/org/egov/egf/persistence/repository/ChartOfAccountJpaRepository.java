package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartOfAccountJpaRepository
		extends JpaRepository<ChartOfAccount, java.lang.Long>, JpaSpecificationExecutor<ChartOfAccount> {

	ChartOfAccount findByName(String name);

}