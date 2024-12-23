package com.ust.retail.store.pim.dto.catalog;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdditionalFeeFilterDTO extends BaseFilterDTO {
	private String feeName;
}
