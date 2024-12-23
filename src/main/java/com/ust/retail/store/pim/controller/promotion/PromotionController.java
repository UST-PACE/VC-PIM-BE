package com.ust.retail.store.pim.controller.promotion;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.catalogs.DiscountsCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionFilterDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionFilterResultDTO;
import com.ust.retail.store.pim.service.promotion.PromotionService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/promotion/p")
@Validated
public class PromotionController extends BaseController {
	private final PromotionService promotionService;
	private final DiscountsCatalog discountsCatalog;

	public PromotionController(PromotionService promotionService, DiscountsCatalog discountsCatalog) {
		this.promotionService = promotionService;
		this.discountsCatalog = discountsCatalog;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public PromotionDTO create(@Valid @RequestBody PromotionDTO promotionDTO) {
		return promotionService.saveOrUpdate(promotionDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public PromotionDTO update(@Valid @RequestBody PromotionDTO promotionDTO) {
		return promotionService.saveOrUpdate(promotionDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@DeleteMapping("/delete/id/{id}")
	public GenericResponse deleteById(@PathVariable(value = "id") Long promotionId) {
		promotionService.deleteById(promotionId);
		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "true");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/find/id/{id}")
	public PromotionDTO findById(@PathVariable(value = "id") Long promotionId) {
		return promotionService.findById(promotionId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<PromotionFilterResultDTO> findByFilters(@Valid @RequestBody PromotionFilterDTO dto) {
		return promotionService.getPromotionsByFilters(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/types/load")
	public List<CatalogDTO> getPromotionTypes() {
		return discountsCatalog.getCatalogOptions();
	}
}
