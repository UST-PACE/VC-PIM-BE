package com.ust.retail.store.pim.dto.report;

import java.util.Date;
import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseOrderDraftedDTO {
	private Long purchaseOrderId;
	private String purchaseOrderNum;
	private String vendorName;
	private Double cost;
	private Double retailPrice;
	private Double margin;
	private String draftedBy;
	private Date draftedOn;

	public PurchaseOrderDraftedDTO(Long purchaseOrderId,
								   String purchaseOrderNum,
								   String vendorName,
								   Double cost,
								   String draftedBy,
								   Date draftedOn) {
		this.purchaseOrderId = purchaseOrderId;
		this.purchaseOrderNum = purchaseOrderNum;
		this.vendorName = vendorName;
		this.cost = cost;
		this.draftedBy = draftedBy;
		this.draftedOn = draftedOn;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
		this.margin = Optional.ofNullable(retailPrice).map(price -> price - this.cost).orElse(0D);
	}
}
