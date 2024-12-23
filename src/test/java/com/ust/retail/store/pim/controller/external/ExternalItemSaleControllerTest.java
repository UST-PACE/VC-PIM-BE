package com.ust.retail.store.pim.controller.external;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.sale.ExternalSoldItemListRequestDTO;
import com.ust.retail.store.pim.service.external.ExternalItemSaleService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalItemSaleControllerTest {
	@Mock
	private ExternalItemSaleService mockExternalItemSaleService;

	@InjectMocks
	private ExternalItemSaleController controller;

	@Test
	void saleReturnsExpected() {
		ExternalSoldItemListRequestDTO request = new ExternalSoldItemListRequestDTO();
		when(mockExternalItemSaleService.process(request)).thenReturn("success");

		ExternalApiResponse<String> result = controller.sale(request);

		MatcherAssert.assertThat(result.getResponse(), is("success"));
	}
}
