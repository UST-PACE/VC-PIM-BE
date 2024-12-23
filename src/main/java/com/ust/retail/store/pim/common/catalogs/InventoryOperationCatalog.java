package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class InventoryOperationCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "INVENTORY_OPERATION";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
