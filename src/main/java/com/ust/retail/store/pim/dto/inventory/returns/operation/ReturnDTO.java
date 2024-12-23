package com.ust.retail.store.pim.dto.inventory.returns.operation;

import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnDTO {
	
	@NotNull(message = "Invenory Product Id is Mandatory.", groups = { OnUpdate.class})
	private Long inventoryProductReturnId;

	public InventoryProductReturnModel createModel(Long userCreatedId) {
		return new InventoryProductReturnModel(userCreatedId);
	}
}
