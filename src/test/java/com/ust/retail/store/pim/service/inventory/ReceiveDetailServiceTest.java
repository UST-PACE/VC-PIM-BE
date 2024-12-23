package com.ust.retail.store.pim.service.inventory;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestUpdateDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResponseDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResumeResponseDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResumeResponseDetailDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceptionNotificationRequestDTO;
import com.ust.retail.store.pim.exceptions.ItemReceivingException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.PoReceiveDetailModel;
import com.ust.retail.store.pim.model.inventory.PoReceiveWarningModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;
import com.ust.retail.store.pim.model.vendormaster.VendorContactModel;
import com.ust.retail.store.pim.repository.inventory.PoReceiveDetailRepository;
import com.ust.retail.store.pim.repository.inventory.PoReceiveWarningRepository;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderDetailRepository;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderRepository;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import com.ust.retail.store.pim.service.vendormaster.VendorMasterService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiveDetailServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private PoReceiveDetailRepository mockPoReceiveDetailRepository;
	@Mock
	private PoReceiveWarningRepository mockPoReceiveWarningRepository;
	@Mock
	private PurchaseOrderRepository mockPurchaseOrderRepository;
	@Mock
	private PurchaseOrderDetailRepository mockPurchaseOrderDetailRepository;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private VendorMasterService mockVendorMasterService;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;
	@Mock
	private StoreNumberService mockStoreNumberService;
	@Mock
	private EmailSend mockEmailSend;

	@InjectMocks
	private ReceiveDetailService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ReceiveDetailServiceTest.class);
	}

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(service, "customer", "UST");
		ReflectionTestUtils.setField(service, "host", "https://example.com");
		ReflectionTestUtils.setField(service, "customerBgColor", "black");
		ReflectionTestUtils.setField(service, "customerFgColor", "white");
		ReflectionTestUtils.setField(service, "websiteUrl", "https://example.com");
		ReflectionTestUtils.setField(service, "contactAddress", "user@example.com");
	}

	@Test
	void receiveInventoryReturnsExpected() {
		ReceivingRequestDTO request = fixtureLoader.getObject("receivingRequest", ReceivingRequestDTO.class).orElse(new ReceivingRequestDTO());
		Optional<PurchaseOrderDetailModel> purchaseOrderDetail = fixtureLoader.getObject("purchaseOrderDetailModel", PurchaseOrderDetailModel.class);
		when(mockPoReceiveDetailRepository.save(any())).then(invocation -> invocation.getArgument(0));
		when(mockPurchaseOrderDetailRepository.findById(1L)).thenReturn(purchaseOrderDetail);

		ReceivingResponseDTO result = service.receiveInventory(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void receiveInventoryThrowsExceptionWhenOrderDetailNotFound() {
		ReceivingRequestDTO request = fixtureLoader.getObject("receivingRequest", ReceivingRequestDTO.class).orElse(new ReceivingRequestDTO());
		when(mockPoReceiveDetailRepository.save(any())).then(invocation -> invocation.getArgument(0));

		assertThrows(ResourceNotFoundException.class, () -> service.receiveInventory(request));
	}

	@Test
	void receiveInventoryThrowsExceptionWhenOrderDetailReceived() {
		ReceivingRequestDTO request = fixtureLoader.getObject("receivingRequest", ReceivingRequestDTO.class).orElse(new ReceivingRequestDTO());
		Optional<PurchaseOrderDetailModel> purchaseOrderDetail = fixtureLoader.getObject("receivedPurchaseOrderDetailModel", PurchaseOrderDetailModel.class);
		when(mockPoReceiveDetailRepository.save(any())).then(invocation -> invocation.getArgument(0));
		when(mockPurchaseOrderDetailRepository.findById(1L)).thenReturn(purchaseOrderDetail);

		assertThrows(ItemReceivingException.class, () -> service.receiveInventory(request));
	}

	@Test
	void updateReceiveInventoryReturnsExpected() {
		ReceivingRequestUpdateDTO request = fixtureLoader.getObject("receivingUpdateRequest", ReceivingRequestUpdateDTO.class).orElse(new ReceivingRequestUpdateDTO());
		Optional<PoReceiveDetailModel> poReceiveModel = fixtureLoader.getObject("poReceiveModel", PoReceiveDetailModel.class);
		when(mockPoReceiveDetailRepository.findById(2L)).thenReturn(poReceiveModel);

		ReceivingResponseDTO result = service.updateReceiveInventory(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReceiveInventoryThrowsExceptionWhenPoReceiveModelNotFound() {
		ReceivingRequestUpdateDTO request = fixtureLoader.getObject("receivingUpdateRequest", ReceivingRequestUpdateDTO.class).orElse(new ReceivingRequestUpdateDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.updateReceiveInventory(request));
	}

	@Test
	void getReceivingSumaryReturnsExpected() {
		List<ReceivingResumeResponseDetailDTO> summaryList = fixtureLoader.getObject("summaryList", new ReceivingResumeResponseDetailListReference()).orElse(List.of());
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		when(mockPoReceiveDetailRepository.findByPurchaseOrderDetailPurchaseOrderPurchaseOrderId(1L)).thenReturn(summaryList);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockVendorMasterService.getVendorSalesRepresentative(1L)).thenReturn(Optional.of(new VendorContactModel()));
		when(mockVendorMasterService.getVendorEscalationContact(1L)).thenReturn(Optional.of(new VendorContactModel()));

		ReceivingResumeResponseDTO result = service.getReceivingSumary(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getReceivingSumaryThrowsExceptionWhenOrderNotFound() {
		List<ReceivingResumeResponseDetailDTO> summaryList = fixtureLoader.getObject("summaryList", new ReceivingResumeResponseDetailListReference()).orElse(List.of());
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		when(mockPoReceiveDetailRepository.findByPurchaseOrderDetailPurchaseOrderPurchaseOrderId(1L)).thenReturn(summaryList);

		assertThrows(ResourceNotFoundException.class, () -> service.getReceivingSumary(1L));
	}

	@Test
	void getReceivingSumaryThrowsExceptionWhenVendorSaleRepNotFound() {
		List<ReceivingResumeResponseDetailDTO> summaryList = fixtureLoader.getObject("summaryList", new ReceivingResumeResponseDetailListReference()).orElse(List.of());
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		when(mockPoReceiveDetailRepository.findByPurchaseOrderDetailPurchaseOrderPurchaseOrderId(1L)).thenReturn(summaryList);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);

		assertThrows(ResourceNotFoundException.class, () -> service.getReceivingSumary(1L));
	}

	@Test
	void getReceivingSumaryThrowsExceptionWhenVendorEscalationContactNotFound() {
		List<ReceivingResumeResponseDetailDTO> summaryList = fixtureLoader.getObject("summaryList", new ReceivingResumeResponseDetailListReference()).orElse(List.of());
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		when(mockPoReceiveDetailRepository.findByPurchaseOrderDetailPurchaseOrderPurchaseOrderId(1L)).thenReturn(summaryList);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockVendorMasterService.getVendorSalesRepresentative(1L)).thenReturn(Optional.of(new VendorContactModel()));

		assertThrows(ResourceNotFoundException.class, () -> service.getReceivingSumary(1L));
	}

	@Test
	void finishReceptionReturnsExpectedWhenCompleteReception() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);

		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);

		boolean result = service.finishReception(1L);

		assertThat(result, is(true));
	}

	@Test
	void finishReceptionReturnsExpectedWhenInCompleteReception() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		List<PoReceiveWarningModel> receiveWarningList = fixtureLoader.getObject("receiveWarningList", new ReceiveWarningListReference()).orElse(List.of());

		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockPoReceiveWarningRepository.checkIncompleteReception(1L)).thenReturn(receiveWarningList);

		boolean result = service.finishReception(1L);

		assertThat(result, is(true));
	}

	@Test
	void finishReceptionThrowsExceptionWhenPurchaseOrderNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.finishReception(1L));
	}

	@Test
	void findReceiveDetailsByPoOrderDetailIdReturnsExpected() {
		when(mockPoReceiveDetailRepository.findByPurchaseOrderDetailPurchaseOrderDetailId(1L)).thenReturn(new PoReceiveDetailModel());

		PoReceiveDetailModel result = service.findReceiveDetailsByPoOrderDetailId(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void notifyReceptionSendsEmail() {
		ReceptionNotificationRequestDTO request = fixtureLoader.getObject("notifyReceptionRequest", ReceptionNotificationRequestDTO.class).orElse(new ReceptionNotificationRequestDTO());
		List<ReceivingResumeResponseDetailDTO> summaryList = fixtureLoader.getObject("summaryList", new ReceivingResumeResponseDetailListReference()).orElse(List.of());
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);

		when(mockPoReceiveDetailRepository.findByPurchaseOrderDetailPurchaseOrderPurchaseOrderId(1L)).thenReturn(summaryList);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockVendorMasterService.getVendorSalesRepresentative(1L)).thenReturn(Optional.of(new VendorContactModel()));
		when(mockVendorMasterService.getVendorEscalationContact(1L)).thenReturn(Optional.of(new VendorContactModel()));
		when(mockStoreNumberService.getStoreManagerEmailList(1L)).thenReturn(List.of("user@example.com"));

		assertDoesNotThrow(() -> service.notifyReception(request));
	}


	@Test
	void notifyReceptionSendsEmailWithNoErrors() {
		ReceptionNotificationRequestDTO request = fixtureLoader.getObject("notifyReceptionRequest", ReceptionNotificationRequestDTO.class).orElse(new ReceptionNotificationRequestDTO());
		List<ReceivingResumeResponseDetailDTO> summaryList = fixtureLoader.getObject("summaryListNoErrors", new ReceivingResumeResponseDetailListReference()).orElse(List.of());
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);

		when(mockPoReceiveDetailRepository.findByPurchaseOrderDetailPurchaseOrderPurchaseOrderId(1L)).thenReturn(summaryList);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockVendorMasterService.getVendorSalesRepresentative(1L)).thenReturn(Optional.of(new VendorContactModel()));
		when(mockVendorMasterService.getVendorEscalationContact(1L)).thenReturn(Optional.of(new VendorContactModel()));
		when(mockStoreNumberService.getStoreManagerEmailList(1L)).thenReturn(List.of("user@example.com"));

		assertDoesNotThrow(() -> service.notifyReception(request));
	}

	private static class ReceivingResumeResponseDetailListReference extends TypeReference<List<ReceivingResumeResponseDetailDTO>> {
	}

	private static class ReceiveWarningListReference extends TypeReference<List<PoReceiveWarningModel>> {
	}
}
