package com.ust.retail.store.pim.service.external;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.pim.common.StoreFacade;
import com.ust.retail.store.pim.dto.external.sale.ExternalReturnItemListRequestDTO;
import com.ust.retail.store.pim.service.inventory.InventoryService;

@Service
public class ExternalItemReturnService {
	private final InventoryService inventoryService;
	private final StoreFacade storeFacade;

	public ExternalItemReturnService(InventoryService inventoryService, StoreFacade storeFacade) {
		this.inventoryService = inventoryService;
		this.storeFacade = storeFacade;
	}

	@Transactional
	public String process(ExternalReturnItemListRequestDTO request) {
		inventoryService.customerReturnInventory(
				request.toInventoryProductReturnDTO(storeFacade.getStoreLocationForSales(request.getStoreNumId())));
		return "success";
	}
}
