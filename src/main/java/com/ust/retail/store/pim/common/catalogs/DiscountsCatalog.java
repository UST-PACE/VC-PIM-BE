package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class DiscountsCatalog extends CatalogEngine {
	public static final Long DISCOUNT_CURRENCY = 6000L;
	public static final Long DISCOUNT_PERCENTAGE = 6001L;
	private static final String CATALOG_NAME = "DISCOUNT";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
