package com.ust.retail.store.pim.service.external;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.dto.external.store.ExternalStoreDTO;
import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.repository.catalog.StoreNumberRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalStoreNumberServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private StoreNumberRepository mockStoreNumberRepository;

	@InjectMocks
	private ExternalStoreNumberService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ExternalStoreNumberServiceTest.class);
	}

	@Test
	void loadCatalogReturnsExpected() {
		StoreNumberModel storeNumber = fixtureLoader.getObject("storeNumber", StoreNumberModel.class).orElse(new StoreNumberModel());
		when(mockStoreNumberRepository.findAll()).thenReturn(List.of(storeNumber));
		List<ExternalStoreDTO> result = service.loadCatalog();

		assertThat(result, is(notNullValue()));
	}
}
