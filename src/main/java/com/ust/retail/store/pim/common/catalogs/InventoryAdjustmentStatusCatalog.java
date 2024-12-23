package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class InventoryAdjustmentStatusCatalog extends CatalogEngine {
	public static final Long INVENTORY_ADJUSTMENT_STATUS_PENDING_REVIEW = 17000L;
	public static final Long INVENTORY_ADJUSTMENT_STATUS_PARTIAL_REVIEW = 17001L;
	public static final Long INVENTORY_ADJUSTMENT_STATUS_REVIEWED = 17002L;

	private static final String CATALOG_NAME = "INVENTORY_ADJUSTMENT_STATUS";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
