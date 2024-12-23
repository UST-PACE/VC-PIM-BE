package com.ust.retail.store.common.controller;

import com.ust.retail.store.common.util.UnitConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/converter/p")
@Validated
public class ConverterController {
	private final UnitConverter unitConverter;

	public ConverterController(UnitConverter unitConverter) {
		this.unitConverter = unitConverter;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/from/{unitIdFrom}/to/{unitIdTo}/qty/{qty}")
	public Double loadUpcMasterInformation(@PathVariable Long unitIdFrom, @PathVariable Long unitIdTo, @PathVariable Double qty) {
		return unitConverter.convert(unitIdFrom, unitIdTo, qty);
	}
}
