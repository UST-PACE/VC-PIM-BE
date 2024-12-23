package com.ust.retail.store.common.controller;

import com.ust.retail.store.common.catalogs.UnitTypeCatalog;
import com.ust.retail.store.common.dto.UnitOfMeasureDTO;
import com.ust.retail.store.common.dto.UnitOfMeasureFilterRequestDTO;
import com.ust.retail.store.common.service.UnitOfMeasureService;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/catalogs/p")
public class UnitOfMeasureController {
	private final UnitOfMeasureService unitOfMeasureService;
	private final UnitTypeCatalog unitTypeCatalog;

	public UnitOfMeasureController(UnitOfMeasureService unitOfMeasureService,
								   UnitTypeCatalog unitTypeCatalog) {
		this.unitOfMeasureService = unitOfMeasureService;
		this.unitTypeCatalog = unitTypeCatalog;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/unit/create")
	@Validated(OnCreate.class)
	public UnitOfMeasureDTO create(@Valid @RequestBody UnitOfMeasureDTO unitOfMeasureDTO) {
		return unitOfMeasureService.saveOrUpdate(unitOfMeasureDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/unit/update")
	@Validated(OnUpdate.class)
	public UnitOfMeasureDTO update(@Valid @RequestBody UnitOfMeasureDTO unitOfMeasureDTO) {
		return unitOfMeasureService.saveOrUpdate(unitOfMeasureDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/unit/find/id/{id}")
	public UnitOfMeasureDTO findById(@PathVariable(value = "id") Long unitOfMeasureId) {
		return unitOfMeasureService.findById(unitOfMeasureId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/unit/filter")
	@Validated(OnFilter.class)
	public Page<UnitOfMeasureDTO> findByFilters(@Valid @RequestBody UnitOfMeasureFilterRequestDTO request) {
		return unitOfMeasureService.getUnitOfMeasuresByFilters(request);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/unit/autocomplete/{unitOfMeasureName}")
	public List<UnitOfMeasureDTO> findAutoCompleteOptions(@PathVariable(value = "unitOfMeasureName") String unitOfMeasureName) {
		return unitOfMeasureService.getAutocomplete(unitOfMeasureName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/unittype/load")
	public List<CatalogDTO> loadUnitTypes() {
		return unitTypeCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/unittype/{unitTypeId}/baseunit")
	public UnitOfMeasureDTO getBaseUnitFor(@PathVariable Long unitTypeId) {
		return unitOfMeasureService.findBaseUnitFor(unitTypeId);
	}

}
