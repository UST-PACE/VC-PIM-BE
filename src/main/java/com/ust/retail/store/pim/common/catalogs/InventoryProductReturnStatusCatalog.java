package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class InventoryProductReturnStatusCatalog extends CatalogEngine {
	public static final Long INVENTORY_PRODUCT_RETURN_STATUS_PENDING_REVIEW = 16000L;
	public static final Long INVENTORY_PRODUCT_RETURN_STATUS_PARTIAL_REVIEW = 16001L;
	public static final Long INVENTORY_PRODUCT_RETURN_STATUS_REVIEWED = 16002L;

	private static final String CATALOG_NAME = "INVENTORY_PRODUCT_RETURN_STATUS";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
