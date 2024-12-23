package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class UserStatusCatalog extends CatalogEngine {
	public static final Long USER_STATUS_ACTIVE = 80200L;
	public static final Long USER_STATUS_INACTIVE = 80201L;
	private static final String CATALOG_NAME = "USER_STATUS";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
