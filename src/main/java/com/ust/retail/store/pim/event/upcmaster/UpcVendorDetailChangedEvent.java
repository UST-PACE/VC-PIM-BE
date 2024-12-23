package com.ust.retail.store.pim.event.upcmaster;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpcVendorDetailChangedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private final Long upcVendorDetailId;

	public UpcVendorDetailChangedEvent(Object source, Long upcVendorDetailId) {
		super(source);
		this.upcVendorDetailId = upcVendorDetailId;
	}
}
