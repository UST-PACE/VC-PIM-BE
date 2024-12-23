package com.ust.retail.store.bistro.service.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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
import com.ust.retail.store.pim.service.upcmaster.UpcMasterService;
import com.ust.retail.store.pim.service.upcmaster.UpcVendorDetailsService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeAddonServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private RecipeRepository mockRecipeRepository;
	@Mock
	private RecipeAddonRepository mockRecipeAddonRepository;
	@Mock
	private UpcVendorDetailsService mockUpcVendorDetailsService;
	@Mock
	private UpcMasterService mockUpcMasterService;
	@Mock
	private RecipeService mockRecipeService;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private RecipeAddonService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(RecipeAddonServiceTest.class);
	}

	@Test
	void addAddOnReturnsExpected() {
		AddOnDTO request = fixtureLoader.getObject("request", AddOnDTO.class).orElse(new AddOnDTO());
		Optional<RecipeModel> recipeModel = fixtureLoader.getObject("recipeModel", RecipeModel.class);
		when(mockRecipeRepository.findById(1L)).thenReturn(recipeModel);

		GenericResponse result = service.addAddon(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void addAddOnThrowsExceptionWhenRecipeNotFound() {
		AddOnDTO request = fixtureLoader.getObject("request", AddOnDTO.class).orElse(new AddOnDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.addAddon(request));
	}

	@Test
	void updateAddOnReturnsExpected() {
		AddOnDTO request = fixtureLoader.getObject("request", AddOnDTO.class).orElse(new AddOnDTO());
		Optional<RecipeAddonModel> recipeAddOnModel = fixtureLoader.getObject("recipeAddOnTopping", RecipeAddonModel.class);
		when(mockRecipeAddonRepository.findById(1L)).thenReturn(recipeAddOnModel);

		GenericResponse result = service.updateAddon(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateAddOnThrowsExceptionWhenRecipeAddOnNotFound() {
		AddOnDTO request = fixtureLoader.getObject("request", AddOnDTO.class).orElse(new AddOnDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.updateAddon(request));
	}

	@Test
	void removeAddOnReturnsExpected() {
		Optional<RecipeAddonModel> recipeAddOnModel = fixtureLoader.getObject("recipeAddOnTopping", RecipeAddonModel.class);
		when(mockRecipeAddonRepository.findById(1L)).thenReturn(recipeAddOnModel);

		GenericResponse result = service.removeAddOn(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeAddOnThrowsExceptionWhenAddOnNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.removeAddOn(1L));
	}

	@Test
	void loadAllAddonsPerRecipeReturnsExpected() {
		LoadUpcAddOnDTO request = new LoadUpcAddOnDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");
		RecipeAddonModel recipeAddOnTopping = fixtureLoader.getObject("recipeAddOnTopping", RecipeAddonModel.class).orElse(new RecipeAddonModel());

		when(mockRecipeAddonRepository.findByRecipeRecipeId(any(), any())).thenReturn(new PageImpl<>(List.of(recipeAddOnTopping)));

		Page<LoadUpcAddOnDTO> result = service.loadAllAddonsPerRecipe(request);

		assertThat(result.getContent(), is(notNullValue()));
	}

	@Test
	void loadUpcAddonInformationReturnsExpectedWhenProductIsNotTopping() {
		UpcMasterDTO product = fixtureLoader.getObject("rmAddOnProduct", UpcMasterDTO.class).orElse(new UpcMasterDTO());

		when(mockUpcMasterService.findById(1L)).thenReturn(product);

		LoadUpcAddOnDTO result = service.loadUpcAddonInformation(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<RecipeAddonModel> recipeAddOnTopping = fixtureLoader.getObject("recipeAddOnTopping", RecipeAddonModel.class);

		when(mockRecipeAddonRepository.findById(1L)).thenReturn(recipeAddOnTopping);

		AddOnDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenAddOnNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void loadAddOnListReturnsExpectedWhenRecipeIdIsNull() {
		List<RecipeElementDTO> result = service.loadAddOnList(null);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadAddOnListReturnsExpectedWhenRecipeIsNotNull() {
		RecipeElementDTO rmAddOnProduct = fixtureLoader.getObject("rmRecipeElement", RecipeElementDTO.class).orElse(new RecipeElementDTO());
		RecipeElementDTO rmAddOnProduct2 = fixtureLoader.getObject("rmRecipeElement2", RecipeElementDTO.class).orElse(new RecipeElementDTO());
		RecipeElementDTO rmAddOnProduct3 = fixtureLoader.getObject("rmRecipeElement3", RecipeElementDTO.class).orElse(new RecipeElementDTO());
		RecipeElementDTO toppingAddOnProduct = fixtureLoader.getObject("toppingRecipeElement", RecipeElementDTO.class).orElse(new RecipeElementDTO());

		RecipeAddonModel recipeAddOnTopping = fixtureLoader.getObject("recipeAddOnTopping", RecipeAddonModel.class).orElse(new RecipeAddonModel());

		when(mockRecipeService.getRawMaterialsAndToppings()).thenReturn(new ArrayList<>(List.of(rmAddOnProduct, rmAddOnProduct2, rmAddOnProduct3, toppingAddOnProduct)));
		when(mockRecipeAddonRepository.findByRecipeRecipeId(1L)).thenReturn(List.of(recipeAddOnTopping));
		when(mockUpcVendorDetailsService.productHasDefaultVendor(any())).then(invocation -> Objects.equals(invocation.getArgument(0), 3L));

		List<RecipeElementDTO> result = service.loadAddOnList(1L);

		assertThat(result, hasSize(2));
	}
}
