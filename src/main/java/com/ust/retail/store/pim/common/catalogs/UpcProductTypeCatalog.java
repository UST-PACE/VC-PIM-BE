package com.ust.retail.store.pim.common.catalogs;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.engine.CatalogEngine;

@Component
public class UpcProductTypeCatalog extends CatalogEngine {
	
	public static Long ROTATING_TYPE = 70013L;
	public static Long MODIFYE_TYPE = 70014L;
	private static final String CATALOG_NAME = "UPC_PRODUCT_TYPE";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
