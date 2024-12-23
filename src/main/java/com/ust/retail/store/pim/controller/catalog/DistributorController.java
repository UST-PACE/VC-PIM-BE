package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.DistributorDTO;
import com.ust.retail.store.pim.service.catalog.DistributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/catalogs/p/distributor")
@Validated
public class DistributorController extends BaseController {

	private final DistributorService distributorService;

	@Autowired
	public DistributorController(DistributorService distributorService) {
		this.distributorService = distributorService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public DistributorDTO create(@Valid @RequestBody DistributorDTO distributorDTO) {
		return distributorService.saveOrUpdate(distributorDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public DistributorDTO update(@Valid @RequestBody DistributorDTO distributorDTO) {
		return distributorService.saveOrUpdate(distributorDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public DistributorDTO findById(@PathVariable(value = "id") Long distributorId) {
		return distributorService.findById(distributorId);
	}	 

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<DistributorDTO> findByFilters(@Valid @RequestBody DistributorDTO distributorDTO) {
		return distributorService.getDistributorsByFilters(distributorDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/autocomplete/{distributorName}")
	public List<DistributorDTO> findAutoCompleteOptions(@PathVariable(value = "distributorName") String distributorName) {
		return distributorService.getAutocomplete(distributorName);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load")
	public List<DistributorDTO> loadCatalog() {
		return distributorService.load();
	}

}
