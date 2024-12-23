package com.ust.retail.store.pim.repository.inventory;

import com.ust.retail.store.pim.dto.productreturn.ProductReturnDetailDTO;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.List;

@Repository
public interface InventoryProductReturnRepository extends JpaRepository<InventoryProductReturnModel, Long> {

	@Query(value = "SELECT r"
			+ " FROM InventoryProductReturnModel r"
			+ "   JOIN r.returnDetails d"
			+ "   JOIN d.upcMaster u"
			+ "   LEFT JOIN d.vendorMaster v"
			+ " WHERE r.finishAt IS NOT NULL"
			+ "   AND (?1 = NULL OR COALESCE(v.vendorName, 'Not Assigned' ) LIKE %?1%)"
			+ "   AND (?2 = NULL OR COALESCE(v.vendorCode, 'Not Assigned' ) LIKE %?2%)"
			+ "   AND (?3 = NULL OR u.brandOwner.brandOwnerId = ?3)"
			+ "   AND (?4 = NULL OR u.productType.catalogId = ?4)"
			+ "   AND (?5 = NULL OR u.productGroup.productGroupId = ?5)"
			+ "   AND (?6 = NULL OR u.productCategory.productCategoryId = ?6)"
			+ "   AND (?7 = NULL OR u.productSubcategory.productSubcategoryId = ?7)"
			+ "   AND (?8 = NULL OR u.productItem.productItemId = ?8)"
			+ "   AND (?9 = NULL OR u.productName LIKE %?9%)"
			+ "   AND (?10 = NULL OR r.status.catalogId = ?10)"
			+ " GROUP BY r"
	)
	Page<InventoryProductReturnModel> findByFilters(
			String vendorName,
			String vendorCode,
			Long brandOwnerId,
			Long productTypeId,
			Long productGroupId,
			Long productCategoryId,
			Long productSubcategoryId,
			Long productItemId,
			String productName,
			Long statusId,
			Pageable pageable
	);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.productreturn.ProductReturnDetailDTO("
			+ "        d.inventoryProductReturnDetailId, "
			+ "        v.vendorName, "
			+ "        p.productName, "
			+ "        d.batchNumber, "
			+ "        sn.storeName, "
			+ "        sl.storeLocationName, "
			+ "        d.qty, "
			+ "        rr.catalogOptions, "
			+ "        COALESCE(d.credit, 0), "
			+ "        st.catalogId, "
			+ "        st.catalogOptions, "
			+ "        p.upcMasterId, "
			+ "        ih.inventoryHistoryId)"
			+ " FROM InventoryProductReturnDetailModel d"
			+ "   JOIN d.upcMaster p"
			+ "   LEFT JOIN d.vendorMaster v"
			+ "   JOIN d.storeLocation sl"
			+ "   JOIN sl.storeNumber sn"
			+ "   JOIN d.returnReason rr"
			+ "   JOIN InventoryModel i ON p.upcMasterId = i.upcMaster.upcMasterId"
			+ "     AND d.storeLocation = i.storeLocation"
			+ "   JOIN InventoryHistoryModel ih ON d.productReturn.inventoryProductReturnId = ih.referenceId"
			+ "     AND i = ih.inventory"
			+ "     AND ih.operationModule.catalogId = ?2"
			+ "     AND ih.operationType.catalogId = ?3"
			+ "   JOIN ih.authorizationStatus st"
			+ " WHERE d.productReturn.inventoryProductReturnId = ?1"
	)
	List<ProductReturnDetailDTO> findCreditDetailWithStatus(Long returnId, Long operationModuleId, Long operationTypeId);

	@Query("SELECT"
			+ "  p.principalUpc,"
			+ "  p.productName,"
			+ "  sl.storeLocationName,"
			+ "  d.qty,"
			+ "  rr.catalogOptions,"
			+ "  v.vendorCode,"
			+ "  v.vendorName,"
			+ "  d.credit,"
			+ "  st.catalogOptions,"
			+ "  pr.createdAt"
			+ " FROM InventoryProductReturnDetailModel d"
			+ "   JOIN d.productReturn pr"
			+ "   JOIN d.upcMaster p"
			+ "   LEFT JOIN d.vendorMaster v"
			+ "   JOIN d.storeLocation sl"
			+ "   JOIN sl.storeNumber sn"
			+ "   JOIN d.returnReason rr"
			+ "   JOIN InventoryModel i ON p.upcMasterId = i.upcMaster.upcMasterId"
			+ "     AND d.storeLocation = i.storeLocation"
			+ "   JOIN InventoryHistoryModel ih ON d.productReturn.inventoryProductReturnId = ih.referenceId"
			+ "     AND i = ih.inventory"
			+ "     AND ih.operationModule.catalogId = ?3"
			+ "   JOIN ih.authorizationStatus st"
			+ " WHERE pr.createdAt BETWEEN ?1 AND ?2"
	)
	List<Tuple> getReturnsReport(Date startDate, Date endDate, Long operationModuleId);
}
