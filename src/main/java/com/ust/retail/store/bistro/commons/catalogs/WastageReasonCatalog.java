package com.ust.retail.store.bistro.commons.catalogs;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.engine.CatalogEngine;

@Component
public class WastageReasonCatalog extends CatalogEngine {

	private static final String CATALOG_NAME = "KITCHEN_WASTAGE_REASON";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
