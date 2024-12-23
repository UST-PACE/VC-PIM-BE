package com.ust.retail.store.pim.controller.external;

import com.ust.retail.store.pim.common.annotations.OnExternalSkuSearch;
import com.ust.retail.store.pim.common.annotations.OnSimpleFilter;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.product.ExternalProductByStoreAndSkuListRequest;
import com.ust.retail.store.pim.dto.external.product.ExternalProductByStoreRequest;
import com.ust.retail.store.pim.dto.external.product.ExternalProductDTO;
import com.ust.retail.store.pim.service.external.ExternalProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/external/p/catalogs/product")
@Validated
public class ExternalProductController extends BaseController {
	private final ExternalProductService externalProductService;

	public ExternalProductController(ExternalProductService externalProductService) {
		this.externalProductService = externalProductService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@GetMapping("/store")
	@Validated(OnSimpleFilter.class)
	public ExternalApiResponse<List<ExternalProductDTO>> findByStore(@Valid ExternalProductByStoreRequest request) {
		return new ExternalApiResponse<>(externalProductService.findByFilters(request));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@GetMapping("/store/single")
	@Validated({OnExternalSkuSearch.class})
	public ExternalApiResponse<ExternalProductDTO> findUpcByStore(@Valid ExternalProductByStoreRequest request) {
		return new ExternalApiResponse<>(externalProductService.findProductByStore(request));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@PostMapping("/store/skulist")
	@Validated(OnSimpleFilter.class)
	public ExternalApiResponse<List<ExternalProductDTO>> findByStoreAndSkuList(@Valid @RequestBody ExternalProductByStoreAndSkuListRequest request) {
		return new ExternalApiResponse<>(externalProductService.findByStoreAndSkuList(request));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@PatchMapping("/id/{upcMasterId}/imagetrained/{trainedValue}")
	public ExternalApiResponse<String> updateImageTrained(@PathVariable Long upcMasterId, @PathVariable boolean trainedValue) {
		externalProductService.updateImageTrained(upcMasterId, trainedValue);

		return new ExternalApiResponse<>("Success");
	}
}
