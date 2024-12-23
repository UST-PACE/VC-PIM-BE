package com.ust.retail.store.pim.common.catalogs;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.engine.CatalogEngine;

@Component
public class MenuQuarterCalendarCatalog extends CatalogEngine{

	private static final String CATALOG_NAME = "MENU_QUARTER";
	
	public static final Map<Long, Long> quarters = Map.of(3L,3001L,6L,3002L,9L,3003L,12L,3004L);
			
	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
