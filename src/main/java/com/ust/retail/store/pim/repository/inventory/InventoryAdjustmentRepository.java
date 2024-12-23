package com.ust.retail.store.pim.repository.inventory;

import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationDetailDTO;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryAdjustmentRepository extends JpaRepository<InventoryAdjustmentModel, Long> {

	@Query(value = "SELECT a"
			+ " FROM InventoryAdjustmentModel a"
			+ "   JOIN a.details d"
			+ "   JOIN d.upcMaster p"
			+ " WHERE a.endTime IS NOT NULL"
			+ "   AND(?1 = NULL OR p.brandOwner.brandOwnerId = ?1)"
			+ "   AND (?2 = NULL OR p.productType.catalogId = ?2)"
			+ "   AND (?3 = NULL OR p.productGroup.productGroupId = ?3)"
			+ "   AND (?4 = NULL OR p.productCategory.productCategoryId = ?4)"
			+ "   AND (?5 = NULL OR p.productName LIKE %?5%)"
			+ "   AND ((?6 = NULL AND a.status IS NOT NULL) OR a.status.catalogId = ?6)"
			+ " GROUP BY a"
	)
	Page<InventoryAdjustmentModel> findByFilters(Long brandOwnerId, Long productTypeId, Long productGroupId, Long productCategoryId, String productName, Long statusId, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationDetailDTO("
			+ "        d.inventoryAdjustmentDetailId, "
			+ "        c.productCategoryName, "
			+ "        p.productName, "
			+ "        p.principalUpc, "
			+ "        p.productType.catalogOptions, "
			+ "        d.setQty, "
			+ "        p.inventoryUnit.catalogOptions, "
			+ "        ias.qty, "
			+ "        sr.catalogOptions, "
			+ "        ias.evidence, "
			+ "        st.catalogId, "
			+ "        st.catalogOptions, "
			+ "        ih.txnNum,"
			+ "        ih.inventoryHistoryId)"
			+ " FROM InventoryAdjustmentDetailModel d"
			+ "   JOIN d.upcMaster p"
			+ "   JOIN p.productCategory c"
			+ "   JOIN d.inventoryAdjustment ia"
			+ "   JOIN InventoryModel i ON d.upcMaster = i.upcMaster"
			+ "     AND ia.storeLocation = i.storeLocation"
			+ "   JOIN InventoryHistoryModel ih ON ia.inventoryAdjustmentId = ih.referenceId"
			+ "     AND i = ih.inventory"
			+ "     AND ih.operationModule.catalogId = ?2"
			+ "     AND ih.operationType.catalogId = ?3"
			+ "   JOIN ih.authorizationStatus st"
			+ "   LEFT JOIN InventoryAdjustmentShrinkageModel ias ON ia = ias.inventoryAdjustment"
			+ "     AND d.upcMaster = ias.upcMaster"
			+ "   LEFT JOIN ias.shrinkageReason sr"
			+ " WHERE ia.inventoryAdjustmentId = ?1"
	)
	List<InventoryAdjustmentAuthorizationDetailDTO> findAdjustmentDetailWithStatus(Long adjustmentId, Long operationModule, Long operationType);
}
