package com.ust.retail.store.pim.controller.external;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.sale.ExternalReturnItemListRequestDTO;
import com.ust.retail.store.pim.service.external.ExternalItemReturnService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalItemReturnControllerTest {
	@Mock
	private ExternalItemReturnService mockExternalItemReturnService;

	@InjectMocks
	private ExternalItemReturnController controller;

	@Test
	void returnReturnsExpected() {
		ExternalReturnItemListRequestDTO request = new ExternalReturnItemListRequestDTO();
		when(mockExternalItemReturnService.process(request)).thenReturn("success");

		ExternalApiResponse<String> result = controller.productReturn(request);

		MatcherAssert.assertThat(result.getResponse(), is("success"));
	}
}
