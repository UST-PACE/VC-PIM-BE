package com.ust.retail.store.pim.service.purchaseorder;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ZeroDiscountStrategyTest {

	@Test
	void calculateDiscountReturnsExpected() {
		Double result = new ZeroDiscountStrategy().calculateDiscount(1d, 1, 1d);

		assertThat(result, is(0d));
	}
}
