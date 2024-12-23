package com.ust.retail.store.pim.controller.purchaseorder;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.catalogs.PoStatusCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.ItemCurrentInventory;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDTO;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDetailDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderAddProductRequestDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderFilterRequestDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderFilterResultDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderFindDetailsByIdRequestDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderModifyLineItemRequestDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderModifyLineItemResultDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderModifyStatusResultDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderSaveRequestDTO;
import com.ust.retail.store.pim.dto.purchaseorder.screens.PurchaseOrderPendingFulfillmentDTO;
import com.ust.retail.store.pim.dto.purchaseorder.screens.PurchaseOrderUpcInformationDTO;
import com.ust.retail.store.pim.exceptions.PurchaseOrderPrintException;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.service.purchaseorder.PurchaseOrderService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderControllerTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private PurchaseOrderService mockPurchaseOrderService;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private PoStatusCatalog mockPoStatusCatalog;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private PurchaseOrderController controller;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(PurchaseOrderControllerTest.class);
	}

	@AfterEach
	void tearDown() {
		fixtureLoader.enableAnnotations();
	}

	@Test
	void loadProductByUpcReturnsExpected() {
		when(mockPurchaseOrderService.loadProductByUpcSuppliedByVendor("ABC", 1L, 1L)).thenReturn(new PurchaseOrderUpcInformationDTO());

		PurchaseOrderUpcInformationDTO result = controller.loadProductByUpc(1L, 1L, "ABC");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadProductByPurchaseOrderAndUpcReturnsExpected() {
		when(mockPurchaseOrderService.loadProductByPurchaseOrderAndUpc(1L, "ABC")).thenReturn(new PurchaseOrderUpcInformationDTO());

		PurchaseOrderUpcInformationDTO result = controller.loadProductByPurchaseOrderAndUpc(1L, "ABC");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void createOrderWithLineItemReturnsExpected() {
		PurchaseOrderAddProductRequestDTO request = new PurchaseOrderAddProductRequestDTO();
		when(mockPurchaseOrderService.createOrderWithLineItem(request)).thenReturn(new PurchaseOrderModifyLineItemResultDTO());

		PurchaseOrderModifyLineItemResultDTO result = controller.createOrderWithLineItem(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void appendOrderWithProductReturnsExpected() {
		PurchaseOrderAddProductRequestDTO request = new PurchaseOrderAddProductRequestDTO();
		when(mockPurchaseOrderService.appendLineItemToOrder(request)).thenReturn(new PurchaseOrderModifyLineItemResultDTO());

		PurchaseOrderModifyLineItemResultDTO result = controller.appendOrderWithProduct(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void appendOrderWithProductSendsEmail() {
		PurchaseOrderAddProductRequestDTO request = new PurchaseOrderAddProductRequestDTO();
		fixtureLoader.disableAnnotations();
		PurchaseOrderModifyLineItemResultDTO appendResult = fixtureLoader.getObject("revisionUpdatedResult", PurchaseOrderModifyLineItemResultDTO.class).orElse(new PurchaseOrderModifyLineItemResultDTO());

		when(mockPurchaseOrderService.appendLineItemToOrder(request)).thenReturn(appendResult);

		PurchaseOrderModifyLineItemResultDTO result = controller.appendOrderWithProduct(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeLineItemReturnsExpected() {
		when(mockPurchaseOrderService.removeLineItem(1L)).thenReturn(new PurchaseOrderModifyLineItemResultDTO());

		PurchaseOrderModifyLineItemResultDTO result = controller.removeLineItem(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeLineItemSendsEmail() {
		fixtureLoader.disableAnnotations();
		PurchaseOrderModifyLineItemResultDTO removeResult = fixtureLoader.getObject("revisionUpdatedResult", PurchaseOrderModifyLineItemResultDTO.class).orElse(new PurchaseOrderModifyLineItemResultDTO());
		when(mockPurchaseOrderService.removeLineItem(1L)).thenReturn(removeResult);

		PurchaseOrderModifyLineItemResultDTO result = controller.removeLineItem(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void modifyLineItemReturnsExpected() {
		PurchaseOrderModifyLineItemRequestDTO request = new PurchaseOrderModifyLineItemRequestDTO();
		when(mockPurchaseOrderService.updateLineItem(request)).thenReturn(new PurchaseOrderModifyLineItemResultDTO());

		PurchaseOrderModifyLineItemResultDTO result = controller.modifyLineItem(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void modifyLineItemSendsEmail() {
		PurchaseOrderModifyLineItemRequestDTO request = new PurchaseOrderModifyLineItemRequestDTO();
		fixtureLoader.disableAnnotations();
		PurchaseOrderModifyLineItemResultDTO modifyResult = fixtureLoader.getObject("revisionUpdatedResult", PurchaseOrderModifyLineItemResultDTO.class).orElse(new PurchaseOrderModifyLineItemResultDTO());
		when(mockPurchaseOrderService.updateLineItem(request)).thenReturn(modifyResult);

		PurchaseOrderModifyLineItemResultDTO result = controller.modifyLineItem(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void saveDraftReturnsExpected() {
		PurchaseOrderSaveRequestDTO request = new PurchaseOrderSaveRequestDTO();
		when(mockPurchaseOrderService.saveDraft(request)).thenReturn(new PurchaseOrderModifyStatusResultDTO());

		PurchaseOrderModifyStatusResultDTO result = controller.saveDraft(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void saveDraftSendsEmail() {
		PurchaseOrderSaveRequestDTO request = new PurchaseOrderSaveRequestDTO();
		fixtureLoader.disableAnnotations();
		PurchaseOrderModifyStatusResultDTO modifyResult = fixtureLoader.getObject("revisionUpdatedResult", PurchaseOrderModifyStatusResultDTO.class).orElse(new PurchaseOrderModifyStatusResultDTO());
		when(mockPurchaseOrderService.saveDraft(request)).thenReturn(modifyResult);

		PurchaseOrderModifyStatusResultDTO result = controller.saveDraft(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void processPurchaseOrderReturnsExpected() {
		PurchaseOrderSaveRequestDTO request = new PurchaseOrderSaveRequestDTO();
		when(mockPurchaseOrderService.processPurchaseOrder(request)).thenReturn(new PurchaseOrderModifyStatusResultDTO());

		PurchaseOrderModifyStatusResultDTO result = controller.processPurchaseOrder(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void resendOrderReturnsExpected() {
		when(mockPurchaseOrderService.findById(1L)).thenReturn(new PurchaseOrderDTO());

		GenericResponse result = controller.resendOrder(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void deleteByIdReturnsExpected() {
		when(mockPurchaseOrderService.deleteById(1L)).thenReturn(new PurchaseOrderModifyLineItemResultDTO());

		GenericResponse result = controller.deleteById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void deleteByIdSendsInvalidationEmail() {
		fixtureLoader.disableAnnotations();
		PurchaseOrderModifyLineItemResultDTO deleteResult = fixtureLoader.getObject("revisionUpdatedResult", PurchaseOrderModifyLineItemResultDTO.class).orElse(new PurchaseOrderModifyLineItemResultDTO());
		when(mockPurchaseOrderService.deleteById(1L)).thenReturn(deleteResult);

		GenericResponse result = controller.deleteById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void deleteByIdSendsInvalidationEmailToEscalationContact() {
		fixtureLoader.disableAnnotations();
		PurchaseOrderModifyLineItemResultDTO deleteResult = fixtureLoader.getObject("escalationWarningResult", PurchaseOrderModifyLineItemResultDTO.class).orElse(new PurchaseOrderModifyLineItemResultDTO());
		when(mockPurchaseOrderService.deleteById(1L)).thenReturn(deleteResult);

		GenericResponse result = controller.deleteById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void printPurchaseOrderReturnsExpected() throws Exception {
		when(mockPurchaseOrderService.getPurchaseOrderPdf(1L)).thenReturn(Files.createTempFile("temp", "file").toFile());

		ResponseEntity<Resource> result = controller.printPurchaseOrder(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void printPurchaseOrderThrowsException() {
		File mockFile = mock(File.class);
		when(mockPurchaseOrderService.getPurchaseOrderPdf(1L)).thenReturn(mockFile);
		when(mockFile.getAbsolutePath()).thenReturn("/invalid/path.pdf");

		assertThrows(PurchaseOrderPrintException.class, () -> controller.printPurchaseOrder(1L));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockPurchaseOrderService.findById(1L)).thenReturn(new PurchaseOrderDTO());

		PurchaseOrderDTO result = controller.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByPoNumberReturnsExpected() {
		when(mockPurchaseOrderService.findByPurchaseOrderNumber("PO_NUM")).thenReturn(List.of());

		List<PurchaseOrderFilterResultDTO> result = controller.findByPoNumber("PO_NUM");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findDetailsReturnsExpected() {
		PurchaseOrderFindDetailsByIdRequestDTO request = new PurchaseOrderFindDetailsByIdRequestDTO();
		when(mockPurchaseOrderService.findDetailsById(request)).thenReturn(Page.empty());

		Page<PurchaseOrderDetailDTO> result = controller.findDetails(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByFiltersReturnsExpected() {
		PurchaseOrderFilterRequestDTO request = new PurchaseOrderFilterRequestDTO();
		when(mockPurchaseOrderService.findOrdersByFilters(request)).thenReturn(Page.empty());

		Page<PurchaseOrderFilterResultDTO> result = controller.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findOrdersPendingFulfillmentByProductReturnsExpected() {
		when(mockPurchaseOrderService.findOrdersPendingFulfillmentByProduct(1L)).thenReturn(List.of());

		List<PurchaseOrderPendingFulfillmentDTO> result = controller.findOrdersPendingFulfillmentByProduct(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findProductByStoreReturnsExpected() {
		List<ItemCurrentInventory> inventoryList = fixtureLoader.getObject("inventoryList", new ItemCurrentInventoryListReference()).orElse(List.of());
		when(mockInventoryService.findInventoryByUpcMasterId(1L)).thenReturn(inventoryList);

		List<ItemCurrentInventory> result = controller.findProductByStore(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadStatusCatalogReturnsExpected() {
		when(mockPoStatusCatalog.getCatalogOptions()).thenReturn(List.of());

		List<CatalogDTO> result = controller.loadStatusCatalog();

		assertThat(result, is(notNullValue()));
	}

	private static class ItemCurrentInventoryListReference extends TypeReference<List<ItemCurrentInventory>> {
	}
}
