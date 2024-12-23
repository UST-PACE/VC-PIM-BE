package com.ust.retail.store.pim.dto.catalog;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.DistributorModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DistributorDTO extends BaseFilterDTO {

	@Null(message = "Distributor ID should not be present when creating.", groups = { OnCreate.class })
	@NotNull(message = "Distributir ID is mandatory when updating.", groups = { OnUpdate.class })
	private Long distributorId;
	
	@NotNull(message = "Ditributor Name is mandatory.", groups = { OnCreate.class,OnUpdate.class })
	private String distributorName;
	

	public DistributorModel createModel(Long userId) {
		return new DistributorModel(this.distributorId,this.distributorName,userId);
	}
	
	public DistributorDTO parseToDTO(DistributorModel model) {
	
		this.distributorId = model.getDistributorId();
		this.distributorName = model.getDistributorName();
		
		return this;
	}

	public DistributorDTO(Long distributorId,String distributorName) {
		this.distributorId = distributorId;
		this.distributorName = distributorName;
	}
}
