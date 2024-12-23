package com.ust.retail.store.pim.dto.inventory.adjustment.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class InventoryAdjustmentSumaryDTO {

	private Map<String, List<InventoryAdjustmentItemDetailDTO>> summary;


	public void add(String category, InventoryAdjustmentItemDetailDTO summaryItem) {
		if (summary == null) summary = new HashMap<>();

		if (summary.get(category) == null) {

			List<InventoryAdjustmentItemDetailDTO> ls = new ArrayList<>();
			ls.add(summaryItem);
			summary.put(category, ls);

		} else {
			summary.get(category).add(summaryItem);

		}
	}
}
