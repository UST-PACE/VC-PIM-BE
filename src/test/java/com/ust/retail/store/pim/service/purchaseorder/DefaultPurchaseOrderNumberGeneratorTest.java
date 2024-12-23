package com.ust.retail.store.pim.service.purchaseorder;


import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultPurchaseOrderNumberGeneratorTest {
	@Mock
	private PurchaseOrderRepository mockPurchaseOrderRepository;
	@InjectMocks
	private DefaultPurchaseOrderNumberGenerator generator;

	@Test
	void generateNumberReturnsExpectedWhenNoPreviousPOFound() {
		when(mockPurchaseOrderRepository.findLastPurchaseOrderNum()).thenReturn(Optional.empty());

		assertThat(generator.generateNumber(), is("PIM-0000000001-001"));
	}

	@Test
	void generateNumberReturnsExpectedWhenPreviousPOFound() {
		when(mockPurchaseOrderRepository.findLastPurchaseOrderNum()).thenReturn(Optional.of("PIM-0000000030-001"));

		assertThat(generator.generateNumber(), is("PIM-0000000031-001"));
	}

	@Test
	void generateRevisionReturnsExpectedWhenRevisionIsNull() {
		when(mockPurchaseOrderRepository.findLastPurchaseOrderNum()).thenReturn(Optional.empty());

		assertThat(generator.generateRevision(null), is("PIM-0000000001-001"));
	}

	@Test
	void generateRevisionReturnsExpectedWhenRevisionIsNotNull() {
		assertThat(generator.generateRevision("PIM-0000000001-001"), is("PIM-0000000001-002"));
	}

	@Test
	void isFirstRevisionReturnsFalseWhenRevisionIsNull() {
		assertThat(generator.isFirstRevision(null), is(false));
	}

	@Test
	void isFirstRevisionReturnsFalseWhenRevisionIsAfterOne() {
		assertThat(generator.isFirstRevision("PIM-0000000001-02"), is(false));
	}

	@Test
	void isFirstRevisionReturnsTrueWhenRevisionIsOne() {
		assertThat(generator.isFirstRevision("PIM-0000000001-01"), is(true));
	}
}
