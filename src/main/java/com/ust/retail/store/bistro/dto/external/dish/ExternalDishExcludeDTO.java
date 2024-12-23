package com.ust.retail.store.bistro.dto.external.dish;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.bistro.model.recipes.RecipeDetailModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalDishExcludeDTO {
	private Long productId;
	private String productSku;
	private String productName;
	private String productDisplayName;
	private List<ExternalDishSubstitutionDTO> substitutions;

	public ExternalDishExcludeDTO parseToDTO(RecipeDetailModel ingredient) {
		this.productId = ingredient.getUpcMaster().getUpcMasterId();
		this.productSku = ingredient.getUpcMaster().getPrincipalUpc();
		this.productName = ingredient.getUpcMaster().getProductName();
		this.productDisplayName = ingredient.getExcludeName();

		this.substitutions = ingredient.getSubstitutions().stream()
				.map(s -> new ExternalDishSubstitutionDTO().parseToDTO(s))
				.collect(Collectors.toUnmodifiableList());
		return this;
	}
}
