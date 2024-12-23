package com.ust.retail.store.pim.dto.tax;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TaxFilterDTO extends BaseFilterDTO {
	private Long storeNumId;
	private Long taxTypeId;
	private Long productGroupId;
	private Long productCategoryId;
	private Long productSubcategoryId;
}
