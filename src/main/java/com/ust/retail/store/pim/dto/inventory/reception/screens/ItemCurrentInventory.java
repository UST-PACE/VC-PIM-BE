package com.ust.retail.store.pim.dto.inventory.reception.screens;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemCurrentInventory {
	private Long storeNumId;
	private String storeName;
	private Long storeLocationId;
	private String storeLocationName;
	private Double items;

	public ItemCurrentInventory(String storeLocationName, Double items) {
		this.storeLocationName = storeLocationName;
		this.items = items;
	}
}
