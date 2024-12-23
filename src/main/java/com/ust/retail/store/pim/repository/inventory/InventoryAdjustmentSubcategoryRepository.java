package com.ust.retail.store.pim.repository.inventory;

import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentCategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryAdjustmentSubcategoryRepository extends JpaRepository<InventoryAdjustmentCategoryModel, Long> {

	@Query("SELECT a FROM InventoryAdjustmentCategoryModel a"
			+ " WHERE a.inventoryAdjustment.storeLocation.storeLocationId = ?1"
			+ " AND a.productCategory.productCategoryId in ?2"
			+ " AND a.dailyCountStatus.catalogId = ?3")
	List<InventoryAdjustmentCategoryModel> getActiveCountsBySubcategory(Long storeLocationId, List<Long> subcategoryIdList, Long dailyCountStatusId);

	@Modifying
	@Query("UPDATE InventoryAdjustmentCategoryModel a"
			+ " SET a.dailyCountStatus.catalogId=?3"
			+ " WHERE a.inventoryAdjustment.storeLocation.storeLocationId = ?1"
			+ " AND a.productCategory.productCategoryId=?2"
			+ " AND a.dailyCountStatus.catalogId=14000")
	void interruptDailyCount(Long storeLocationId, Long subcategoryId, Long dailyCountStatusId);
}
