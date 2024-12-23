package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO;
import com.ust.retail.store.pim.service.catalog.BrandOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/catalogs/p/brandowner")
@Validated
public class BrandOwnerController extends BaseController {

	private final BrandOwnerService brandOwnerService;

	@Autowired
	public BrandOwnerController(BrandOwnerService brandOwnerService) {
		
		super();
		this.brandOwnerService = brandOwnerService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public BrandOwnerDTO create(@Valid @RequestBody BrandOwnerDTO brandOwnerDTO) {
		return brandOwnerService.saveOrUpdate(brandOwnerDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public BrandOwnerDTO update(@Valid @RequestBody BrandOwnerDTO brandOwnerDTO) {
		return brandOwnerService.saveOrUpdate(brandOwnerDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/find/id/{id}")
	public BrandOwnerDTO findById(@PathVariable(value = "id") Long brandOwnerId) {
		return brandOwnerService.findById(brandOwnerId);
	}	 

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<BrandOwnerDTO> findByFilters(@Valid @RequestBody BrandOwnerDTO brandOwnerDTO) {
		return brandOwnerService.getBrandOwnersByFilters(brandOwnerDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/autocomplete/{brandOwnerName}")
	public List<BrandOwnerDTO> findAutoCompleteOptions(@PathVariable(value = "brandOwnerName") String brandOwnerName) {
		return brandOwnerService.getAutocomplete(brandOwnerName);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/load")
	public List<BrandOwnerDTO> loadCatalog() {
		return brandOwnerService.load();
	}

}
