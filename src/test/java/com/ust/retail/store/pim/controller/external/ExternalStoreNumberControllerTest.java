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
import com.ust.retail.store.pim.dto.external.store.ExternalStoreDTO;
import com.ust.retail.store.pim.service.external.ExternalStoreNumberService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalStoreNumberControllerTest {

	private static FixtureLoader fixtureLoader;

	@Mock
	private ExternalStoreNumberService mockExternalStoreNumberService;

	@InjectMocks
	private ExternalStoreNumberController controller;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ExternalStoreNumberControllerTest.class);
	}

	@Test
	void loadCatalogReturnsExpected() {
		List<ExternalStoreDTO> externalStoreList = fixtureLoader.getObject("externalStoreList", new ExternalStoreListReference()).orElse(List.of());
		when(mockExternalStoreNumberService.loadCatalog()).thenReturn(externalStoreList);

		ExternalApiResponse<List<ExternalStoreDTO>> result = controller.loadCatalog();

		assertThat(result.getResponse(), hasSize(2));
		assertThat(result.getResponse(), contains(List.of(
				allOf(hasProperty("storeId", is(1L)), hasProperty("storeName", is("Store 1"))),
				allOf(hasProperty("storeId", is(2L)), hasProperty("storeName", is("Store 2")))
		)));
	}

	private static class ExternalStoreListReference extends TypeReference<List<ExternalStoreDTO>> {
	}
}
