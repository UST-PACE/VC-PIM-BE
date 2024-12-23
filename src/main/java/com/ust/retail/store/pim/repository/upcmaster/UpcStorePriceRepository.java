package com.ust.retail.store.pim.repository.upcmaster;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ust.retail.store.pim.model.upcmaster.UpcStorePriceModel;

public interface UpcStorePriceRepository extends JpaRepository<UpcStorePriceModel, Long> {
	List<UpcStorePriceModel> findByUpcMasterUpcMasterId(Long upcMasterId);
	
	UpcStorePriceModel findByUpcMasterUpcMasterIdAndStoreNumberStoreNumId(Long upcMasterId,Long storeNumberId);
	
	@Modifying
	@Query("UPDATE UpcStorePriceModel SET salePrice = null WHERE upcMaster.upcMasterId = ?1 AND storeNumber.storeNumId = ?2")
	void resetAllStorePrices(Long upcMasterId, Long storeNumId);
}
