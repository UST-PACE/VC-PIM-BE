package com.ust.retail.store.pim.dto.purchaseorder.operation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class PurchaseOrderFilterResultDTO {
	private Long purchaseOrderId;
	private String purchaseOrderNum;
	private String creator;
	private Long statusId;
	private String statusName;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date creationDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date sentDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date eta;
	private Double finalCost;
	private Double appliedVendorCredit;

	public PurchaseOrderFilterResultDTO(Long purchaseOrderId, String purchaseOrderNum) {
		this.purchaseOrderId = purchaseOrderId;
		this.purchaseOrderNum = purchaseOrderNum;
	}
}
