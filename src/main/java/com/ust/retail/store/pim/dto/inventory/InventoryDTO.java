package com.ust.retail.store.pim.dto.inventory;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnInventoryAdjustment;
import com.ust.retail.store.pim.common.annotations.OnReceive;
import com.ust.retail.store.pim.common.annotations.OnVendorCredits;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryDTO{
	
	@Valid
	@NotNull(message = "Inventory Item List are mandatory.", groups = { OnReceive.class,OnVendorCredits.class,OnInventoryAdjustment.class })
	private List<Item> items;
	
	@Valid
	@NotNull(message = "shrinkage is mandatory.", groups = { OnInventoryAdjustment.class })
	private List<Item> shrinkageItems;

	

}
