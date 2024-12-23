package com.ust.retail.store.pim.service.purchaseorder;

public class PercentageDiscountStrategy implements DiscountStrategy {
	@Override
	public Double calculateDiscount(Double productCost, Integer quantity, Double discount) {
		double total = productCost * quantity;
		return discount * total / 100;
	}
}
