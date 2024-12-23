package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class ItemStatusCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "ITEM_STATUS";
	
	public static final long ITEM_STATUS_PENDING = 13000;
	public static final long ITEM_STATUS_RECEIVED = 13001;
	
	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
