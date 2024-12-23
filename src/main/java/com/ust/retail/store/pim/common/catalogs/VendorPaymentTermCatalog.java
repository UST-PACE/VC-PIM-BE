package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component

public class VendorPaymentTermCatalog  extends CatalogEngine {
	
	private static final String CATALOG_NAME = "VENDOR_PAYMENT_TERM_DAYS";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
