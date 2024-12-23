package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.common.catalogs.ProductUnitCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/catalogs/p/productunit")
@Validated
@Slf4j
public class ProductUnitController {

	private final UnitConverter unitConverter;
	private final ProductUnitCatalog productUnitCatalog;

	public ProductUnitController(UnitConverter unitConverter, ProductUnitCatalog productUnitCatalog) {
		this.unitConverter = unitConverter;
		this.productUnitCatalog = productUnitCatalog;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load/unitfrom/{unitFromId}")
	public List<CatalogDTO> loadUnitCatalogForId(@PathVariable(value = "unitFromId") Long unitFromId) {
		List<CatalogDTO> validUnits = unitConverter.findConvertableUnitsFor(unitFromId);
		if (validUnits.isEmpty()) {
			try {
				validUnits = List.of(productUnitCatalog.findCatalogById(unitFromId));
			} catch (ResourceNotFoundException e) {
				log.trace("Ignore unit not found");
			}
		}
		return validUnits;
	}
}
