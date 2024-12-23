package com.ust.retail.store.bistro.service.recipes;

import com.ust.retail.store.bistro.dto.recipes.AddOnDTO;
import com.ust.retail.store.bistro.dto.recipes.LoadUpcAddOnDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeElementDTO;
import com.ust.retail.store.bistro.model.recipes.RecipeAddonModel;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.bistro.repository.recipes.RecipeAddonRepository;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.service.upcmaster.UpcMasterService;
import com.ust.retail.store.pim.service.upcmaster.UpcVendorDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecipeAddonService {
	private final RecipeRepository recipeRepository;
	private final RecipeAddonRepository recipeAddonRepository;
	private final UpcVendorDetailsService upcVendorDetailsService;
	private final UpcMasterService upcMasterService;
	private final RecipeService recipeService;
	private final AuthenticationFacade authenticationFacade;

	public RecipeAddonService(RecipeRepository recipeRepository,
							  RecipeAddonRepository recipeAddonRepository,
							  UpcVendorDetailsService upcVendorDetailsService,
							  UpcMasterService upcMasterService,
							  RecipeService recipeService,
							  AuthenticationFacade authenticationFacade) {
		this.recipeRepository = recipeRepository;
		this.recipeAddonRepository = recipeAddonRepository;
		this.upcVendorDetailsService = upcVendorDetailsService;
		this.upcMasterService = upcMasterService;
		this.recipeService = recipeService;
		this.authenticationFacade = authenticationFacade;
	}

	@Transactional
	public GenericResponse addAddon(AddOnDTO addOnDTO) {
		RecipeModel recipe = recipeRepository.findById(addOnDTO.getRecipeId())
				.orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", addOnDTO.getRecipeId()));

		recipe.addAddOn(addOnDTO, this.authenticationFacade.getCurrentUserId());

		recipeRepository.save(recipe);

		return new GenericResponse(GenericResponse.OP_TYPE_REGISTER, GenericResponse.SUCCESS_CODE, "Addon saved successfully");
	}

	public GenericResponse updateAddon(AddOnDTO addOnDTO) {

		RecipeAddonModel addon = recipeAddonRepository.findById(addOnDTO.getRecipeAddOnId())
				.orElseThrow(() -> new ResourceNotFoundException("Recipe addon", "id", addOnDTO.getRecipeAddOnId()));

		recipeAddonRepository.save(addOnDTO.merge(addon));

		return new GenericResponse(GenericResponse.OP_TYPE_UPDATE, GenericResponse.SUCCESS_CODE,
				"Addon was updated successfully");
	}

	public GenericResponse removeAddOn(Long recipeAddOnId) {

		recipeAddonRepository.findById(recipeAddOnId)
				.orElseThrow(() -> new ResourceNotFoundException("Recipe addon", "id", recipeAddOnId));

		recipeAddonRepository.deleteById(recipeAddOnId);

		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE,
				"Addon was removed successfully from recipe");
	}

	@Transactional
	public Page<LoadUpcAddOnDTO> loadAllAddonsPerRecipe(LoadUpcAddOnDTO loadUpcAddOnDTO) {
		return recipeAddonRepository.findByRecipeRecipeId(loadUpcAddOnDTO.getRecipeId(), loadUpcAddOnDTO.createPageable())
				.map(this::parseToDtoWithCostPerUnit);
	}

	public LoadUpcAddOnDTO loadUpcAddonInformation(Long upcMasterId) {

		UpcMasterDTO upcMasterDTO = upcMasterService.findById(upcMasterId);

		return new LoadUpcAddOnDTO(upcMasterId,
				upcMasterDTO.getProductName(),
				upcMasterService.getCostPerUnit(upcMasterDTO),
				upcMasterDTO.getContentPerUnitUomDesc());
	}

	@Transactional
	public AddOnDTO findById(Long recipeAddOnId) {
		RecipeAddonModel addon = recipeAddonRepository.findById(recipeAddOnId)
				.orElseThrow(() -> new ResourceNotFoundException("addon", "id", recipeAddOnId));

		UpcMasterDTO upcMasterDTO = new UpcMasterDTO().parseToDTO(addon.getUpcMaster());

		return new AddOnDTO().parseToDTO(addon, upcMasterService.getCostPerUnit(upcMasterDTO));
	}

	public List<RecipeElementDTO> loadAddOnList(Long recipeId) {
		List<RecipeElementDTO> rawMaterialsAndToppings = recipeService.getRawMaterialsAndToppings();
		if (Objects.nonNull(recipeId)) {
			List<Long> addOnsInRecipe = recipeAddonRepository.findByRecipeRecipeId(recipeId).stream()
					.map(RecipeAddonModel::getUpcMaster)
					.map(UpcMasterModel::getUpcMasterId)
					.collect(Collectors.toList());
			rawMaterialsAndToppings
					.removeIf(material -> addOnsInRecipe.contains(material.getUpcMasterId())
							|| (!material.isTopping() && !upcVendorDetailsService.productHasDefaultVendor(material.getUpcMasterId())));
		}
		return rawMaterialsAndToppings;
	}

	private LoadUpcAddOnDTO parseToDtoWithCostPerUnit(RecipeAddonModel m) {
		LoadUpcAddOnDTO dto = new LoadUpcAddOnDTO().parseToDto(m);
		UpcMasterDTO upcMasterDTO = new UpcMasterDTO().parseToDTO(m.getUpcMaster());

		dto.setCostPerUnit(upcMasterService.getCostPerUnit(upcMasterDTO));
		return dto;
	}

}
