package com.ust.retail.store.bistro.dto.kitchen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KitchenExecutionTossDTO {
	private Long recipeId;
	private String recipeName;
	private Integer portions;
}
