package com.ust.retail.store.pim.dto.inventory.reception.screens;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResumeResponseDTO;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PoOrderMasterDTO  {

	private Long vendorMasterId;
	private String vendorName;
	private String receiver;
	private Long storeNum;
	
	private String poNumber;
	
	private Long purchaseOrderId;
	private Date poArrivalDate;

	private Integer totalCases;
	private Integer totalPallets;
	private Integer totalUnits;

	private Long poStatusId;
	private String poStatusDesc;

	private List<OrderDetailsDTO> orderDetails;
	private List<Long> purchaseOrderDetailIds;

	public PoOrderMasterDTO(PurchaseOrderModel po, String storeAssociateName) {
		purchaseOrderId = po.getPurchaseOrderId();
		vendorMasterId = po.getVendorMaster().getVendorMasterId();
		vendorName = po.getVendorMaster().getVendorName();
		poNumber = po.getPurchaseOrderNum();
		receiver = storeAssociateName;
		storeNum = po.getStoreNumber().getStoreNumId();
		poStatusId = po.getStatus().getCatalogId();
		poStatusDesc = po.getStatus().getCatalogOptions();

		orderDetails = new ArrayList<OrderDetailsDTO>();
		purchaseOrderDetailIds = new ArrayList<Long>();
		totalCases = 0;
		totalPallets = 0;
		totalUnits = 0;

		for (PurchaseOrderDetailModel currentPODetail : po.getDetails()) {

			totalCases += currentPODetail.getCaseNum();
			totalPallets += currentPODetail.getPalletNum();
			totalUnits += currentPODetail.getTotalAmount();

			orderDetails.add(new OrderDetailsDTO(
					currentPODetail.getPurchaseOrderDetailId(),
					currentPODetail.getItemStatus().getCatalogId(),
					currentPODetail.getUpcMaster().getProductName(),
					currentPODetail.getCaseNum(),
					currentPODetail.getPalletNum(),
					currentPODetail.getUpcMaster().getProductTypeBuyingUnit().getCatalogOptions(),
					currentPODetail.getTotalAmount(),
					currentPODetail.getUpcMaster().getPrincipalUpc()));

			purchaseOrderDetailIds.add(currentPODetail.getPurchaseOrderDetailId());
		}
	}

	public PoOrderMasterDTO addReceptionDetails(ReceivingResumeResponseDTO reception) {
		orderDetails.forEach(orderDetail ->
				reception.getDetails().stream()
						.filter(receptionDetail -> Objects.equals(receptionDetail.getPrincipalUPC(), orderDetail.getPrincipalUPC()))
						.findFirst()
						.ifPresent(orderDetail::setReceptionDetail)
		);
		return this;
	}
}
