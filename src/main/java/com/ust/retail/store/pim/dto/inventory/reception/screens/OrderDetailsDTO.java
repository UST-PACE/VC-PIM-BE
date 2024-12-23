package com.ust.retail.store.pim.dto.inventory.reception.screens;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResumeResponseDetailDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailsDTO {

	private Long upcMasterId;
	private Long purchaseOrderDetailId;
	private Long itemStatusId;
	private String productName;
	private Integer totalCases;
	private Integer totalPallets;
	private String uom;
	private Integer totalItems;
	private String principalUPC;

	@Setter
	private ReceivingResumeResponseDetailDTO receptionDetail;

	public OrderDetailsDTO(Long upcMasterId,
						   Long purchaseOrderDetailId,
						   Long itemStatusId,
						   String productName,
						   Integer totalCases,
						   Integer totalPallets,
						   String uom,
						   Integer totalItems,
						   String principalUPC) {
		this.upcMasterId = upcMasterId;
		this.purchaseOrderDetailId = purchaseOrderDetailId;
		this.itemStatusId = itemStatusId;
		this.productName = productName;
		this.totalCases = totalCases;
		this.totalPallets = totalPallets;
		this.uom = uom;
		this.totalItems = totalItems;
		this.principalUPC = principalUPC;
	}

	public OrderDetailsDTO(Long purchaseOrderDetailId,
						   Long itemStatusId,
						   String productName,
						   Integer totalCases,
						   Integer totalPallets,
						   String uom,
						   Integer totalItems,
						   String principalUPC) {
		super();
		this.purchaseOrderDetailId = purchaseOrderDetailId;
		this.itemStatusId = itemStatusId;
		this.productName = productName;
		this.totalCases = totalCases;
		this.totalPallets = totalPallets;
		this.uom = uom;
		this.totalItems = totalItems;
		this.principalUPC = principalUPC;
	}
}
