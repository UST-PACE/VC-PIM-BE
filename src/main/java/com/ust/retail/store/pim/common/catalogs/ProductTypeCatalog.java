package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeCatalog extends CatalogEngine {
	public static final Long PRODUCT_TYPE_RM = 4001L;
	public static final Long PRODUCT_TYPE_FG = 4002L;
	private static final String CATALOG_NAME = "PRODUCT_TYPE";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
