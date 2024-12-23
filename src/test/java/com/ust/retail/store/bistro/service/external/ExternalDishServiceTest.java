package com.ust.retail.store.bistro.service.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishByStoreAndSkuListRequest;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishByStoreRequest;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishDTO;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.pim.service.tax.TaxService;
import com.ust.retail.store.pim.util.FixtureLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalDishServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private RecipeRepository mockRecipeRepository;
	@Mock
	private TaxService mockTaxService;
	@InjectMocks
	private ExternalDishService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ExternalDishServiceTest.class);
	}

	@Test
	void findByFiltersReturnsExpected() {
		ExternalDishByStoreRequest request = new ExternalDishByStoreRequest();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");
		List<RecipeModel> recipes = fixtureLoader.getObject("recipes", new RecipeListReference()).orElse(List.of());

		when(mockRecipeRepository.findByStoreAndFilters(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(recipes));

		List<ExternalDishDTO> result = service.findByFilters(request);

		assertThat(result, is(notNullValue()));
		assertThat(result, is(not(empty())));
	}

	@Test
	void findByStoreAndSkuListReturnsExpected() {
		ExternalDishByStoreAndSkuListRequest request = new ExternalDishByStoreAndSkuListRequest();

		List<ExternalDishDTO> result = service.findByStoreAndSkuList(request);

		assertThat(result, is(notNullValue()));
	}

	private static class RecipeListReference extends TypeReference<List<RecipeModel>> {
	}
}
