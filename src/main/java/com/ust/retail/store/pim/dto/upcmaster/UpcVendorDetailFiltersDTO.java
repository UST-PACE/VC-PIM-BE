package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpcVendorDetailFiltersDTO extends BaseFilterDTO {

	private Long upcVendorDetailId;

	private String code;

	private String vendorName;

	private String productName;

	private String principalUpc;

	private String sku;

	private String caseUpc;

	private String countryOfOriginName;

	public UpcVendorDetailFiltersDTO parseToDTO(UpcVendorDetailsModel model) {
		this.upcVendorDetailId = model.getUpcVendorDetailId();
		this.vendorName = model.getVendorMaster().getVendorName();
		this.productName = model.getUpcMaster().getProductName();
		this.principalUpc = model.getUpcMaster().getPrincipalUpc();
		this.sku = model.getUpcMaster().getSku();
		this.caseUpc = model.getCaseUpc();
		this.countryOfOriginName = model.getCountryOfOrigin().getCatalogOptions();
		return this;
	}
	
}

