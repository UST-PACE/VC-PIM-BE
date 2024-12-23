package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class ReturnWarningsCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "RETURN_WARNINGS";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
