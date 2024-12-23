package com.ust.retail.store.pim.controller.inventory;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.inventory.InventoryTransferencesDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryTransferencesFiltersDTO;
import com.ust.retail.store.pim.service.inventory.InventoryTransferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/inventory/p/transference/")
@Validated
public class InventoryTransferenceController extends BaseController {

	private InventoryTransferenceService inventoryTransferenceService;

	@Autowired
	public InventoryTransferenceController(InventoryTransferenceService inventoryTransferenceService) {

		super();
		this.inventoryTransferenceService = inventoryTransferenceService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/register")
	@Validated(OnCreate.class)
	public InventoryTransferencesDTO registerTransference(@Valid @RequestBody InventoryTransferencesDTO transferenceDTO) {

		return inventoryTransferenceService.saveOrUpdate(transferenceDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/find/id/{id}")
	public InventoryTransferencesDTO findById(@PathVariable(value = "id") Long inventoryTransferId) {
		return inventoryTransferenceService.findById(inventoryTransferId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<InventoryTransferencesFiltersDTO> findByFilters(@Valid @RequestBody InventoryTransferencesFiltersDTO inventoryTransferencesFiltersDTO) {
		return inventoryTransferenceService.getTransferenceByFilters(inventoryTransferencesFiltersDTO);
	}
}
