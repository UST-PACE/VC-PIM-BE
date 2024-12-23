package com.ust.retail.store.bistro.service.recipes;

import com.ust.retail.store.bistro.dto.recipes.LoadUpcSubstitutionDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeElementDTO;
import com.ust.retail.store.bistro.dto.recipes.SubstitutionDTO;
import com.ust.retail.store.bistro.model.recipes.RecipeDetailModel;
import com.ust.retail.store.bistro.model.recipes.RecipeSubstitutionModel;
import com.ust.retail.store.bistro.repository.recipes.RecipeDetailRepository;
import com.ust.retail.store.bistro.repository.recipes.RecipeSubstitutionRepository;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.service.upcmaster.UpcMasterService;
import com.ust.retail.store.pim.service.upcmaster.UpcVendorDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class RecipeSubstitutionService {
	private final RecipeDetailRepository recipeDetailRepository;
	private final RecipeSubstitutionRepository recipeSubstitutionRepository;
	private final UpcVendorDetailsService upcVendorDetailsService;
	private final UpcMasterService upcMasterService;
	private final RecipeService recipeService;
	private final AuthenticationFacade authenticationFacade;

	public RecipeSubstitutionService(RecipeDetailRepository recipeDetailRepository,
									 RecipeSubstitutionRepository recipeSubstitutionRepository,
									 UpcVendorDetailsService upcVendorDetailsService,
									 UpcMasterService upcMasterService,
									 RecipeService recipeService,
									 AuthenticationFacade authenticationFacade) {
		this.recipeDetailRepository = recipeDetailRepository;
		this.recipeSubstitutionRepository = recipeSubstitutionRepository;
		this.upcVendorDetailsService = upcVendorDetailsService;
		this.upcMasterService = upcMasterService;
		this.recipeService = recipeService;
		this.authenticationFacade = authenticationFacade;
	}

	@Transactional
	public GenericResponse addSubstitution(SubstitutionDTO substitutionDTO) {
		RecipeDetailModel ingredient = recipeDetailRepository.findById(substitutionDTO.getRecipeDetailId())
				.orElseThrow(() -> new ResourceNotFoundException("Recipe Detail", "id", substitutionDTO.getRecipeDetailId()));

		recipeSubstitutionRepository.save(substitutionDTO.createModel(ingredient, authenticationFacade.getCurrentUserId()));

		return new GenericResponse(GenericResponse.OP_TYPE_REGISTER, GenericResponse.SUCCESS_CODE, "Substitution saved successfully");
	}

	public GenericResponse updateSubstitution(SubstitutionDTO substitutionDTO) {
		RecipeSubstitutionModel substitution = recipeSubstitutionRepository.findById(substitutionDTO.getRecipeSubstitutionId())
				.orElseThrow(() -> new ResourceNotFoundException("Recipe substitution", "id", substitutionDTO.getRecipeSubstitutionId()));

		substitution.updateWith(substitutionDTO);

		recipeSubstitutionRepository.save(substitution);

		return new GenericResponse(GenericResponse.OP_TYPE_UPDATE, GenericResponse.SUCCESS_CODE,
				"Substitution was updated successfully");
	}

	public GenericResponse removeSubstitution(Long recipeSubstitutionId) {
		recipeSubstitutionRepository.findById(recipeSubstitutionId)
				.orElseThrow(() -> new ResourceNotFoundException("Recipe substitution", "id", recipeSubstitutionId));

		recipeSubstitutionRepository.deleteById(recipeSubstitutionId);

		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE,
				"Substitution was removed successfully from recipe");
	}

	@Transactional
	public Page<SubstitutionDTO> loadAllSubstitutionsPerRecipe(LoadUpcSubstitutionDTO loadUpcSubstitutionDTO) {
		return recipeSubstitutionRepository.findByIngredientRecipeRecipeId(loadUpcSubstitutionDTO.getRecipeId(), loadUpcSubstitutionDTO.createPageable())
				.map(this::parseToDtoWithCostPerUnit);
	}

	public LoadUpcSubstitutionDTO loadUpcSubstitutionInformation(Long upcMasterId) {
		UpcMasterDTO upcMasterDTO = upcMasterService.findById(upcMasterId);

		return new LoadUpcSubstitutionDTO(upcMasterId,
				upcMasterService.getCostPerUnit(upcMasterDTO),
				upcMasterDTO.getContentPerUnitUomId(),
				upcMasterDTO.getContentPerUnitUomDesc());
	}

	@Transactional
	public SubstitutionDTO findById(Long recipeSubstitutionId) {
		RecipeSubstitutionModel substitution = recipeSubstitutionRepository.findById(recipeSubstitutionId)
				.orElseThrow(() -> new ResourceNotFoundException("Substitution", "id", recipeSubstitutionId));

		UpcMasterDTO upcMasterDTO = new UpcMasterDTO().parseToDTO(substitution.getSubstituteUpcMaster());

		return new SubstitutionDTO().parseToDTO(substitution, upcMasterService.getCostPerUnit(upcMasterDTO));
	}

	private SubstitutionDTO parseToDtoWithCostPerUnit(RecipeSubstitutionModel m) {
		UpcMasterDTO upcMasterDTO = new UpcMasterDTO().parseToDTO(m.getSubstituteUpcMaster());

		return new SubstitutionDTO().parseToDTO(m, upcMasterService.getCostPerUnit(upcMasterDTO));
	}

	public List<RecipeElementDTO> loadSubstitutionCandidates(Long recipeDetailId) {
		RecipeDetailModel ingredient = recipeDetailRepository.findById(recipeDetailId)
				.orElseThrow(() -> new ResourceNotFoundException("Recipe Detail", "id", recipeDetailId));

		List<RecipeElementDTO> rawMaterialsAndToppings = recipeService.getRawMaterialsAndToppings();
		rawMaterialsAndToppings
				.removeIf(material -> Objects.equals(ingredient.getUpcMaster().getUpcMasterId(), material.getUpcMasterId())
						|| (!material.isTopping() && !upcVendorDetailsService.productHasDefaultVendor(material.getUpcMasterId())));
		return rawMaterialsAndToppings;
	}
}
