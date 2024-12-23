package com.ust.retail.store.bistro.controller.kitchen;

import com.ust.retail.store.bistro.commons.catalogs.WastageReasonCatalog;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionLineDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionRequestDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionTossDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionWastageLineDTO;
import com.ust.retail.store.bistro.service.kitchen.KitchenService;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/bistro/p/kitchen")
@Validated
public class KitchenExecutionController extends BaseController {

	private final KitchenService kitchenService;
	private final WastageReasonCatalog wastageReasonCatalog;


	public KitchenExecutionController(KitchenService kitchenService, WastageReasonCatalog wastageReasonCatalog) {
		this.kitchenService = kitchenService;
		this.wastageReasonCatalog = wastageReasonCatalog;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/execution")
	public Page<KitchenExecutionLineDTO> loadExecution(@RequestBody KitchenExecutionRequestDTO request) {
		return kitchenService.loadExecutionFor(request);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/currentwastage")
	public Page<KitchenExecutionWastageLineDTO> loadWastage(@RequestBody KitchenExecutionRequestDTO request) {
		return kitchenService.loadWastageFor(request);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/currenttossing")
	public Page<KitchenExecutionTossDTO> loadTossing(@RequestBody KitchenExecutionRequestDTO request) {
		return kitchenService.loadTossingFor(request);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/wastage")
	public GenericResponse registerWastage(@RequestBody KitchenExecutionWastageLineDTO wastage) {
		kitchenService.registerWastage(wastage);
		return new GenericResponse(GenericResponse.OP_TYPE_REGISTER, GenericResponse.SUCCESS_CODE, "success");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/toss")
	public GenericResponse registerToss(@RequestBody KitchenExecutionTossDTO toss) {
		kitchenService.registerToss(toss);
		return new GenericResponse(GenericResponse.OP_TYPE_REGISTER, GenericResponse.SUCCESS_CODE, "success");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@DeleteMapping("/wastage/id/{wastageId}")
	public GenericResponse deleteWastage(@PathVariable("wastageId") Long wastageId) {
		kitchenService.deleteWastage(wastageId);
		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "success");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/load/wastagereasons")
	public List<CatalogDTO> loadWastageReasons() {
		return wastageReasonCatalog.getCatalogOptions();
	}

}
