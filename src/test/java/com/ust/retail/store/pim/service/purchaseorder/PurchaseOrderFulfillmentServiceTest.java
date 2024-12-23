package com.ust.retail.store.pim.service.purchaseorder;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.pim.common.catalogs.PoStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.dto.purchaseorder.operation.FulfillmentCandidateDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderFulfillmentRequestDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderModifyLineItemResultDTO;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderFulfillmentRepository;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderFulfillmentServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private PurchaseOrderFulfillmentRepository mockPurchaseOrderFulfillmentRepository;
	@Mock
	private PurchaseOrderService mockPurchaseOrderService;
	@Mock
	private StoreNumberService mockStoreNumberService;
	@Mock
	private AutoFulfillmentHelper mockAutoFulfillmentHelper;
	@Mock
	private EmailSend mockEmailSend;

	@InjectMocks
	private PurchaseOrderFulfillmentService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(PurchaseOrderFulfillmentServiceTest.class);
	}

	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(service, "customer", "UST");
		ReflectionTestUtils.setField(service, "websiteUrl", "info@example.com");
		ReflectionTestUtils.setField(service, "host", "https://example.com");
		ReflectionTestUtils.setField(service, "customerBgColor", "black");
		ReflectionTestUtils.setField(service, "customerFgColor", "white");
		ReflectionTestUtils.setField(service, "contactAddress", "info@example.com");
		ReflectionTestUtils.setField(service, "basePath", "https://example.com/#/inventory/purchase-order");
	}

	@Test
	void runInventoryAutoFulfillmentCreatesNewOrder() {
		List<FulfillmentCandidateDTO> reorderCandidates = fixtureLoader.getObject("reorderCandidatesNewOrder", new FulfillmentCandidateListReference()).orElse(List.of());
		PurchaseOrderModifyLineItemResultDTO purchaseOrderResult = fixtureLoader.getObject("purchaseOrderResult", PurchaseOrderModifyLineItemResultDTO.class).orElse(new PurchaseOrderModifyLineItemResultDTO());
		PurchaseOrderFulfillmentRequestDTO reorderCandidateNewOrderRequest = fixtureLoader.getObject("reorderCandidateNewOrderRequest", PurchaseOrderFulfillmentRequestDTO.class).orElse(new PurchaseOrderFulfillmentRequestDTO());
		when(mockPurchaseOrderFulfillmentRepository.getReorderCandidates(PoStatusCatalog.PO_STATUS_DRAFT, UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE)).thenReturn(reorderCandidates);
		when(mockPurchaseOrderService.createOrderWithLineItem(any())).thenReturn(purchaseOrderResult);
		when(mockAutoFulfillmentHelper.evaluateCandidate(any(), anyMap())).thenReturn(reorderCandidateNewOrderRequest);

		assertDoesNotThrow(() -> service.runInventoryAutoFulfillment());
	}

	@Test
	void runInventoryAutoFulfillmentRemovesItem() {
		List<FulfillmentCandidateDTO> reorderCandidates = fixtureLoader.getObject("reorderCandidatesDetailToRemove", new FulfillmentCandidateListReference()).orElse(List.of());
		PurchaseOrderModifyLineItemResultDTO purchaseOrderResult = fixtureLoader.getObject("purchaseOrderResult", PurchaseOrderModifyLineItemResultDTO.class).orElse(new PurchaseOrderModifyLineItemResultDTO());
		PurchaseOrderFulfillmentRequestDTO reorderCandidateDetailToRemoveRequest = fixtureLoader.getObject("reorderCandidateDetailToRemoveRequest", PurchaseOrderFulfillmentRequestDTO.class).orElse(new PurchaseOrderFulfillmentRequestDTO());
		when(mockPurchaseOrderFulfillmentRepository.getReorderCandidates(PoStatusCatalog.PO_STATUS_DRAFT, UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE)).thenReturn(reorderCandidates);
		when(mockPurchaseOrderService.removeLineItem(any())).thenReturn(purchaseOrderResult);
		when(mockAutoFulfillmentHelper.evaluateCandidate(any(), anyMap())).thenReturn(reorderCandidateDetailToRemoveRequest);

		assertDoesNotThrow(() -> service.runInventoryAutoFulfillment());
	}

	@Test
	void runInventoryAutoFulfillmentUpdatesItem() {
		List<FulfillmentCandidateDTO> reorderCandidates = fixtureLoader.getObject("reorderCandidatesDetailToUpdate", new FulfillmentCandidateListReference()).orElse(List.of());
		PurchaseOrderModifyLineItemResultDTO purchaseOrderResult = fixtureLoader.getObject("purchaseOrderResult", PurchaseOrderModifyLineItemResultDTO.class).orElse(new PurchaseOrderModifyLineItemResultDTO());
		PurchaseOrderFulfillmentRequestDTO reorderCandidateDetailToUpdateRequest = fixtureLoader.getObject("reorderCandidateDetailToUpdateRequest", PurchaseOrderFulfillmentRequestDTO.class).orElse(new PurchaseOrderFulfillmentRequestDTO());
		when(mockPurchaseOrderFulfillmentRepository.getReorderCandidates(PoStatusCatalog.PO_STATUS_DRAFT, UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE)).thenReturn(reorderCandidates);
		when(mockPurchaseOrderService.updateLineItem(any())).thenReturn(purchaseOrderResult);
		when(mockAutoFulfillmentHelper.evaluateCandidate(any(), anyMap())).thenReturn(reorderCandidateDetailToUpdateRequest);

		assertDoesNotThrow(() -> service.runInventoryAutoFulfillment());
	}

	@Test
	void runInventoryAutoFulfillmentSkipsItem() {
		List<FulfillmentCandidateDTO> reorderCandidates = fixtureLoader.getObject("reorderCandidatesNotFulfill", new FulfillmentCandidateListReference()).orElse(List.of());
		PurchaseOrderFulfillmentRequestDTO reorderCandidateNotFulfillRequest = fixtureLoader.getObject("reorderCandidateNotFulfillRequest", PurchaseOrderFulfillmentRequestDTO.class).orElse(new PurchaseOrderFulfillmentRequestDTO());
		when(mockPurchaseOrderFulfillmentRepository.getReorderCandidates(PoStatusCatalog.PO_STATUS_DRAFT, UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE)).thenReturn(reorderCandidates);
		when(mockAutoFulfillmentHelper.evaluateCandidate(any(), anyMap())).thenReturn(reorderCandidateNotFulfillRequest);

		assertDoesNotThrow(() -> service.runInventoryAutoFulfillment());
	}

	@Test
	void runInventoryAutoFulfillmentAppendsItem() {
		List<FulfillmentCandidateDTO> reorderCandidates = fixtureLoader.getObject("reorderCandidatesAppend", new FulfillmentCandidateListReference()).orElse(List.of());
		PurchaseOrderModifyLineItemResultDTO purchaseOrderResult = fixtureLoader.getObject("purchaseOrderResult", PurchaseOrderModifyLineItemResultDTO.class).orElse(new PurchaseOrderModifyLineItemResultDTO());
		PurchaseOrderFulfillmentRequestDTO reorderCandidateAppendRequest = fixtureLoader.getObject("reorderCandidateAppendRequest", PurchaseOrderFulfillmentRequestDTO.class).orElse(new PurchaseOrderFulfillmentRequestDTO());
		when(mockPurchaseOrderFulfillmentRepository.getReorderCandidates(PoStatusCatalog.PO_STATUS_DRAFT, UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE)).thenReturn(reorderCandidates);
		when(mockPurchaseOrderService.appendLineItemToOrder(any())).thenReturn(purchaseOrderResult);
		when(mockAutoFulfillmentHelper.evaluateCandidate(any(), anyMap())).thenReturn(reorderCandidateAppendRequest);

		assertDoesNotThrow(() -> service.runInventoryAutoFulfillment());
	}

	@Test
	void runInventoryAutoFulfillmentFinishesSuccessfullyWhenNothingToDo() {
		when(mockPurchaseOrderFulfillmentRepository.getReorderCandidates(PoStatusCatalog.PO_STATUS_DRAFT, UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE)).thenReturn(List.of());

		assertDoesNotThrow(() -> service.runInventoryAutoFulfillment());
	}

	private static class FulfillmentCandidateListReference extends TypeReference<List<FulfillmentCandidateDTO>> {
	}
}
