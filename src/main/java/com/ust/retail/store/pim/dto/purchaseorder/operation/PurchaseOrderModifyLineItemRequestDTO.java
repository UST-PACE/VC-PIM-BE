package com.ust.retail.store.pim.dto.purchaseorder.operation;

import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.bases.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PurchaseOrderModifyLineItemRequestDTO extends BaseDTO {
	@NotNull(message = "Purchase Order Detail ID is mandatory.")
	private Long purchaseOrderDetailId;

	@NotNull(message = "Number of Cases is mandatory.")
	private Integer caseNum;

	@NotNull(message = "Number of Pallets is mandatory.")
	private Integer palletNum;

	@NotNull(message = "Total Amount is mandatory.")
	private Integer totalAmount;
}
