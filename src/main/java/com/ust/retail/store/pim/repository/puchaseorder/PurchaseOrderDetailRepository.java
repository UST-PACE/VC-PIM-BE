package com.ust.retail.store.pim.repository.puchaseorder;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetailModel, Long> {
	Optional<PurchaseOrderDetailModel> findByPurchaseOrderPurchaseOrderIdAndUpcMasterPrincipalUpc(Long purchaseOrderId, String principalUPC);

	List<PurchaseOrderDetailModel> findByUpcMasterUpcMasterIdAndPurchaseOrderStatusCatalogIdNotIn(Long upcMasterId, List<Long> finalizedStatusList);

}
