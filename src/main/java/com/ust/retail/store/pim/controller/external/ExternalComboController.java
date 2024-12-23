package com.ust.retail.store.pim.controller.external;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ust.retail.store.pim.common.annotations.OnSimpleFilter;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.ExternalComboDTO;
import com.ust.retail.store.pim.service.external.ExternalComboService;

@RestController
@RequestMapping(path = "/api/external/p/combo")
@Validated
public class ExternalComboController extends BaseController{
	
	private final ExternalComboService externalComboService;

	public ExternalComboController(ExternalComboService externalComboService) {
		this.externalComboService = externalComboService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@GetMapping("/load")
	@Validated(OnSimpleFilter.class)
	public ExternalApiResponse<List<ExternalComboDTO>> loadByCombo() {
		return new ExternalApiResponse<>(externalComboService.loadByCombo());
	}

}
