package com.ust.retail.store.pim.dto.tax;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.dto.upcmaster.ApplicableTaxDTO;

import lombok.Getter;

@Getter
public class StoreTaxesDTO {
	public StoreTaxesDTO(StoreNumberDTO store, List<ApplicableTaxDTO> applicableTaxes) {
		this.storeNumId = store.getStoreNumId();
		this.storeName = store.getStoreName();
		this.applicableTaxes = applicableTaxes;
	}

	@JsonIgnore
	private final Long storeNumId;
	private final String storeName;
	private final List<ApplicableTaxDTO> applicableTaxes;
}
