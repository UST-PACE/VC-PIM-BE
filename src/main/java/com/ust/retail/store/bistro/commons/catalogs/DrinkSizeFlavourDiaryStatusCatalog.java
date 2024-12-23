package com.ust.retail.store.bistro.commons.catalogs;

import com.ust.retail.store.pim.engine.CatalogEngine;
import org.springframework.stereotype.Component;

@Component
public class DrinkSizeFlavourDiaryStatusCatalog extends CatalogEngine {

    private static final String CATALOG_NAME = "DRINK_CATALOG_STATUS";

    public static final Long ENABLED = 5000L;
    public static final Long DISABLED = 5001L;

    @Override
    protected String getCatalogName() {
        return CATALOG_NAME;
    }
}
