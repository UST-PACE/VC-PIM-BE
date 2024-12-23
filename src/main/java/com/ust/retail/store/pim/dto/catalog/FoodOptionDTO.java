package com.ust.retail.store.pim.dto.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.model.catalog.FoodOptionModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(Include.NON_EMPTY)
public class FoodOptionDTO extends BaseFilterDTO {

	@Null(message = "Food Option Id should not be present when creating.", groups = { OnCreate.class })
	@NotNull(message = "Food Option Id is mandatory when updating.", groups = { OnUpdate.class })
	private Long foodOptionId;

	@NotNull(message = "Food Option Catalogue Name is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private String foodOptionCatalogueName;

	@Size(min = 2, message = "Food Option Catalogue UPC should have minimum two upcs", groups = { OnCreate.class, OnUpdate.class })
	private List<UpcMasterDTO> foodOptionCatalogueUpcs = new ArrayList<>();
	
	@NotNull(message = "Food Option Status ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long foodOptionStatusId;
	private String foodOptionStatusName;
	
	public FoodOptionDTO(Long  foodOptionId, String foodOptioncatalogueName) {
			this.foodOptionId = foodOptionId;
			this.foodOptionCatalogueName = foodOptioncatalogueName;
	}

	public FoodOptionDTO(FoodOptionModel model) {
		FoodOptionModel nullSafeModel = Optional.ofNullable(model).orElse(new FoodOptionModel());
		this.foodOptionId = nullSafeModel.getFoodOptionId();
		this.foodOptionCatalogueName = nullSafeModel.getFoodOptionCatalogueName();
		this.foodOptionCatalogueUpcs = nullSafeModel.getFoodOptionCatalogueUpcs().stream().map(m -> new UpcMasterDTO().parseToDTO(m))
				.collect(Collectors.toList());
		this.foodOptionStatusId = model.getFoodOptionStatus().getCatalogId();
		this.foodOptionStatusName = model.getFoodOptionStatus().getCatalogOptions();
	}

	public FoodOptionModel createModel(Long userId) {
		return new FoodOptionModel(this.foodOptionCatalogueName, this.foodOptionStatusId, userId, this.foodOptionId);
	}
	
	public FoodOptionDTO parseToDTO(FoodOptionModel model) {
		if (model != null)
			return new FoodOptionDTO(model.getFoodOptionId(), model.getFoodOptionCatalogueName());
		return null;
	}
	public FoodOptionDTO parseToAllDTO(FoodOptionModel model){
		if (model != null)
			return new FoodOptionDTO(model);
		return null;
	}
}
