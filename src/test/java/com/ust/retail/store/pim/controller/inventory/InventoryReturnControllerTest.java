package com.ust.retail.store.pim.controller.inventory;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.catalogs.ReturnWarningsCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryProductDTO;
import com.ust.retail.store.pim.dto.inventory.returns.operation.ReturnDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.InventoryReturnSumaryDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.LoadItemReturnInventoryDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.ReturnItemDTO;
import com.ust.retail.store.pim.service.inventory.InventoryReturnService;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryReturnControllerTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private InventoryReturnService mockInventoryReturnService;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private ReturnWarningsCatalog mockReturnWarningsCatalog;

	@InjectMocks
	private InventoryReturnController controller;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(InventoryReturnControllerTest.class);
	}

	@Test
	void startReturnsReturnsExpected() {
		when(mockInventoryReturnService.start(any())).thenReturn(new ReturnDTO());

		ReturnDTO result = controller.startReturns();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadItemReturnsExpectedWhenNoPreviousRecord() {
		InventoryProductDTO inventory = fixtureLoader.getObject("inventory", InventoryProductDTO.class).orElse(new InventoryProductDTO());
		InventoryReturnSumaryDTO summary = fixtureLoader.getObject("emptySummary", InventoryReturnSumaryDTO.class).orElse(new InventoryReturnSumaryDTO());
		when(mockInventoryService.findInventoryByCodeAndStoreLocation("ABC", 1L)).thenReturn(inventory);
		when(mockInventoryReturnService.getReturnSummary(any())).thenReturn(summary);

		LoadItemReturnInventoryDTO result = controller.loadItem(1L, "ABC", 1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadItemReturnsExpectedWhenPreviousRecord() {
		InventoryProductDTO inventory = fixtureLoader.getObject("inventory", InventoryProductDTO.class).orElse(new InventoryProductDTO());
		InventoryReturnSumaryDTO summary = fixtureLoader.getObject("summary", InventoryReturnSumaryDTO.class).orElse(new InventoryReturnSumaryDTO());
		when(mockInventoryService.findInventoryByCodeAndStoreLocation("ABC", 1L)).thenReturn(inventory);
		when(mockInventoryReturnService.getReturnSummary(any())).thenReturn(summary);

		LoadItemReturnInventoryDTO result = controller.loadItem(1L, "ABC", 1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadItemReturnsExpectedWhenNoSummary() {
		InventoryProductDTO inventory = fixtureLoader.getObject("inventory", InventoryProductDTO.class).orElse(new InventoryProductDTO());
		InventoryReturnSumaryDTO summary = fixtureLoader.getObject("summaryNoMap", InventoryReturnSumaryDTO.class).orElse(new InventoryReturnSumaryDTO());
		when(mockInventoryService.findInventoryByCodeAndStoreLocation("ABC", 1L)).thenReturn(inventory);
		when(mockInventoryReturnService.getReturnSummary(any())).thenReturn(summary);

		LoadItemReturnInventoryDTO result = controller.loadItem(1L, "ABC", 1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void adjustInventoryReturnsExpected() {
		ReturnItemDTO request = new ReturnItemDTO();
		when(mockInventoryReturnService.returnItems(request)).thenReturn(true);

		Boolean result = controller.adjustInventory(request);

		assertThat(result, is(true));
	}

	@Test
	void finishInventoryAdjustmentReturnsExpected() {
		ReturnDTO request = new ReturnDTO();
		when(mockInventoryReturnService.finish(any())).thenReturn(true);

		boolean result = controller.finishInventoryAdjustment(request);

		assertThat(result, is(true));
	}

	@Test
	void loadSummaryReturnsExpected() {
		when(mockInventoryReturnService.getReturnSummary(1L)).thenReturn(new InventoryReturnSumaryDTO());

		InventoryReturnSumaryDTO result = controller.loadSumary(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadWarningsCatalogReturnsExpected() {
		when(mockReturnWarningsCatalog.getCatalogOptions()).thenReturn(List.of());

		List<CatalogDTO> result = controller.loadReturnWarningsCatalog();

		assertThat(result, is(notNullValue()));
	}
}
