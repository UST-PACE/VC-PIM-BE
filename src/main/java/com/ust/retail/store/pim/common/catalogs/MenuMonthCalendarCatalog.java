package com.ust.retail.store.pim.common.catalogs;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.engine.CatalogEngine;

@Component
public class MenuMonthCalendarCatalog extends CatalogEngine {

	private static final String CATALOG_NAME = "MENU_MONTH";
	
	public static final Map<Long, List<Long>> quarterMonths = Map.of(3001L, List.of(30011L, 30012L, 30013L), 
																  3002L,List.of(30014L, 30015L, 30016L), 
			                                                      3003L, List.of(30017L, 30018L, 30019L), 
			                                                      3004L,List.of(30020L, 30021L, 30022L));
	
	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
}
