package com.ust.retail.store.pim.dto.external.store;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExternalStoreLocationDTO extends BaseFilterDTO {

	private Long storeLocationId;

	private String storeLocationName;


	public ExternalStoreLocationDTO parseToDTO(StoreLocationModel model) {
		this.storeLocationId = model.getStoreLocationId();
		this.storeLocationName = model.getStoreLocationName();

		return this;
	}
}
