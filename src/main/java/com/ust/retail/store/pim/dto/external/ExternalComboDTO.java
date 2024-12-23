package com.ust.retail.store.pim.dto.external;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.model.upcmaster.ComboModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalComboDTO {

	
	private Long comboId;

	private String comboName;

	private String principalUPC;

	private String comboStatus;

	private Double taxPercentage;

	private Double price;

	Set<ExternalComboProductCategoryDTO> comboCategories;
	
	public ExternalComboDTO(ComboModel model) {
		this.comboId = model.getComboId();
		this.comboName = model.getComboName();
		this.principalUPC = model.getPrincipalUpc();
		this.comboStatus = model.getComboStatus().getCatalogOptions();
		this.taxPercentage = model.getTaxPercentage();
		this.price = model.getPrice();
		this.comboCategories = model.getComboCategories().stream().map(cp -> new ExternalComboProductCategoryDTO(cp))
				.collect(Collectors.toUnmodifiableSet());
	}
}
