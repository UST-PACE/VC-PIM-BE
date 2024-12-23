package com.ust.retail.store.bistro.dto.kitchen;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KitchenExecutionRequestDTO extends BaseFilterDTO {
	private Long storeNumId;
	private String recipeName;
}
