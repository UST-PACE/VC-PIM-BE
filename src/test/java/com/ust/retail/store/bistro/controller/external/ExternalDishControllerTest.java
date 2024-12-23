package com.ust.retail.store.bistro.controller.external;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishByStoreAndSkuListRequest;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishByStoreRequest;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishDTO;
import com.ust.retail.store.bistro.service.external.ExternalDishService;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalDishControllerTest {

	private static FixtureLoader fixtureLoader;

	@Mock
	private ExternalDishService mockExternalDishService;

	@InjectMocks
	private ExternalDishController controller;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ExternalDishControllerTest.class);
	}

	@Test
	void findByStoreReturnsExpected() {
		List<ExternalDishDTO> externalStoreList = fixtureLoader.getObject("externalDishList", new ExternalDishControllerTest.ExternalDishListReference()).orElse(List.of());
		ExternalDishByStoreRequest request = new ExternalDishByStoreRequest();
		request.setStoreId(1L);

		when(mockExternalDishService.findByFilters(request)).thenReturn(externalStoreList);

		ExternalApiResponse<List<ExternalDishDTO>> result = controller.findByStore(request);

		assertThat(result.getResponse(), hasSize(2));
		assertThat(result.getResponse(), contains(List.of(
				allOf(hasProperty("productId", is(1L)), hasProperty("productName", is("Dish 1"))),
				allOf(hasProperty("productId", is(2L)), hasProperty("productName", is("Dish 2")))
		)));
	}

	@Test
	void findByStoreAndSkuListReturnsExpected() {
		List<ExternalDishDTO> externalStoreList = fixtureLoader.getObject("externalDishList", new ExternalDishControllerTest.ExternalDishListReference()).orElse(List.of());
		ExternalDishByStoreAndSkuListRequest request = new ExternalDishByStoreAndSkuListRequest();
		request.setStoreId(1L);

		when(mockExternalDishService.findByStoreAndSkuList(request)).thenReturn(externalStoreList);

		ExternalApiResponse<List<ExternalDishDTO>> result = controller.findByStoreAndSkuList(request);

		assertThat(result.getResponse(), hasSize(2));
		assertThat(result.getResponse(), contains(List.of(
				allOf(hasProperty("productId", is(1L)), hasProperty("productName", is("Dish 1"))),
				allOf(hasProperty("productId", is(2L)), hasProperty("productName", is("Dish 2")))
		)));
	}

	private static class ExternalDishListReference extends TypeReference<List<ExternalDishDTO>> {
	}
}
