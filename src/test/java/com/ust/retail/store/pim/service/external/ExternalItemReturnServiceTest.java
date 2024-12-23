package com.ust.retail.store.pim.service.external;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.StoreFacade;
import com.ust.retail.store.pim.dto.external.sale.ExternalReturnItemListRequestDTO;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class ExternalItemReturnServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private StoreFacade mockStoreFacade;

	@InjectMocks
	private ExternalItemReturnService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ExternalItemReturnServiceTest.class);
	}

	@Test
	void processReturnsExpected() {
		ExternalReturnItemListRequestDTO request = fixtureLoader.getObject("request", ExternalReturnItemListRequestDTO.class).orElse(new ExternalReturnItemListRequestDTO());

		String result = service.process(request);

		assertThat(result, is("success"));
	}
}
