package com.ust.retail.store.pim.dto.vendormaster;

import java.util.List;
import java.util.stream.Collectors;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterStoreModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VendorMasterFilterDTO extends BaseFilterDTO {

	private Long vendorMasterId;

	private String vendorName;

	private String vendorCode;

	private Long distributorId;

	private List<StoreNumberDTO> storeNumbers;

	private Long storeNumberId;

	public VendorMasterFilterDTO(Long vendorMasterId, String vendorName, String vendorCode,
			List<VendorMasterStoreModel> stores) {
		super();
		this.vendorMasterId = vendorMasterId;
		this.vendorName = vendorName;
		this.vendorCode = vendorCode;

		if (stores != null) {
			this.storeNumbers = stores.stream()
					.map(currentStore -> new StoreNumberDTO().parseToDTO(currentStore.getPk().getStoreNum()))
					.collect(Collectors.toList());
		}
	}

}
