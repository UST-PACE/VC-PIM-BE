package com.ust.retail.store.pim.service.inventory;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.dto.inventory.InventoryFiltersDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryProductDTO;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventoryAdjustmentDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventoryProductReturnDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventorySalesDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestUpdateDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.ItemCurrentInventory;
import com.ust.retail.store.pim.dto.inventory.returns.screen.ReturnItemDTO;
import com.ust.retail.store.pim.engine.inventory.BistroProduceInventory;
import com.ust.retail.store.pim.engine.inventory.BistroReduceInventory;
import com.ust.retail.store.pim.engine.inventory.InventoryAdjustment;
import com.ust.retail.store.pim.engine.inventory.ReceiveInventory;
import com.ust.retail.store.pim.engine.inventory.ReturnMerchandiseInventory;
import com.ust.retail.store.pim.engine.inventory.SaleItemInventory;
import com.ust.retail.store.pim.engine.inventory.TransferIncreaseInventory;
import com.ust.retail.store.pim.engine.inventory.TransferReduceInventory;
import com.ust.retail.store.pim.engine.inventory.VendorCredits;
import com.ust.retail.store.pim.exceptions.InvalidInventoryHistoryTransactionException;
import com.ust.retail.store.pim.exceptions.InvalidTxnNumberAuthorizationException;
import com.ust.retail.store.pim.exceptions.InvalidTxnNumberRejectException;
import com.ust.retail.store.pim.exceptions.InvalidUPCException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.InventoryHistoryModel;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.inventory.InventoryHistoryRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private ReceiveInventory mockReceiveInventory;
	@Mock
	private ReturnMerchandiseInventory mockReturnMerchandiseInventory;
	@Mock
	private InventoryAdjustment mockInventoryAdjustment;
	@Mock
	private VendorCredits mockVendorCredits;
	@Mock
	private SaleItemInventory mockSaleItemInventory;
	@Mock
	private BistroProduceInventory mockBistroProduceInventory;
	@Mock
	private BistroReduceInventory mockBistroReduceInventory;
	@Mock
	private TransferIncreaseInventory mockTransferIncreaseInventory;
	@Mock
	private TransferReduceInventory mockTransferReduceInventory;
	@Mock
	private InventoryHistoryRepository mockInventoryHistoryRepository;
	@Mock
	private InventoryRepository mockInventoryRepository;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private UnitConverter mockUnitConverter;

	@InjectMocks
	private InventoryService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(InventoryServiceTest.class);
	}

	@Test
	void receiveInventoryReturnsExpected() {
		ReceivingRequestDTO receivingDTO = fixtureLoader.getObject("receivingRequest", ReceivingRequestDTO.class).orElse(new ReceivingRequestDTO());
		Optional<UpcMasterModel> receivingProduct = fixtureLoader.getObject("itemProduct", UpcMasterModel.class);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(receivingProduct);
		when(mockUnitConverter.convert(any(), any(), any())).thenReturn(1d);

		assertDoesNotThrow(() -> service.receiveInventory(receivingDTO, 1L));
	}

	@Test
	void reduceBistroInventoryReturnsExpected() {
		Item item = fixtureLoader.getObject("reduceBistroItem", Item.class).orElse(new Item());

		assertDoesNotThrow(() -> service.reduceBistroInventory(List.of(item), 1L));
	}

	@Test
	void updateReceiveInventoryReturnsExpected() {
		ReceivingRequestUpdateDTO request = fixtureLoader.getObject("receivingUpdateRequest", ReceivingRequestUpdateDTO.class).orElse(new ReceivingRequestUpdateDTO());

		assertDoesNotThrow(() -> service.updateReceiveInventory(request, 1L));
	}

	@Test
	void invetoryAdjustmentReturnsExpectedWhenNoHistoryFound() {
		InventoryAdjustmentDTO request = fixtureLoader.getObject("inventoryAdjustmentRequest", InventoryAdjustmentDTO.class).orElse(new InventoryAdjustmentDTO());

		assertDoesNotThrow(() -> service.invetoryAdjustment(request, 1L));
	}

	@Test
	void invetoryAdjustmentReturnsExpectedWhenHistoryFound() {
		InventoryAdjustmentDTO request = fixtureLoader.getObject("inventoryAdjustmentRequest", InventoryAdjustmentDTO.class).orElse(new InventoryAdjustmentDTO());
		List<InventoryHistoryModel> historyList = fixtureLoader.getObject("inventoryAdjustmentHistory", new InventoryHistoryListReference()).orElse(List.of());

		when(mockInventoryHistoryRepository.findByReferenceIdAndOperationModuleCatalogIdAndInventoryUpcMasterUpcMasterId(any(), any(), any())).thenReturn(historyList);

		assertDoesNotThrow(() -> service.invetoryAdjustment(request, 1L));
	}

	@Test
	void vendorCreditsReturnsExpectedWhenNoHistoryFound() {
		ReturnItemDTO request = fixtureLoader.getObject("vendorCreditsRequest", ReturnItemDTO.class).orElse(new ReturnItemDTO());
		Optional<UpcMasterModel> receivingProduct = fixtureLoader.getObject("itemProduct", UpcMasterModel.class);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(receivingProduct);
		when(mockUnitConverter.convert(any(), any(), any())).thenReturn(1d);

		assertDoesNotThrow(() -> service.vendorCredits(request, 1L));
	}

	@Test
	void vendorCreditsReturnsExpectedWhenHistoryFound() {
		ReturnItemDTO request = fixtureLoader.getObject("vendorCreditsRequest", ReturnItemDTO.class).orElse(new ReturnItemDTO());
		List<InventoryHistoryModel> historyList = fixtureLoader.getObject("vendorCreditsHistory", new InventoryHistoryListReference()).orElse(List.of());
		Optional<UpcMasterModel> receivingProduct = fixtureLoader.getObject("itemProduct", UpcMasterModel.class);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(receivingProduct);
		when(mockUnitConverter.convert(any(), any(), any())).thenReturn(1d);

		when(mockInventoryHistoryRepository.findByReferenceIdAndOperationModuleCatalogIdAndInventoryUpcMasterUpcMasterId(any(), any(), any())).thenReturn(historyList);

		assertDoesNotThrow(() -> service.vendorCredits(request, 1L));
	}

	@Test
	void inventorySalesReturnsExpected() {
		InventorySalesDTO request = fixtureLoader.getObject("inventorySalesRequest", InventorySalesDTO.class).orElse(new InventorySalesDTO());

		assertDoesNotThrow(() -> service.inventorySales(request));
	}

	@Test
	void customerReturnInventoryReturnsExpected() {
		InventoryProductReturnDTO request = fixtureLoader.getObject("customerReturnRequest", InventoryProductReturnDTO.class).orElse(new InventoryProductReturnDTO());

		assertDoesNotThrow(() -> service.customerReturnInventory(request));
	}

	@Test
	void authorizeByTxnNumberReturnsExpected() {
		Optional<List<InventoryHistoryModel>> inventoryHistory = fixtureLoader.getObject("authorizeByTxnHistory", new InventoryHistoryListReference());
		Optional<InventoryModel> inventory = fixtureLoader.getObject("authorizeByTxnInventory", InventoryModel.class);
		when(mockInventoryHistoryRepository.findByTxnNumAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(any(), any())).thenReturn(inventoryHistory);
		when(mockInventoryRepository.findById(1L)).thenReturn(inventory);

		boolean result = service.authorizeByTxnNumber(1L);

		assertThat(result, is(true));
	}

	@Test
	void authorizeByTxnNumberReturnsExpectedWhenOtherOperationModule() {
		Optional<List<InventoryHistoryModel>> inventoryHistory = fixtureLoader.getObject("authorizeByTxnHistoryAlt", new InventoryHistoryListReference());
		Optional<InventoryModel> inventory = fixtureLoader.getObject("authorizeByTxnInventory", InventoryModel.class);
		when(mockInventoryHistoryRepository.findByTxnNumAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(any(), any())).thenReturn(inventoryHistory);
		when(mockInventoryRepository.findById(1L)).thenReturn(inventory);

		boolean result = service.authorizeByTxnNumber(1L);

		assertThat(result, is(true));
	}

	@Test
	void authorizeByTxnNumberThrowsExceptionWhenTxnNotExists() {
		assertThrows(InvalidTxnNumberAuthorizationException.class, () -> service.authorizeByTxnNumber(1L));
	}

	@Test
	void authorizeReturnsExpected() {
		Optional<List<InventoryHistoryModel>> inventoryHistory = fixtureLoader.getObject("authorizeHistory", new InventoryHistoryListReference());
		Optional<InventoryModel> inventory = fixtureLoader.getObject("authorizeInventory", InventoryModel.class);
		when(mockInventoryHistoryRepository.findByReferenceIdAndAuthorizationStatusCatalogIdAndOperationModuleCatalogIdOrderByInventoryHistoryIdDesc(any(), any(), any())).thenReturn(inventoryHistory);
		when(mockInventoryRepository.findById(1L)).thenReturn(inventory);

		boolean result = service.authorize(1L, InventoryAdjustment.OPERATION_MODULE);

		assertThat(result, is(true));
	}

	@Test
	void authorizeThrowsExceptionWhenNotExists() {
		assertThrows(InvalidTxnNumberAuthorizationException.class, () -> service.authorize(1L, 1L));
	}

	@Test
	void authorizeThrowsExceptionWhenInventoryNotExists() {
		Optional<List<InventoryHistoryModel>> inventoryHistory = fixtureLoader.getObject("authorizeHistory", new InventoryHistoryListReference());
		when(mockInventoryHistoryRepository.findByReferenceIdAndAuthorizationStatusCatalogIdAndOperationModuleCatalogIdOrderByInventoryHistoryIdDesc(any(), any(), any())).thenReturn(inventoryHistory);

		assertThrows(ResourceNotFoundException.class, () -> service.authorize(1L, 1L));
	}

	@Test
	void rejectByTxnNumberReturnsExpected() {
		Optional<List<InventoryHistoryModel>> inventoryHistory = fixtureLoader.getObject("rejectByTxnHistory", new InventoryHistoryListReference());
		when(mockInventoryHistoryRepository.findByTxnNumAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(any(), any())).thenReturn(inventoryHistory);

		boolean result = service.rejectByTxnNumber(1L);

		assertThat(result, is(true));
	}

	@Test
	void rejectByTxnNumberThrowsExceptionWhenTxnNotExists() {
		assertThrows(InvalidTxnNumberRejectException.class, () -> service.rejectByTxnNumber(1L));
	}

	@Test
	void rejectByReferenceIdReturnsExpected() {
		Optional<List<InventoryHistoryModel>> inventoryHistory = fixtureLoader.getObject("rejectByTxnHistory", new InventoryHistoryListReference());
		when(mockInventoryHistoryRepository.findByReferenceIdAndAuthorizationStatusCatalogIdAndOperationModuleCatalogIdOrderByInventoryHistoryIdDesc(any(), any(), any())).thenReturn(inventoryHistory);

		boolean result = service.rejectByReferenceId(1L, 1L);

		assertThat(result, is(true));
	}

	@Test
	void rejectByReferenceIdThrowsExceptionWhenTxnNotExists() {
		assertThrows(InvalidTxnNumberRejectException.class, () -> service.rejectByReferenceId(1L, 1L));
	}

	@Test
	void getItemInventoryDetailPerStoreNumberReturnsExpected() {
		List<InventoryModel> inventory = fixtureLoader.getObject("itemInventoryPerStoreNumberInventory", new InventoryListReference()).orElse(List.of());
		when(mockInventoryRepository.findByUpcMasterUpcMasterIdAndStoreLocationStoreNumberStoreNumId(1L, 1L)).thenReturn(inventory);

		List<InventoryModel> result = service.getItemInventoryDetailPerStoreNumber(1L, 1L);

		assertThat(result, hasSize(1));
	}

	@Test
	void findInventoryByCodeAndStoreLocationReturnsExpected() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("inventoryByCodeAndStoreLocationProduct", UpcMasterModel.class);
		Optional<InventoryModel> inventory = fixtureLoader.getObject("inventoryByCodeAndStoreLocationInventory", InventoryModel.class);
		when(mockUpcMasterRepository.findByPrincipalUpc("CODE")).thenReturn(product);
		when(mockInventoryRepository.findByUpcMasterUpcMasterIdAndStoreLocationStoreLocationId(1L, 1L)).thenReturn(inventory);

		InventoryProductDTO result = service.findInventoryByCodeAndStoreLocation("CODE", 1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findInventoryByCodeAndStoreLocationThrowsExceptionWhenProductNotFound() {
		assertThrows(InvalidUPCException.class, () -> service.findInventoryByCodeAndStoreLocation("CODE", 1L));
	}

	@Test
	void findInventoryByCodeAndStoreLocationThrowsExceptionWhenInventoryNotFound() {
		UpcMasterModel upcMasterModel = new UpcMasterModel();
		upcMasterModel.updateUpcMasterStatus(UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE);
		when(mockUpcMasterRepository.findByPrincipalUpc("CODE")).thenReturn(Optional.of(upcMasterModel));

		assertThrows(InvalidUPCException.class, () -> service.findInventoryByCodeAndStoreLocation("CODE", 1L));
	}

	@Test
	void findInventoryByUpcMasterIdReturnsExpected() {
		List<InventoryModel> inventory = fixtureLoader.getObject("itemInventoryPerStoreNumberInventory", new InventoryListReference()).orElse(List.of());
		when(mockInventoryRepository.findByUpcMasterUpcMasterId(1L)).thenReturn(inventory);
		when(mockUnitConverter.convert(any(), any(), any())).thenReturn(1d);

		List<ItemCurrentInventory> result = service.findInventoryByUpcMasterId(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void authorizeByInventoryHistoryIdReturnsExpected() {
		Optional<List<InventoryHistoryModel>> inventoryHistory = fixtureLoader.getObject("authorizeByInventoryHistoryIdHistory", new InventoryHistoryListReference());
		Optional<InventoryModel> inventory = fixtureLoader.getObject("authorizeByInventoryHistoryIdInventory", InventoryModel.class);
		when(mockInventoryHistoryRepository.findByInventoryHistoryIdAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(any(), any())).thenReturn(inventoryHistory);
		when(mockInventoryRepository.findById(1L)).thenReturn(inventory);

		boolean result = service.authorizeByInventoryHistoryId(1L);

		assertThat(result, is(true));
	}

	@Test
	void authorizeByInventoryHistoryIdThrowsExceptionWhenIdNotExists() {
		assertThrows(InvalidInventoryHistoryTransactionException.class, () -> service.authorizeByInventoryHistoryId(1L));
	}

	@Test
	void rejectByInventoryHistoryIdReturnsExpected() {
		Optional<List<InventoryHistoryModel>> inventoryHistory = fixtureLoader.getObject("rejectByInventoryHistoryIdHistory", new InventoryHistoryListReference());
		when(mockInventoryHistoryRepository.findByInventoryHistoryIdAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(any(), any())).thenReturn(inventoryHistory);

		boolean result = service.rejectByInventoryHistoryId(1L);

		assertThat(result, is(true));
	}

	@Test
	void rejectByInventoryHistoryIdThrowsExceptionWhenIdNotExists() {
		assertThrows(InvalidInventoryHistoryTransactionException.class, () -> service.rejectByInventoryHistoryId(1L));
	}

	@Test
	void clearInterruptedTransactionsCompletesSuccessfully() {
		assertDoesNotThrow(() -> service.clearInterruptedTransactions(1L, 1L));
	}

	@Test
	void trasferInventoryCompletesSuccessfully() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("inventoryByCodeAndStoreLocationProduct", UpcMasterModel.class);
		Optional<InventoryModel> inventory = fixtureLoader.getObject("inventoryByCodeAndStoreLocationInventory", InventoryModel.class);
		when(mockUpcMasterRepository.findByPrincipalUpc("CODE")).thenReturn(product);
		when(mockInventoryRepository.findByUpcMasterUpcMasterIdAndStoreLocationStoreLocationId(1L, 1L)).thenReturn(inventory);

		assertDoesNotThrow(() -> service.trasferInventory("CODE", 1L, 2L, 20d, 1L));
	}

	@Test
	void findInventoryReturnsExpected() {
		InventoryFiltersDTO request = new InventoryFiltersDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("storeNumberId");
		request.setOrderDir("asc");
		when(mockInventoryRepository.findByFilters(eq(null), eq(null), eq(null), eq(null), any())).thenReturn(new PageImpl<>(List.of()));

		Page<InventoryFiltersDTO> result = service.findInventory(request);

		assertThat(result.getContent(), is(notNullValue()));
	}

	@Test
	void findInventorySummaryReturnsExpected() {
		InventoryFiltersDTO request = new InventoryFiltersDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("storeNumberId");
		request.setOrderDir("asc");
		when(mockInventoryRepository.findByFiltersSummary(eq(null), eq(null), eq(null), eq(null), any())).thenReturn(new PageImpl<>(List.of()));

		Page<InventoryFiltersDTO> result = service.findInventorySummary(request);

		assertThat(result.getContent(), is(notNullValue()));
	}

	@Test
	void upcHasInventoryReturnsTrueWhenInventoryIsGreaterThanZero() {
		List<InventoryModel> inventory = fixtureLoader.getObject("itemInventoryPerStoreNumberInventory", new InventoryListReference()).orElse(List.of());
		when(mockInventoryRepository.findByUpcMasterUpcMasterId(1L)).thenReturn(inventory);
		when(mockUnitConverter.convert(any(), any(), any())).thenReturn(1d);

		boolean result = service.upcHasInventory(1L);

		assertThat(result, is(true));
	}

	@Test
	void upcHasInventoryReturnsFalseWhenInventoryIsZero() {
		boolean result = service.upcHasInventory(1L);

		assertThat(result, is(false));
	}

	private static class InventoryHistoryListReference extends TypeReference<List<InventoryHistoryModel>> {
	}

	private static class InventoryListReference extends TypeReference<List<InventoryModel>> {
	}
}
