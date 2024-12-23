package com.ust.retail.store.bistro.commons.catalogs;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.engine.CatalogEngine;

@Component
public class RecipePreparationAreaCatalog extends CatalogEngine {
	public static final Long KITCHEN = 29001L;
	private static final String CATALOG_NAME = "RECIPE_PREPARATION_AREA";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
