package com.ust.retail.store.bistro.controller.recipe;

import com.ust.retail.store.bistro.dto.recipes.AddOnDTO;
import com.ust.retail.store.bistro.dto.recipes.LoadUpcAddOnDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeElementDTO;
import com.ust.retail.store.bistro.service.recipes.RecipeAddonService;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import org.hibernate.annotations.OnDelete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/bistro/p/recipe/addons")
@Validated
public class RecipeAddonsController extends BaseController {

	private final RecipeAddonService recipeAddonService;

	@Autowired
	public RecipeAddonsController(RecipeAddonService  recipeAddonService) {
		
		super();
		this.recipeAddonService = recipeAddonService;
	}
		
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/add")
	@Validated(OnCreate.class)
	public GenericResponse addAddon(@Valid @RequestBody AddOnDTO addOnDTO) {
		return recipeAddonService.addAddon(addOnDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public GenericResponse update(@Valid @RequestBody AddOnDTO addOnDTO) {
		return recipeAddonService.updateAddon(addOnDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@DeleteMapping("/remove/{recipeAddOnId}")
	@Validated(OnDelete.class)
	public GenericResponse removeAddon(@Valid @PathVariable(value = "recipeAddOnId") Long recipeAddOnId) {
		return recipeAddonService.removeAddOn(recipeAddOnId);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/load")
	@Validated(OnFilter.class)
	public Page<LoadUpcAddOnDTO> loadAddons(@Valid @RequestBody LoadUpcAddOnDTO loadUpcAddOnDTO) {
		return recipeAddonService.loadAllAddonsPerRecipe(loadUpcAddOnDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/load/upcmaster/{upcMasterId}")
	public LoadUpcAddOnDTO loadUpcMasterInformation(@Valid @PathVariable(value = "upcMasterId") Long upcMasterId) {
		return recipeAddonService.loadUpcAddonInformation(upcMasterId);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping({"/load/addonlist", "/load/addonlist/{recipeId}"})
	public List<RecipeElementDTO> loadAddOnList(@PathVariable(required = false) Long recipeId) {
		return recipeAddonService.loadAddOnList(recipeId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/find/recipeaddon/id/{recipeAddOnId}")
	public AddOnDTO findById(@Valid @PathVariable(value = "recipeAddOnId") Long recipeAddOnId) {
		return recipeAddonService.findById(recipeAddOnId);
	}

}
