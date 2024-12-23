package com.ust.retail.store.common.dto;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UnitOfMeasureFilterRequestDTO extends BaseFilterDTO {
	private Long unitTypeId;
	private String unitName;
}
