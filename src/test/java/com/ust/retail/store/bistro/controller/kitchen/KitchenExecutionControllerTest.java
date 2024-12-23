package com.ust.retail.store.bistro.controller.kitchen;

import java.util.List;

import com.ust.retail.store.bistro.commons.catalogs.WastageReasonCatalog;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionLineDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionRequestDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionTossDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionWastageLineDTO;
import com.ust.retail.store.bistro.service.kitchen.KitchenService;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.bistro.commons.catalogs.WastageReasonCatalog;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionLineDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionRequestDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionTossDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionWastageLineDTO;
import com.ust.retail.store.bistro.service.kitchen.KitchenService;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KitchenExecutionControllerTest {
	@Mock
	private KitchenService mockKitchenService;
	@Mock
	private WastageReasonCatalog mockWastageReasonCatalog;

	@InjectMocks
	private KitchenExecutionController controller;

	@Test
	void loadExecutionForReturnsExpected() {
		when(mockKitchenService.loadExecutionFor(any())).thenReturn(new PageImpl<>(List.of()));
				KitchenExecutionRequestDTO request = new KitchenExecutionRequestDTO();

						Page<KitchenExecutionLineDTO> result = controller.loadExecution(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void registerWastageReturnsExpected() {
			KitchenExecutionWastageLineDTO request = new KitchenExecutionWastageLineDTO();

		GenericResponse result = controller.registerWastage(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void registerTossReturnsExpected() {
		KitchenExecutionTossDTO request = new KitchenExecutionTossDTO();

		GenericResponse result = controller.registerToss(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void deleteWastageReturnsExpected() {
		GenericResponse result = controller.deleteWastage(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadWastageReasonsReturnsExpected() {
		when(mockWastageReasonCatalog.getCatalogOptions()).thenReturn(List.of());

		List<CatalogDTO> result = controller.loadWastageReasons();

		assertThat(result, is(notNullValue()));
	}
}
