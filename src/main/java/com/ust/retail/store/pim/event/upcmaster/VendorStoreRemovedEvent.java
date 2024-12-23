package com.ust.retail.store.pim.event.upcmaster;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VendorStoreRemovedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private final Long vendorMasterId;
	private final Long storeNumId;

	public VendorStoreRemovedEvent(Object source, Long vendorMasterId, Long storeNumId) {
		super(source);
		this.vendorMasterId = vendorMasterId;
		this.storeNumId = storeNumId;
	}
}
