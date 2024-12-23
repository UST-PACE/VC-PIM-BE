package com.ust.retail.store.bistro.dto.recipes;

import com.ust.retail.store.bistro.model.recipes.RecipeAddonModel;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoadUpcAddOnDTO extends BaseFilterDTO {


	@NotNull(message = "recipeAddOnId is Mandatory.", groups = { OnFilter.class })
	private Long recipeId;

	/*FOR RETURN ON SCREEN*/
	private Long recipeAddonId;
	private Long upcMasterId;
	private String productName;
	@Setter
	private Double costPerUnit;
	private String saleUnitDesc;
	private Double sellingPrice;
	private Integer maxRequests;
	private Double qty;
	
	public LoadUpcAddOnDTO(Long upcMasterId, String productName, Double costPerUnit, String saleUnitDesc) {
		this.upcMasterId = upcMasterId;
		this.productName = productName;
		this.costPerUnit = costPerUnit;
		this.saleUnitDesc = saleUnitDesc;
		
	}

	public LoadUpcAddOnDTO parseToDto(RecipeAddonModel m) {
		this.recipeId = m.getRecipe().getRecipeId();
		this.recipeAddonId = m.getRecipeAddonId();
		this.upcMasterId = m.getUpcMaster().getUpcMasterId();
		this.productName = m.getUpcMaster().getProductName();
		this.saleUnitDesc = m.getUpcMaster().getContentPerUnitUom().getCatalogOptions();
		this.sellingPrice = m.getSellingPrice();
		this.maxRequests = m.getMaxRequests();
		this.qty = m.getQty();

		return this;
	}
}
