package com.ust.retail.store.bistro.service.recipes;

import static com.ust.retail.store.common.util.BarcodeGeneratorUtils.generateCode128BarcodeImage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ust.retail.store.bistro.commons.catalogs.MealCategoryCatalog;
import com.ust.retail.store.bistro.dto.recipes.BarcodeDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeDetailDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeElementDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeFilterDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeFinancialInfoDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeStorePriceDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipieLabelDTO;
import com.ust.retail.store.bistro.model.recipes.RecipeDetailModel;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.bistro.repository.recipes.RecipeDetailRepository;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.bistro.repository.recipes.RecipeSubstitutionRepository;
import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.catalogs.ProductTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.dto.upcmaster.SimpleUpcDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcVendorDetailDTO;
import com.ust.retail.store.pim.event.upcmaster.UpcVendorDetailChangedEvent;
import com.ust.retail.store.pim.event.upcmaster.VendorStoreRemovedEvent;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcStorePriceModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcStorePriceRepository;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import com.ust.retail.store.pim.service.upcmaster.UpcVendorDetailsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipeService {
	private final RecipeRepository recipeRepository;
	private final RecipeDetailRepository recipeDetailRepository;
	private final RecipeSubstitutionRepository recipeSubstitutionRepository;
	private final UpcMasterRepository upcMasterRepository;
	private final UpcVendorDetailsService upcVendorDetailsService;
	private final StoreNumberService storeNumberService;
	private final UpcStorePriceRepository upcStorePriceRepository;
	private final UnitConverter unitConverter;
	private final AuthenticationFacade authenticationFacade;
	private final RecipeDrinkConfService recipeDrinkConfService;

	public RecipeService(RecipeRepository recipeRepository,
						 RecipeDetailRepository recipeDetailRepository,
						 RecipeSubstitutionRepository recipeSubstitutionRepository,
						 UpcMasterRepository upcMasterRepository,
						 UpcVendorDetailsService upcVendorDetailsService,
						 StoreNumberService storeNumberService,
						 UpcStorePriceRepository upcStorePriceRepository,
						 UnitConverter unitConverter,
						 AuthenticationFacade authenticationFacade,
						 RecipeDrinkConfService recipeDrinkConfService) {
		this.recipeRepository = recipeRepository;
		this.recipeDetailRepository = recipeDetailRepository;
		this.recipeSubstitutionRepository = recipeSubstitutionRepository;
		this.upcMasterRepository = upcMasterRepository;
		this.upcVendorDetailsService = upcVendorDetailsService;
		this.storeNumberService = storeNumberService;
		this.upcStorePriceRepository = upcStorePriceRepository;
		this.unitConverter = unitConverter;
		this.authenticationFacade = authenticationFacade;
		this.recipeDrinkConfService = recipeDrinkConfService;
	}

	@Transactional
	public RecipeDTO update(RecipeDTO recipeDTO) {
		Long recipeId = recipeDTO.getRecipeId();
		RecipeModel recipe = recipeRepository.findById(recipeDTO.getRecipeId())
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeId));

		recipe.updateInformation(recipeDTO);

		return recipeDTO.parseToDTO(recipeRepository.save(recipe));
	}

	public RecipeDTO findById(Long recipeId) {
		return recipeRepository.findById(recipeId)
				.map(m -> new RecipeDTO().parseToDTO(m))
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeId));
	}

	public RecipeDTO findByUpcMasterId(Long upcMasterId) {
		return recipeRepository.findByRelatedUpcMasterUpcMasterId(upcMasterId)
				.map(m -> new RecipeDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("Recipe", "upcMasterId", upcMasterId));
	}

	public Page<RecipeDetailDTO> findDetailsByRecipeId(RecipeFilterDTO filterDTO) {
		return recipeDetailRepository.findByRecipeRecipeId(filterDTO.getRecipeId(), filterDTO.createPageable())
				.map(this::toDtoCalculatingCost);
	}

	public Page<RecipeFilterDTO> findByFilters(RecipeFilterDTO filterDTO) {
		return recipeRepository.findByFilters(
						filterDTO.getRecipeName(),
						filterDTO.getFoodClassificationId(),
						filterDTO.getMealTemperatureId(),
						filterDTO.getPreparationAreaId(),
						filterDTO.createPageable())
				.map(m -> new RecipeFilterDTO().parseToDTO(m));
	}

	@Transactional
	public RecipeDTO addItem(RecipeDetailDTO recipeDetailDTO) {
		Long recipeId = recipeDetailDTO.getRecipeId();
		RecipeModel recipe = recipeRepository.findById(recipeId)
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeId));

		recipe.addItem(recipeDetailDTO, this.authenticationFacade.getCurrentUserId());

		recipeRepository.saveAndFlush(recipe);

		return findById(recipeId);
	}

	@Transactional
	public RecipeDTO updateItem(RecipeDetailDTO recipeDetailDTO) {
		RecipeDetailModel detail = recipeDetailRepository.findById(recipeDetailDTO.getRecipeDetailId())
				.orElseThrow(() -> new ResourceNotFoundException("Recipe Detail", "id", recipeDetailDTO.getRecipeDetailId()));

		detail = recipeDetailRepository.saveAndFlush(recipeDetailDTO.merge(detail));

		if (!recipeDetailDTO.isToExclude()) {
			recipeSubstitutionRepository.deleteByIngredientRecipeDetailId(recipeDetailDTO.getRecipeDetailId());
		}

		Long recipeId = detail.getRecipe().getRecipeId();

		return findById(recipeId);
	}

	public RecipeDTO removeItem(Long recipeDetailId) {
		RecipeDetailModel detailModel = recipeDetailRepository.findById(recipeDetailId)
				.orElseThrow(() -> new ResourceNotFoundException("Recipe Detail", "id", recipeDetailId));

		Long recipeId = detailModel.getRecipe().getRecipeId();

		recipeDetailRepository.deleteById(recipeDetailId);

		return findById(recipeId);
	}

	@Transactional
	public RecipeDTO updatePreparationInstructions(RecipeDTO recipeDTO) {
		RecipeModel recipe = recipeRepository.findById(recipeDTO.getRecipeId())
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeDTO.getRecipeId()));

		recipe.addInstructions(recipeDTO.getPrepInstructions());

		return recipeDTO.parseToDTO(recipeRepository.save(recipe));
	}

	@Transactional
	public RecipeDTO updateNutritionalValues(RecipeDTO recipeDTO) {
		RecipeModel recipe = recipeRepository.findById(recipeDTO.getRecipeId())
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeDTO.getRecipeId()));

		recipe.getRelatedUpcMaster().updateUpcNutritionInformationModel(recipeDTO.getNutritionalInfo());

		return recipeDTO.parseToDTO(recipeRepository.save(recipe));
	}

	@Transactional
	public RecipeDTO updateLabelTexts(RecipeDTO recipeDTO) {
		RecipeModel recipe = recipeRepository.findById(recipeDTO.getRecipeId())
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeDTO.getRecipeId()));

		recipe.addLabelTexts(recipeDTO.getContainsText(), recipeDTO.getIngredientsText());

		return recipeDTO.parseToDTO(recipeRepository.save(recipe));
	}

	public List<RecipeDTO> findByName(String recipeName) {
		return recipeRepository.findByRelatedUpcMasterProductNameContaining(recipeName).stream()
				.map(r -> new RecipeDTO().parseToSimpleDTO(r))
				.collect(Collectors.toUnmodifiableList());
	}

	public BarcodeDTO getRecipeBarcode(Long recipeId) {
		RecipeModel recipe = recipeRepository.findById(recipeId)
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeId));

		return new BarcodeDTO(generateCode128BarcodeImage(recipe.getRelatedUpcMaster().getPrincipalUpc()));
	}

	public List<RecipeDTO> loadDishList() {
		return recipeRepository.findByFoodClassificationCatalogId(MealCategoryCatalog.DISH).stream()
				.map(m -> new RecipeDTO().parseToSimpleDTO(m))
				.collect(Collectors.toList());
	}

	public List<RecipeElementDTO> loadIngredientList(Long recipeId) {
		List<RecipeElementDTO> rawMaterialsAndToppings = getRawMaterialsAndToppings();
		if (Objects.nonNull(recipeId)) {
			List<Long> ingredientsInRecipe = recipeDetailRepository.findByRecipeRecipeId(recipeId).stream()
					.map(RecipeDetailModel::getUpcMaster)
					.map(UpcMasterModel::getUpcMasterId)
					.collect(Collectors.toList());
			rawMaterialsAndToppings
					.removeIf(material -> ingredientsInRecipe.contains(material.getUpcMasterId()));
		}
		rawMaterialsAndToppings
				.removeIf(material -> !material.isTopping()
						&& !upcVendorDetailsService.productHasDefaultVendor(material.getUpcMasterId()));
		return rawMaterialsAndToppings;
	}

	public List<RecipeDetailDTO> findDetailsByRecipeId(Long recipeId) {
		return recipeDetailRepository.findByRecipeRecipeId(recipeId).stream()
				.map(m -> new RecipeDetailDTO().parseToDTO(m))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<RecipeDetailDTO> findOptionalDetailsByRecipeId(Long recipeId) {
		return recipeDetailRepository.findByToExcludeTrueAndRecipeRecipeId(recipeId).stream()
				.map(m -> new RecipeDetailDTO().parseToDTO(m))
				.collect(Collectors.toUnmodifiableList());
	}

	public boolean isUpcUsedOnRecipes(Long upcMasterId) {
		return !recipeDetailRepository.findByUpcMasterUpcMasterId(upcMasterId).isEmpty();
	}

	public List<RecipeElementDTO> getRawMaterialsAndToppings() {
		List<RecipeElementDTO> result = new ArrayList<>();
		result.addAll(upcMasterRepository.findByProductTypeCatalogIdAndUpcMasterStatusCatalogId(
						ProductTypeCatalog.PRODUCT_TYPE_RM,
						UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE).stream()
				.map(upc -> new RecipeElementDTO(
						upc.getUpcMasterId(),
						upc.getPrincipalUpc(),
						upc.getProductName(),
						upc.getContentPerUnitUom().getCatalogId(),
						upc.getSalePrice(),
						false))
				.collect(Collectors.toList())
		);
		result.addAll(getToppingRecipeElements());
		return result;
	}

	private RecipeDetailDTO toDtoCalculatingCost(RecipeDetailModel detail) {
		return toDtoCalculatingCost(detail, null);
	}

	private RecipeDetailDTO toDtoCalculatingCost(RecipeDetailModel detail, Long storeNumId) {
		RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO().parseToDTO(detail);
		UpcMasterModel detailProduct = detail.getUpcMaster();

		getIngredientCost(
				detailProduct.getUpcMasterId(),
				detailProduct.getContentPerUnit(),
				detailProduct.getContentPerUnitUom().getCatalogId(),
				detail.getUnit().getCatalogId(),
				storeNumId,
				detail.isTopping())
				.ifPresent(cost -> recipeDetailDTO.updateCost(detail.getQty(), cost));
		return recipeDetailDTO;
	}

	public Optional<Double> getIngredientCost(Long ingredientUpcMasterId,
											  Double ingredientProductContentPerUnit,
											  Long ingredientProductContentPerUnitUomId,
											  Long ingredientUnitId,
											  boolean isTopping) {
		return getIngredientCost(ingredientUpcMasterId,
				ingredientProductContentPerUnit,
				ingredientProductContentPerUnitUomId,
				ingredientUnitId,
				null,
				isTopping);
	}
	public Optional<Double> getIngredientCost(Long ingredientUpcMasterId,
											  Double ingredientProductContentPerUnit,
											  Long ingredientProductContentPerUnitUomId,
											  Long ingredientUnitId,
											  Long storeNumId,
											  boolean isTopping) {
		Optional<Long> storeNumIdOp = Optional.ofNullable(storeNumId);
		Double convertedUnit = unitConverter.convert(
				ingredientProductContentPerUnitUomId,
				ingredientUnitId,
				ingredientProductContentPerUnit);
		Optional<Double> costOptional;

		if (isTopping) {
			costOptional = upcMasterRepository.findById(ingredientUpcMasterId)
					.map(upc -> storeNumIdOp.map(upc::getSalePrice).orElse(upc.getSalePrice()))
					.map(cost -> cost / convertedUnit);
		} else {
			costOptional = upcVendorDetailsService.findDefaultVendorDetailsFor(ingredientUpcMasterId)
					.map(vendorDetails -> storeNumIdOp.map(vendorDetails::getProductCost).orElse(vendorDetails.getAverageProductCost()))
					.map(cost -> cost / convertedUnit);
		}
		return costOptional;
	}

	private Supplier<ResourceNotFoundException> getResourceNotFoundByRecipeIdException(Long recipeId) {
		return () -> new ResourceNotFoundException("Recipe", "id", recipeId);
	}

	public List<RecipeElementDTO> getToppingRecipeElements() {
		return recipeRepository.findByFoodClassificationCatalogId(MealCategoryCatalog.TOPPING).stream()
				.map(RecipeModel::getRelatedUpcMaster)
				.map(upc -> new RecipeElementDTO(
						upc.getUpcMasterId(),
						upc.getPrincipalUpc(),
						upc.getProductName(),
						upc.getContentPerUnitUom().getCatalogId(),
						upc.getSalePrice(),
						true))
				.collect(Collectors.toList());
	}

	public List<RecipeDetailDTO> getRecipeIngredientsByUpcMasterId(Long upcMasterId) {
		return recipeDetailRepository.findByRecipeRelatedUpcMasterUpcMasterId(upcMasterId).stream()
				.map(this::toDtoCalculatingCost)
				.collect(Collectors.toUnmodifiableList());
	}

	public boolean isTopping(Long upcMasterId) {
		return recipeRepository.findByRelatedUpcMasterUpcMasterId(upcMasterId)
				.map(RecipeModel::getFoodClassification)
				.map(category -> Objects.equals(category.getCatalogId(), MealCategoryCatalog.TOPPING))
				.orElse(false);
	}

	public void enableExternally(Long recipeId) {
		RecipeModel recipe = recipeRepository.findById(recipeId)
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeId));

		recipe.setDisplayExternally(true);
		recipeRepository.save(recipe);
	}

	public void disableExternally(Long recipeId) {
		RecipeModel recipe = recipeRepository.findById(recipeId)
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeId));

		recipe.setDisplayExternally(false);
		recipeRepository.save(recipe);
	}

	@EventListener
	@Transactional
	public void handleUpcVendorDetailChangedEvent(UpcVendorDetailChangedEvent event) {
		log.info("Updating Topping recipe prices after Vendor - UPC modification");
		UpcVendorDetailDTO upcVendorDetailDTO = upcVendorDetailsService.findById(event.getUpcVendorDetailId());
		log.info(String.format("Vendor: %s-%s, UPC: %s-%s",
				upcVendorDetailDTO.getVendorCode(),
				upcVendorDetailDTO.getVendorName(),
				upcVendorDetailDTO.getUpc(),
				upcVendorDetailDTO.getProductName()));
		updateSellingPriceOfToppingsWithUpcMasterId(upcVendorDetailDTO.getUpcMasterId(), null);
	}

	@EventListener
	@Transactional
	public void handleVendorStoreRemovedEvent(VendorStoreRemovedEvent event) {
		log.info(String.format("Updating Topping recipe prices after Vendor[%d] - Store[%d] removal",
				event.getVendorMasterId(),
				event.getStoreNumId()));
		upcVendorDetailsService.findProductsForVendor(event.getVendorMasterId()).stream()
				.map(SimpleUpcDTO::getUpcMasterId)
				.forEach(upcMasterId -> updateSellingPriceOfToppingsWithUpcMasterId(upcMasterId, event.getStoreNumId()));
	}

	private void updateSellingPriceOfToppingsWithUpcMasterId(Long upcMasterId, Long storeNumberId) {
		recipeDetailRepository.findByUpcMasterUpcMasterId(upcMasterId).stream()
				.map(RecipeDetailModel::getRecipe)
				.filter(r -> Objects.equals(r.getFoodClassification().getCatalogId(), MealCategoryCatalog.TOPPING))
				.forEach(r -> {
					double sellingPrice = Optional.ofNullable(r.getDetails()).orElse(List.of()).stream()
							.map(detail -> toDtoCalculatingCost(detail, storeNumberId).getCost())
							.filter(Objects::nonNull)
							.reduce(0d, Double::sum);
					r.updateSalePrice(sellingPrice, storeNumberId);
					recipeRepository.save(r);
				});
	}

	public List<RecipeStorePriceDTO> getStorePrices(Long recipeId) {
		RecipeModel recipe = recipeRepository.findById(recipeId)
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeId));

		List<RecipeStorePriceDTO> currentStorePrices = upcStorePriceRepository
				.findByUpcMasterUpcMasterId(recipe.getRelatedUpcMaster().getUpcMasterId()).stream()
				.map(sp -> new RecipeStorePriceDTO().parseToDTO(sp))
				.collect(Collectors.toUnmodifiableList());

		return storeNumberService.load().stream()
				.map(st -> {
					Double totalCost = recipe.getDetails().stream()
							.map(detail -> toDtoCalculatingCost(detail, st.getStoreNumId()).getCost())
							.filter(Objects::nonNull)
							.reduce(0d, Double::sum);

					RecipeStorePriceDTO storePriceDTO = new RecipeStorePriceDTO()
							.setStoreInfo(st)
							.setCostInformation(totalCost, recipe.getIndirectCost(), recipe.getBufferPricePercentage());

					currentStorePrices.stream().filter(sp -> sp.equals(storePriceDTO))
							.findFirst()
							.ifPresent(storePriceDTO::merge);
					return storePriceDTO;
				})
				.sorted(Comparator.comparingLong(RecipeStorePriceDTO::getStoreNumId))
				.collect(Collectors.toUnmodifiableList());
	}

	@Transactional
	public RecipeDTO updateFinancialInfo(RecipeFinancialInfoDTO financialInfoDTO) {
		Long recipeId = financialInfoDTO.getRecipeId();
		RecipeModel recipeToUpdate = recipeRepository.findById(recipeId)
				.orElseThrow(getResourceNotFoundByRecipeIdException(recipeId));

		recipeToUpdate.updateFinancialInformation(financialInfoDTO, authenticationFacade.getCurrentUserId());
		upcMasterRepository.save(recipeToUpdate.getRelatedUpcMaster());
		
		for(RecipeStorePriceDTO currentStorePrice : financialInfoDTO.getStorePrices()) {
			if(currentStorePrice.getSalePrice() !=null && currentStorePrice.getSalePrice()>0) {
				recipeDrinkConfService.disableDrinksConfiguration(currentStorePrice.getStoreNumId(), recipeToUpdate.getRelatedUpcMaster().getUpcMasterId());
			}
		}
			

		return new RecipeDTO().parseToDTO(recipeRepository.save(recipeToUpdate));
	}

	public RecipieLabelDTO findByIdAndStore(Long recipeId, Long storeNumberId) {
		RecipeModel recipeModel= recipeRepository.findById(recipeId)
									.orElseThrow(getResourceNotFoundByRecipeIdException(recipeId));
		
		RecipeDTO recipeDetails = new RecipeDTO().parseToDTO(recipeModel);
			
		
		
		UpcStorePriceModel upcStorePriceDetails = upcStorePriceRepository.
				findByUpcMasterUpcMasterIdAndStoreNumberStoreNumId(recipeModel.getRelatedUpcMaster().getUpcMasterId(), storeNumberId);
		
		return new RecipieLabelDTO(recipeDetails, upcStorePriceDetails.getStoreNumber().getStoreName(), 
				upcStorePriceDetails.getSalePrice(), upcStorePriceDetails.getStoreNumber().getAddress());
	
	}
}
