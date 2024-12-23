package com.ust.retail.store.pim.repository.inventory;

import com.ust.retail.store.pim.dto.external.report.sale.ExternalAdjustmentReportDTO;
import com.ust.retail.store.pim.dto.external.report.sale.ExternalInventoryReportDTO;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InventoryExternalReportRepository extends JpaRepository<InventoryModel, Long> {

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.external.report.sale.ExternalInventoryReportDTO("
			+ "p.upcMasterId,"
			+ "p.principalUpc,"
			+ "SUM(i.qty),"
			+ "AVG(COALESCE(sc.cost, 0d)))"
			+ " FROM UpcMasterModel p"
			+ "   JOIN InventoryModel i ON p = i.upcMaster"
			+ "   JOIN i.storeLocation l"
			+ "   LEFT JOIN UpcVendorDetailsModel uvd ON p = uvd.upcMaster"
			+ "   LEFT JOIN UpcVendorStoreCostModel sc ON sc.upcVendorDetail = uvd AND sc.storeNumber.storeNumId = ?1"
			+ " WHERE l.storeNumber.storeNumId = ?1"
			+ "   AND p.principalUpc IN ?2"
			+ "   AND p.upcMasterType.catalogId = ?3"
			+ "   AND p.productType.catalogId = ?4"
			+ " GROUP BY p.upcMasterId"
	)
	List<ExternalInventoryReportDTO> getGroceryOnHandInventoryReport(Long storeNumberId, List<String> upcList, Long pimUpcMasterTypeId, Long fgProductTypeId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.external.report.sale.ExternalInventoryReportDTO("
			+ "p.upcMasterId,"
			+ "p.principalUpc,"
			+ "SUM(i.qty),"
			+ "0d)"
			+ " FROM UpcMasterModel p"
			+ "   JOIN RecipeModel r ON p = r.relatedUpcMaster"
			+ "   JOIN InventoryModel i ON p = i.upcMaster"
			+ "   JOIN i.storeLocation l"
			+ " WHERE l.storeNumber.storeNumId = ?1"
			+ "   AND p.principalUpc IN ?2"
			+ "   AND p.upcMasterType.catalogId = ?3"
			+ "   AND p.productType.catalogId = ?4"
			+ " GROUP BY p.upcMasterId"
	)
	List<ExternalInventoryReportDTO> getBistroOnHandInventoryReport(Long storeNumberId, List<String> upcList, Long bistroUpcMasterTypeId, Long fgProductTypeId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.external.report.sale.ExternalAdjustmentReportDTO("
			+ "p.principalUpc,"
			+ "ih.operationQty,"
			+ "ih.finalQty,"
			+ "AVG(sc.cost))"
			+ " FROM InventoryHistoryModel ih"
			+ "   JOIN ih.inventory.upcMaster p"
			+ "   JOIN UpcVendorDetailsModel uvd ON p = uvd.upcMaster"
			+ "   LEFT JOIN UpcVendorStoreCostModel sc ON sc.upcVendorDetail = uvd AND sc.storeNumber.storeNumId = ih.inventory.storeLocation.storeNumber.storeNumId"
			+ " WHERE ih.operationQty > 0"
			+ "   AND ih.createdAt BETWEEN ?1 AND ?2"
			+ "   AND ih.authorizationStatus.catalogId = ?3"
			+ "   AND ih.operationType.catalogId = ?4"
			+ " GROUP BY p.upcMasterId, ih.operationQty, ih.finalQty"
	)
	List<ExternalAdjustmentReportDTO> getShrinkageReportForDates(Date start, Date end, Long authorizedStatusId, Long shrinkageOperationTypeId);
}
