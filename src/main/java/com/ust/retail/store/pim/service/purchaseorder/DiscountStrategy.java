package com.ust.retail.store.pim.service.purchaseorder;

public interface DiscountStrategy {
	Double calculateDiscount(Double productCost, Integer quantity, Double discount);
}
