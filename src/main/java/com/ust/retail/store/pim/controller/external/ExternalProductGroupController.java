package com.ust.retail.store.pim.controller.external;

import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.ExternalProductGroupDTO;
import com.ust.retail.store.pim.service.external.ExternalProductGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/external/p/catalogs/productgroup")
@Validated
public class ExternalProductGroupController extends BaseController {

	private final ExternalProductGroupService externalProductGroupService;

	public ExternalProductGroupController(ExternalProductGroupService externalProductGroupService) {
		this.externalProductGroupService = externalProductGroupService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@GetMapping({"", "/sn/{storeNumberId}"})
	public ExternalApiResponse<List<ExternalProductGroupDTO>> loadCatalog(@PathVariable(required = false) Long storeNumberId) {
		return new ExternalApiResponse<>(externalProductGroupService.loadCatalog(storeNumberId));
	}
}
