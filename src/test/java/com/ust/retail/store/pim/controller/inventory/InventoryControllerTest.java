package com.ust.retail.store.pim.controller.inventory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.ust.retail.store.pim.dto.inventory.InventoryFiltersDTO;
import com.ust.retail.store.pim.service.inventory.InventoryService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {
	@Mock
	private InventoryService mockInventoryService;

	@InjectMocks
	private InventoryController controller;

	@Test
	void authorizeByTxnNumReturnsExpected() {
		when(mockInventoryService.authorizeByTxnNumber(1L)).thenReturn(true);

		boolean result = controller.authorizeByTxnNum(1L);

		assertThat(result, is(true));
	}

	@Test
	void authorizeByIdReturnsExpected() {
		when(mockInventoryService.authorizeByInventoryHistoryId(1L)).thenReturn(true);

		boolean result = controller.authorizeById(1L);

		assertThat(result, is(true));
	}

	@Test
	void authorizeReturnsExpected() {
		when(mockInventoryService.authorize(1L, 1L)).thenReturn(true);

		boolean result = controller.authorize(1L, 1L);

		assertThat(result, is(true));
	}

	@Test
	void rejectByTxnNum() {
		when(mockInventoryService.rejectByTxnNumber(1L)).thenReturn(true);

		boolean result = controller.rejectByTxnNum(1L);

		assertThat(result, is(true));
	}

	@Test
	void rejectReturnsExpected() {
		when(mockInventoryService.rejectByReferenceId(1L, 1L)).thenReturn(true);

		boolean result = controller.reject(1L, 1L);

		assertThat(result, is(true));
	}

	@Test
	void rejectByIdReturnsExpected() {
		when(mockInventoryService.rejectByInventoryHistoryId(1L)).thenReturn(true);

		boolean result = controller.rejectById(1L);

		assertThat(result, is(true));
	}

	@Test
	void findInventoryByFiltersReturnsExpected() {
		InventoryFiltersDTO request = new InventoryFiltersDTO();
		when(mockInventoryService.findInventory(request)).thenReturn(Page.empty());

		Page<InventoryFiltersDTO> result = controller.findInventoryByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findInventoryByFiltersSummaryReturnsExpected() {
		InventoryFiltersDTO request = new InventoryFiltersDTO();
		when(mockInventoryService.findInventorySummary(request)).thenReturn(Page.empty());

		Page<InventoryFiltersDTO> result = controller.findInventoryByFiltersSummary(request);

		assertThat(result, is(notNullValue()));
	}
}
