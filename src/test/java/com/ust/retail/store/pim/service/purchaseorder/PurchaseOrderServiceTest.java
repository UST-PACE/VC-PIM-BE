package com.ust.retail.store.pim.service.purchaseorder;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.common.pdf.PdfGenerator;
import com.ust.retail.store.pim.dto.inventory.reception.screens.PurchaseOrdersDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionDTO;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDTO;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDetailDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderAddBulkProductRequestDTO;
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
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.exceptions.InvalidPurchaseOrderException;
import com.ust.retail.store.pim.exceptions.PurchaseOrderAuthorizationException;
import com.ust.retail.store.pim.exceptions.PurchaseOrderPrintException;
import com.ust.retail.store.pim.exceptions.PurchaseOrderUpdateException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcExistsInPurchaseOrderException;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import com.ust.retail.store.pim.model.vendormaster.VendorContactModel;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderDetailRepository;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderFulfillmentRepository;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcVendorDetailsRepository;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.service.promotion.PromotionService;
import com.ust.retail.store.pim.service.security.UserService;
import com.ust.retail.store.pim.service.vendormaster.VendorMasterService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceTest {

	private static FixtureLoader fixtureLoader;

	@Mock
	private PurchaseOrderRepository mockPurchaseOrderRepository;
	@Mock
	private PurchaseOrderFulfillmentRepository mockPurchaseOrderFulfillmentRepository;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private UpcVendorDetailsRepository mockUpcVendorDetailsRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;
	@Mock
	private PromotionService mockPromotionService;
	@Mock
	private VendorMasterService mockVendorMasterService;
	@Mock
	private UserService mockUserService;
	@Mock
	private PurchaseOrderNumberGenerator mockPurchaseOrderNumberGenerator;
	@Mock
	private EntityManager mockEntityManager;
	@Mock
	private PurchaseOrderDetailRepository mockPurchaseOrderDetailRepository;
	@Mock
	private EmailSend mockEmailSend;
	@Mock
	private StoreNumberService mockStoreNumberService;
	@Mock
	private PdfGenerator mockPdfGenerator;
	@Mock
	private InventoryService mockInventoryService;

	@InjectMocks
	private PurchaseOrderService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(PurchaseOrderServiceTest.class);
	}

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(service, "host", "https://example.com");
		ReflectionTestUtils.setField(service, "contactAddress", "info@example.com");
		ReflectionTestUtils.setField(service, "poBasePath", "/some/path");
		ReflectionTestUtils.setField(service, "customer", "Customer");
		ReflectionTestUtils.setField(service, "customerBgColor", "/some/path");
		ReflectionTestUtils.setField(service, "customerFgColor", "/some/path");
		ReflectionTestUtils.setField(service, "websiteUrl", "/some/path");
	}

	@Test
	void findByIdThrowsExceptionWhenPurchaseOrderIdNotFound() {
		Long purchaseOrderId = 1L;

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(purchaseOrderId));

		assertThat(exception, allOf(
				hasProperty("resourceName", is("Purchase Order")),
				hasProperty("fieldName", is("id")),
				hasProperty("fieldValue", is(1L)),
				hasProperty("errorCode", is("PIM-RNF-001"))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenPurchaseOrderIdFound() {
		Long purchaseOrderId = 1L;
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		UserModel currentUserDetails = fixtureLoader.getObject("currentUserDetails", UserModel.class).orElse(new UserModel(1L));

		when(mockPurchaseOrderRepository.findById(purchaseOrderId)).thenReturn(purchaseOrderModel);
		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(currentUserDetails);

		PurchaseOrderDTO result = service.findById(purchaseOrderId);

		assertThat(result, hasProperty("purchaseOrderId", is(1L)));
	}

	@Test
	void findDetailsByIdReturnsEmptyPageWhenNoRecordsFound() {
		PurchaseOrderFindDetailsByIdRequestDTO request = new PurchaseOrderFindDetailsByIdRequestDTO();
		request.setOrderColumn("purchaseOrderDetailId");
		request.setOrderDir("desc");
		request.setPage(1);
		request.setSize(10);
		when(mockPurchaseOrderRepository.getOrderDetailPage(any(), any())).thenReturn(new PageImpl<>(List.of()));

		Page<PurchaseOrderDetailDTO> result = service.findDetailsById(request);

		assertThat(result.isEmpty(), is(true));
	}

	@Test
	void findDetailsByIdReturnsPageWhenRecordsFound() {
		PurchaseOrderFindDetailsByIdRequestDTO request = new PurchaseOrderFindDetailsByIdRequestDTO();
		request.setOrderColumn("purchaseOrderDetailId");
		request.setOrderDir("desc");
		request.setPage(1);
		request.setSize(10);

		List<PurchaseOrderDetailModel> details = fixtureLoader.getObject("details", new PurchaseOrderListReference()).orElse(List.of());

		when(mockPurchaseOrderRepository.getOrderDetailPage(any(), any())).thenReturn(new PageImpl<>(details));

		Page<PurchaseOrderDetailDTO> result = service.findDetailsById(request);

		assertThat(result.getTotalElements(), is(1L));
		assertThat(result.getContent(), contains(hasProperty("purchaseOrderDetailId", is(1L))));
	}

	@Test
	void findOrdersByFiltersReturnsPageWhenRecordsFound() {
		PurchaseOrderFilterRequestDTO request = new PurchaseOrderFilterRequestDTO();
		request.setOrderColumn("purchaseOrderId");
		request.setOrderDir("desc");
		request.setPage(1);
		request.setSize(10);
		List<PurchaseOrderFilterResultDTO> purchaseOrderFilterResult = fixtureLoader.getObject("filterResult", new PurchaseOrderFilterResultListReference()).orElse(List.of());

		when(mockPurchaseOrderRepository.findByFilters(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(new PageImpl<>(purchaseOrderFilterResult));

		Page<PurchaseOrderFilterResultDTO> result = service.findOrdersByFilters(request);

		assertThat(result.getTotalElements(), is(1L));
		assertThat(result.getContent(), contains(hasProperty("purchaseOrderId", is(1L))));
	}

	@Test
	void findOrdersPendingFulfillmentByProductReturnsListWhenRecordsFound() {
		PurchaseOrderFilterRequestDTO request = new PurchaseOrderFilterRequestDTO();
		request.setOrderColumn("purchaseOrderId");
		request.setOrderDir("desc");
		request.setPage(1);
		request.setSize(10);
		List<PurchaseOrderPendingFulfillmentDTO> filterResult = fixtureLoader.getObject("pendingFulfillmentFilterResult", new PurchaseOrderPendingFulfillmentListReference()).orElse(List.of());

		when(mockPurchaseOrderFulfillmentRepository.findByStatusAndProduct(any(), any()))
				.thenReturn(filterResult);

		List<PurchaseOrderPendingFulfillmentDTO> result = service.findOrdersPendingFulfillmentByProduct(1L);

		assertThat(result.size(), is(1));
		assertThat(result, contains(hasProperty("purchaseOrderId", is(1L))));
	}

	@Test
	void createOrderWithLineItemThrowsExceptionWhenProductNotFound() {
		PurchaseOrderAddProductRequestDTO dto = new PurchaseOrderAddProductRequestDTO(null, 1L, 1L, 1L, 0, 0, 1, false);

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.createOrderWithLineItem(dto));

		assertThat(exception, allOf(
				hasProperty("resourceName", is("Product")),
				hasProperty("fieldName", is("id")),
				hasProperty("fieldValue", is(1L)),
				hasProperty("errorCode", is("PIM-RNF-001"))
		));
	}

	@Test
	void createOrderWithLineItemThrowsExceptionWhenVendorNotFound() {
		PurchaseOrderAddProductRequestDTO dto = new PurchaseOrderAddProductRequestDTO(null, 1L, 1L, 1L, 0, 0, 1, false);
		Optional<UpcMasterModel> upcMaster = fixtureLoader.getObject("activeUpcMaster", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(upcMaster);

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.createOrderWithLineItem(dto));

		assertThat(exception, allOf(
				hasProperty("resourceName", is("Vendor Details")),
				hasProperty("fieldName", is("id")),
				hasProperty("fieldValue", is(1L)),
				hasProperty("errorCode", is("PIM-RNF-001"))
		));
	}

	@Test
	void createOrderWithLineItemReturnsExpected() {
		PurchaseOrderAddProductRequestDTO dto = new PurchaseOrderAddProductRequestDTO(null, 1L, 1L, 1L, 0, 0, 1, false);
		Optional<UpcVendorDetailsModel> vendorDetailsModel = fixtureLoader.getObject("upcVendorDetailsModel", UpcVendorDetailsModel.class);
		Optional<UpcMasterModel> upcMaster = fixtureLoader.getObject("activeUpcMaster", UpcMasterModel.class);
		VendorMasterDTO vendorMasterDTO = fixtureLoader.getObject("vendorMasterDTO", VendorMasterDTO.class).orElse(new VendorMasterDTO());
		PurchaseOrderModel purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class).orElse(new PurchaseOrderModel());
		when(mockUpcMasterRepository.findById(1L)).thenReturn(upcMaster);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L))
				.thenReturn(vendorDetailsModel);
		when(mockVendorMasterService.findById(1L)).thenReturn(vendorMasterDTO);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).thenReturn(purchaseOrderModel);

		PurchaseOrderModifyLineItemResultDTO result = service.createOrderWithLineItem(dto);

		assertThat(result.getPurchaseOrderId(), is(1L));
	}

	@Test
	void createOrderWithLineItemReturnsExpectedWhePromotionExists() {
		PurchaseOrderAddProductRequestDTO dto = new PurchaseOrderAddProductRequestDTO(null, 1L, 1L, 1L, 0, 0, 1, false);
		Optional<UpcVendorDetailsModel> vendorDetailsModel = fixtureLoader.getObject("upcVendorDetailsModel", UpcVendorDetailsModel.class);
		Optional<UpcMasterModel> upcMaster = fixtureLoader.getObject("activeUpcMaster", UpcMasterModel.class);
		Optional<PromotionDTO> promotionDTO = fixtureLoader.getObject("promotionDTO", PromotionDTO.class);
		VendorMasterDTO vendorMasterDTO = fixtureLoader.getObject("vendorMasterDTO", VendorMasterDTO.class).orElse(new VendorMasterDTO());
		when(mockUpcMasterRepository.findById(1L)).thenReturn(upcMaster);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L))
				.thenReturn(vendorDetailsModel);
		when(mockPromotionService.findApplicablePromotionForProduct(1L, 1L)).thenReturn(promotionDTO);
		when(mockVendorMasterService.findById(1L)).thenReturn(vendorMasterDTO);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> {
			PurchaseOrderModel argument = invocation.getArgument(0);
			ReflectionTestUtils.setField(argument, "purchaseOrderId", 1L);
			return argument;
		});

		PurchaseOrderModifyLineItemResultDTO result = service.createOrderWithLineItem(dto);

		assertThat(result.getPurchaseOrderId(), is(1L));
		assertThat(result.getFinalCost(), is(-0.5d));
	}

	@Test
	void appendLineItemToOrderThrowsExceptionWhenPurchaseOrderNotFound() {
		PurchaseOrderAddProductRequestDTO dto = new PurchaseOrderAddProductRequestDTO(1L, 1L, 1L, 1L, 0, 0, 1, false);

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.appendLineItemToOrder(dto));

		assertThat(exception, allOf(
				hasProperty("resourceName", is("Purchase Order")),
				hasProperty("fieldName", is("id")),
				hasProperty("fieldValue", is(1L)),
				hasProperty("errorCode", is("PIM-RNF-001"))
		));
	}

	@Test
	void appendLineItemToOrderReturnsExpectedWhenOrderIsDraft() {
		PurchaseOrderAddProductRequestDTO dto = new PurchaseOrderAddProductRequestDTO(1L, 1L, 1L, 1L, 0, 0, 1, false);
		Optional<UpcVendorDetailsModel> vendorDetailsModel = fixtureLoader.getObject("upcVendorDetailsModel", UpcVendorDetailsModel.class);
		Optional<UpcMasterModel> upcMaster = fixtureLoader.getObject("activeUpcMaster", UpcMasterModel.class);
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		Optional<PromotionDTO> promotionDTO = fixtureLoader.getObject("promotionDTO", PromotionDTO.class);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(upcMaster);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L))
				.thenReturn(vendorDetailsModel);
		when(mockPromotionService.findApplicablePromotionForProduct(1L, 1L)).thenReturn(promotionDTO);

		PurchaseOrderModifyLineItemResultDTO result = service.appendLineItemToOrder(dto);

		assertThat(result.isRevisionUpdated(), is(false));
	}

	@Test
	void appendLineItemToOrderReturnsExpectedWhenOrderIsOrdered() {
		PurchaseOrderAddProductRequestDTO dto = new PurchaseOrderAddProductRequestDTO(1L, 1L, 1L, 1L, 0, 0, 1, false);
		Optional<UpcVendorDetailsModel> vendorDetailsModel = fixtureLoader.getObject("upcVendorDetailsModel", UpcVendorDetailsModel.class);
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("orderedPurchaseOrderModel", PurchaseOrderModel.class);
		Optional<UpcMasterModel> upcMaster = fixtureLoader.getObject("activeUpcMaster", UpcMasterModel.class);
		Optional<PromotionDTO> promotionDTO = fixtureLoader.getObject("promotionDTO", PromotionDTO.class);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(upcMaster);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L))
				.thenReturn(vendorDetailsModel);
		when(mockPromotionService.findApplicablePromotionForProduct(1L, 1L)).thenReturn(promotionDTO);

		PurchaseOrderModifyLineItemResultDTO result = service.appendLineItemToOrder(dto);

		assertThat(result.isRevisionUpdated(), is(true));
	}

	@Test
	void appendLineItemToOrderReturnsExpectedWhenOrderIsOrderedWithAppliedVendorCredit() {
		PurchaseOrderAddProductRequestDTO dto = new PurchaseOrderAddProductRequestDTO(1L, 1L, 1L, 1L, 0, 0, 1, false);
		Optional<UpcVendorDetailsModel> vendorDetailsModel = fixtureLoader.getObject("upcVendorDetailsModel", UpcVendorDetailsModel.class);
		Optional<UpcMasterModel> upcMaster = fixtureLoader.getObject("activeUpcMaster", UpcMasterModel.class);
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("orderedPurchaseOrderModelWithVendorCredit", PurchaseOrderModel.class);
		Optional<PromotionDTO> promotionDTO = fixtureLoader.getObject("promotionDTO", PromotionDTO.class);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(upcMaster);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L))
				.thenReturn(vendorDetailsModel);
		when(mockPromotionService.findApplicablePromotionForProduct(1L, 1L)).thenReturn(promotionDTO);

		PurchaseOrderModifyLineItemResultDTO result = service.appendLineItemToOrder(dto);

		assertThat(result.isRevisionUpdated(), is(true));
		assertThat(result.isVendorCreditModified(), is(true));
	}

	@Test
	void updateLineItemThrowsExceptionWhenDetailNotFound() {
		PurchaseOrderModifyLineItemRequestDTO dto = new PurchaseOrderModifyLineItemRequestDTO(1L, 1, 0, 10);

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.updateLineItem(dto));

		assertThat(exception, allOf(
				hasProperty("resourceName", is("Purchase Order Detail")),
				hasProperty("fieldName", is("purchaseOrderDetailId")),
				hasProperty("fieldValue", is(1L)),
				hasProperty("errorCode", is("PIM-RNF-001"))
		));
	}

	@Test
	void updateLineItemModifiesFinalCost() {
		PurchaseOrderModifyLineItemRequestDTO dto = new PurchaseOrderModifyLineItemRequestDTO(1L, 1, 0, 10);
		Optional<PurchaseOrderModel> purchaseOrderDetail = fixtureLoader.getObject("purchaseOrderModelWithDetails", PurchaseOrderModel.class);
		when(mockPurchaseOrderRepository.findByDetailsPurchaseOrderDetailId(1L)).thenReturn(purchaseOrderDetail);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> invocation.<PurchaseOrderModel>getArgument(0));

		PurchaseOrderModifyLineItemResultDTO result = service.updateLineItem(dto);

		assertThat(result.isRevisionUpdated(), is(false));
		assertThat(result.getFinalCost(), is(10d));
	}

	@Test
	void updateLineItemUpdatesRevision() {
		PurchaseOrderModifyLineItemRequestDTO dto = new PurchaseOrderModifyLineItemRequestDTO(1L, 1, 0, 10);
		Optional<PurchaseOrderModel> purchaseOrderDetail = fixtureLoader.getObject("orderedPurchaseOrderModelWithDetails", PurchaseOrderModel.class);
		when(mockPurchaseOrderRepository.findByDetailsPurchaseOrderDetailId(1L)).thenReturn(purchaseOrderDetail);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> invocation.<PurchaseOrderModel>getArgument(0));

		PurchaseOrderModifyLineItemResultDTO result = service.updateLineItem(dto);

		assertThat(result.isRevisionUpdated(), is(true));
		assertThat(result.getFinalCost(), is(5d));
	}

	@Test
	void removeLineItemUpdatesFinalCost() {
		Optional<PurchaseOrderModel> purchaseOrderDetail = fixtureLoader.getObject("purchaseOrderModelWithDetails", PurchaseOrderModel.class);
		when(mockPurchaseOrderRepository.findByDetailsPurchaseOrderDetailId(1L)).thenReturn(purchaseOrderDetail);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> invocation.<PurchaseOrderModel>getArgument(0));

		PurchaseOrderModifyLineItemResultDTO result = service.removeLineItem(1L);

		assertThat(result.isRevisionUpdated(), is(false));
		assertThat(result.getFinalCost(), is(0d));
	}

	@Test
	void removeLineItemUpdatesUpdatesRevision() {
		Optional<PurchaseOrderModel> purchaseOrderDetail = fixtureLoader.getObject("orderedPurchaseOrderModelWithDetails", PurchaseOrderModel.class);
		when(mockPurchaseOrderRepository.findByDetailsPurchaseOrderDetailId(1L)).thenReturn(purchaseOrderDetail);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> invocation.<PurchaseOrderModel>getArgument(0));

		PurchaseOrderModifyLineItemResultDTO result = service.removeLineItem(1L);

		assertThat(result.isRevisionUpdated(), is(true));
		assertThat(result.getFinalCost(), is(0d));
	}

	@Test
	void saveDraftDoesNotUpdateRevision() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		PurchaseOrderSaveRequestDTO dto = fixtureLoader.getObject("purchaseOrderSaveRequest", PurchaseOrderSaveRequestDTO.class).orElse(new PurchaseOrderSaveRequestDTO());
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> invocation.<PurchaseOrderModel>getArgument(0));

		PurchaseOrderModifyStatusResultDTO result = service.saveDraft(dto);

		assertThat(result.isRevisionUpdated(), is(false));
	}

	@Test
	void saveDraftUpdatesRevision() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("orderedPurchaseOrderModel", PurchaseOrderModel.class);
		PurchaseOrderSaveRequestDTO dto = fixtureLoader.getObject("purchaseOrderSaveRequest", PurchaseOrderSaveRequestDTO.class).orElse(new PurchaseOrderSaveRequestDTO());
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> invocation.<PurchaseOrderModel>getArgument(0));

		PurchaseOrderModifyStatusResultDTO result = service.saveDraft(dto);

		assertThat(result.isRevisionUpdated(), is(true));
	}

	@Test
	void saveDraftThrowsExceptionWhenOrderIsNotModifiable() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("completePurchaseOrderModel", PurchaseOrderModel.class);
		PurchaseOrderSaveRequestDTO dto = fixtureLoader.getObject("purchaseOrderSaveRequest", PurchaseOrderSaveRequestDTO.class).orElse(new PurchaseOrderSaveRequestDTO());
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);

		assertThrows(PurchaseOrderUpdateException.class, () -> service.saveDraft(dto));
	}

	@Test
	void processPurchaseOrderDoesNotModifyVendorCredit() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		PurchaseOrderSaveRequestDTO dto = fixtureLoader.getObject("purchaseOrderSaveRequest", PurchaseOrderSaveRequestDTO.class).orElse(new PurchaseOrderSaveRequestDTO());
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> invocation.<PurchaseOrderModel>getArgument(0));

		PurchaseOrderModifyStatusResultDTO result = service.processPurchaseOrder(dto);

		assertThat(result.isVendorCreditModified(), is(false));
	}

	@Test
	void processPurchaseOrderSetsPendingApprovalStatus() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("toApprovePurchaseOrderModel", PurchaseOrderModel.class);
		PurchaseOrderSaveRequestDTO dto = fixtureLoader.getObject("purchaseOrderSaveRequest", PurchaseOrderSaveRequestDTO.class).orElse(new PurchaseOrderSaveRequestDTO());
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> invocation.<PurchaseOrderModel>getArgument(0));

		PurchaseOrderModifyStatusResultDTO result = service.processPurchaseOrder(dto);

		assertThat(result.isToAuthorize(), is(true));
	}

	@Test
	void deleteByIdDoesNotUpdateRevision() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);

		PurchaseOrderModifyLineItemResultDTO result = service.deleteById(1L);

		assertThat(result.isRevisionUpdated(), is(false));
	}

	@Test
	void deleteByIdUpdatesRevision() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("orderedPurchaseOrderModel", PurchaseOrderModel.class);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);

		PurchaseOrderModifyLineItemResultDTO result = service.deleteById(1L);

		assertThat(result.isRevisionUpdated(), is(true));
	}

	@Test
	void sendOrderInvalidatedEmailToSalesRepresentativeCompletesWhenSalesRepresentative() {
		Optional<VendorContactModel> salesRepresentative = fixtureLoader.getObject("salesRepresentative", VendorContactModel.class);
		when(mockVendorMasterService.getVendorSalesRepresentative(1L)).thenReturn(salesRepresentative);

		assertDoesNotThrow(() -> service.sendOrderInvalidatedEmailToSalesRepresentative(1L, "ORDER_NUM"));
	}

	@Test
	void sendOrderInvalidatedEmailToEscalationContactCompletesWhenSalesRepresentative() {
		Optional<VendorContactModel> escalationContact = fixtureLoader.getObject("escalationContact", VendorContactModel.class);
		when(mockVendorMasterService.getVendorEscalationContact(1L)).thenReturn(escalationContact);

		assertDoesNotThrow(() -> service.sendOrderInvalidatedEmailToEscalationContact(1L, "ORDER_NUM"));
	}

	@Test
	void sendOrderInvalidatedEmailToStoreManagerCompletesWithoutErrorsWhenNoStoreManagersExist() {
		when(mockStoreNumberService.getStoreManagerEmailList(1L)).thenReturn(List.of());

		assertDoesNotThrow(() -> service.sendOrderInvalidatedEmailToStoreManager(1L, "ORDER_NUM", false, 1L, 0d));
	}

	@Test
	void sendOrderInvalidatedEmailToStoreManagerCompletesWithoutErrorsWhenStoreManagersExist() {
		VendorMasterDTO vendorMasterDTO = fixtureLoader.getObject("vendorMasterDTO", VendorMasterDTO.class).orElse(new VendorMasterDTO());
		when(mockStoreNumberService.getStoreManagerEmailList(1L)).thenReturn(List.of("user1@example.com"));
		when(mockVendorMasterService.findById(1L)).thenReturn(vendorMasterDTO);

		assertDoesNotThrow(() -> service.sendOrderInvalidatedEmailToStoreManager(1L, "ORDER_NUM", false, 1L, 0d));
	}

	@Test
	void sendOrderInvalidatedEmailToStoreManagerCompletesWithoutErrorsWhenStoreManagersExistAndVendorCreditModified() {
		VendorMasterDTO vendorMasterDTO = fixtureLoader.getObject("vendorMasterDTO", VendorMasterDTO.class).orElse(new VendorMasterDTO());
		when(mockStoreNumberService.getStoreManagerEmailList(1L)).thenReturn(List.of("user1@example.com"));
		when(mockVendorMasterService.findById(1L)).thenReturn(vendorMasterDTO);

		assertDoesNotThrow(() -> service.sendOrderInvalidatedEmailToStoreManager(1L, "ORDER_NUM", true, 1L, 10d));
	}

	@Test
	void sendNewOrderEmailToSalesRepresentativeCompletesSuccessfully() throws Exception {
		Optional<VendorContactModel> salesRepresentative = fixtureLoader.getObject("salesRepresentative", VendorContactModel.class);
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		UserModel currentUserDetails = fixtureLoader.getObject("currentUserDetails", UserModel.class).orElse(new UserModel(1L));
		when(mockVendorMasterService.getVendorSalesRepresentative(1L)).thenReturn(salesRepresentative);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(currentUserDetails);
		when(mockPdfGenerator.generatePdfFile(any(), any(), any())).thenReturn(new File(""));

		assertDoesNotThrow(() -> service.sendNewOrderEmailToSalesRepresentative(1L, 1L, "ORDER_NUM", "user"));
	}

	@Test
	void sendNewOrderEmailToStoreManagerCompletesSuccessfully() {
		PurchaseOrderModifyStatusResultDTO dto = fixtureLoader.getObject("purchaseOrderModifyStatusResultDTO", PurchaseOrderModifyStatusResultDTO.class).orElse(new PurchaseOrderModifyStatusResultDTO());
		dto.setVendorCreditModified(false, 0d);
		VendorMasterDTO vendorMasterDTO = fixtureLoader.getObject("vendorMasterDTO", VendorMasterDTO.class).orElse(new VendorMasterDTO());
		when(mockVendorMasterService.findById(1L)).thenReturn(vendorMasterDTO);
		when(mockStoreNumberService.getStoreManagerEmailList(1L)).thenReturn(List.of("user1@example.com"));

		assertDoesNotThrow(() -> service.sendNewOrderEmailToStoreManager(dto));
	}

	@Test
	void sendNewOrderEmailToStoreManagerCompletesSuccessfullyWhenVendorCreditModified() {
		PurchaseOrderModifyStatusResultDTO dto = fixtureLoader.getObject("purchaseOrderModifyStatusResultDTO", PurchaseOrderModifyStatusResultDTO.class).orElse(new PurchaseOrderModifyStatusResultDTO());
		dto.setVendorCreditModified(true, 10d);
		VendorMasterDTO vendorMasterDTO = fixtureLoader.getObject("vendorMasterDTO", VendorMasterDTO.class).orElse(new VendorMasterDTO());
		when(mockVendorMasterService.findById(1L)).thenReturn(vendorMasterDTO);
		when(mockStoreNumberService.getStoreManagerEmailList(1L)).thenReturn(List.of("user1@example.com"));

		assertDoesNotThrow(() -> service.sendNewOrderEmailToStoreManager(dto));
	}

	@Test
	void sendNewOrderEmailToStoreManagerCompletesSuccessfullyWhenStoreManagerListEmpty() {
		PurchaseOrderModifyStatusResultDTO dto = fixtureLoader.getObject("purchaseOrderModifyStatusResultDTO", PurchaseOrderModifyStatusResultDTO.class).orElse(new PurchaseOrderModifyStatusResultDTO());
		when(mockStoreNumberService.getStoreManagerEmailList(1L)).thenReturn(List.of());

		assertDoesNotThrow(() -> service.sendNewOrderEmailToStoreManager(dto));
	}

	@Test
	void findPurchaseOrderByNumberThrowsException() {
		assertThrows(InvalidPurchaseOrderException.class, () -> service.findPurchaseOrderByNumber("order_num"));
	}

	@Test
	void findPurchaseOrderByNumberReturnsExpected() {
		Optional<PurchaseOrderModel> model = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		when(mockPurchaseOrderRepository.findByPurchaseOrderNum(any())).thenReturn(model);

		PurchaseOrderModel result = service.findPurchaseOrderByNumber("order_num");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findOrdersByNumberAndStatusReturnsExpected() {
		List<PurchaseOrdersDTO> result = service.findOrdersByNumberAndStatus("PO-NUM", List.of(302L));

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findPurchaseOrderByDateReturnsEmptyList() {
		List<PurchaseOrdersDTO> result = service.findPurchaseOrderByDate(new Date(), List.of());

		assertThat(result, is(empty()));
	}

	@Test
	void loadProductByPurchaseOrderAndUpcReturnsExpected() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		Optional<UpcMasterModel> upcMasterModel = fixtureLoader.getObject("upcMasterModel", UpcMasterModel.class);
		Optional<UpcVendorDetailsModel> upcVendorDetailsModel = fixtureLoader.getObject("upcVendorDetailsModel", UpcVendorDetailsModel.class);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockUpcMasterRepository.findByPrincipalUpcAndVendor("111-ALGO", 1L, 1L)).thenReturn(upcMasterModel);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L)).thenReturn(upcVendorDetailsModel);

		PurchaseOrderUpcInformationDTO result = service.loadProductByPurchaseOrderAndUpc(1L, "111-ALGO");

		assertThat(result.getPurchaseOrderId(), is(1L));
	}

	@Test
	void loadProductByPurchaseOrderAndUpcThrowsExceptionWhenProductAlreadyAdded() {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModelWithDetails", PurchaseOrderModel.class);
		Optional<UpcMasterModel> upcMasterModel = fixtureLoader.getObject("upcMasterModel", UpcMasterModel.class);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockUpcMasterRepository.findByPrincipalUpcAndVendor("111-ALGO", 1L, 1L)).thenReturn(upcMasterModel);

		assertThrows(UpcExistsInPurchaseOrderException.class, () -> service.loadProductByPurchaseOrderAndUpc(1L, "111-ALGO"));
	}

	@Test
	void loadProductByUpcSuppliedByVendorReturnsExpected() {
		Optional<UpcMasterModel> upcMasterModel = fixtureLoader.getObject("upcMasterModel", UpcMasterModel.class);
		Optional<UpcVendorDetailsModel> upcVendorDetailsModel = fixtureLoader.getObject("upcVendorDetailsModel", UpcVendorDetailsModel.class);
		when(mockUpcMasterRepository.findByPrincipalUpcAndVendor("111-ALGO", 1L, 1L)).thenReturn(upcMasterModel);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L)).thenReturn(upcVendorDetailsModel);

		PurchaseOrderUpcInformationDTO result = service.loadProductByUpcSuppliedByVendor("111-ALGO", 1L, 1L);

		assertThat(result.getPrincipalUpc(), is("111-ALGO"));
	}

	@Test
	void findByPurchaseOrderNumberReturnsExpected() {
		PurchaseOrderModel purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class).orElse(new PurchaseOrderModel());
		when(mockPurchaseOrderRepository.findByPurchaseOrderNumContaining("ORDER")).thenReturn(List.of(purchaseOrderModel));

		List<PurchaseOrderFilterResultDTO> result = service.findByPurchaseOrderNumber("ORDER");

		assertThat(result, contains(hasProperty("purchaseOrderNum", is("ORDER_NUM"))));
	}

	@Test
	void sendOrderAuthorizationEmailCompletesSuccessfully() {
		PurchaseOrderModifyStatusResultDTO dto = fixtureLoader.getObject("purchaseOrderModifyStatusResultDTO", PurchaseOrderModifyStatusResultDTO.class).orElse(new PurchaseOrderModifyStatusResultDTO());
		UserDTO userDto = fixtureLoader.getObject("userDTO", UserDTO.class).orElse(new UserDTO());
		VendorMasterDTO vendorMasterDTO = fixtureLoader.getObject("vendorMasterDTO", VendorMasterDTO.class).orElse(new VendorMasterDTO());

		when(mockStoreNumberService.getPoApproverEmailList(1L)).thenReturn(List.of("user@example.com"));
		when(mockVendorMasterService.findById(1L)).thenReturn(vendorMasterDTO);
		when(mockUserService.findById(1L)).thenReturn(userDto);

		assertDoesNotThrow(() -> service.sendOrderAuthorizationEmail(dto, 1L));
	}

	@Test
	void sendOrderAuthorizationEmailCompletesSuccessfullyWhenNoApproverEmailList() {
		PurchaseOrderModifyStatusResultDTO dto = fixtureLoader.getObject("purchaseOrderModifyStatusResultDTO", PurchaseOrderModifyStatusResultDTO.class).orElse(new PurchaseOrderModifyStatusResultDTO());

		when(mockStoreNumberService.getPoApproverEmailList(1L)).thenReturn(List.of());

		assertDoesNotThrow(() -> service.sendOrderAuthorizationEmail(dto, 1L));
	}

	@Test
	void getPurchaseOrderPdfCompletesSuccessfully() throws Exception {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		UserModel currentUserDetails = fixtureLoader.getObject("currentUserDetails", UserModel.class).orElse(new UserModel(1L));

		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(currentUserDetails);
		when(mockPdfGenerator.generatePdfFile(any(), any(), any())).thenReturn(mock(File.class));

		File result = service.getPurchaseOrderPdf(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getPurchaseOrderPdfThrowsException() throws Exception {
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		UserModel currentUserDetails = fixtureLoader.getObject("currentUserDetails", UserModel.class).orElse(new UserModel(1L));

		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(currentUserDetails);
		when(mockPdfGenerator.generatePdfFile(any(), any(), any())).thenThrow(new IOException("TEST_EXCEPTION"));

		assertThrows(PurchaseOrderPrintException.class, () -> service.getPurchaseOrderPdf(1L));
	}

	@Test
	void authorizePurchaseOrderThrowsExceptionWhenUserNotApprover() {
		PurchaseOrderSaveRequestDTO dto = fixtureLoader.getObject("purchaseOrderSaveRequest", PurchaseOrderSaveRequestDTO.class).orElse(new PurchaseOrderSaveRequestDTO());
		UserModel currentUserDetails = fixtureLoader.getObject("currentUserDetails", UserModel.class).orElse(new UserModel(1L));

		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(currentUserDetails);

		assertThrows(PurchaseOrderAuthorizationException.class, () -> service.authorizePurchaseOrder(dto));
	}

	@Test
	void authorizePurchaseOrderCompletesSuccessfully() {
		PurchaseOrderSaveRequestDTO dto = fixtureLoader.getObject("purchaseOrderSaveRequest", PurchaseOrderSaveRequestDTO.class).orElse(new PurchaseOrderSaveRequestDTO());
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class);
		UserModel currentUserDetails = fixtureLoader.getObject("approverCurrentUserDetails", UserModel.class).orElse(new UserModel(1L));
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).then(invocation -> invocation.<PurchaseOrderModel>getArgument(0));

		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(currentUserDetails);

		assertDoesNotThrow(() -> service.authorizePurchaseOrder(dto));
	}

	@Test
	void createOrUpdateOrderWithMultipleLineItemsReturnsExpectedWhenNewOrder() {
		PurchaseOrderAddBulkProductRequestDTO dto = fixtureLoader.getObject("addBulkCreateRequest", PurchaseOrderAddBulkProductRequestDTO.class).orElse(new PurchaseOrderAddBulkProductRequestDTO());
		Optional<UpcVendorDetailsModel> vendorDetailsModel = fixtureLoader.getObject("upcVendorDetailsModel", UpcVendorDetailsModel.class);
		Optional<UpcMasterModel> upcMaster = fixtureLoader.getObject("activeUpcMaster", UpcMasterModel.class);
		VendorMasterDTO vendorMasterDTO = fixtureLoader.getObject("vendorMasterDTO", VendorMasterDTO.class).orElse(new VendorMasterDTO());
		PurchaseOrderModel purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class).orElse(new PurchaseOrderModel());
		when(mockUpcMasterRepository.findById(any())).thenReturn(upcMaster);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(any(), eq(1L)))
				.thenReturn(vendorDetailsModel);
		when(mockVendorMasterService.findById(1L)).thenReturn(vendorMasterDTO);
		when(mockPurchaseOrderRepository.saveAndFlush(any())).thenReturn(purchaseOrderModel);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrderModel));

		PurchaseOrderModifyLineItemResultDTO result = service.createOrUpdateOrderWithMultipleLineItems(dto);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void createOrUpdateOrderWithMultipleLineItemsReturnsExpectedWhenExistingOrder() {
		PurchaseOrderAddBulkProductRequestDTO dto = fixtureLoader.getObject("addBulkUpdateRequest", PurchaseOrderAddBulkProductRequestDTO.class).orElse(new PurchaseOrderAddBulkProductRequestDTO());
		Optional<UpcVendorDetailsModel> vendorDetailsModel = fixtureLoader.getObject("upcVendorDetailsModel", UpcVendorDetailsModel.class);
		Optional<UpcMasterModel> upcMaster = fixtureLoader.getObject("activeUpcMaster", UpcMasterModel.class);
		PurchaseOrderModel purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModel", PurchaseOrderModel.class).orElse(new PurchaseOrderModel());

		when(mockUpcMasterRepository.findById(any())).thenReturn(upcMaster);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(any(), eq(1L)))
				.thenReturn(vendorDetailsModel);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrderModel));

		PurchaseOrderModifyLineItemResultDTO result = service.createOrUpdateOrderWithMultipleLineItems(dto);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadProductsByVendorReturnsExpectedWhenPurchaseOrderIdIsNull() {
		List<UpcVendorDetailsModel> vendorProductList = fixtureLoader.getObject("vendorProductList", new UpcVendorDetailListReference()).orElse(List.of());
		when(mockUpcVendorDetailsRepository.findByVendorMasterIdAndStoreNumId(1L, 1L)).thenReturn(vendorProductList);

		List<PurchaseOrderUpcInformationDTO> result = service.loadProductsByVendor(1L, 1L, null);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadProductsByVendorReturnsExpectedWhenPurchaseOrderIdIsNotNull() {
		List<UpcVendorDetailsModel> vendorProductList = fixtureLoader.getObject("vendorProductList", new UpcVendorDetailListReference()).orElse(List.of());
		Optional<PurchaseOrderModel> purchaseOrderModel = fixtureLoader.getObject("purchaseOrderModelWithDetails", PurchaseOrderModel.class);
		when(mockUpcVendorDetailsRepository.findByVendorMasterIdAndStoreNumId(1L, 1L)).thenReturn(vendorProductList);
		when(mockPurchaseOrderRepository.findById(1L)).thenReturn(purchaseOrderModel);

		List<PurchaseOrderUpcInformationDTO> result = service.loadProductsByVendor(1L, 1L, 1L);

		assertThat(result, is(notNullValue()));
	}

	private static class UpcVendorDetailListReference extends TypeReference<List<UpcVendorDetailsModel>> {
	}

	private static class PurchaseOrderListReference extends TypeReference<List<PurchaseOrderDetailModel>> {
	}

	private static class PurchaseOrderFilterResultListReference extends TypeReference<List<PurchaseOrderFilterResultDTO>> {
	}

	private static class PurchaseOrderPendingFulfillmentListReference extends TypeReference<List<PurchaseOrderPendingFulfillmentDTO>> {
	}
}
