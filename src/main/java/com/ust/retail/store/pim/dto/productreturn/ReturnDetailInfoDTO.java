package com.ust.retail.store.pim.dto.productreturn;

import com.ust.retail.store.pim.model.inventory.InventoryProductReturnDetailModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReturnDetailInfoDTO {

	private InventoryProductReturnDetailModel returnDetail;
	
	private Double credit;
}
