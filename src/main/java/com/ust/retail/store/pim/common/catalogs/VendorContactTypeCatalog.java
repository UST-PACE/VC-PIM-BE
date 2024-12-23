package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class VendorContactTypeCatalog extends CatalogEngine {
	public static final Long CONTACT_TYPE_SALES_REPRESENTATIVE = 9000L;
	public static final Long CONTACT_TYPE_ESCALATION = 9001L;
	private static final String CATALOG_NAME = "VENDOR_CONTACT_TYPE";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
