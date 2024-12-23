package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreNumberRepository extends JpaRepository<StoreNumberModel, Long> {
	@Query(value = "SELECT a"
			+ " FROM StoreNumberModel a"
			+ " WHERE ?1 = NULL OR a.storeName LIKE %?1%")
	Page<StoreNumberModel> findByFilters(String storeName, Pageable pageable);

	@Query(value = "SELECT a"
			+ " FROM StoreNumberModel a"
			+ " WHERE ?1 = NULL OR a.storeName LIKE %?1%")
	List<StoreNumberModel> getAutocompleteList(String storeName);

	@Query(value = "SELECT sn FROM VendorMasterStoreModel vms join vms.pk.storeNum sn WHERE vms.pk.vendorMaster.vendorMasterId = ?1")
	List<StoreNumberModel> findByVendorMasterId(Long vendorMasterId);
}
