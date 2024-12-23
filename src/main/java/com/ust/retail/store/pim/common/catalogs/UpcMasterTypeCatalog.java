package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class UpcMasterTypeCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "UPC_MASTER_TYPE";
	
	public static final Long PIM_TYPE = 21000L;
	public static final Long BISTRO_TYPE = 21001L;
	
	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
