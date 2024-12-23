package com.ust.retail.store.pim.controller.external;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.product.ExternalProductByStoreAndSkuListRequest;
import com.ust.retail.store.pim.dto.external.product.ExternalProductByStoreRequest;
import com.ust.retail.store.pim.dto.external.product.ExternalProductDTO;
import com.ust.retail.store.pim.service.external.ExternalProductService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalProductControllerTest {

	private static FixtureLoader fixtureLoader;

	@Mock
	private ExternalProductService mockExternalProductService;

	@InjectMocks
	private ExternalProductController controller;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ExternalProductControllerTest.class);
	}

	@Test
	void findByStoreReturnsExpected() {
		List<ExternalProductDTO> externalStoreList = fixtureLoader.getObject("externalProductList", new ExternalProductListReference()).orElse(List.of());
		ExternalProductByStoreRequest request = new ExternalProductByStoreRequest();
		request.setStoreId(1L);

		when(mockExternalProductService.findByFilters(request)).thenReturn(externalStoreList);

		ExternalApiResponse<List<ExternalProductDTO>> result = controller.findByStore(request);

		assertThat(result.getResponse(), hasSize(2));
		assertThat(result.getResponse(), contains(List.of(
				allOf(hasProperty("productId", is(1L)), hasProperty("productName", is("Product 1"))),
				allOf(hasProperty("productId", is(2L)), hasProperty("productName", is("Product 2")))
		)));
	}

	@Test
	void findByStoreAndSkuListReturnsExpected() {
		List<ExternalProductDTO> externalStoreList = fixtureLoader.getObject("externalProductList", new ExternalProductListReference()).orElse(List.of());
		ExternalProductByStoreAndSkuListRequest request = new ExternalProductByStoreAndSkuListRequest();
		request.setStoreId(1L);

		when(mockExternalProductService.findByStoreAndSkuList(request)).thenReturn(externalStoreList);

		ExternalApiResponse<List<ExternalProductDTO>> result = controller.findByStoreAndSkuList(request);

		assertThat(result.getResponse(), hasSize(2));
		assertThat(result.getResponse(), contains(List.of(
				allOf(hasProperty("productId", is(1L)), hasProperty("productName", is("Product 1"))),
				allOf(hasProperty("productId", is(2L)), hasProperty("productName", is("Product 2")))
		)));
	}

	@Test
	void updateImageTrainedReturnsExpected() {
		ExternalApiResponse<String> result = controller.updateImageTrained(1L, true);

		assertThat(result.getResponse(), is("Success"));
	}

	private static class ExternalProductListReference extends TypeReference<List<ExternalProductDTO>> {
	}
}
