package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class ProductUnitCatalog extends CatalogEngine {
	public static final Long UNIT_UNIT = 104L;
	public static final Long UNIT_OZ = 101L;
	public static final Long UNIT_G = 125L;
	public static final Long UNIT_IN = 129L;
	public static final Long UNIT_M = 128L;
	public static final String CATALOG_NAME = "BUYING_SELLING_UNIT";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
