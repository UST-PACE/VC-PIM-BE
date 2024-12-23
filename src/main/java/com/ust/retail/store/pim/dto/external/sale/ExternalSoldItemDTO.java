package com.ust.retail.store.pim.dto.external.sale;

import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnSale;
import com.ust.retail.store.pim.dto.inventory.Item;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExternalSoldItemDTO {
	@NotNull(message = "Product Qty is mandatory.", groups = {OnSale.class})
	private Double qty;

	@NotNull(message = "Product ID is mandatory.", groups = {OnSale.class})
	private Long productId;

	public Item toItem(Long storeLocationIdForSales) {
		return new Item(qty, productId, storeLocationIdForSales);
	}
}
