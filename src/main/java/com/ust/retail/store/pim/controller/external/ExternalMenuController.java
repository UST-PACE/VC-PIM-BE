package com.ust.retail.store.pim.controller.external;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnSimpleFilter;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.ExternalMenuConfiguratorDTO;
import com.ust.retail.store.pim.service.external.ExternalMenuService;

@RestController
@RequestMapping(path = "/api/external/p/menu")
@Validated
public class ExternalMenuController extends BaseController {

	private final ExternalMenuService externalMenuService;

	public ExternalMenuController(ExternalMenuService externalMenuService) {
		this.externalMenuService = externalMenuService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@GetMapping("/load")
	@Validated(OnSimpleFilter.class)
	public ExternalApiResponse<List<ExternalMenuConfiguratorDTO>> filterByCalendarMenu(@Valid ExternalMenuConfiguratorDTO request) {
		return new ExternalApiResponse<>(externalMenuService.filterByCalendarMenu(request));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@GetMapping("/products")
	@Validated(OnFilter.class)
	public ExternalApiResponse<ExternalMenuConfiguratorDTO> getScheduledMenuProducts(@Valid ExternalMenuConfiguratorDTO request) {
		return new ExternalApiResponse<>(externalMenuService.getScheduledMenuProducts(request));
	}

}
