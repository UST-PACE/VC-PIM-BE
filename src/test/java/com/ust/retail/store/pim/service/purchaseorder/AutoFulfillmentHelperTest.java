package com.ust.retail.store.pim.service.purchaseorder;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.dto.purchaseorder.operation.FulfillmentCandidateDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderFulfillmentRequestDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class AutoFulfillmentHelperTest {

	private static FixtureLoader fixtureLoader;

	@Mock
	private UnitConverter mockUnitConverter;

	@Mock
	private UpcMasterRepository mockUpcMasterRepository;

	private AutoFulfillmentHelper helper;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(DefaultAutoFulfillmentHelperTest.class);
	}

	@BeforeEach
	void setUp() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		lenient().when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		lenient().when(mockUnitConverter.convert(104L, 104L, 1d)).thenReturn(1d);
		helper = new DefaultAutoFulfillmentHelper(mockUnitConverter, mockUpcMasterRepository);
	}

	@Test
	void evaluateCandidateReturnsRequestToIgnoreWhenAmountIsGreaterThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 10.0, 1L, 1L, 50, 9.0, 0d);

		PurchaseOrderFulfillmentRequestDTO result = helper.evaluateCandidate(candidate, Map.of());

		assertThat(result.isToFulfill(), is(false));
	}

	@Test
	void evaluateCandidateReturnsRequestToIgnoreWhenInventoryIsGreaterThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 10.0, 1L, 1L, 9, 50.0, 0d);

		PurchaseOrderFulfillmentRequestDTO result = helper.evaluateCandidate(candidate, Map.of());

		assertThat(result.isToFulfill(), is(false));
	}

	@Test
	void evaluateCandidateReturnsRequestToUpdateDetailWhenAmountPlusInventoryIsLessThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 8.0, 0d);

		PurchaseOrderFulfillmentRequestDTO result = helper.evaluateCandidate(candidate, Map.of());

		assertThat(result.isToFulfill(), is(true));
		assertThat(result.isDetailToUpdate(), is(true));
		assertThat(result.isDetailToRemove(), is(false));
		assertThat(result.getAmountToRequest(), is(12));
	}

	@Test
	void evaluateCandidateReturnsRequestToRemoveDetailWhenInventoryIsGreaterThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 28.0, 0d);

		PurchaseOrderFulfillmentRequestDTO result = helper.evaluateCandidate(candidate, Map.of());

		assertThat(result.isToFulfill(), is(false));
		assertThat(result.isDetailToUpdate(), is(false));
		assertThat(result.isDetailToRemove(), is(true));
	}

	@Test
	void evaluateCandidateReturnsRequestToCreateOrderWhenOrderIdNotExists() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, null, null, 0, 8.0, 0d);

		PurchaseOrderFulfillmentRequestDTO result = helper.evaluateCandidate(candidate, Map.of());

		assertThat(result.isToFulfill(), is(true));
		assertThat(result.isNewOrder(), is(true));
		assertThat(result.isDetailToUpdate(), is(false));
	}

	@Test
	void evaluateCandidateReturnsRequestToAddDetailToCreatedOrderWhenOrderCreatedBefore() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, null, null, 0, 8.0, 0d);

		PurchaseOrderFulfillmentRequestDTO result = helper.evaluateCandidate(candidate, Map.of("1_1", 1L));

		assertThat(result.isToFulfill(), is(true));
		assertThat(result.isNewOrder(), is(false));
		assertThat(result.isDetailToUpdate(), is(false));
		assertThat(result.getAmountToRequest(), is(12));
	}

	@Test
	void evaluateCandidateReturnsRequestToAddDetailToOrderWhenOrderIdExists() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, null, 10, 8.0, 0d);

		PurchaseOrderFulfillmentRequestDTO result = helper.evaluateCandidate(candidate, Map.of());

		assertThat(result.isToFulfill(), is(true));
		assertThat(result.isNewOrder(), is(false));
		assertThat(result.isDetailToUpdate(), is(false));
		assertThat(result.getAmountToRequest(), is(12));
	}

	@Test
	void getNewOrderLineItemReturnsExpected() {
		assertThat(helper.getNewOrderLineItem(new PurchaseOrderFulfillmentRequestDTO()), is(notNullValue()));
	}

	@Test
	void getUpdateDetailLineItemReturnsExpected() {
		assertThat(helper.getUpdateDetailLineItem(new PurchaseOrderFulfillmentRequestDTO()), is(notNullValue()));
	}

	@Test
	void getAppendDetailLineItemReturnsExpected() {
		assertThat(helper.getAppendDetailLineItem(new PurchaseOrderFulfillmentRequestDTO()), is(notNullValue()));
	}
}
