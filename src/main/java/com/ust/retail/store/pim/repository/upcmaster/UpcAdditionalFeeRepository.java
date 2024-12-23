package com.ust.retail.store.pim.repository.upcmaster;

import com.ust.retail.store.pim.model.upcmaster.UpcAdditionalFeeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UpcAdditionalFeeRepository extends JpaRepository<UpcAdditionalFeeModel, Long> {
	@Query(value = "SELECT uaf" +
			" FROM UpcAdditionalFeeModel uaf" +
			" JOIN uaf.upcMaster u" +
			" JOIN uaf.storeNumber s" +
			" JOIN uaf.additionalFee af" +
			" WHERE (?1 = NULL OR u.upcMasterId = ?1)" +
			"   AND (?2 = NULL OR s.storeNumId = ?2)" +
			"   AND (?3 = NULL OR af.additionalFeeId = ?3)")
	Page<UpcAdditionalFeeModel> findByFilters(Long upcMasterId, Long storeNumId, Long additionalFeeId, Pageable pageable);
}
