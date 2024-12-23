package com.ust.retail.store.pim.dto.inventory.reception.screens;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseOrdersDTO {
	
	private Long purchaseOrderId;
	private String poNumber;
	private String vendorName;
	
	@JsonFormat(pattern = "yyyy/MM/dd")
	private Date poArrivalDate;
	private Long poStatusId;
	private String poStatusDesc;
	
}
