package com.ust.retail.store.pim.dto.upcmaster;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.upcmaster.ComboModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ComboDTO extends BaseFilterDTO {

	@Null(message = "Combo Id should not be present when creating.", groups = { OnCreate.class })
	@NotNull(message = "Combo Id is mandatory when updating.", groups = { OnUpdate.class })
	private Long comboId;

	@NotNull(message = "Combo Name is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 3, max = 50, message = "Combo Name must have between 3 and 50 characters", groups = { OnCreate.class,
			OnUpdate.class })
	private String comboName;

	@NotNull(message = "Principal UPC is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private String principalUPC;

	@NotNull(message = "Combo Status ID is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long comboStatusId;

	private String comboStatus;

	private Double taxPercentage;

	private Boolean taxPercentageActive;

	@NotNull(message = "Price is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Min(value = 1, message = "Combo price should not be less than  1", groups = { OnCreate.class, OnUpdate.class })
	private Double price;

	@NotNull(message = "Combo Categories are mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 1, message = "Combo categories should have min one category.")
	Set<ComboProductCategoryDTO> comboCategories;

	@JsonIgnore
	private Long createdUserId;

	public ComboDTO(ComboModel model) {
		this.comboId = model.getComboId();
		this.comboName = model.getComboName();
		this.principalUPC = model.getPrincipalUpc();
		this.comboStatusId = model.getComboStatus().getCatalogId();
		this.comboStatus = model.getComboStatus().getCatalogOptions();
		this.taxPercentage = model.getTaxPercentage();
		this.price = model.getPrice();
		this.taxPercentageActive = model.getTaxPercentageActive();
		this.comboCategories = model.getComboCategories().stream().map(cp -> new ComboProductCategoryDTO(cp))
				.collect(Collectors.toUnmodifiableSet());
	}
	
	public ComboDTO(Long comboId, String comboName) {
		this.comboId = comboId;
		this.comboName = comboName;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	@JsonIgnore
	public Set<Long> getProductCategoryIds() {
		return comboCategories.stream().map(ComboProductCategoryDTO::getProductCategoryId).collect(Collectors.toSet());
	}

}
