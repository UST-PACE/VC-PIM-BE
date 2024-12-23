package com.ust.retail.store.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class UnitTypeCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "UNIT_TYPE";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
