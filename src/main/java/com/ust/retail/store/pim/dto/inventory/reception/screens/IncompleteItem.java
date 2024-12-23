package com.ust.retail.store.pim.dto.inventory.reception.screens;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncompleteItem {

	private String productName;
	private String sku;
	private Double requestedQty;
	private Double receivedQty;
	private Double totalReceivedQty;
	
	private Double qty;
	private Long reasonId;
	
}
