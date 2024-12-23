package com.ust.retail.store.bistro.commons.catalogs;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.engine.CatalogEngine;

@Component
public class MealCategoryCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "MEAL_CATEGORY";
	
	public static final Long TOPPING = 18000L;
	public static final Long DISH = 18001L;
	
	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
