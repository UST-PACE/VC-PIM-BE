package com.ust.retail.store.bistro.controller.recipe;

import com.ust.retail.store.bistro.commons.catalogs.MealCategoryCatalog;
import com.ust.retail.store.bistro.commons.catalogs.MealTemperatureCatalog;
import com.ust.retail.store.bistro.dto.recipes.*;
import com.ust.retail.store.bistro.service.recipes.RecipeService;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {
	@Mock
	private RecipeService mockRecipeService;
	@Mock
	private MealCategoryCatalog mockMealCategoryCatalog;
	@Mock
	private MealTemperatureCatalog mockMealTemperatureCatalog;

	@InjectMocks
	private RecipeController controller;

	@Test
	void findByIdReturnsExpected() {
		when(mockRecipeService.findById(1L)).thenReturn(new RecipeDTO());

		RecipeDTO result = controller.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByUpcMasterIdReturnsExpected() {
		when(mockRecipeService.findByUpcMasterId(1L)).thenReturn(new RecipeDTO());

		RecipeDTO result = controller.findByUpcMasterId(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByNameReturnsExpected() {
		when(mockRecipeService.findByName("ANY")).thenReturn(List.of());

		List<RecipeDTO> result = controller.findByName("ANY");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findDetailsByIdReturnsExpected() {
		when(mockRecipeService.findDetailsByRecipeId(1L)).thenReturn(List.of());

		List<RecipeDetailDTO> result = controller.findDetailsById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findDetailsByIdReturnsExpectedPage() {
		RecipeFilterDTO request = new RecipeFilterDTO();
		when(mockRecipeService.findDetailsByRecipeId(request)).thenReturn(Page.empty());

		Page<RecipeDetailDTO> result = controller.findDetailsById(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		RecipeDTO request = new RecipeDTO();
		when(mockRecipeService.update(request)).thenReturn(new RecipeDTO());

		RecipeDTO result = controller.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void addItemReturnsExpected() {
		RecipeDetailDTO request = new RecipeDetailDTO();
		when(mockRecipeService.addItem(request)).thenReturn(new RecipeDTO());

		RecipeDTO result = controller.addItem(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateItemReturnsExpected() {
		RecipeDetailDTO request = new RecipeDetailDTO();
		when(mockRecipeService.updateItem(request)).thenReturn(new RecipeDTO());

		RecipeDTO result = controller.updateItem(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeItemReturnsExpected() {
		when(mockRecipeService.removeItem(1L)).thenReturn(new RecipeDTO());

		RecipeDTO result = controller.removeItem(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updatePreparationInstructionsReturnsExpected() {
		RecipeDTO request = new RecipeDTO();
		when(mockRecipeService.updatePreparationInstructions(request)).thenReturn(new RecipeDTO());

		RecipeDTO result = controller.updatePreparationInstructions(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateNutritionalValuesReturnsExpected() {
		RecipeDTO request = new RecipeDTO();
		when(mockRecipeService.updateNutritionalValues(request)).thenReturn(new RecipeDTO());

		RecipeDTO result = controller.updateNutritionalValues(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateLabelTextsReturnsExpected() {
		RecipeDTO request = new RecipeDTO();
		when(mockRecipeService.updateLabelTexts(request)).thenReturn(new RecipeDTO());

		RecipeDTO result = controller.updateLabelTexts(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByFiltersReturnsExpected() {
		RecipeFilterDTO request = new RecipeFilterDTO();
		when(mockRecipeService.findByFilters(request)).thenReturn(Page.empty());

		Page<RecipeFilterDTO> result = controller.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadMealCategoryCatalogReturnsExpected() {
		when(mockMealCategoryCatalog.getCatalogOptions()).thenReturn(List.of());

		List<CatalogDTO> result = controller.loadMealCategoryCatalog();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeBarcodeReturnsExpected() {
		when(mockRecipeService.getRecipeBarcode(1L)).thenReturn(new BarcodeDTO(null));

		BarcodeDTO result = controller.getRecipeBarcode(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadMealTemperatureCatalogReturnsExpected() {
		when(mockMealTemperatureCatalog.getCatalogOptions()).thenReturn(List.of());

		List<CatalogDTO> result = controller.loadMealTemperatureCatalog();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadDishListReturnsExpected() {
		when(mockRecipeService.loadDishList()).thenReturn(List.of());

		List<RecipeDTO> result = controller.loadDishList();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadIngredientListReturnsExpected() {
		when(mockRecipeService.loadIngredientList(null)).thenReturn(List.of());

		List<RecipeElementDTO> result = controller.loadIngredientList(null);

		assertThat(result, is(notNullValue()));
	}
}
