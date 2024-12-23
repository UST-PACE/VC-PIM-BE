package com.ust.retail.store.pim.controller.inventory;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.catalogs.ReceptionWarningCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestUpdateDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResponseDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResumeResponseDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.ItemDetailsDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.PoOrderMasterDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.PurchaseOrdersDTO;
import com.ust.retail.store.pim.model.inventory.PoReceiveDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.service.inventory.ReceiveDetailService;
import com.ust.retail.store.pim.service.purchaseorder.PurchaseOrderDetailService;
import com.ust.retail.store.pim.service.purchaseorder.PurchaseOrderService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryReceivingControllerTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private PurchaseOrderDetailService mockPurchaseOrderDetailService;
	@Mock
	private PurchaseOrderService mockPurchaseOrderService;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private ReceiveDetailService mockReceiveDetailService;
	@Mock
	private ReceptionWarningCatalog mockReceptionWarningCatalog;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private InventoryReceivingController controller;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(InventoryReceivingControllerTest.class);
	}

	@Test
	void loadOrderReturnsExpected() {
		PurchaseOrderModel purchaseOrder = fixtureLoader.getObject("purchaseOrder", PurchaseOrderModel.class).orElse(new PurchaseOrderModel());
		when(mockPurchaseOrderService.findPurchaseOrderByNumber("PO_NUM")).thenReturn(purchaseOrder);
		when(mockReceiveDetailService.getReceivingSumary(any())).thenReturn(new ReceivingResumeResponseDTO());
		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(new UserModel());

		PoOrderMasterDTO result = controller.loadOrder("PO_NUM");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadItem() {
		PurchaseOrderDetailModel detail = fixtureLoader.getObject("orderDetail", PurchaseOrderDetailModel.class).orElse(new PurchaseOrderDetailModel());
		PoReceiveDetailModel receiveDetail = fixtureLoader.getObject("receiveDetail", PoReceiveDetailModel.class).orElse(new PoReceiveDetailModel());

		when(mockPurchaseOrderDetailService.getPendingItemByCode("ABC", 1L, 1L, true)).thenReturn(detail);
		when(mockReceiveDetailService.findReceiveDetailsByPoOrderDetailId(1L)).thenReturn(receiveDetail);
		when(mockInventoryService.getItemInventoryDetailPerStoreNumber(any(), any())).thenReturn(List.of());

		ItemDetailsDTO result = controller.loadItem("ABC", 1L, 1L, true);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void receiveInventoryReturnsExpected() {
		ReceivingRequestDTO request = new ReceivingRequestDTO();
		when(mockReceiveDetailService.receiveInventory(request)).thenReturn(new ReceivingResponseDTO(1L, 1L));

		ReceivingResponseDTO result = controller.receiveInventory(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReceiveInventoryReturnsExpected() {
		ReceivingRequestUpdateDTO request = new ReceivingRequestUpdateDTO();
		when(mockReceiveDetailService.updateReceiveInventory(request)).thenReturn(new ReceivingResponseDTO(1L, 1L));

		ReceivingResponseDTO result = controller.updateReceiveInventory(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void receivingResumeReturnsExpected() {
		when(mockReceiveDetailService.getReceivingSumary(1L)).thenReturn(new ReceivingResumeResponseDTO());

		ReceivingResumeResponseDTO result = controller.receivingResume(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadPendingPurchaseOrdersReturnsExpected() {
		when(mockPurchaseOrderService.findPurchaseOrderByDate(any(), any())).thenReturn(List.of());

		List<PurchaseOrdersDTO> result = controller.loadPendingPurchaseOrders(new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadReceivedPurchaseOrdersReturnsExpected() {
		when(mockPurchaseOrderService.findPurchaseOrderByDate(any(), any())).thenReturn(List.of());

		List<PurchaseOrdersDTO> result = controller.loadReceivedPurchaseOrders(new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void finishReceptionReturnsExpected() {
		when(mockReceiveDetailService.finishReception(1L)).thenReturn(true);

		boolean result = controller.finishReception(1L);

		assertThat(result, is(true));
	}

	@Test
	void loadReceptionWarnings() {
		when(mockReceptionWarningCatalog.getCatalogOptions()).thenReturn(List.of());

		List<CatalogDTO> result = controller.loadReceptionWarnings();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findOrderReturnsExpected() {
		List<PurchaseOrdersDTO> result = controller.findOrder("PO-NUM", InventoryReceivingController.SearchStatus.PENDING);

		assertThat(result, is(notNullValue()));
	}
}
