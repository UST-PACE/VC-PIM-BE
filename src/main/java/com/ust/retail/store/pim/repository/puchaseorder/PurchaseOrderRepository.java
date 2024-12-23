package com.ust.retail.store.pim.repository.puchaseorder;

import com.ust.retail.store.pim.dto.inventory.reception.screens.PurchaseOrdersDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderFilterResultDTO;
import com.ust.retail.store.pim.dto.report.PurchaseOrderDraftedDTO;
import com.ust.retail.store.pim.dto.report.PurchaseOrderOrderedDTO;
import com.ust.retail.store.pim.dto.report.PurchaseOrderPendingApprovalDTO;
import com.ust.retail.store.pim.dto.report.PurchaseOrderReceptionDTO;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderModel, Long> {
	String FIND_BY_FILTERS_FROM_CLAUSE = " FROM PurchaseOrderModel p"
			+ " JOIN p.vendorMaster"
			+ " JOIN p.storeNumber"
			+ " JOIN p.status"
			+ " JOIN p.userCreate"
			+ " LEFT JOIN p.details d"
			+ " LEFT JOIN d.upcMaster um";
	String FIND_BY_FILTERS_WHERE_CLAUSE = " WHERE (?1 = NULL OR p.vendorMaster.vendorMasterId = ?1)"
			+ "    AND (?2 = NULL OR p.vendorMaster.vendorCode LIKE %?2%)"
			+ "    AND (?3 = NULL OR p.vendorMaster.vendorName LIKE %?3%)"
			+ "    AND (?4 = NULL OR p.storeNumber.storeNumId = ?4)"
			+ "    AND (?5 = NULL OR p.purchaseOrderNum LIKE %?5%)"
			+ "    AND (?6 = NULL OR COALESCE(um.principalUpc, '') LIKE %?6%)"
			+ "    AND (?7 = NULL OR COALESCE(um.productName, '') LIKE %?7%)"
			+ "    AND (?8 = NULL OR p.status.catalogId = ?8)"
			+ "    AND (?9 = NULL OR p.sentAt >= ?9)"
			+ "    AND (?10 = NULL OR p.sentAt <= ?10)"
			+ "    AND (?11 = NULL OR p.createdAt >= ?11)"
			+ "    AND (?12 = NULL OR p.createdAt <= ?12)"
			+ " GROUP BY p";


	@Query("from PurchaseOrderDetailModel d where d.purchaseOrder.purchaseOrderId = ?1")
	Page<PurchaseOrderDetailModel> getOrderDetailPage(Long purchaseOrderId, Pageable pageable);

	Optional<PurchaseOrderModel> findByDetailsPurchaseOrderDetailId(Long purchaseOrderDetailId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderFilterResultDTO("
			+ "p.purchaseOrderId, "
			+ "p.purchaseOrderNum, "
			+ "p.userCreate.nameDesc, "
			+ "p.status.catalogId, "
			+ "p.status.catalogOptions, "
			+ "p.createdAt, "
			+ "p.sentAt, "
			+ "p.eta, "
			+ "p.finalCost, "
			+ "COALESCE(p.appliedVendorCredit, 0))"
			+ FIND_BY_FILTERS_FROM_CLAUSE
			+ FIND_BY_FILTERS_WHERE_CLAUSE,
			countQuery = "SELECT COUNT(DISTINCT p)"
					+ FIND_BY_FILTERS_FROM_CLAUSE
					+ FIND_BY_FILTERS_WHERE_CLAUSE)
	Page<PurchaseOrderFilterResultDTO> findByFilters(
			Long vendorMasterId,
			String vendorMasterCode,
			String vendorMasterName,
			Long storeNumId,
			String purchaseOrderNum,
			String principalUpc,
			String productName,
			Long statusId,
			Date startSendDate,
			Date endSendDate,
			Date startCreateDate,
			Date endCreateDate,
			Pageable pageable
	);

	Optional<PurchaseOrderModel> findByPurchaseOrderNum(String purchaseOrderNumber);

	@Query("SELECT new com.ust.retail.store.pim.dto.inventory.reception.screens.PurchaseOrdersDTO("
			+ "a.purchaseOrderId,"
			+ "a.purchaseOrderNum,"
			+ "a.vendorMaster.vendorName,"
			+ "a.eta,"
			+ "a.status.catalogId,"
			+ "a.status.catalogOptions)"
			+ " FROM PurchaseOrderModel a"
			+ " WHERE a.eta BETWEEN ?1 AND ?2"
			+ " AND a.status.catalogId IN ?3")
	List<PurchaseOrdersDTO> findByDate(Date d1, Date d2, List<Long> purchaseStatusIds);

	List<PurchaseOrderModel> findByPurchaseOrderNumContaining(String poNumber);

	@Query("SELECT new com.ust.retail.store.pim.dto.inventory.reception.screens.PurchaseOrdersDTO("
			+ "a.purchaseOrderId,"
			+ "a.purchaseOrderNum,"
			+ "a.vendorMaster.vendorName,"
			+ "a.eta,"
			+ "a.status.catalogId,"
			+ "a.status.catalogOptions)"
			+ " FROM PurchaseOrderModel a"
			+ " WHERE a.purchaseOrderNum LIKE %?1%"
			+ " AND a.status.catalogId IN ?2"
			+ " ORDER BY a.eta DESC, a.purchaseOrderId DESC"
	)
	List<PurchaseOrdersDTO> findOrdersByNumberAndStatus(String purchaseOrderNumber, List<Long> poStatusList);

	@Query(value = "SELECT MAX(po.purchaseOrderNum) FROM PurchaseOrderModel po")
	Optional<String> findLastPurchaseOrderNum();

	@Query(value = "FROM PurchaseOrderModel po"
			+ "   JOIN FETCH po.vendorMaster"
			+ "   JOIN FETCH po.storeNumber"
			+ "   JOIN FETCH po.userCreate"
			+ "   JOIN FETCH po.status"
			+ " WHERE po.createdAt BETWEEN ?1 AND ?2")
	List<PurchaseOrderModel> getGeneratedPurchaseOrdersReport(Date startDate, Date endDate);


	@Query(value = "SELECT SUM(od.finalCost)"
			+ " FROM PurchaseOrderDetailModel od"
			+ " JOIN od.purchaseOrder po"
			+ " WHERE po.status.catalogId IN ?1"
			+ " AND po.storeNumber.storeNumId = ?2"
	)
	Double getCostTotalForStatus(List<Long> statusList, Long storeNumId);

	@Query(value = "SELECT"
			+ " COUNT(po)"
			+ " FROM PurchaseOrderModel po"
			+ " WHERE function('date_format', po.createdAt, '%U') = function('date_format', CURRENT_DATE, '%U')"
			+ "   AND po.storeNumber.storeNumId = ?1"
	)
	Integer getOrdersWeekToDate(Long storeNumId);

	@Query(value = "SELECT"
			+ " COUNT(po)"
			+ " FROM PurchaseOrderModel po"
			+ " WHERE function('date_format', po.createdAt, '%Y-%m') = function('date_format', CURRENT_DATE, '%Y-%m')"
			+ "   AND po.storeNumber.storeNumId = ?1"
	)
	Integer getOrdersMonthToDate(Long storeNumId);

	@Query(value = "SELECT SUM(COALESCE(sp.salePrice, 0))"
			+ " FROM PurchaseOrderDetailModel pod"
			+ "   JOIN pod.purchaseOrder po"
			+ "   JOIN pod.upcMaster upc"
			+ "   LEFT JOIN upc.storePrices sp ON sp.storeNumber = po.storeNumber"
			+ " WHERE pod.purchaseOrder.purchaseOrderId = ?1")
	Double getOrderRetailPrice(Long purchaseOrderId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.PurchaseOrderDraftedDTO("
			+ "    po.purchaseOrderId,"
			+ "    po.purchaseOrderNum,"
			+ "    po.vendorMaster.vendorName,"
			+ "    po.finalCost,"
			+ "    po.userCreate.nameDesc,"
			+ "    po.createdAt"
			+ ")"
			+ " FROM PurchaseOrderModel po"
			+ " WHERE po.status.catalogId = ?1"
			+ "   AND po.storeNumber.storeNumId = ?2"
			+ " ORDER BY po.createdAt DESC"
	)
	List<PurchaseOrderDraftedDTO> getOrdersDrafted(Long draftPoStatusId, Long storeNumId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.PurchaseOrderDraftedDTO("
			+ "    po.purchaseOrderId,"
			+ "    po.purchaseOrderNum,"
			+ "    po.vendorMaster.vendorName,"
			+ "    po.finalCost,"
			+ "    po.userCreate.nameDesc,"
			+ "    po.createdAt"
			+ ")"
			+ " FROM PurchaseOrderModel po"
			+ " WHERE po.status.catalogId = ?1"
			+ "   AND po.storeNumber.storeNumId = ?2"
	)
	Page<PurchaseOrderDraftedDTO> getOrdersDrafted(Long draftPoStatusId, Long storeNumId, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.PurchaseOrderOrderedDTO("
			+ "    po.purchaseOrderId,"
			+ "    po.purchaseOrderNum,"
			+ "    po.vendorMaster.vendorName,"
			+ "    po.finalCost,"
			+ "    po.userCreate.nameDesc,"
			+ "    po.createdAt"
			+ ")"
			+ " FROM PurchaseOrderModel po"
			+ " WHERE po.status.catalogId = ?1"
			+ "   AND po.storeNumber.storeNumId = ?2"
			+ " ORDER BY po.createdAt DESC"
	)
	List<PurchaseOrderOrderedDTO> getOrdersOrdered(Long poStatusOrdered, Long storeNumId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.PurchaseOrderOrderedDTO("
			+ "    po.purchaseOrderId,"
			+ "    po.purchaseOrderNum,"
			+ "    po.vendorMaster.vendorName,"
			+ "    po.finalCost,"
			+ "    po.userCreate.nameDesc,"
			+ "    po.createdAt"
			+ ")"
			+ " FROM PurchaseOrderModel po"
			+ " WHERE po.status.catalogId = ?1"
			+ "   AND po.storeNumber.storeNumId = ?2"
	)
	Page<PurchaseOrderOrderedDTO> getOrdersOrdered(Long poStatusOrdered, Long storeNumId, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.PurchaseOrderPendingApprovalDTO("
			+ "    po.purchaseOrderId,"
			+ "    po.purchaseOrderNum,"
			+ "    po.vendorMaster.vendorName,"
			+ "    po.finalCost,"
			+ "    po.userCreate.nameDesc,"
			+ "    po.createdAt"
			+ ")"
			+ " FROM PurchaseOrderModel po"
			+ " WHERE po.status.catalogId = ?1"
			+ "   AND po.storeNumber.storeNumId = ?2"
			+ " ORDER BY po.createdAt DESC"
	)
	List<PurchaseOrderPendingApprovalDTO> getOrdersPendingApproval(Long poStatusPendingAuthorization, Long storeNumId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.PurchaseOrderPendingApprovalDTO("
			+ "    po.purchaseOrderId,"
			+ "    po.purchaseOrderNum,"
			+ "    po.vendorMaster.vendorName,"
			+ "    po.finalCost,"
			+ "    po.userCreate.nameDesc,"
			+ "    po.createdAt"
			+ ")"
			+ " FROM PurchaseOrderModel po"
			+ " WHERE po.status.catalogId = ?1"
			+ "   AND po.storeNumber.storeNumId = ?2"
			+ " ORDER BY po.createdAt DESC"
	)
	Page<PurchaseOrderPendingApprovalDTO> getOrdersPendingApproval(Long poStatusPendingAuthorization, Long storeNumId, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.PurchaseOrderReceptionDTO("
			+ "    pod.upcMaster.principalUpc,"
			+ "    pod.upcMaster.productName,"
			+ "    pod.purchaseOrder.vendorMaster.vendorName,"
			+ "    pod.productCost * rd.receivedQty,"
			+ "    sp.salePrice * rd.receivedQty,"
			+ "    (sp.salePrice * rd.receivedQty) - (pod.productCost * rd.receivedQty),"
			+ "    rd.receivedQty,"
			+ "    rd.userCreate.nameDesc,"
			+ "    rd.createdAt,"
			+ "    CASE WHEN rd.error = true THEN 'Partial' ELSE 'Complete' END"
			+ ")"
			+ " FROM PoReceiveDetailModel rd"
			+ "   JOIN rd.purchaseOrderDetail pod"
			+ "   JOIN pod.purchaseOrder po"
			+ "   JOIN pod.upcMaster upc"
			+ "   JOIN upc.storePrices sp ON sp.storeNumber = po.storeNumber"
			+ " WHERE po.status.catalogId IN ?1 "
			+ "   AND po.storeNumber.storeNumId = ?2"
			+ " ORDER BY rd.createdAt DESC"
	)
	List<PurchaseOrderReceptionDTO> getReceptionDetails(List<Long> poStatusCompleted, Long storeNumId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.PurchaseOrderReceptionDTO("
			+ "    pod.upcMaster.principalUpc,"
			+ "    pod.upcMaster.productName,"
			+ "    pod.purchaseOrder.vendorMaster.vendorName,"
			+ "    pod.productCost * rd.receivedQty,"
			+ "    sp.salePrice * rd.receivedQty,"
			+ "    (sp.salePrice * rd.receivedQty) - (pod.productCost * rd.receivedQty),"
			+ "    rd.receivedQty,"
			+ "    rd.userCreate.nameDesc,"
			+ "    rd.createdAt,"
			+ "    CASE WHEN rd.error = true THEN 'Partial' ELSE 'Complete' END"
			+ ")"
			+ " FROM PoReceiveDetailModel rd"
			+ "   JOIN rd.purchaseOrderDetail pod"
			+ "   JOIN pod.purchaseOrder po"
			+ "   JOIN pod.upcMaster upc"
			+ "   JOIN upc.storePrices sp ON sp.storeNumber = po.storeNumber"
			+ " WHERE po.status.catalogId IN ?1 "
			+ "   AND po.storeNumber.storeNumId = ?2"
	)
	Page<PurchaseOrderReceptionDTO> getReceptionDetails(List<Long> poStatusCompleted, Long storeNumId, Pageable pageable);
}
