package com.ust.retail.store.pim.controller.inventory;

import com.ust.retail.store.pim.common.annotations.OnAdjustInventory;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.catalogs.DailyCountStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.ShrinkageReasonCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryProductDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.*;
import com.ust.retail.store.pim.service.inventory.InventoryAdjustmentService;
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
@RequestMapping(path = "/api/inventory/p/adjustment/")
@Validated
public class InventoryAdjustmentController extends BaseController {

	private final InventoryAdjustmentService inventoryAdjustmentService;
	private final InventoryService inventoryService;
	private final DailyCountStatusCatalog dailyCountStatusCatalog;
	private final ShrinkageReasonCatalog shrinkageReasonCatalog;

	@Autowired
	public InventoryAdjustmentController(InventoryAdjustmentService inventoryAdjustmentService,
										 InventoryService inventoryService,
										 DailyCountStatusCatalog dailyCountStatusCatalog,
										 ShrinkageReasonCatalog shrinkageReasonCatalog) {

		super();
		this.inventoryAdjustmentService = inventoryAdjustmentService;
		this.inventoryService = inventoryService;
		this.dailyCountStatusCatalog = dailyCountStatusCatalog;
		this.shrinkageReasonCatalog = shrinkageReasonCatalog;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/start")
	@Validated(OnCreate.class)
	public InventoryAdjustmentDTO startInventoryAdjustment(@Valid @RequestBody StartDailyCountDTO startDailyCountDTO) {
		return inventoryAdjustmentService.start(startDailyCountDTO);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/interrupt")
	@Validated(OnUpdate.class)
	public Boolean interruptInventoryCount(@Valid @RequestBody StartDailyCountDTO startDailyCountDTO) {
		return inventoryAdjustmentService.interrupt(startDailyCountDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/id/{inventoryAdjustmentId}/load/code/{code}/storeloc/{storeLocationid}")
	@Validated(OnCreate.class)
	public ItemAdjustInventoryDTO loadItem(@Valid @PathVariable("inventoryAdjustmentId") Long inventoryAdjustmentId,
										   @Valid @PathVariable("code") String code,
										   @Valid @PathVariable("storeLocationid") Long storeLocationId) {

		InventoryProductDTO inventoryProductDTO = inventoryService.findInventoryByCodeAndStoreLocation(code, storeLocationId);

		InventoryAdjustmentItemDetailDTO previousDetail = null;

		InventoryAdjustmentSumaryDTO result = inventoryAdjustmentService.getAdjustmentSumary(inventoryAdjustmentId);
		if (result.getSummary() != null) {
			previousDetail = result.getSummary().values().stream()
				.flatMap(Collection::stream)
				.filter(detail -> Objects.equals(detail.getUpcMasterId(), inventoryProductDTO.getUpcMasterModel().getUpcMasterId()))
				.findFirst()
				.orElse(null);
		}

		return new ItemAdjustInventoryDTO(inventoryProductDTO.getInventoryModel().getInventoryId(),
				inventoryProductDTO.getUpcMasterModel().getUpcMasterId(),
				inventoryProductDTO.getInventoryModel().getStoreLocation().getStoreLocationId(),
				inventoryProductDTO.getUpcMasterModel().getPrincipalUpc(),
				inventoryProductDTO.getUpcMasterModel().getProductName(),
				previousDetail);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/adjust")
	@Validated(OnAdjustInventory.class)
	public Boolean adjustInventory(@Valid @RequestBody InventoryAdjustmentDTO inventoryAdjustmentDTO) {
		return inventoryAdjustmentService.adjustInventory(inventoryAdjustmentDTO);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/finish")
	@Validated(OnUpdate.class)
	public boolean finishInventoryAdjustment(@Valid @RequestBody InventoryAdjustmentDTO inventoryAdjustmentDTO) {
		return inventoryAdjustmentService.finish(inventoryAdjustmentDTO.getInventoryAdjustmentId());

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/sumary/id/{inventoryAdjustmentId}")
	public InventoryAdjustmentSumaryDTO loadSumary(
			@Valid @PathVariable("inventoryAdjustmentId") Long inventoryAdjustmentId) {

		return inventoryAdjustmentService.getAdjustmentSumary(inventoryAdjustmentId);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/load/dailycountstatus")
	public List<CatalogDTO> loadDailyCountStatusCatalog() {
		return dailyCountStatusCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/load/shrinkagereason")
	public List<CatalogDTO> loadShrinkageReasonCatalog() {
		return shrinkageReasonCatalog.getCatalogOptions();
	}

}
