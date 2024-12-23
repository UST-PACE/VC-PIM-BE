package com.ust.retail.store.bistro.controller.recipe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.bistro.dto.recipes.BistroNewUpcResponseDTO;
import com.ust.retail.store.bistro.dto.recipes.BistroUpcMasterDTO;
import com.ust.retail.store.bistro.service.recipes.RecipeUpcMasterService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeUpcMasterControllerTest {
	@Mock
	private RecipeUpcMasterService mockRecipeUpcMasterService;

	@InjectMocks
	private RecipeUpcMasterController controller;

	@Test
	void createReturnsExpected() {
		BistroUpcMasterDTO request = new BistroUpcMasterDTO();
		when(mockRecipeUpcMasterService.save(request)).thenReturn(new BistroNewUpcResponseDTO());

		BistroNewUpcResponseDTO result = controller.create(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		BistroUpcMasterDTO request = new BistroUpcMasterDTO();
		when(mockRecipeUpcMasterService.update(request)).thenReturn(new BistroUpcMasterDTO());

		BistroUpcMasterDTO result = controller.update(request);

		assertThat(result, is(notNullValue()));
	}
}
