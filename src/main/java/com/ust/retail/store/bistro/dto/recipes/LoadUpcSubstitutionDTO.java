package com.ust.retail.store.bistro.dto.recipes;

import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class LoadUpcSubstitutionDTO extends BaseFilterDTO {


	@NotNull(message = "recipeId is Mandatory.", groups = {OnFilter.class})
	private Long recipeId;

	private Long upcMasterId;

	private Long ingredientUnitId;
	private String ingredientUnitDesc;

	@Setter
	private Double costPerUnit;

	public LoadUpcSubstitutionDTO(Long upcMasterId,
								  Double costPerUnit,
								  Long ingredientUnitId,
								  String ingredientUnitDesc) {
		this.upcMasterId = upcMasterId;
		this.costPerUnit = costPerUnit;
		this.ingredientUnitId = ingredientUnitId;
		this.ingredientUnitDesc = ingredientUnitDesc;
	}
}
