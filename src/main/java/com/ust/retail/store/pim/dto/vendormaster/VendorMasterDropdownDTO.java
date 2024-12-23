package com.ust.retail.store.pim.dto.vendormaster;

import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VendorMasterDropdownDTO {
	private Long vendorMasterId;

	private String vendorName;

	private String vendorCode;

	public VendorMasterDropdownDTO parseToDTO(VendorMasterModel model) {
		this.vendorMasterId = model.getVendorMasterId();
		this.vendorName = model.getVendorName();
		this.vendorCode = model.getVendorCode();
		return this;
	}
}
