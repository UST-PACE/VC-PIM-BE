package com.ust.retail.store.bistro.controller.recipe;

import com.ust.retail.store.common.annotations.OnFinancialInfo;
import com.ust.retail.store.bistro.commons.annotations.OnInstructions;
import com.ust.retail.store.bistro.commons.annotations.OnLabelTexts;
import com.ust.retail.store.bistro.commons.annotations.OnNutritionalValues;
import com.ust.retail.store.bistro.commons.catalogs.MealCategoryCatalog;
import com.ust.retail.store.bistro.commons.catalogs.MealTemperatureCatalog;
import com.ust.retail.store.bistro.commons.catalogs.RecipeIngredientTypeCatalog;
import com.ust.retail.store.bistro.commons.catalogs.RecipePreparationAreaCatalog;
import com.ust.retail.store.bistro.dto.recipes.*;
import com.ust.retail.store.bistro.service.recipes.RecipeService;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnRemove;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/bistro/p/recipe")
@Validated
public class RecipeController extends BaseController {

	private final RecipeService recipeService;
	private final MealCategoryCatalog mealCategoryCatalog;
	private final RecipePreparationAreaCatalog recipePreparationAreaCatalog;
	private final RecipeIngredientTypeCatalog recipeIngredientTypeCatalog;
	private final MealTemperatureCatalog mealTemperatureCatalog;

	@Autowired
	public RecipeController(
			RecipeService recipeService,
			MealCategoryCatalog mealCategoryCatalog,
			RecipePreparationAreaCatalog recipePreparationAreaCatalog,
			RecipeIngredientTypeCatalog recipeIngredientTypeCatalog,
			MealTemperatureCatalog mealTemperatureCatalog) {
		
		super();
		this.recipeService = recipeService;
		this.mealCategoryCatalog = mealCategoryCatalog;
		this.recipePreparationAreaCatalog = recipePreparationAreaCatalog;
		this.recipeIngredientTypeCatalog = recipeIngredientTypeCatalog;
		this.mealTemperatureCatalog = mealTemperatureCatalog;
	}
		
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/find/id/{id}")
	public RecipeDTO findById(@PathVariable("id") Long recipeId) {
		return recipeService.findById(recipeId);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/find/id/{recipeId}/store/{storeId}")
	public RecipieLabelDTO findByIdAndStore(@PathVariable("recipeId") Long recipeId,@PathVariable("storeId") Long storeId) {
		return recipeService.findByIdAndStore(recipeId,storeId);
	}
	

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/find/upcmasterid/{upcMasterId}")
	public RecipeDTO findByUpcMasterId(@PathVariable("upcMasterId") Long upcMasterId) {
		return recipeService.findByUpcMasterId(upcMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/find/name/{recipeName}")
	public List<RecipeDTO> findByName(@PathVariable(value = "recipeName") String recipeName) {
		return recipeService.findByName(recipeName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/details")
	@Validated(OnFilter.class)
	public Page<RecipeDetailDTO> findDetailsById(@Valid @RequestBody RecipeFilterDTO recipeFilterDTO) {
		return recipeService.findDetailsByRecipeId(recipeFilterDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/details/{recipeId}")
	public List<RecipeDetailDTO> findDetailsById(@PathVariable(name = "recipeId") Long recipeId) {
		return recipeService.findDetailsByRecipeId(recipeId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/details/optional/{recipeId}")
	public List<RecipeDetailDTO> findOptionalDetailsById(@PathVariable(name = "recipeId") Long recipeId) {
		return recipeService.findOptionalDetailsByRecipeId(recipeId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public RecipeDTO update(@Valid @RequestBody RecipeDTO recipeDTO) {
		return recipeService.update(recipeDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/add/detail")
	@Validated(OnCreate.class)
	public RecipeDTO addItem(@Valid @RequestBody RecipeDetailDTO recipeDetailDTO ) {
		return recipeService.addItem(recipeDetailDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update/detail")
	@Validated(OnUpdate.class)
	public RecipeDTO updateItem(@Valid @RequestBody RecipeDetailDTO recipeDetailDTO ) {
		return recipeService.updateItem(recipeDetailDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@DeleteMapping("/remove/detail/{recipeDetailId}")
	@Validated(OnRemove.class)
	public RecipeDTO removeItem(@Valid @PathVariable(value = "recipeDetailId") Long recipeDetailId) {
		return recipeService.removeItem(recipeDetailId);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update/instructions")
	@Validated(OnInstructions.class)
	public RecipeDTO updatePreparationInstructions(@Valid @RequestBody RecipeDTO recipeDTO) {
		return recipeService.updatePreparationInstructions(recipeDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update/nutritionalvalues")
	@Validated(OnNutritionalValues.class)
	public RecipeDTO updateNutritionalValues(@Valid @RequestBody RecipeDTO recipeDTO) {
		return recipeService.updateNutritionalValues(recipeDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update/labeltexts")
	@Validated(OnLabelTexts.class)
	public RecipeDTO updateLabelTexts(@Valid @RequestBody RecipeDTO recipeDTO) {
		return recipeService.updateLabelTexts(recipeDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update/financialinfo")
	@Validated(OnFinancialInfo.class)
	public RecipeDTO updateFinancialInformation(@Valid @RequestBody RecipeFinancialInfoDTO financialInfoDTO) {
		return recipeService.updateFinancialInfo(financialInfoDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/find/recipes/filters")
	@Validated(OnFilter.class)
	public Page<RecipeFilterDTO> findByFilters(@Valid @RequestBody RecipeFilterDTO recipeFilterDTO ) {
		return recipeService.findByFilters(recipeFilterDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/enable/{recipeId}")
	public GenericResponse enableExternally(@PathVariable(name = "recipeId") Long recipeId) {
		recipeService.enableExternally(recipeId);
		return new GenericResponse(GenericResponse.OP_TYPE_UPDATE, GenericResponse.SUCCESS_CODE, "ENABLED");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/disable/{recipeId}")
	public GenericResponse disableExternally(@PathVariable(name = "recipeId") Long recipeId) {
		recipeService.disableExternally(recipeId);
		return new GenericResponse(GenericResponse.OP_TYPE_UPDATE, GenericResponse.SUCCESS_CODE, "DISABLED");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/storeprices/{recipeId}")
	public List<RecipeStorePriceDTO> getStorePrices(@PathVariable(name = "recipeId") Long recipeId) {
		return recipeService.getStorePrices(recipeId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/load/mealcategories")
	public List<CatalogDTO> loadMealCategoryCatalog() {
		return mealCategoryCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/barcode/{recipeId}")
	public BarcodeDTO getRecipeBarcode(@PathVariable Long recipeId) {
		return recipeService.getRecipeBarcode(recipeId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/load/mealtemperatures")
	public List<CatalogDTO> loadMealTemperatureCatalog() {
		return mealTemperatureCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/load/preparationareas")
	public List<CatalogDTO> loadPreparationAreaCatalog() {
		return recipePreparationAreaCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/load/ingredienttypes")
	public List<CatalogDTO> loadIngredientTypeCatalog() {
		return recipeIngredientTypeCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/load/dishlist")
	public List<RecipeDTO> loadDishList() {
		return recipeService.loadDishList();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping({"/load/ingredientcandidates", "/load/ingredientcandidates/{recipeId}"})
	public List<RecipeElementDTO> loadIngredientList(@PathVariable(required = false) Long recipeId) {
		return recipeService.loadIngredientList(recipeId);
	}

}
