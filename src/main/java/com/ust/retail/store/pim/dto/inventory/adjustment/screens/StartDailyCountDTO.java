package com.ust.retail.store.pim.dto.inventory.adjustment.screens;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.catalogs.DailyCountStatusCatalog;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StartDailyCountDTO {

	@NotNull(message = "Store Location Id is mandatory",groups = {OnCreate.class, OnUpdate.class})
	private Long storeLocationId;
	
	@Valid
	@NotNull(message = "At leas 1 subcatergory must be present",groups = {OnCreate.class,OnUpdate.class})
	private List<InventoryAdjustmentCategoryDTO> categories;
	

	public InventoryAdjustmentModel createModel(Long userCreatedId) {
		
		InventoryAdjustmentModel adjustment = new InventoryAdjustmentModel(storeLocationId,userCreatedId);
		
		for(InventoryAdjustmentCategoryDTO currentCategory : categories) {
			adjustment.addSubcategory(currentCategory.getProductCategoryId(), DailyCountStatusCatalog.DAILY_COUNT_STATUS_IN_PROCESS);
			
		}
		
		return adjustment;
	}
}
