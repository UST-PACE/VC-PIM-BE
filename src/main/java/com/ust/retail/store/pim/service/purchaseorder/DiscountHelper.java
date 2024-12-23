package com.ust.retail.store.pim.service.purchaseorder;


import java.util.Map;

import com.ust.retail.store.pim.common.catalogs.DiscountsCatalog;

public class DiscountHelper {

	private DiscountHelper() {
	}

	private static final Map<Long, DiscountStrategy> strategyMap = Map.of(
			DiscountsCatalog.DISCOUNT_CURRENCY, new CurrencyDiscountStrategy(),
			DiscountsCatalog.DISCOUNT_PERCENTAGE, new PercentageDiscountStrategy()
	);

	public static DiscountStrategy getDiscountStrategyFor(Long discountTypeId) {
		return strategyMap.getOrDefault(discountTypeId, new ZeroDiscountStrategy());
	}
}
