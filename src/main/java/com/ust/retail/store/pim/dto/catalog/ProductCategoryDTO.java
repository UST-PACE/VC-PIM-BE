package com.ust.retail.store.pim.dto.catalog;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductCategoryDTO extends BaseFilterDTO {
	@Null(message = "Product Category ID should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "Product Category ID is mandatory when updating.", groups = {OnUpdate.class})
	private Long productCategoryId;

	@NotNull(message = "Product Group ID is mandatory.", groups = {OnUpdate.class})
	private Long productGroupId;

	private String productGroupName;

	@NotNull(message = "Product Category Name is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private String productCategoryName;

	private String picture;

	public ProductCategoryDTO(Long productCategoryId, Long productGroupId, String productGroupName, String productCategoryName) {
		this.productCategoryId = productCategoryId;
		this.productGroupId = productGroupId;
		this.productGroupName = productGroupName;
		this.productCategoryName = productCategoryName;
	}

	public ProductCategoryDTO parseToDTO(ProductCategoryModel model) {
		this.productCategoryId = model.getProductCategoryId();
		this.productGroupId = model.getProductGroup().getProductGroupId();
		this.productGroupName = model.getProductGroup().getProductGroupName();
		this.productCategoryName = model.getProductCategoryName();
		this.picture = model.getPictureUrl();
		return this;
	}

	public ProductCategoryModel createModel(Long userId) {
		return new ProductCategoryModel(this.productCategoryId, this.productGroupId, this.productCategoryName, userId);
	}
}
