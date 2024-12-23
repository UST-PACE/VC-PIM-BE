package com.ust.retail.store.pim.dto.inventory.adjustment.screens;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnAdjustInventory;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.inventory.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryProductReturnDTO {

	@NotNull(message = "Product Inventory externalReferenceId is mandatory", groups = { OnAdjustInventory.class, OnUpdate.class })
	private Long externalReferenceId;

	@Valid
	@NotNull(message = "Item details are Mandatory.", groups = { OnAdjustInventory.class})
	private List<Item> items;

}
