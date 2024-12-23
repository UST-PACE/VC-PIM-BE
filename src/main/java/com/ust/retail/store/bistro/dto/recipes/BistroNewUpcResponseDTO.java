package com.ust.retail.store.bistro.dto.recipes;

import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BistroNewUpcResponseDTO {
	
	private BistroUpcMasterDTO upcMaster;
	private RecipeDTO recipe;
	
	public BistroNewUpcResponseDTO(UpcMasterModel upcMaster, RecipeModel recipe) {
		super();
		this.upcMaster = new BistroUpcMasterDTO().parseToDTO(upcMaster);
		this.recipe = new RecipeDTO().parseToDTO(recipe);
	}

}
