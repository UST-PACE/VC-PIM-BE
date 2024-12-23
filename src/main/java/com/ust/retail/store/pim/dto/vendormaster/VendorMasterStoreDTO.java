package com.ust.retail.store.pim.dto.vendormaster;

import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterStoreModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VendorMasterStoreDTO {

	@NotNull(message = "Vendor Master id is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long vendorMasterId;

	@NotNull(message = "Vendor Master store number id is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long storeNumId;

	public VendorMasterStoreModel createModel() {
		return new VendorMasterStoreModel(vendorMasterId, storeNumId);
	}
	public VendorMasterStoreDTO parseToDTO(VendorMasterStoreModel model) {
		this.vendorMasterId = model.getPk().getVendorMaster().getVendorMasterId();
		this.storeNumId = model.getPk().getStoreNum().getStoreNumId();
		
		return this;
	}
}
