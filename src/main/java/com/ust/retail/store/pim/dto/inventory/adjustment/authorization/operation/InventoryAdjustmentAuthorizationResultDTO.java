package com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class InventoryAdjustmentAuthorizationResultDTO {
	@Setter
	private Long statusId;
	private final List<InventoryAdjustmentAuthorizationResultLineDTO> results = new ArrayList<>();
}
