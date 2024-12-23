package com.ust.retail.store.pim.service.external;

import com.ust.retail.store.pim.common.StoreFacade;
import com.ust.retail.store.pim.dto.external.sale.ExternalSoldItemListRequestDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventorySalesDTO;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExternalItemSaleService {
	private final InventoryService inventoryService;
	private final StoreFacade storeFacade;

	public ExternalItemSaleService(InventoryService inventoryService, StoreFacade storeFacade) {
		this.inventoryService = inventoryService;
		this.storeFacade = storeFacade;
	}

	@Transactional
	public String process(ExternalSoldItemListRequestDTO request) {

		inventoryService.inventorySales(
				request.toInventorySalesDTO(storeFacade.getStoreLocationForSales(request.getStoreNumId())));
		return "success";
	}

	@Transactional
	public String processBistro(ExternalSoldItemListRequestDTO request) {
		InventorySalesDTO inventorySales = request.toInventorySalesDTO(storeFacade.getStoreLocationForSales(request.getStoreNumId()));
		inventoryService.reduceBistroInventory(inventorySales.getItems(), inventorySales.getExternalReferenceId());
		return "success";
	}
}
