package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.AdditionalFeeDTO;
import com.ust.retail.store.pim.dto.catalog.AdditionalFeeFilterDTO;
import com.ust.retail.store.pim.dto.catalog.AdditionalFeeFilterResultDTO;
import com.ust.retail.store.pim.service.catalog.AdditionalFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/catalogs/p/additional-fee")
@Validated
public class AdditionalFeeController extends BaseController {

	private final AdditionalFeeService additionalFeeService;

	@Autowired
	public AdditionalFeeController(AdditionalFeeService additionalFeeService) {
		this.additionalFeeService = additionalFeeService;
	}
	 
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public AdditionalFeeDTO create(@Valid @RequestBody AdditionalFeeDTO additionalFeeDTO) {
		return additionalFeeService.saveOrUpdate(additionalFeeDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public AdditionalFeeDTO update(@Valid @RequestBody AdditionalFeeDTO additionalFeeDTO) {
		return additionalFeeService.saveOrUpdate(additionalFeeDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public AdditionalFeeDTO findById(@PathVariable(value = "id") Long additionalFeeId) {
		return additionalFeeService.findById(additionalFeeId);
	}	 

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<AdditionalFeeFilterResultDTO> findByFilters(@Valid @RequestBody AdditionalFeeFilterDTO filterDTO) {
		return additionalFeeService.findByFilters(filterDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load")
	public List<AdditionalFeeDTO> loadCatalog() {
		return additionalFeeService.load();
	}

}
