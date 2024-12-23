package com.ust.retail.store.bistro.controller.recipe;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.ust.retail.store.bistro.dto.recipes.AddOnDTO;
import com.ust.retail.store.bistro.dto.recipes.LoadUpcAddOnDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeElementDTO;
import com.ust.retail.store.bistro.service.recipes.RecipeAddonService;
import com.ust.retail.store.pim.common.GenericResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeAddonsControllerTest {
	@Mock
	private RecipeAddonService mockRecipeAddonService;

	@InjectMocks
	private RecipeAddonsController controller;

	@Test
	void addAddOnReturnsExpected() {
		AddOnDTO request = new AddOnDTO();
		when(mockRecipeAddonService.addAddon(request)).thenReturn(new GenericResponse());

		GenericResponse result = controller.addAddon(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		AddOnDTO request = new AddOnDTO();
		when(mockRecipeAddonService.updateAddon(request)).thenReturn(new GenericResponse());

		GenericResponse result = controller.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeAddonReturnsExpected() {
		when(mockRecipeAddonService.removeAddOn(1L)).thenReturn(new GenericResponse());

		GenericResponse result = controller.removeAddon(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadAddonsReturnsExpected() {
		LoadUpcAddOnDTO request = new LoadUpcAddOnDTO();
		when(mockRecipeAddonService.loadAllAddonsPerRecipe(request)).thenReturn(Page.empty());

		Page<LoadUpcAddOnDTO> result = controller.loadAddons(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadUpcMasterInformationReturnsExpected() {
		when(mockRecipeAddonService.loadUpcAddonInformation(1L)).thenReturn(new LoadUpcAddOnDTO());

		LoadUpcAddOnDTO result = controller.loadUpcMasterInformation(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadAddOnListReturnsExpected() {
		when(mockRecipeAddonService.loadAddOnList(1L)).thenReturn(List.of());

		List<RecipeElementDTO> result = controller.loadAddOnList(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockRecipeAddonService.findById(1L)).thenReturn(new AddOnDTO());

		AddOnDTO result = controller.findById(1L);

		assertThat(result, is(notNullValue()));
	}
}
