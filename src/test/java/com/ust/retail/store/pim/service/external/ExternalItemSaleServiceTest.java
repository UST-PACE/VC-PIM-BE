package com.ust.retail.store.pim.service.external;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.StoreFacade;
import com.ust.retail.store.pim.dto.external.sale.ExternalSoldItemListRequestDTO;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class ExternalItemSaleServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private StoreFacade mockStoreFacade;

	@InjectMocks
	private ExternalItemSaleService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ExternalItemSaleServiceTest.class);
	}

	@Test
	void processReturnsExpected() {
		ExternalSoldItemListRequestDTO request = fixtureLoader.getObject("request", ExternalSoldItemListRequestDTO.class).orElse(new ExternalSoldItemListRequestDTO());

		String result = service.process(request);

		assertThat(result, is("success"));
	}
}
