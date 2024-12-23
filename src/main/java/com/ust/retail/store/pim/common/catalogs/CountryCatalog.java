package com.ust.retail.store.pim.common.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CountryCatalog extends CatalogEngine {
	@Value("${pim.catalogs.country.origin}")
	private Long countryOriginId;

	public static final Long COUNTRY_US = 2187L;

	private static final String CATALOG_NAME = "COUNTRY_OF_ORIGIN";

	@Override
	protected String getCatalogName() {
		return CATALOG_NAME;
	}
	public Long getCountryOriginId(){
		return countryOriginId;
	}
}
