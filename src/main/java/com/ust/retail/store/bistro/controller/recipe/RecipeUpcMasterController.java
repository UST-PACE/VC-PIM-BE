package com.ust.retail.store.bistro.controller.recipe;

import com.ust.retail.store.bistro.dto.recipes.BistroNewUpcResponseDTO;
import com.ust.retail.store.bistro.dto.recipes.BistroUpcMasterDTO;
import com.ust.retail.store.bistro.service.recipes.RecipeUpcMasterService;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/bistro/p/upc")
@Validated
public class RecipeUpcMasterController extends BaseController {

	private final RecipeUpcMasterService recipeUpcMasterService;

	@Autowired
	public RecipeUpcMasterController(RecipeUpcMasterService recipeUpcMasterService) {
		this.recipeUpcMasterService = recipeUpcMasterService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public BistroNewUpcResponseDTO create(@Valid @RequestBody BistroUpcMasterDTO bistroUpcMasterDTO) {
		return recipeUpcMasterService.save(bistroUpcMasterDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public BistroUpcMasterDTO update(@Valid @RequestBody BistroUpcMasterDTO bistroUpcMasterDTO) {
		return recipeUpcMasterService.update(bistroUpcMasterDTO);
	}
	
}
