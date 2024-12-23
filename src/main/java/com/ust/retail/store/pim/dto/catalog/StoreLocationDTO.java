package com.ust.retail.store.pim.dto.catalog;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StoreLocationDTO extends BaseFilterDTO {

	@Null(message = "Store Location ID should not be present when creating.", groups = { OnCreate.class })
	@NotNull(message = "Store Location ID is mandatory when updating.", groups = { OnUpdate.class })
	private Long storeLocationId;
	
	@NotNull(message = "Store Location Name is mandatory.", groups = { OnCreate.class,OnUpdate.class })
	private String storeLocationName;
	
	@NotNull(message = "Store Number ID should be present when creating or updating.", groups = { OnCreate.class,OnUpdate.class })
	private Long storeNumId;
	
	private String storeNumberName;

	public StoreLocationModel createModel(Long userId) {
		return new StoreLocationModel(this.storeLocationId,this.storeNumId,this.storeLocationName,userId);
	}
	
	public StoreLocationDTO parseToDTO(StoreLocationModel model) {
	
		this.storeLocationId = model.getStoreLocationId();
		this.storeLocationName = model.getStoreLocationName();
		this.storeNumId = model.getStoreNumber().getStoreNumId();
		this.storeNumberName = model.getStoreNumber().getStoreName();
		
		return this;
	}

	public StoreLocationDTO(Long storeLocationId,String storeLocationName,Long storeNumberId,String storeNumberName ) {
		this.storeLocationId = storeLocationId;
		this.storeLocationName = storeLocationName;
		this.storeNumId = storeNumberId;
		this.storeNumberName = storeNumberName;
	}
}
