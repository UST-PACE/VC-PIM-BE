package com.ust.retail.store.pim.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.service.catalog.CatalogService;

@Component
public abstract class CatalogEngine {

	@Autowired
	private CatalogService catalogService;

	public List<CatalogDTO> getCatalogOptions() {
		return catalogService.findByCatalogName(getCatalogName());
	}
	
	public CatalogDTO findCatalogById(Long catalogId) {
		return catalogService.findCatalogById(catalogId);
	}

	protected abstract String getCatalogName();
	
}
