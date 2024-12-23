package com.ust.retail.store.pim.dto.purchaseorder.operation;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class PurchaseOrderModifyLineItemResultDTO {
	@Setter
	private Long purchaseOrderId;
	private Long vendorMasterId;
	private String vendorName;
	private Long storeNumId;
	private String storeName;
	private String statusName;
	private String purchaseOrderNum;
	private Double totalCost;
	private Double discount;
	private Double finalCost;
	private boolean orderUnderMoq;
	private Date suggestedEta;
	@JsonIgnore
	private boolean revisionUpdated;
	@JsonIgnore
	private String previousPurchaseOrderNum;
	@JsonIgnore
	private boolean vendorCreditModified;
	@JsonIgnore
	private Double creditAmount;
	private boolean escalationWarning;

	private final Set<LineItemError> errors = new HashSet<>();

	public PurchaseOrderModifyLineItemResultDTO parseToDTO(PurchaseOrderModel model) {
		this.purchaseOrderId = model.getPurchaseOrderId();
		this.vendorMasterId = model.getVendorMaster().getVendorMasterId();
		this.vendorName = model.getVendorMaster().getVendorName();
		this.storeNumId = model.getStoreNumber().getStoreNumId();
		this.storeName = model.getStoreNumber().getStoreName();
		this.statusName = model.getStatus().getCatalogOptions();
		this.purchaseOrderNum = model.getPurchaseOrderNum();
		this.totalCost = model.getTotalCost();
		this.discount = model.getDiscount();
		this.finalCost = model.getFinalCost();
		this.suggestedEta = model.getSuggestedEta();
		this.orderUnderMoq = model.isOrderUnderMoq();
		return this;
	}

	public PurchaseOrderModifyLineItemResultDTO setRevisionUpdated(boolean revisionUpdated, String currentPurchaseOrderNum) {
		this.revisionUpdated = revisionUpdated;
		previousPurchaseOrderNum = currentPurchaseOrderNum;
		return this;
	}

	public PurchaseOrderModifyLineItemResultDTO setVendorCreditModified(boolean vendorCreditModified, Double creditAmount) {
		this.vendorCreditModified = vendorCreditModified;
		this.creditAmount = creditAmount;
		return this;
	}

	public PurchaseOrderModifyLineItemResultDTO setEscalationWarning(boolean escalationWarning) {
		this.escalationWarning = escalationWarning;
		return this;
	}

	@AllArgsConstructor
	@Getter
	@EqualsAndHashCode
	public static class LineItemError {
		private String upc;
		private String errorCode;
	}
}
