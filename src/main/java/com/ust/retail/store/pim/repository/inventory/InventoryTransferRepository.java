package com.ust.retail.store.pim.repository.inventory;

import com.ust.retail.store.pim.dto.inventory.InventoryTransferencesFiltersDTO;
import com.ust.retail.store.pim.model.inventory.InventoryTransferModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryTransferRepository extends JpaRepository<InventoryTransferModel, Long> {


	@Query(value = "SELECT new com.ust.retail.store.pim.dto.inventory.InventoryTransferencesFiltersDTO("
			+ "a.inventoryTransferId,"
			+ "a.upcMaster.principalUpc,"
			+ "a.qty,"
			+ "a.storeLocationFrom.storeLocationId,"
			+ "a.storeLocationTo.storeLocationId,"
			+ "a.createdAt"
			+ ")"
			+ " FROM InventoryTransferModel a"
			+ " WHERE (?1 = NULL OR a.upcMaster.principalUpc LIKE %?1%)"
			+ " AND (?2 = NULL OR a.storeLocationFrom.storeLocationId = ?2)"
			+ " AND (?3 = NULL OR a.storeLocationTo.storeLocationId = ?3)"
			+ " ORDER BY a.createdAt DESC")
	Page<InventoryTransferencesFiltersDTO> findByFilters(String principalUpc, Long storeLocationToFromId, Long storeLocationToId, Pageable pageable);
}
