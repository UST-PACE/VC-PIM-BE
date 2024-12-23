package com.ust.retail.store.pim.dto.menuconfigurator;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorProductModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class MenuConfiguratorProductDTO {

	@NotNull(message = "menu product id is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long upcMasterId;

	private String principalUpc;
	
	private String productName;

	private Long categoryId;

	private String categoryName;
	
	private boolean vcEnabled;

	public MenuConfiguratorProductDTO(MenuConfiguratorProductModel model) {
		this.upcMasterId = model.getMenuConfiguratorProduct().getUpcMasterId();
		this.principalUpc = model.getMenuConfiguratorProduct().getPrincipalUpc();
		this.productName = model.getMenuConfiguratorProduct().getProductName();
		this.categoryId = model.getMenuConfiguratorProduct().getProductCategory().getProductCategoryId();
		this.categoryName = model.getMenuConfiguratorProduct().getProductCategory().getProductCategoryName();
		this.vcEnabled = model.isVcEnabled();
	}

}
