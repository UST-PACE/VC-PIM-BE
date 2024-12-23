package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.model.upcmaster.UpcVendorStoreCostModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpcVendorStoreCostDTO {
	private Long upcVendorCostId;
	private Long storeNumId;
	private String storeName;
	private Double supplierCost;
	private Double salePrice;

	public UpcVendorStoreCostDTO parseToDTO(UpcVendorStoreCostModel vc) {
		this.upcVendorCostId = vc.getUpcVendorCostId();
		this.storeNumId = vc.getStoreNumber().getStoreNumId();
		this.storeName = vc.getStoreNumber().getStoreName();
		this.supplierCost = vc.getCost();
		this.salePrice = vc.getUpcVendorDetail().getUpcMaster().getSalePrice(this.storeNumId);

		return this;
	}

/*
	public UpcVendorStoreCostDTO parseToDTO(StoreNumberModel st, Double salePrice) {
		this.storeNumId = st.getStoreNumId();
		this.storeName = st.getStoreName();
		this.salePrice = salePrice;

		return this;
	}

*/
	public UpcVendorStoreCostModel createModel() {
		return new UpcVendorStoreCostModel(upcVendorCostId, storeNumId, supplierCost);
	}
}
