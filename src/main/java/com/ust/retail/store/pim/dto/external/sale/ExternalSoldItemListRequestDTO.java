package com.ust.retail.store.pim.dto.external.sale;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnSale;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventorySalesDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExternalSoldItemListRequestDTO {
	@NotNull(message = "Reference Id is mandatory", groups = { OnSale.class })
	private Long referenceId;

	@NotNull(message = "Store Num Id is mandatory", groups = { OnSale.class })
	private Long storeNumId;

	@Valid
	@NotEmpty(message = "Items are mandatory.", groups = { OnSale.class})
	private List<@Valid ExternalSoldItemDTO> items;

	public InventorySalesDTO toInventorySalesDTO(Long storeLocationIdForSales) {
		return new InventorySalesDTO(
				this.referenceId,
				this.items.stream()
						.map(externalItem -> externalItem.toItem(storeLocationIdForSales))
						.collect(Collectors.toUnmodifiableList()));
	}
}
