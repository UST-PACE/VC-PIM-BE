package com.ust.retail.store.pim.dto.report;

import java.util.Date;

import com.ust.retail.store.pim.util.DateUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseOrderOrderedDTO {
	private Long purchaseOrderId;
	private String purchaseOrderNum;
	private String vendorName;
	private Double cost;
	private Double retailPrice;
	private Double margin;
	private Integer daysPending;
	private String draftedBy;
	private Date draftedOn;

	public PurchaseOrderOrderedDTO(Long purchaseOrderId,
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
		this.margin = retailPrice - this.cost;
	}

	public void setDaysPending() {
		this.daysPending = DateUtils.daysBetween(this.draftedOn, new Date());
	}
}
