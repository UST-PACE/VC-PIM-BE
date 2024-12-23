package com.ust.retail.store.pim.dto.external.product;

import com.ust.retail.store.pim.common.annotations.OnFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ExternalProductByStoreAndSkuListRequest {
	@NotNull(message = "Store ID is mandatory.", groups = OnFilter.class)
	private Long storeId;

	private List<String> skuList = new ArrayList<>();

	private List<SkuDetail> productList = new ArrayList<>();

	@Getter
	@Setter
	public static class SkuDetail {
		private String sku;
		private Double amount;
	}
}
