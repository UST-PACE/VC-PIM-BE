package com.ust.retail.store.pim.repository.inventory;

import com.ust.retail.store.pim.dto.inventory.InventoryFiltersDTO;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryModel, Long> {

	Optional<InventoryModel> findByUpcMasterUpcMasterIdAndStoreLocationStoreLocationId(Long upcMasterId, Long storeLocationId);

	List<InventoryModel> findByUpcMasterUpcMasterIdAndStoreLocationStoreNumberStoreNumId(Long upcMasterId, Long storeNumber);

	List<InventoryModel> findByUpcMasterUpcMasterId(Long upcMasterId);


	@Query(value = "SELECT new com.ust.retail.store.pim.dto.inventory.InventoryFiltersDTO("
			+ "a.storeLocation.storeNumber.storeNumId,"
			+ "a.storeLocation.storeNumber.storeName,"
			+ "a.storeLocation.storeLocationId,"
			+ "a.storeLocation.storeLocationName,"
			+ "a.upcMaster.productName,"
			+ "a.upcMaster.principalUpc,"
			+ "a.qty,"
			+ "a.upcMaster.inventoryUnit.catalogOptions"
			+ ")"
			+ " FROM InventoryModel a"
			+ " WHERE (?1 = NULL OR a.storeLocation.storeNumber.storeNumId = ?1)"
			+ " AND (?2 = NULL OR  a.storeLocation.storeLocationId = ?2)"
			+ " AND (?3 = NULL OR  a.upcMaster.principalUpc = ?3)"
			+ " AND (?4 = NULL OR  a.upcMaster.productName like %?4%)"
	)
	Page<InventoryFiltersDTO> findByFilters(Long storeNumberId, Long storeLocationId, String principalUpc, String productName, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.inventory.InventoryFiltersDTO("
			+ "a.storeLocation.storeNumber.storeName,"
			+ "a.upcMaster.productName,"
			+ "a.upcMaster.principalUpc,"
			+ "SUM(a.qty),"
			+ "a.upcMaster.inventoryUnit.catalogOptions"
			+ ")"
			+ " FROM InventoryModel a"
			+ " WHERE (?1 = NULL OR a.storeLocation.storeNumber.storeNumId = ?1)"
			+ "   AND (?2 = NULL OR  a.storeLocation.storeLocationId = ?2)"
			+ "   AND (?3 = NULL OR  a.upcMaster.principalUpc = ?3)"
			+ "   AND (?4 = NULL OR  a.upcMaster.productName LIKE %?4%)"
			+ " GROUP BY a.storeLocation.storeNumber.storeNumId,a.upcMaster.upcMasterId")
	Page<InventoryFiltersDTO> findByFiltersSummary(Long storeNumberId, Long storeLocationId, String principalUpc, String productName, Pageable pageable);

	@Query(value = "SELECT i"
			+ " FROM InventoryModel i"
			+ "   JOIN i.upcMaster u"
			+ "   JOIN u.storePrices sp"
			+ " WHERE i.storeLocation.frontSale = true"
			+ "   AND i.upcMaster.productGroup.displayExternally = true"
			+ "   AND i.storeLocation.storeNumber.storeNumId = ?1"
			+ "   AND sp.storeNumber.storeNumId = ?1"
			+ "   AND i.upcMaster.principalUpc IN ?2"
			+ "   AND i.upcMaster.upcMasterType.catalogId = ?3"
			+ "   AND i.upcMaster.productType.catalogId = ?4"
			+ "   AND i.upcMaster.upcMasterStatus.catalogId <> ?5"
	)
	List<InventoryModel> findByStoreNumIdAndPrincipalUpcIn(Long storeNumberId,
														   List<String> upcList,
														   Long pimUpcMasterTypeId,
														   Long fgProductTypeId,
														   Long disableTradingStatusId);

	@Query(value = "SELECT i"
			+ " FROM InventoryModel i"
			+ " WHERE i.storeLocation.frontSale = true"
			+ "   AND i.storeLocation.storeLocationId = ?1"
			+ "   AND i.upcMaster.upcMasterId IN ?2"
	)
	List<InventoryModel> findByStoreLocationIdAndUpcMasterIdIn(Long storeLocationId, List<Long> upcMasterIdList);
}
