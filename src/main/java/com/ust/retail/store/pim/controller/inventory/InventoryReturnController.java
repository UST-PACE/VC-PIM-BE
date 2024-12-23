package com.ust.retail.store.pim.controller.inventory;

import com.ust.retail.store.pim.common.annotations.OnAdjustInventory;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.catalogs.ReturnWarningsCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryProductDTO;
import com.ust.retail.store.pim.dto.inventory.returns.operation.ReturnDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.InventoryReturnItemDetailDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.InventoryReturnSumaryDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.LoadItemReturnInventoryDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.ReturnItemDTO;
import com.ust.retail.store.pim.service.inventory.InventoryReturnService;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/inventory/p/returns/")
@Validated
public class InventoryReturnController extends BaseController {

	private InventoryReturnService inventoryReturnService;
	private InventoryService inventoryService;
	private ReturnWarningsCatalog returnWarningsCatalog;

	@Autowired
	public InventoryReturnController(InventoryReturnService inventoryReturnService,
									 InventoryService inventoryService,
									 ReturnWarningsCatalog returnWarningsCatalog) {

		super();
		this.inventoryReturnService = inventoryReturnService;
		this.inventoryService = inventoryService;
		this.returnWarningsCatalog = returnWarningsCatalog;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/start")
	@Validated(OnCreate.class)
	public ReturnDTO startReturns() {
		return inventoryReturnService.start(new ReturnDTO());

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/id/{inventoryProductReturnId}/load/code/{code}/storeloc/{storeLocationid}")
	@Validated(OnCreate.class)
	public LoadItemReturnInventoryDTO loadItem(
			@Valid @PathVariable("inventoryProductReturnId") Long inventoryProductReturnId,
			@Valid @PathVariable("code") String code,
			@Valid @PathVariable("storeLocationid") Long storeLocationId) {

		InventoryProductDTO inventoryProductDTO = inventoryService.findInventoryByCodeAndStoreLocation(code, storeLocationId);

		InventoryReturnSumaryDTO returnSummary = inventoryReturnService.getReturnSummary(inventoryProductReturnId);

		InventoryReturnItemDetailDTO previousDetail = null;

		if (returnSummary.getSumary() != null) {
			previousDetail = returnSummary.getSumary().values().stream()
					.flatMap(Collection::stream)
					.filter(detail -> Objects.equals(detail.getUpcMasterId(), inventoryProductDTO.getUpcMasterModel().getUpcMasterId()))
					.findFirst()
					.orElse(null);
		}

		return new LoadItemReturnInventoryDTO(
				inventoryProductDTO.getInventoryModel().getInventoryId(),
				inventoryProductDTO.getUpcMasterModel().getPrincipalUpc(),
				inventoryProductDTO.getInventoryModel().getStoreLocation().getStoreLocationId(),
				inventoryProductDTO.getUpcMasterModel().getProductName(),
				inventoryProductDTO.getUpcMasterModel().getUpcMasterId(),
				previousDetail);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/return")
	@Validated(OnAdjustInventory.class)
	public Boolean adjustInventory(@Valid @RequestBody ReturnItemDTO returnItemDTO) {
		return inventoryReturnService.returnItems(returnItemDTO);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/finish")
	@Validated(OnUpdate.class)
	public boolean finishInventoryAdjustment(@Valid @RequestBody ReturnDTO returnDTO) {
		return inventoryReturnService.finish(returnDTO.getInventoryProductReturnId());

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/sumary/id/{inventoryReturnId}")
	public InventoryReturnSumaryDTO loadSumary(
			@Valid @PathVariable("inventoryReturnId") Long inventoryReturnItemDetailDTO) {

		return inventoryReturnService.getReturnSummary(inventoryReturnItemDetailDTO);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/load/returnwarnings")
	public List<CatalogDTO> loadReturnWarningsCatalog() {
		return returnWarningsCatalog.getCatalogOptions();
	}

}
