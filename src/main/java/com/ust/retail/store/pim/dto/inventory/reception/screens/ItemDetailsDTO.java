package com.ust.retail.store.pim.dto.inventory.reception.screens;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.dto.inventory.UnitMeasurmentsDTO;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.inventory.PoReceiveDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDetailsDTO {

	private boolean loteNumRequired;
	private boolean expirationDateRequired;
	private Long poReceiveDetailId;
	private Long poReceiveDetailStatusId;
	private String poReceiveDetailStatusDesc;

	private OrderDetailsDTO requestedOrder;
	private List<ItemCurrentInventory> itemCurrentInventoryList;

	private UnitMeasurmentsDTO measurments;

	public ItemDetailsDTO(PoReceiveDetailModel receiveDetail, PurchaseOrderDetailModel poDetail, List<InventoryModel> inventoryPerStoreLocation) {

		this.loteNumRequired = poDetail.getUpcMaster().isBatchRequired();
		this.expirationDateRequired = poDetail.getUpcMaster().isExpirationDateRequired();

		this.poReceiveDetailId = receiveDetail != null ? receiveDetail.getPoReceiveDetailId() : null;
		this.poReceiveDetailStatusId = poDetail.getItemStatus().getCatalogId();
		this.poReceiveDetailStatusDesc = poDetail.getItemStatus().getCatalogOptions();

		this.itemCurrentInventoryList = new ArrayList<>();

		for (InventoryModel currentInventory : inventoryPerStoreLocation) {
			itemCurrentInventoryList.add(new ItemCurrentInventory(
					currentInventory.getStoreLocation().getStoreLocationName(), currentInventory.getQty()));
		}

		this.requestedOrder = new OrderDetailsDTO(
				poDetail.getUpcMaster().getUpcMasterId(),
				poDetail.getPurchaseOrderDetailId(),
				poDetail.getItemStatus().getCatalogId(),
				poDetail.getUpcMaster().getProductName(),
				poDetail.getCaseNum(),
				poDetail.getPalletNum(),
				poDetail.getUpcMaster().getProductTypeBuyingUnit().getCatalogOptions(),
				poDetail.getTotalAmount(),
				poDetail.getUpcMaster().getPrincipalUpc());


		this.measurments = new UnitMeasurmentsDTO(poDetail.getUnitsPerCase(), poDetail.getUnitsPerPallet());
		// TODO Expiration dates

	}

}
