package com.ust.retail.store.pim.dto.report;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PurchaseOrderReceptionDTO {
	private String upc;
	private String productName;
	private String vendorName;
	private Double cost;
	private Double retailPrice;
	private Double margin;
	private Double qty;
	private String receivedBy;
	private Date receivedDate;
	private String status;
}
