package com.ust.retail.store.pim.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.dto.security.StoreNumberInfoDTO;
import com.ust.retail.store.pim.service.catalog.StoreLocationService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreFacadeTest {
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;
	@Mock
	private StoreLocationService mockStoreLocationService;

	@InjectMocks
	private StoreFacade facade;

	@Test
	void getStoreLocationForSalesForCurrentUserStoreReturnsExpected() {
		when(mockAuthenticationFacade.getUserStoreNumber()).thenReturn(new StoreNumberInfoDTO(1L, "StoreName"));
		when(mockStoreLocationService.findStoreLocationForSales(1L)).thenReturn(1L);

		assertThat(facade.getStoreLocationForSales(), is(1L));
	}

	@Test
	void getStoreLocationForSalesReturnsExpected() {
		when(mockStoreLocationService.findStoreLocationForSales(1L)).thenReturn(1L);

		assertThat(facade.getStoreLocationForSales(1L), is(1L));
	}
}
