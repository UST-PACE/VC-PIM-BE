package com.ust.retail.store.pim.common.catalogs;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.engine.CatalogEngine;

@Component
public class ComboStatusCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "COMBO_STATUS";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}

}
