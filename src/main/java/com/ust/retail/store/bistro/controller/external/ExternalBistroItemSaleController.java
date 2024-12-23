package com.ust.retail.store.bistro.controller.external;

import com.ust.retail.store.pim.common.annotations.OnSale;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.sale.ExternalSoldItemListRequestDTO;
import com.ust.retail.store.pim.service.external.ExternalItemSaleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/external/p/bistro/product")
@Validated
public class ExternalBistroItemSaleController extends BaseController {
	private final ExternalItemSaleService externalItemSaleService;

	public ExternalBistroItemSaleController(ExternalItemSaleService externalItemSaleService) {
		this.externalItemSaleService = externalItemSaleService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@PutMapping("/sale")
	@Validated(OnSale.class)
	public ExternalApiResponse<String> sale(@Valid @RequestBody ExternalSoldItemListRequestDTO request) {
		return new ExternalApiResponse<>(externalItemSaleService.processBistro(request));
	}
}
