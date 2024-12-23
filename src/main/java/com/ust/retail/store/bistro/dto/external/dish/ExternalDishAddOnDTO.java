package com.ust.retail.store.bistro.dto.external.dish;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.bistro.model.recipes.RecipeAddonModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalDishAddOnDTO {
	private String upc;
	private String productName;
	private Integer maxRequests;
	private Double price;
	private Double sellingPrice;

	public ExternalDishAddOnDTO parseToDTO(RecipeAddonModel addOn) {
		this.upc = addOn.getUpcMaster().getPrincipalUpc();
		this.productName = addOn.getUpcMaster().getProductName();
		this.maxRequests = addOn.getMaxRequests();
		this.sellingPrice = addOn.getSellingPrice();
		this.price = addOn.getSellingPrice();

		return this;
	}
}
