package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.model.catalog.AdditionalFeeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdditionalFeeRepository extends JpaRepository<AdditionalFeeModel, Long> {
	@Query(value = "SELECT af" +
			" FROM AdditionalFeeModel af" +
			" WHERE (?1 = NULL OR af.feeName LIKE %?1%)")
	Page<AdditionalFeeModel> findByFilters(String feeName, Pageable pageable);
}
