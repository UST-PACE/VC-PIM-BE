package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class PoStatusCatalog extends CatalogEngine {
	public static final Long PO_STATUS_DRAFT = 301L;
	public static final Long PO_STATUS_ORDERED = 302L;
	public static final Long PO_STATUS_COMPLETED = 303L;
	public static final Long PO_STATUS_INCOMPLETE = 304L;
	public static final Long PO_STATUS_PENDING_AUTHORIZATION = 305L;
	public static final Long PO_STATUS_IN_RECEPTION = 306L;

	private static final String CATALOG_NAME = "PO_STATUS";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
