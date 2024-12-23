package com.ust.retail.store.bistro.service.recipes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.ust.retail.store.bistro.repository.recipes.RecipeSubstitutionRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.bistro.commons.catalogs.MealCategoryCatalog;
import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.bistro.dto.recipes.BarcodeDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeDetailDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeElementDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeFilterDTO;
import com.ust.retail.store.bistro.model.recipes.RecipeDetailModel;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.bistro.repository.recipes.RecipeAddonRepository;
import com.ust.retail.store.bistro.repository.recipes.RecipeDetailRepository;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.service.upcmaster.UpcVendorDetailsService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private RecipeRepository mockRecipeRepository;
	@Mock
	private RecipeDetailRepository mockRecipeDetailRepository;
	@Mock
	private RecipeAddonRepository mockRecipeAddonRepository;
	@Mock
	private RecipeSubstitutionRepository recipeSubstitutionRepository;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private UpcVendorDetailsService mockUpcVendorDetailsService;
	@Mock
	private UnitConverter mockUnitConverter;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private RecipeService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(RecipeServiceTest.class);
	}

	@Test
	void updateReturnsExpected() {
		RecipeDTO request = fixtureLoader.getObject("updateRequest", RecipeDTO.class).orElse(new RecipeDTO());
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeRepository.findById(1L)).thenReturn(recipeModel);
		when(mockRecipeRepository.save(any())).then(invocation -> invocation.getArgument(0));

		RecipeDTO result = service.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateThrowsExceptionWheRecipeNotFound() {
		RecipeDTO request = fixtureLoader.getObject("updateRequest", RecipeDTO.class).orElse(new RecipeDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.update(request));
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeRepository.findById(1L)).thenReturn(recipeModel);

		RecipeDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWheRecipeNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void findByUpcMasterIdReturnsExpected() {
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeRepository.findByRelatedUpcMasterUpcMasterId(1L)).thenReturn(recipeModel);

		RecipeDTO result = service.findByUpcMasterId(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByUpcMasterIdThrowsExceptionWheRecipeNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findByUpcMasterId(1L));
	}

	@Test
	void findDetailsByRecipeIdReturnsExpected() {
		RecipeFilterDTO request = new RecipeFilterDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");

		List<RecipeDetailModel> recipeDetails = fixtureLoader.getObject("recipeDetails", new RecipeDetailListReference()).orElse(List.of());
		Optional<UpcVendorDetailsModel> vendorDetails = fixtureLoader.getObject("vendorDetails", UpcVendorDetailsModel.class);
		when(mockRecipeDetailRepository.findByRecipeRecipeId(any(), any())).thenReturn(new PageImpl<>(recipeDetails));
		when(mockUnitConverter.convert(any(), any(), any())).thenReturn(1d);
		when(mockUpcVendorDetailsService.findDefaultVendorDetailsFor(2L)).thenReturn(vendorDetails);

		Page<RecipeDetailDTO> result = service.findDetailsByRecipeId(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByFiltersReturnsExpected() {
		RecipeFilterDTO request = fixtureLoader.getObject("byFiltersRequest", RecipeFilterDTO.class).orElse(new RecipeFilterDTO());
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");

		List<RecipeModel> recipeList = fixtureLoader.getObject("recipeList", new RecipeListReference()).orElse(List.of());
		when(mockRecipeRepository.findByFilters(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(recipeList));
		Page<RecipeFilterDTO> result = service.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void addItemReturnsExpected() {
		RecipeDetailDTO request = fixtureLoader.getObject("addItemRequest", RecipeDetailDTO.class).orElse(new RecipeDetailDTO());
		Optional<RecipeModel> firstRecipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		Optional<RecipeModel> secondRecipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeRepository.findById(1L)).thenReturn(firstRecipeModel, Arrays.array(secondRecipeModel));

		RecipeDTO result = service.addItem(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void addItemThrowsExceptionWhenRecipeNotFound() {
		RecipeDetailDTO request = fixtureLoader.getObject("addItemRequest", RecipeDetailDTO.class).orElse(new RecipeDetailDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.addItem(request));
	}

	@Test
	void updateItemReturnsExpected() {
		RecipeDetailDTO request = fixtureLoader.getObject("updateItemRequest", RecipeDetailDTO.class).orElse(new RecipeDetailDTO());
		Optional<RecipeDetailModel> recipeDetail = fixtureLoader.getObject("recipeDetail", RecipeDetailModel.class);
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeDetailRepository.findById(1L)).thenReturn(recipeDetail);
		when(mockRecipeDetailRepository.saveAndFlush(any())).then(invocation -> invocation.getArgument(0));
		when(mockRecipeRepository.findById(1L)).thenReturn(recipeModel);

		RecipeDTO result = service.updateItem(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateItemThrowsExceptionWhenRecipeDetailNotFound() {
		RecipeDetailDTO request = fixtureLoader.getObject("updateItemRequest", RecipeDetailDTO.class).orElse(new RecipeDetailDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.updateItem(request));
	}

	@Test
	void removeItemReturnsExpected() {
		Optional<RecipeDetailModel> recipeDetail = fixtureLoader.getObject("recipeDetail", RecipeDetailModel.class);
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeDetailRepository.findById(1L)).thenReturn(recipeDetail);
		when(mockRecipeRepository.findById(1L)).thenReturn(recipeModel);

		RecipeDTO result = service.removeItem(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeItemThrowsExceptionWhenRecipeDetailNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.removeItem(1L));
	}

	@Test
	void updatePreparationInstructionsReturnsExpected() {
		RecipeDTO request = fixtureLoader.getObject("updateRequest", RecipeDTO.class).orElse(new RecipeDTO());
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeRepository.findById(1L)).thenReturn(recipeModel);
		when(mockRecipeRepository.save(any())).then(invocation -> invocation.getArgument(0));

		RecipeDTO result = service.updatePreparationInstructions(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updatePreparationInstructionsThrowsExceptionWhenRecipeNotFound() {
		RecipeDTO request = fixtureLoader.getObject("updateRequest", RecipeDTO.class).orElse(new RecipeDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.updatePreparationInstructions(request));
	}

	@Test
	void updateNutritionalValuesReturnsExpected() {
		RecipeDTO request = fixtureLoader.getObject("updateNutritionalValuesRequest", RecipeDTO.class).orElse(new RecipeDTO());
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeRepository.findById(1L)).thenReturn(recipeModel);
		when(mockRecipeRepository.save(any())).then(invocation -> invocation.getArgument(0));

		RecipeDTO result = service.updateNutritionalValues(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateNutritionalValuesThrowsExceptionWhenRecipeNotFound() {
		RecipeDTO request = fixtureLoader.getObject("updateNutritionalValuesRequest", RecipeDTO.class).orElse(new RecipeDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.updateNutritionalValues(request));
	}

	@Test
	void updateLabelTextsReturnsExpected() {
		RecipeDTO request = fixtureLoader.getObject("updateRequest", RecipeDTO.class).orElse(new RecipeDTO());
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeRepository.findById(1L)).thenReturn(recipeModel);
		when(mockRecipeRepository.save(any())).then(invocation -> invocation.getArgument(0));

		RecipeDTO result = service.updateLabelTexts(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateLabelTextsThrowsExceptionWhenRecipeNotFound() {
		RecipeDTO request = fixtureLoader.getObject("updateRequest", RecipeDTO.class).orElse(new RecipeDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.updateLabelTexts(request));
	}

	@Test
	void findByNameReturnsExpected() {
		RecipeModel recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class).orElse(new RecipeModel());
		when(mockRecipeRepository.findByRelatedUpcMasterProductNameContaining("NAME")).thenReturn(List.of(recipeModel));

		List<RecipeDTO> result = service.findByName("NAME");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeBarcodeReturnsExpected() {
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeRepository.findById(1L)).thenReturn(recipeModel);

		BarcodeDTO result = service.getRecipeBarcode(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeBarcodeThrowsExceptionWhenRecipeNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.getRecipeBarcode(1L));
	}

	@Test
	void loadDishListReturnsExpected() {
		RecipeModel recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class).orElse(new RecipeModel());
		when(mockRecipeRepository.findByFoodClassificationCatalogId(MealCategoryCatalog.DISH)).thenReturn(List.of(recipeModel));

		List<RecipeDTO> result = service.loadDishList();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadIngredientListReturnsExpectedForRecipe() {
		List<RecipeDetailModel> recipeDetails = fixtureLoader.getObject("recipeDetails", new RecipeDetailListReference()).orElse(List.of());
		UpcMasterModel product = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());
		RecipeModel recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class).orElse(new RecipeModel());

		when(mockRecipeRepository.findByFoodClassificationCatalogId(MealCategoryCatalog.TOPPING)).thenReturn(List.of(recipeModel));

		when(mockUpcMasterRepository.findByProductTypeCatalogIdAndUpcMasterStatusCatalogId(any(), any())).thenReturn(List.of(product));
		when(mockRecipeDetailRepository.findByRecipeRecipeId(1L)).thenReturn(recipeDetails);
		when(mockUpcVendorDetailsService.productHasDefaultVendor(any())).then(invocation -> Objects.equals(invocation.getArgument(0), 4L));

		List<RecipeElementDTO> result = service.loadIngredientList(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadIngredientListReturnsExpected() {
		UpcMasterModel product = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());
		RecipeModel recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class).orElse(new RecipeModel());

		when(mockRecipeRepository.findByFoodClassificationCatalogId(MealCategoryCatalog.TOPPING)).thenReturn(List.of(recipeModel));

		when(mockUpcMasterRepository.findByProductTypeCatalogIdAndUpcMasterStatusCatalogId(any(), any())).thenReturn(List.of(product));
		when(mockUpcVendorDetailsService.productHasDefaultVendor(any())).then(invocation -> Objects.equals(invocation.getArgument(0), 4L));

		List<RecipeElementDTO> result = service.loadIngredientList(null);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findDetailsByRecipeIdListReturnsExpected() {
		List<RecipeDetailModel> recipeDetails = fixtureLoader.getObject("recipeDetails", new RecipeDetailListReference()).orElse(List.of());
		when(mockRecipeDetailRepository.findByRecipeRecipeId(1L)).thenReturn(recipeDetails);

		List<RecipeDetailDTO> result = service.findDetailsByRecipeId(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getToppingRecipeElementsReturnsExpected() {
		RecipeModel recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class).orElse(new RecipeModel());
		when(mockRecipeRepository.findByFoodClassificationCatalogId(MealCategoryCatalog.TOPPING)).thenReturn(List.of(recipeModel));

		List<RecipeElementDTO> result = service.getToppingRecipeElements();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void isUpcUsedOnRecipesReturnsFalseWhenNoIngredientUsage() {
		boolean result = service.isUpcUsedOnRecipes(1L);

		assertThat(result, is(false));
	}

	@Test
	void isUpcUsedOnRecipesReturnsTrueWhenIngredientUsage() {
		when(mockRecipeDetailRepository.findByUpcMasterUpcMasterId(1L)).thenReturn(List.of(new RecipeDetailModel()));

		boolean result = service.isUpcUsedOnRecipes(1L);

		assertThat(result, is(true));
	}

	private static class RecipeDetailListReference extends TypeReference<List<RecipeDetailModel>> {
	}

	private static class RecipeListReference extends TypeReference<List<RecipeModel>> {
	}
}
