package com.ust.retail.store.pim.dto.external.product;

import com.ust.retail.store.pim.common.annotations.OnExternalSkuSearch;
import com.ust.retail.store.pim.common.annotations.OnSimpleFilter;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class ExternalProductByStoreRequest extends BaseFilterDTO {
	@NotNull(message = "Store ID is mandatory.", groups = {OnSimpleFilter.class, OnExternalSkuSearch.class})
	private Long storeId;

	private String searchKey;

	private Long groupId;

	private Long categoryId;

	private Long subcategoryId;

	@NotNull(message = "sku is mandatory.", groups = OnExternalSkuSearch.class)
	private String sku;

	@NotNull(message = "Channel ID is mandatory.", groups = {OnSimpleFilter.class, OnExternalSkuSearch.class})
	private Long channelId;
	
	private boolean pageable = true;
	
	private Boolean vcItem;
}
