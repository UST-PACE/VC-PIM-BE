package com.ust.retail.store.pim.dto.inventory.reception.screens;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemReception {

	private Long storeLocationId;
	private String loteNum;
	private Double reveivedQty;
	private Date expirationDate;
	
	private Double requestedTotalCases;
	private Double requestedTotalPallets;
	private Double requestedTotalUnits;
	
	private boolean loteNumRequired;
	private boolean expirationDateRequired;
	
	private List<ItemCurrentInventory> itemCurrentInventoryList;
	
}
