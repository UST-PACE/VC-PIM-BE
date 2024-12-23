package com.ust.retail.store.pim.common;

import com.ust.retail.store.pim.service.catalog.StoreLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreFacade {

	private final StoreLocationService storeLocationService;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public StoreFacade(StoreLocationService storeLocationService, AuthenticationFacade authenticationFacade) {
		super();
		this.storeLocationService = storeLocationService;
		this.authenticationFacade = authenticationFacade;
	}

	public Long getStoreLocationForSales() {
		Long saleStoreNumberByStore = authenticationFacade.getUserStoreNumber().getStoreNumberId();
		return storeLocationService.findStoreLocationForSales(saleStoreNumberByStore);
	}

	public Long getStoreLocationForSales(Long storeNumberId) {
		return storeLocationService.findStoreLocationForSales(storeNumberId);
	}
}
