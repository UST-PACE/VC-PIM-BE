package com.ust.retail.store.pim.controller.external;

import com.ust.retail.store.pim.common.annotations.OnSale;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.sale.ExternalReturnItemListRequestDTO;
import com.ust.retail.store.pim.service.external.ExternalItemReturnService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/external/p/product/return")
@Validated
public class ExternalItemReturnController extends BaseController {
	private final ExternalItemReturnService externalItemReturnService;

	public ExternalItemReturnController(ExternalItemReturnService externalItemReturnService) {
		this.externalItemReturnService = externalItemReturnService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@PutMapping()
	@Validated(OnSale.class)
	public ExternalApiResponse<String> productReturn(@Valid @RequestBody ExternalReturnItemListRequestDTO request) {
		return new ExternalApiResponse<>(externalItemReturnService.process(request));
	}
}
