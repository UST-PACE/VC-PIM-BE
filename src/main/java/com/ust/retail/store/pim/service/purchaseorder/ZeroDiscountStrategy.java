package com.ust.retail.store.pim.service.purchaseorder;

public class ZeroDiscountStrategy implements DiscountStrategy {
	@Override
	public Double calculateDiscount(Double productCost, Integer quantity, Double discount) {
		return 0d;
	}
}
