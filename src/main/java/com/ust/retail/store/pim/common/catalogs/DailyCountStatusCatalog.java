package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class DailyCountStatusCatalog  extends CatalogEngine {
	private static final String CATALOG_NAME = "DAILY_COUNT_STATUS";

	public static final Long DAILY_COUNT_STATUS_IN_PROCESS=14000L;
	public static final Long DAILY_COUNT_STATUS_COMPLETE=14001L;
	public static final Long DAILY_COUNT_STATUS_INTERRUPTED=14002L;
	
	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
