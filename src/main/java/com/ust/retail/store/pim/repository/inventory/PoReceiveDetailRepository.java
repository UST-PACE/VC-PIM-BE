package com.ust.retail.store.pim.repository.inventory;

import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResumeResponseDetailDTO;
import com.ust.retail.store.pim.model.inventory.PoReceiveDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PoReceiveDetailRepository extends JpaRepository<PoReceiveDetailModel, Long> {

	@Query("SELECT new com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResumeResponseDetailDTO("
			+ "a.poReceiveDetailId,"
			+ "a.storeLocation.storeLocationId,"
			+ "a.purchaseOrderDetail.upcMaster.productName,"
			+ "a.purchaseOrderDetail.upcMaster.principalUpc,"
			+ "a.batchNumber,"
			+ "a.userCreate.nameDesc,"
			+ "a.purchaseOrderDetail.totalAmount,"
			+ "a.receivedQty,"
			+ "a.error)"
			+ " FROM PoReceiveDetailModel a"
			+ " WHERE a.purchaseOrderDetail.purchaseOrder.purchaseOrderId=?1")
	List<ReceivingResumeResponseDetailDTO> findByPurchaseOrderDetailPurchaseOrderPurchaseOrderId(Long purchaseOrderId);

	PoReceiveDetailModel findByPurchaseOrderDetailPurchaseOrderDetailId(Long purchaseOrderDetailId);

	@Query("FROM PoReceiveDetailModel rd"
			+ "   JOIN FETCH rd.purchaseOrderDetail pod"
			+ "   JOIN FETCH pod.purchaseOrder"
			+ "   JOIN FETCH pod.upcMaster"
			+ "   JOIN FETCH pod.itemStatus"
			+ " WHERE rd.createdAt BETWEEN ?1 AND ?2")
	List<PoReceiveDetailModel> getReceivedReport(Date startDate, Date endDate);
}
