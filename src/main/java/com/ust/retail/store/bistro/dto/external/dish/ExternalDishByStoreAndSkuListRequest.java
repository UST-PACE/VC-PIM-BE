package com.ust.retail.store.bistro.dto.external.dish;

import com.ust.retail.store.pim.common.annotations.OnFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ExternalDishByStoreAndSkuListRequest {
	@NotNull(message = "Store ID is mandatory.", groups = OnFilter.class)
	private Long storeId;

	@NotEmpty(message = "At least one UPC is required.", groups = OnFilter.class)
	private List<String> skuList;
}
