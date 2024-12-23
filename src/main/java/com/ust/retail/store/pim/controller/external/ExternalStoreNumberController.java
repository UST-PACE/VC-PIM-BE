package com.ust.retail.store.pim.controller.external;

import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.store.ExternalStoreDTO;
import com.ust.retail.store.pim.service.external.ExternalStoreNumberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/external/p/catalogs/store")
@Validated
public class ExternalStoreNumberController extends BaseController {
	private final ExternalStoreNumberService externalStoreNumberService;

	public ExternalStoreNumberController(ExternalStoreNumberService externalStoreNumberService) {
		this.externalStoreNumberService = externalStoreNumberService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@GetMapping
	public ExternalApiResponse<List<ExternalStoreDTO>> loadCatalog() {
		return new ExternalApiResponse<>(externalStoreNumberService.loadCatalog());
	}
}
