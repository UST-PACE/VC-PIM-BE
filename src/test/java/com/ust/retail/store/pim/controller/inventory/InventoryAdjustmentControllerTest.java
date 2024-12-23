package com.ust.retail.store.pim.controller.inventory;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.catalogs.DailyCountStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.ShrinkageReasonCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryProductDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventoryAdjustmentDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventoryAdjustmentSumaryDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.ItemAdjustInventoryDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.StartDailyCountDTO;
import com.ust.retail.store.pim.service.inventory.InventoryAdjustmentService;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryAdjustmentControllerTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private InventoryAdjustmentService mockInventoryAdjustmentService;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private DailyCountStatusCatalog mockDailyCountStatusCatalog;
	@Mock
	private ShrinkageReasonCatalog mockShrinkageReasonCatalog;

	@InjectMocks
	private InventoryAdjustmentController controller;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(InventoryAdjustmentControllerTest.class);
	}

	@Test
	void startInventoryAdjustmentReturnsExpected() {
		StartDailyCountDTO request = fixtureLoader.getObject("startRequest", StartDailyCountDTO.class).orElse(new StartDailyCountDTO());
		InventoryAdjustmentDTO response = fixtureLoader.getObject("startResponse", InventoryAdjustmentDTO.class).orElse(new InventoryAdjustmentDTO());

		when(mockInventoryAdjustmentService.start(request)).thenReturn(response);

		InventoryAdjustmentDTO result = controller.startInventoryAdjustment(request);

		assertThat(result, hasProperty("inventoryAdjustmentId", is(1L)));
	}

	@Test
	void interruptInventoryCountReturnsExpected() {
		StartDailyCountDTO request = fixtureLoader.getObject("startRequest", StartDailyCountDTO.class).orElse(new StartDailyCountDTO());
		when(mockInventoryAdjustmentService.interrupt(request)).thenReturn(true);

		Boolean result = controller.interruptInventoryCount(request);

		assertThat(result, is(true));
	}

	@Test
	void loadItemReturnsExpectedWhenNotInSummary() {
		InventoryProductDTO inventoryProduct = fixtureLoader.getObject("inventoryProduct", InventoryProductDTO.class).orElse(new InventoryProductDTO());
		when(mockInventoryService.findInventoryByCodeAndStoreLocation("CODE", 1L)).thenReturn(inventoryProduct);
		when(mockInventoryAdjustmentService.getAdjustmentSumary(1L)).thenReturn(new InventoryAdjustmentSumaryDTO());

		ItemAdjustInventoryDTO result = controller.loadItem(1L, "CODE", 1L);

		assertThat(result, allOf(
				hasProperty("inventoryId", is(1L)),
				hasProperty("upcMasterId", is(1L)),
				hasProperty("storeLocationId", is(1L)),
				hasProperty("principalUPC", is("CODE")),
				hasProperty("productName", is("Product 1"))
		));
	}

	@Test
	void loadItemReturnsExpectedWhenInSummary() {
		InventoryProductDTO inventoryProduct = fixtureLoader.getObject("inventoryProduct", InventoryProductDTO.class).orElse(new InventoryProductDTO());
		when(mockInventoryService.findInventoryByCodeAndStoreLocation("CODE", 1L)).thenReturn(inventoryProduct);
		InventoryAdjustmentSumaryDTO summary = fixtureLoader.getObject("adjustmentSummary", InventoryAdjustmentSumaryDTO.class).orElse(new InventoryAdjustmentSumaryDTO());
		when(mockInventoryAdjustmentService.getAdjustmentSumary(1L)).thenReturn(summary);

		ItemAdjustInventoryDTO result = controller.loadItem(1L, "CODE", 1L);

		assertThat(result, allOf(
				hasProperty("inventoryId", is(1L)),
				hasProperty("upcMasterId", is(1L)),
				hasProperty("storeLocationId", is(1L)),
				hasProperty("principalUPC", is("CODE")),
				hasProperty("productName", is("Product 1")),
				hasProperty("previousDetail", is(notNullValue()))
		));
	}

	@Test
	void adjustInventoryReturnsExpected() {
		InventoryAdjustmentDTO request = fixtureLoader.getObject("adjustRequest", InventoryAdjustmentDTO.class).orElse(new InventoryAdjustmentDTO());
		when(mockInventoryAdjustmentService.adjustInventory(request)).thenReturn(true);

		Boolean result = controller.adjustInventory(request);

		assertThat(result, is(true));
	}

	@Test
	void finishInventoryAdjustmentReturnsExpected() {
		InventoryAdjustmentDTO request = fixtureLoader.getObject("finishRequest", InventoryAdjustmentDTO.class).orElse(new InventoryAdjustmentDTO());
		when(mockInventoryAdjustmentService.finish(1L)).thenReturn(true);

		boolean result = controller.finishInventoryAdjustment(request);

		assertThat(result, is(true));
	}

	@Test
	void loadSummaryReturnsExpected() {
		InventoryAdjustmentSumaryDTO adjustmentSummary = fixtureLoader.getObject("adjustmentSummary", InventoryAdjustmentSumaryDTO.class).orElse(new InventoryAdjustmentSumaryDTO());
		when(mockInventoryAdjustmentService.getAdjustmentSumary(1L)).thenReturn(adjustmentSummary);

		InventoryAdjustmentSumaryDTO result = controller.loadSumary(1L);

		assertThat(result, hasProperty("summary", is(notNullValue())));
		assertThat(result.getSummary(),
				hasEntry(
						is("category1"),
						contains(allOf(
								hasProperty("inventoryAdjustmentDetailId", is(1L)),
								hasProperty("upcMasterId", is(1L)),
								hasProperty("principalUPC", is("CODE")),
								hasProperty("productName", is("Product 1")),
								hasProperty("qty", is(1000d))
						))));
	}

	@Test
	void loadDailyCountStatusCatalogReturnsExpected() {
		when(mockDailyCountStatusCatalog.getCatalogOptions()).thenReturn(List.of(
				new CatalogDTO(1L, "CATALOG", "Status 1"),
				new CatalogDTO(2L, "CATALOG", "Status 2"),
				new CatalogDTO(3L, "CATALOG", "Status 3")
		));

		List<CatalogDTO> result = controller.loadDailyCountStatusCatalog();

		assertThat(result, hasSize(3));
		assertThat(result, contains(List.of(
				allOf(hasProperty("catalogId", is(1L)), hasProperty("catalogOptions", is("Status 1"))),
				allOf(hasProperty("catalogId", is(2L)), hasProperty("catalogOptions", is("Status 2"))),
				allOf(hasProperty("catalogId", is(3L)), hasProperty("catalogOptions", is("Status 3")))
		)));
	}

	@Test
	void loadShrinkageReasonCatalogReturnsExpected() {
		when(mockShrinkageReasonCatalog.getCatalogOptions()).thenReturn(List.of(
				new CatalogDTO(1L, "CATALOG", "Status 1"),
				new CatalogDTO(2L, "CATALOG", "Status 2"),
				new CatalogDTO(3L, "CATALOG", "Status 3")
		));

		List<CatalogDTO> result = controller.loadShrinkageReasonCatalog();

		assertThat(result, hasSize(3));
		assertThat(result, contains(List.of(
				allOf(hasProperty("catalogId", is(1L)), hasProperty("catalogOptions", is("Status 1"))),
				allOf(hasProperty("catalogId", is(2L)), hasProperty("catalogOptions", is("Status 2"))),
				allOf(hasProperty("catalogId", is(3L)), hasProperty("catalogOptions", is("Status 3")))
		)));
	}
}
