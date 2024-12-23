package com.ust.retail.store.bistro.commons.catalogs;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.engine.CatalogEngine;

@Component
public class MealTimeCatalog extends CatalogEngine {
	public static final Long MEAL_TIME_NA = 22004L;
	private static final String CATALOG_NAME = "MEAL_TIME";
	
	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
