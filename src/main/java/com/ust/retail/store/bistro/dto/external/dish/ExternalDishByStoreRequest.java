package com.ust.retail.store.bistro.dto.external.dish;

import com.ust.retail.store.pim.common.annotations.OnSimpleFilter;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class ExternalDishByStoreRequest extends BaseFilterDTO {
	@NotNull(message = "Store ID is mandatory.", groups = OnSimpleFilter.class)
	private Long storeId;

	private String searchKey;

	private Long groupId;

	private Long categoryId;

	private Long subcategoryId;

	@NotNull(message = "Channel ID is mandatory.", groups = OnSimpleFilter.class)
	private Long channelId;
}
