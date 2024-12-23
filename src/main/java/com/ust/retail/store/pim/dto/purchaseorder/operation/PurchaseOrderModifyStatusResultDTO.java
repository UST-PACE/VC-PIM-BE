package com.ust.retail.store.pim.dto.purchaseorder.operation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ust.retail.store.pim.common.bases.BaseDTO;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseOrderModifyStatusResultDTO extends BaseDTO {
	private Long purchaseOrderId;
	private String purchaseOrderNum;
	private String statusName;
	private Date eta;
	private Long vendorMasterId;
	private Long storeNumId;
	@JsonIgnore
	private boolean revisionUpdated;
	@JsonIgnore
	private boolean toAuthorize;
	@JsonIgnore
	private String previousPurchaseOrderNum;
	@JsonIgnore
	private boolean vendorCreditModified;
	@JsonIgnore
	private Double creditAmount;
	@JsonIgnore
	private Double orderCost;

	public PurchaseOrderModifyStatusResultDTO parseToDTO(PurchaseOrderModel model) {
		this.purchaseOrderId = model.getPurchaseOrderId();
		this.purchaseOrderNum = model.getPurchaseOrderNum();
		this.statusName = model.getStatus().getCatalogOptions();
		this.eta = model.getEta();
		this.vendorMasterId = model.getVendorMaster().getVendorMasterId();
		this.storeNumId = model.getStoreNumber().getStoreNumId();
		this.orderCost = model.getFinalCost();
		return this;
	}

	public PurchaseOrderModifyStatusResultDTO setRevisionUpdated(boolean revisionUpdated, String currentPurchaseOrderNum) {
		this.revisionUpdated = revisionUpdated;
		previousPurchaseOrderNum = currentPurchaseOrderNum;
		return this;
	}

	public PurchaseOrderModifyStatusResultDTO setVendorCreditModified(boolean vendorCreditModified, Double creditAmount) {
		this.vendorCreditModified = vendorCreditModified;
		this.creditAmount = creditAmount;
		return this;
	}

	public PurchaseOrderModifyStatusResultDTO setToAuthorize(boolean toAuthorize) {
		this.toAuthorize = toAuthorize;
		return this;
	}
}
