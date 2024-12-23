package com.ust.retail.store.pim.service.purchaseorder;

import com.ust.retail.store.pim.common.catalogs.ItemStatusCatalog;
import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.controller.purchaseorder.PurchaseOrderController;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDetailDTO;

@Component
public class PurchaseOrderFacade {
	private final PurchaseOrderController purchaseOrderController;
	private final PurchaseOrderDetailService purchaseOrderDetailService;

	public PurchaseOrderFacade(PurchaseOrderController purchaseOrderController,
							   PurchaseOrderDetailService purchaseOrderDetailService) {
		this.purchaseOrderController = purchaseOrderController;
		this.purchaseOrderDetailService = purchaseOrderDetailService;
	}

	public void removeUpcFromPurchaseOrders(Long upcMasterId) {
		
		purchaseOrderDetailService.findByUpcMasterIdInModifiableOrders(upcMasterId).stream()
				.filter(detail -> detail.getItemStatusId() != ItemStatusCatalog.ITEM_STATUS_RECEIVED)
				.map(PurchaseOrderDetailDTO::getPurchaseOrderDetailId)
				.forEach(purchaseOrderController::removeLineItem);
		
	}
}
