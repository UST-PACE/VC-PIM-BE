package com.ust.retail.store.bistro.dto.menu;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;

@Getter
public class MenuEntryFiltersDTO extends BaseFilterDTO {
	private Long storeNumId;
	private String recipeName;
	private Integer weekDay;
	private Integer start;
}
