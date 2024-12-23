package com.ust.retail.store.pim.controller.upcmaster;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.upcmaster.ComboDTO;
import com.ust.retail.store.pim.repository.upcmaster.ComboRepository;
import com.ust.retail.store.pim.service.upcmaster.ComboService;

@RestController
@RequestMapping("/api/upcmaster/p/combo")
@Validated
public class ComboController {

	private final ComboService comboService;
	private final ComboRepository comboRepository;	
    
	@Autowired
	public ComboController(ComboService comboService, ComboRepository comboRepository) {
		this.comboService = comboService;
		this.comboRepository = comboRepository;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public ComboDTO create(@Valid @RequestBody ComboDTO dto) {
		return comboService.saveOrUpdate(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/update")
	@Validated(OnUpdate.class)
	public ComboDTO update(@Valid @RequestBody ComboDTO dto) {
		return comboService.saveOrUpdate(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public ComboDTO findById(@PathVariable(value = "id") Long comboId) {
		return comboService.findById(comboId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<ComboDTO> findByFilters(@Valid @RequestBody ComboDTO dto) {
		return comboService.getComboByFilters(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load")
	public List<ComboDTO> loadCatalog() {
		return comboService.load();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("comboname/autocomplete/{comboName}")
	public List<ComboDTO> findAutoCompleteOptions(@PathVariable(value = "comboName") String comboName) {
		return comboService.getAutocomplete(comboName);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("delete/{comboId}")
	public GenericResponse deleteById (@PathVariable(value = "comboId") Long id) {
		comboRepository.deleteById(id);
		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "true");
	}

}
