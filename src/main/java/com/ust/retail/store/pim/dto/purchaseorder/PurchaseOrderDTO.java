package com.ust.retail.store.pim.dto.purchaseorder;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ust.retail.store.pim.common.bases.BaseDTO;
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;
import com.ust.retail.store.pim.model.vendorcredits.VendorCreditModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PurchaseOrderDTO extends BaseDTO {
	private Long purchaseOrderId;
	private Long vendorMasterId;
	private String vendorMasterName;
	private Long statusId;
	private String statusName;
	private Long storeNumId;
	private String storeName;
	private UserDTO user;
	private String purchaseOrderNum;
	private Double totalCost;
	private Double discount;
	private Double availableVendorCredit;
	private Double appliedVendorCredit;
	private Double finalCost;
	private Date suggestedEta;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date eta;
	private Date sentDate;
	private boolean orderUnderMoq;
	private String shippingOptions;
	private String notes;
	private boolean approvableByUser;
	private Page<PurchaseOrderDetailDTO> detailsPage;
	private List<PurchaseOrderDetailDTO> details;

	public PurchaseOrderDTO parseToDTO(PurchaseOrderModel model) {
		this.purchaseOrderId = model.getPurchaseOrderId();
		this.vendorMasterId = model.getVendorMaster().getVendorMasterId();
		this.vendorMasterName = model.getVendorMaster().getVendorName();
		this.statusId = model.getStatus().getCatalogId();
		this.statusName = model.getStatus().getCatalogOptions();
		this.storeNumId = model.getStoreNumber().getStoreNumId();
		this.storeName = model.getStoreNumber().getStoreName();
		this.user = new UserDTO().parseToDTO(model.getUserCreate());
		this.purchaseOrderNum = model.getPurchaseOrderNum();
		this.totalCost = model.getTotalCost();
		this.discount = model.getDiscount();
		this.availableVendorCredit = Optional.ofNullable(model.getVendorMaster().getVendorCredit())
				.map(VendorCreditModel::getAvailableCredit)
				.orElse(0d);
		this.appliedVendorCredit = model.getAppliedVendorCredit();
		this.finalCost = model.getFinalCost();
		this.suggestedEta = model.getSuggestedEta();
		this.eta = model.getEta();
		this.sentDate = model.getSentAt();
		this.orderUnderMoq = model.isOrderUnderMoq();
		this.shippingOptions = model.getShippingOptions();
		this.notes = model.getNotes();
		return this;
	}

	public PurchaseOrderDTO parseToDTO(PurchaseOrderModel model, List<PurchaseOrderDetailModel> details) {
		this.parseToDTO(model);
		this.details = details.stream().map(m -> new PurchaseOrderDetailDTO().parseToDTO(m)).collect(Collectors.toList());
		return this;
	}

	public void setApprovableByUser(boolean approvableByUser) {
		this.approvableByUser = approvableByUser;
	}
}
