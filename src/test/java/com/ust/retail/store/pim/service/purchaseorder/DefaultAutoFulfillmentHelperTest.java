package com.ust.retail.store.pim.service.purchaseorder;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.dto.purchaseorder.operation.FulfillmentCandidateDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAutoFulfillmentHelperTest {

	private static FixtureLoader fixtureLoader;

	@Mock
	private UnitConverter mockUnitConverter;

	@Mock
	private UpcMasterRepository mockUpcMasterRepository;

	@InjectMocks
	private DefaultAutoFulfillmentHelper helper;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(DefaultAutoFulfillmentHelperTest.class);
	}

	@BeforeEach
	void setUp() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockUnitConverter.convert(104L, 104L, 1d)).thenReturn(1d);
	}

	@Test
	void shouldFulfillReturnsTrueWhenAmountPlusInventoryIsLessThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 8.0);

		assertThat(helper.shouldFulfill(candidate), is(true));
	}

	@Test
	void shouldFulfillReturnsFalseWhenAmountPlusInventoryIsGreaterThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 10.0, 1L, 1L, 50, 9.0);

		assertThat(helper.shouldFulfill(candidate), is(false));
	}

	@Test
	void shouldFulfillReturnsFalseWhenAmountPlusInventoryIsEqualToStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 10.0);

		assertThat(helper.shouldFulfill(candidate), is(false));
	}

	@Test
	void shouldUpdateLineItemReturnsTrueWhenAmountPlusInventoryIsLessThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 0.0);

		assertThat(helper.shouldUpdateLineItem(candidate), is(true));
	}

	@Test
	void shouldUpdateLineItemReturnsFalseWhenAmountPlusInventoryIsEqualToStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 10.0);

		assertThat(helper.shouldUpdateLineItem(candidate), is(false));
	}

	@Test
	void shouldUpdateLineItemReturnsFalseWhenAmountPlusInventoryIsGreaterThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 20, 10.0);

		assertThat(helper.shouldUpdateLineItem(candidate), is(false));
	}

	@Test
	void shouldRemoveLineItemReturnsTrueWhenAmountPlusInventoryIsGreaterThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 25.0);

		assertThat(helper.shouldRemoveLineItem(candidate), is(true));
	}

	@Test
	void shouldRemoveLineItemReturnsFalseWhenAmountPlusInventoryIsLessThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 5.0);

		assertThat(helper.shouldRemoveLineItem(candidate), is(false));
	}

	@Test
	void shouldRemoveLineItemReturnsFalseWhenAmountPlusInventoryIsEqualToStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 10.0);

		assertThat(helper.shouldRemoveLineItem(candidate), is(false));
	}

	@Test
	void calculateAmountToRequestReturnsExpectedWhenAmountPlusInventoryIsLessThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 8.0);

		assertThat(helper.calculateAmountToRequest(candidate), is(12));
	}

	@Test
	void calculateAmountToRequestReturnsExpectedWhenAmountPlusInventoryIsEqualToStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 10.0);

		assertThat(helper.calculateAmountToRequest(candidate), is(10));
	}

	@Test
	void getAmountToCoverFulfillmentReturnsExpectedWhenInventoryIsLessThanStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 8.0);

		assertThat(helper.getAmountToCoverFulfillment(candidate), is(2.0));
	}

	@Test
	void getAmountToCoverFulfillmentReturnsExpectedWhenInventoryIsEqualToStockMin() {
		FulfillmentCandidateDTO candidate = new FulfillmentCandidateDTO(1L, 1L, 1L, 20.0, 1L, 1L, 10, 10.0);

		assertThat(helper.getAmountToCoverFulfillment(candidate), is(0.0));
	}
}
