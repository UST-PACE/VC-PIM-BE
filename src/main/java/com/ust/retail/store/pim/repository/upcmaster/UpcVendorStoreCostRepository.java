package com.ust.retail.store.pim.repository.upcmaster;

import com.ust.retail.store.pim.model.upcmaster.UpcVendorStoreCostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UpcVendorStoreCostRepository extends JpaRepository<UpcVendorStoreCostModel, Long> {
	@Query("SELECT c"
			+ "  FROM UpcVendorStoreCostModel c"
			+ "    JOIN c.upcVendorDetail vd"
			+ "    JOIN vd.vendorMaster vm"
			+ "    JOIN c.storeNumber st"
			+ "  WHERE vm.vendorMasterId = :vendorMasterId"
			+ "    AND st.storeNumId = :storeNumId")
	List<UpcVendorStoreCostModel> findCostsByVendorAndStore(Long vendorMasterId, Long storeNumId);
}
