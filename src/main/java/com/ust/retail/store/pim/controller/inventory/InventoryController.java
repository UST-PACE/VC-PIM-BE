package com.ust.retail.store.pim.controller.inventory;

import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.inventory.InventoryFiltersDTO;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/inventory/p/")
@Validated
public class InventoryController extends BaseController {

	private InventoryService inventoryService;

	@Autowired
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/authorization/txtnum/{txtNum}")
	public boolean authorizeByTxnNum(
			@PathVariable(value = "txtNum") Long txtNum) {
		return inventoryService.authorizeByTxnNumber(txtNum);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/authorization/inventory/history/id/{inventoryHistoryId}")
	public boolean authorizeById(
			@PathVariable(value = "inventoryHistoryId") Long inventoryHistoryId) {
		return inventoryService.authorizeByInventoryHistoryId(inventoryHistoryId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/authorization/referenceId/{referenceId}/moduleId/{operationModuleId}")
	public boolean authorize(
			@PathVariable(value = "referenceId") Long referenceId, @PathVariable(value = "operationModuleId") Long operationModuleId) {
		return inventoryService.authorize(referenceId, operationModuleId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/reject/txtnum/{txtNum}")
	public boolean rejectByTxnNum(
			@PathVariable(value = "txtNum") Long txtNum) {
		return inventoryService.rejectByTxnNumber(txtNum);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/reject/referenceId/{referenceId}/moduleId/{operationModuleId}")
	public boolean reject(
			@PathVariable(value = "referenceId") Long referenceId, @PathVariable(value = "operationModuleId") Long operationModuleId) {
		return inventoryService.rejectByReferenceId(referenceId, operationModuleId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/reject/inventory/history/id/{inventoryHistoryId}")
	public boolean rejectById(
			@PathVariable(value = "inventoryHistoryId") Long inventoryHistoryId) {
		return inventoryService.rejectByInventoryHistoryId(inventoryHistoryId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<InventoryFiltersDTO> findInventoryByFilters(@Valid @RequestBody InventoryFiltersDTO inventoryFilterDTO) {
		return inventoryService.findInventory(inventoryFilterDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/filter/summary")
	@Validated(OnFilter.class)
	public Page<InventoryFiltersDTO> findInventoryByFiltersSummary(@Valid @RequestBody InventoryFiltersDTO inventoryFilterDTO) {
		return inventoryService.findInventorySummary(inventoryFilterDTO);
	}
}
