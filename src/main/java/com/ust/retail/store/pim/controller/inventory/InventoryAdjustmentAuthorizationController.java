package com.ust.retail.store.pim.controller.inventory;

import com.ust.retail.store.pim.common.annotations.OnAuthorize;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnReject;
import com.ust.retail.store.pim.common.catalogs.InventoryAdjustmentStatusCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationFilterDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationFilterResultDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationRequestDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationResultDTO;
import com.ust.retail.store.pim.service.inventory.InventoryAdjustmentAuthorizationService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/inventory/p/adjustment/authorization")
@Validated
public class InventoryAdjustmentAuthorizationController {
	private final InventoryAdjustmentStatusCatalog inventoryAdjustmentStatusCatalog;
	private final InventoryAdjustmentAuthorizationService inventoryAdjustmentAuthorizationService;

	public InventoryAdjustmentAuthorizationController(
			InventoryAdjustmentStatusCatalog inventoryAdjustmentStatusCatalog,
			InventoryAdjustmentAuthorizationService inventoryAdjustmentAuthorizationService) {
		this.inventoryAdjustmentStatusCatalog = inventoryAdjustmentStatusCatalog;
		this.inventoryAdjustmentAuthorizationService = inventoryAdjustmentAuthorizationService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/catalog/status")
	public List<CatalogDTO> loadInventoryAdjustmentStatusCatalog() {
		return inventoryAdjustmentStatusCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<InventoryAdjustmentAuthorizationFilterResultDTO> findByFilters(@Valid @RequestBody InventoryAdjustmentAuthorizationFilterDTO dto) {
		return inventoryAdjustmentAuthorizationService.getInventoryAdjustmentsByFilters(dto);
	}

	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/find/id/{id}")
	public InventoryAdjustmentAuthorizationDTO findById(@PathVariable(value = "id") Long adjustmentId) {
		return inventoryAdjustmentAuthorizationService.findById(adjustmentId);
	}

	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/authorize")
	@Validated(OnAuthorize.class)
	public InventoryAdjustmentAuthorizationResultDTO authorizeAdjustments(@Valid @RequestBody InventoryAdjustmentAuthorizationRequestDTO dto) {
		return inventoryAdjustmentAuthorizationService.authorize(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/reject")
	@Validated(OnReject.class)
	public InventoryAdjustmentAuthorizationResultDTO rejectAdjustments(@Valid @RequestBody InventoryAdjustmentAuthorizationRequestDTO dto) {
		return inventoryAdjustmentAuthorizationService.decline(dto);
	}
}
