package com.ust.retail.store.pim.service.purchaseorder;

public class CurrencyDiscountStrategy implements DiscountStrategy {
	@Override
	public Double calculateDiscount(Double productCost, Integer quantity, Double discount) {
		return quantity * discount;
	}
}
