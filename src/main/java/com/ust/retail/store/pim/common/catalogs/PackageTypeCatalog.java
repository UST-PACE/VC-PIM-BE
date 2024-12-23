package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class PackageTypeCatalog extends CatalogEngine {
	public static final Long PACKAGE_TYPE_NONE = 201L;
	private static final String CATALOG_NAME = "PACKAGE_TYPE";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
