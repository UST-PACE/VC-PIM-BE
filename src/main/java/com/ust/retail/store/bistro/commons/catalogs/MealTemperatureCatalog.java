package com.ust.retail.store.bistro.commons.catalogs;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.engine.CatalogEngine;

@Component
public class MealTemperatureCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "MEAL_TEMPERATURE";
	
	public static final Long HOT = 20000L;
	public static final Long COLD = 20001L;
	
	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
