package com.ust.retail.store.pim.dto.productreturn;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductReturnFilterDTO extends BaseFilterDTO {
	private String vendorName;
	private String vendorCode;
	private Long brandOwnerId;
	private Long productTypeId;
	private Long productGroupId;
	private Long productCategoryId;
	private Long productSubcategoryId;
	private Long productItemId;
	private String productName;
	private Long statusId;
}
