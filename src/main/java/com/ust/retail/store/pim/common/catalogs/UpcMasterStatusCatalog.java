package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class UpcMasterStatusCatalog extends CatalogEngine {
	private static final String CATALOG_NAME = "UPC_MASTER_STATUS";
	public static final Long UPC_MASTER_STATUS_ACTIVE = 30000L;
	public static final Long UPC_MASTER_STATUS_DISCONTINUE_PURCHASE = 30001L;
	public static final Long UPC_MASTER_STATUS_DISCONTINUE_TRADING = 30002L;

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
