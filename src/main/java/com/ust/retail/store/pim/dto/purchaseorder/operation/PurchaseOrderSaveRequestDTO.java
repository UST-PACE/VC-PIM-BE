package com.ust.retail.store.pim.dto.purchaseorder.operation;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ust.retail.store.pim.common.bases.BaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseOrderSaveRequestDTO extends BaseDTO {
	@NotNull(message = "Purchase Order ID is mandatory.")
	private Long purchaseOrderId;

	@NotNull(message = "Store Number ID is mandatory.")
	private Long storeNumId;

	@NotNull(message = "Applied Vendor Credit is mandatory.")
	private Double appliedVendorCredit;

	@NotNull(message = "ETA is mandatory.")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date eta;

	private String shippingOptions;

	private String notes;
}
