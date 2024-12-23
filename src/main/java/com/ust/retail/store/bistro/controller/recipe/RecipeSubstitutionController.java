package com.ust.retail.store.bistro.controller.recipe;

import com.ust.retail.store.bistro.dto.recipes.LoadUpcSubstitutionDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeElementDTO;
import com.ust.retail.store.bistro.dto.recipes.SubstitutionDTO;
import com.ust.retail.store.bistro.service.recipes.RecipeSubstitutionService;
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
@RequestMapping(path = "/api/bistro/p/recipe/substitutions")
@Validated
public class RecipeSubstitutionController extends BaseController {

	private final RecipeSubstitutionService recipeSubstitutionService;

	@Autowired
	public RecipeSubstitutionController(RecipeSubstitutionService recipeSubstitutionService) {

		super();
		this.recipeSubstitutionService = recipeSubstitutionService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/add")
	@Validated(OnCreate.class)
	public GenericResponse addSubstitution(@Valid @RequestBody SubstitutionDTO substitutionDTO) {
		return recipeSubstitutionService.addSubstitution(substitutionDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public GenericResponse update(@Valid @RequestBody SubstitutionDTO substitutionDTO) {
		return recipeSubstitutionService.updateSubstitution(substitutionDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@DeleteMapping("/remove/{recipeSubstitutionId}")
	@Validated(OnDelete.class)
	public GenericResponse removeSubstitution(@Valid @PathVariable(value = "recipeSubstitutionId") Long recipeSubstitutionId) {
		return recipeSubstitutionService.removeSubstitution(recipeSubstitutionId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/load/candidates/{recipeDetailId}")
	public List<RecipeElementDTO> loadSubstitutionCandidates(@Valid @PathVariable Long recipeDetailId) {
		return recipeSubstitutionService.loadSubstitutionCandidates(recipeDetailId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/load")
	@Validated(OnFilter.class)
	public Page<SubstitutionDTO> loadSubstitutions(@Valid @RequestBody LoadUpcSubstitutionDTO loadUpcSubstitutionDTO) {
		return recipeSubstitutionService.loadAllSubstitutionsPerRecipe(loadUpcSubstitutionDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/load/upcmaster/{upcMasterId}")
	public LoadUpcSubstitutionDTO loadUpcMasterInformation(@Valid @PathVariable(value = "upcMasterId") Long upcMasterId) {
		return recipeSubstitutionService.loadUpcSubstitutionInformation(upcMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/id/{recipeSubstitutionId}")
	public SubstitutionDTO findById(@Valid @PathVariable(value = "recipeSubstitutionId") Long recipeSubstitutionId) {
		return recipeSubstitutionService.findById(recipeSubstitutionId);
	}

}
