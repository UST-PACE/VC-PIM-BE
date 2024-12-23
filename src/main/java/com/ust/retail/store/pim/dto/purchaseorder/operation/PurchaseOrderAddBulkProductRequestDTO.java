package com.ust.retail.store.pim.dto.purchaseorder.operation;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PurchaseOrderAddBulkProductRequestDTO {
	@Null(message = "Purchase Order ID should not be present when creating.", groups = OnCreate.class)
	@NotNull(message = "Purchase Order ID is mandatory when updating.", groups = OnUpdate.class)
	@Setter
	private Long purchaseOrderId;

	@NotNull(message = "Vendor Master ID is mandatory when creating.", groups = OnCreate.class)
	private Long vendorMasterId;

	@NotNull(message = "Store Number ID is mandatory when creating.", groups = OnCreate.class)
	private Long storeNumId;

	@Valid
	@NotEmpty(message = "At least one product is required.", groups = {OnCreate.class, OnUpdate.class})
	private List<@Valid BulkLineItem> products;

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class BulkLineItem {
		@NotNull(message = "UPC Master ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
		private Long upcMasterId;

		@NotNull(message = "# Cases is mandatory.", groups = {OnCreate.class, OnUpdate.class})
		private Integer caseNum;

		@NotNull(message = "# Pallets is mandatory.", groups = {OnCreate.class, OnUpdate.class})
		private Integer palletNum;

		@NotNull(message = "Total Qty is mandatory.", groups = {OnCreate.class, OnUpdate.class})
		private Integer totalAmount;
	}
}
