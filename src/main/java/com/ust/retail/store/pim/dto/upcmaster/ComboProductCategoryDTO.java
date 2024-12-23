package com.ust.retail.store.pim.dto.upcmaster;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.upcmaster.ComboProductCategoryModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class ComboProductCategoryDTO {

	@NotNull(message = "ProductCategory ID is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long productCategoryId;

	private String productCategoryName;

	@NotNull(message = "Quantity is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Min(value = 1, message = "Quantity must be 1", groups = { OnCreate.class, OnUpdate.class })
	private Integer quantity;

	public ComboProductCategoryDTO(ComboProductCategoryModel model) {
		this.productCategoryId = model.getProductCategory().getProductCategoryId();
		this.productCategoryName = model.getProductCategory().getProductCategoryName();
		this.quantity = model.getQuantity();
	}

}
