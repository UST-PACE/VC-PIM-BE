package com.ust.retail.store.pim.dto.catalog;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.BrandOwnerModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BrandOwnerDTO extends BaseFilterDTO {

	@Null(message = "Brand Owner ID should not be present when creating.", groups = { OnCreate.class })
	@NotNull(message = "Brand Owner ID is mandatory when updating.", groups = { OnUpdate.class })
	private Long brandOwnerId;
	
	@NotNull(message = "Brand Owner Name is mandatory.", groups = { OnCreate.class,OnUpdate.class })
	private String brandOwnerName;
	

	public BrandOwnerModel createModel(Long userId) {
		return new BrandOwnerModel(this.brandOwnerId,this.brandOwnerName,userId);
	}
	
	public BrandOwnerDTO parseToDTO(BrandOwnerModel model) {
	
		this.brandOwnerId = model.getBrandOwnerId();
		this.brandOwnerName = model.getBrandOwnerName();
		
		return this;
	}

	public BrandOwnerDTO(Long brandOwnerId,String brandOwnerName) {
		this.brandOwnerId = brandOwnerId;
		this.brandOwnerName = brandOwnerName;
	}
}
