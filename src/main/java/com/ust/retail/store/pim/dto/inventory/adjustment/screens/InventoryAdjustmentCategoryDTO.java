package com.ust.retail.store.pim.dto.inventory.adjustment.screens;

import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnCreate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryAdjustmentCategoryDTO {

	@NotNull(message = "Product Category is mandatory",groups = {OnCreate.class})
	private Long productCategoryId;
	private String productSubcategoryName;
	
	
	private Long dailyCountStatusId;
	private String dailyCountStatusDesc;
	
	
	
	
	
}
