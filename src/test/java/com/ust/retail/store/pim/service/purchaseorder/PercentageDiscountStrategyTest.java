package com.ust.retail.store.pim.service.purchaseorder;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PercentageDiscountStrategyTest {
	@Test
	void calculateDiscountReturnsExpected() {
		PercentageDiscountStrategy strategy = new PercentageDiscountStrategy();
		Double result = strategy.calculateDiscount(100d, 1, 25d);
		assertThat(result, is(25d));
	}
}
