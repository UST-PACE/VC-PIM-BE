package com.ust.retail.store.bistro.controller.external;

import com.ust.retail.store.bistro.dto.external.dish.ExternalDishByStoreAndSkuListRequest;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishByStoreRequest;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishDTO;
import com.ust.retail.store.bistro.service.external.ExternalDishService;
import com.ust.retail.store.pim.common.annotations.OnSimpleFilter;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/external/p/bistro/catalogs/dish")
@Validated
public class ExternalDishController {
	private final ExternalDishService externalDishService;

	public ExternalDishController(ExternalDishService externalDishService) {
		this.externalDishService = externalDishService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@GetMapping("/store")
	@Validated(OnSimpleFilter.class)
	public ExternalApiResponse<List<ExternalDishDTO>> findByStore(@Valid ExternalDishByStoreRequest request) {
		return new ExternalApiResponse<>(externalDishService.findByFilters(request));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@PostMapping("/store/skulist")
	@Validated(OnSimpleFilter.class)
	public ExternalApiResponse<List<ExternalDishDTO>> findByStoreAndSkuList(@Valid @RequestBody ExternalDishByStoreAndSkuListRequest request) {
		return new ExternalApiResponse<>(externalDishService.findByStoreAndSkuList(request));
	}
}
