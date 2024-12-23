package com.ust.retail.store.pim.dto.purchaseorder.operation;

import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseOrderFindDetailsByIdRequestDTO extends BaseFilterDTO {
	@NotNull(message = "Purchase Order ID is mandatory.", groups = {OnFilter.class})
	private Long purchaseOrderId;
}
