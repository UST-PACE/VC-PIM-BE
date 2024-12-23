package com.ust.retail.store.pim.service.purchaseorder;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.exceptions.InvalidUPCException;
import com.ust.retail.store.pim.exceptions.ItemReceivingException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderDetailRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderDetailServiceTest {
	@Mock
	private PurchaseOrderDetailRepository mockPurchaseOrderDetailRepository;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;

	@InjectMocks
	private PurchaseOrderDetailService service;
	private static FixtureLoader fixtureLoader;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(PurchaseOrderDetailServiceTest.class);
	}

	@Test
	void getPendingItemByCodeReturnsExpected() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		Optional<PurchaseOrderDetailModel> poDetail = fixtureLoader.getObject("receivedPoDetail", PurchaseOrderDetailModel.class);
		when(mockUpcMasterRepository.findByPrincipalUpc("ABC")).thenReturn(product);
		when(mockPurchaseOrderDetailRepository.findByPurchaseOrderPurchaseOrderIdAndUpcMasterPrincipalUpc(1L, "ABC"))
				.thenReturn(poDetail);

		PurchaseOrderDetailModel result = service.getPendingItemByCode("ABC", 1L, 1L, true);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getPendingItemByCodeReturnsExpectedWhenDetailIsPending() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		Optional<PurchaseOrderDetailModel> poDetail = fixtureLoader.getObject("pendingPoDetail", PurchaseOrderDetailModel.class);
		when(mockUpcMasterRepository.findByPrincipalUpc("ABC")).thenReturn(product);
		when(mockPurchaseOrderDetailRepository.findByPurchaseOrderPurchaseOrderIdAndUpcMasterPrincipalUpc(1L, "ABC"))
				.thenReturn(poDetail);

		PurchaseOrderDetailModel result = service.getPendingItemByCode("ABC", 1L, 1L, true);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getPendingItemByCodeThrowsExceptionWhenProductNotFound() {
		assertThrows(InvalidUPCException.class,() -> service.getPendingItemByCode("ABC", 1L, 1L, true));
	}

	@Test
	void getPendingItemByCodeThrowsExceptionWhenResourceNotFound() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		when(mockUpcMasterRepository.findByPrincipalUpc("ABC")).thenReturn(product);

		assertThrows(ResourceNotFoundException.class,() -> service.getPendingItemByCode("ABC", 1L, 1L, true));
	}

	@Test
	void getPendingItemByCodeThrowsExceptionWhenReceivedItemAndNotUpdate() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		Optional<PurchaseOrderDetailModel> poDetail = fixtureLoader.getObject("receivedPoDetail", PurchaseOrderDetailModel.class);
		when(mockUpcMasterRepository.findByPrincipalUpc("ABC")).thenReturn(product);
		when(mockPurchaseOrderDetailRepository.findByPurchaseOrderPurchaseOrderIdAndUpcMasterPrincipalUpc(1L, "ABC"))
				.thenReturn(poDetail);

		assertThrows(ItemReceivingException.class,() -> service.getPendingItemByCode("ABC", 1L, 1L, false));
	}
}
