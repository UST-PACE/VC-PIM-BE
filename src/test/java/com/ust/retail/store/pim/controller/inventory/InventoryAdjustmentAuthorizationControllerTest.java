package com.ust.retail.store.pim.controller.inventory;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.common.catalogs.InventoryAdjustmentStatusCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationFilterDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationFilterResultDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationRequestDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationResultDTO;
import com.ust.retail.store.pim.service.inventory.InventoryAdjustmentAuthorizationService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryAdjustmentAuthorizationControllerTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private InventoryAdjustmentStatusCatalog mockInventoryAdjustmentStatusCatalog;
	@Mock
	private InventoryAdjustmentAuthorizationService mockInventoryAdjustmentAuthorizationService;

	@InjectMocks
	private InventoryAdjustmentAuthorizationController controller;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(InventoryAdjustmentAuthorizationControllerTest.class);
	}

	@Test
	void loadInventoryAdjustmentStatusCatalogReturnsExpected() {
		when(mockInventoryAdjustmentStatusCatalog.getCatalogOptions()).thenReturn(List.of(
				new CatalogDTO(1L, "CATALOG", "Status 1"),
				new CatalogDTO(2L, "CATALOG", "Status 2"),
				new CatalogDTO(3L, "CATALOG", "Status 3")
		));

		List<CatalogDTO> result = controller.loadInventoryAdjustmentStatusCatalog();

		assertThat(result, hasSize(3));
		assertThat(result, contains(List.of(
				allOf(hasProperty("catalogId", is(1L)), hasProperty("catalogOptions", is("Status 1"))),
				allOf(hasProperty("catalogId", is(2L)), hasProperty("catalogOptions", is("Status 2"))),
				allOf(hasProperty("catalogId", is(3L)), hasProperty("catalogOptions", is("Status 3")))
		)));
	}

	@Test
	void findByFiltersReturnsExpected() {
		InventoryAdjustmentAuthorizationFilterDTO request = new InventoryAdjustmentAuthorizationFilterDTO();
		request.setPage(0);
		request.setSize(10);
		request.setOrderColumn("adjustmentId");
		request.setOrderDir("desc");

		InventoryAdjustmentAuthorizationFilterResultDTO dto = new InventoryAdjustmentAuthorizationFilterResultDTO(1L, "User", "Category", new Date(), new Date(), 200L, "Pending review");
		when(mockInventoryAdjustmentAuthorizationService.getInventoryAdjustmentsByFilters(request)).thenReturn(new PageImpl<>(List.of(dto)));

		Page<InventoryAdjustmentAuthorizationFilterResultDTO> result = controller.findByFilters(request);

		assertThat(result.getContent(), hasSize(1));
		assertThat(result.getContent(), contains(dto));
	}

	@Test
	void findByIdReturnsExpected() {
		InventoryAdjustmentAuthorizationDTO dto = new InventoryAdjustmentAuthorizationDTO(1L, new Date(), new Date(), 1L, "Store 1", 1L, "Front Store", 1L, "Pending review", 100d, 0d, null);
		when(mockInventoryAdjustmentAuthorizationService.findById(1L)).thenReturn(dto);

		InventoryAdjustmentAuthorizationDTO result = controller.findById(1L);

		assertThat(result, is(dto));
	}

	@Test
	void authorizeAdjustmentsReturnsExpected() {
		InventoryAdjustmentAuthorizationRequestDTO request = fixtureLoader.getObject("request", InventoryAdjustmentAuthorizationRequestDTO.class).orElse(new InventoryAdjustmentAuthorizationRequestDTO());
		InventoryAdjustmentAuthorizationResultDTO response = fixtureLoader.getObject("response", InventoryAdjustmentAuthorizationResultDTO.class).orElse(new InventoryAdjustmentAuthorizationResultDTO());

		when(mockInventoryAdjustmentAuthorizationService.authorize(request)).thenReturn(response);

		InventoryAdjustmentAuthorizationResultDTO result = controller.authorizeAdjustments(request);

		assertThat(result, hasProperty("statusId", is(1L)));
		assertThat(result.getResults(), contains(
				allOf(hasProperty("txnNum", is(1L)), hasProperty("returnDetailId", is(1L)), hasProperty("success", is(true)))
		));
	}

	@Test
	void rejectAdjustmentsReturnsExpected() {
		InventoryAdjustmentAuthorizationRequestDTO request = fixtureLoader.getObject("request", InventoryAdjustmentAuthorizationRequestDTO.class).orElse(new InventoryAdjustmentAuthorizationRequestDTO());
		InventoryAdjustmentAuthorizationResultDTO response = fixtureLoader.getObject("response", InventoryAdjustmentAuthorizationResultDTO.class).orElse(new InventoryAdjustmentAuthorizationResultDTO());

		when(mockInventoryAdjustmentAuthorizationService.decline(request)).thenReturn(response);

		InventoryAdjustmentAuthorizationResultDTO result = controller.rejectAdjustments(request);

		assertThat(result, hasProperty("statusId", is(1L)));
		assertThat(result.getResults(), contains(
				allOf(hasProperty("txnNum", is(1L)), hasProperty("returnDetailId", is(1L)), hasProperty("success", is(true)))
		));
	}
}
