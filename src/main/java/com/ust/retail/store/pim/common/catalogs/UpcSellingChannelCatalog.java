package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class UpcSellingChannelCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "UPC_SELLING_CHANNEL";
	public static final Long VC_UPC_SELLING_CHANNEL = 40002L;

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
