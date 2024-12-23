package com.ust.retail.store.pim.dto.catalog;

import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import com.ust.retail.store.pim.model.catalog.ProductSubcategoryModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductSubcategoryDTO extends BaseFilterDTO {
	@Null(message = "Product Subcategory ID should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "Product Subcategory ID is mandatory when updating.", groups = {OnUpdate.class})
	private Long productSubcategoryId;

	private Long productGroupId;
	private String productGroupName;

	@NotNull(message = "Product Category ID is mandatory.", groups = {OnUpdate.class})
	private Long productCategoryId;

	private String productCategoryName;

	@NotNull(message = "Product Subcategory Name is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private String productSubcategoryName;

	private String picture;

	public ProductSubcategoryDTO(Long productSubcategoryId, Long productGroupId, Long productCategoryId, String productCategoryName, String productSubcategoryName) {
		this.productSubcategoryId = productSubcategoryId;
		this.productGroupId = productGroupId;
		this.productCategoryId = productCategoryId;
		this.productCategoryName = productCategoryName;
		this.productSubcategoryName = productSubcategoryName;
	}

	public ProductSubcategoryDTO parseToDTO(ProductSubcategoryModel model) {
		ProductCategoryModel productCategory = model.getProductCategory();
		this.productSubcategoryId = model.getProductSubcategoryId();
		if (Optional.ofNullable(productCategory.getProductGroup()).isPresent()) {
			this.productGroupId = productCategory.getProductGroup().getProductGroupId();
			this.productGroupName = productCategory.getProductGroup().getProductGroupName();
		}
		this.productCategoryId = productCategory.getProductCategoryId();
		this.productCategoryName = productCategory.getProductCategoryName();
		this.productSubcategoryName = model.getProductSubcategoryName();
		this.picture = model.getPictureUrl();
		return this;
	}

	public ProductSubcategoryModel createModel(Long userId) {
		return new ProductSubcategoryModel(this.productSubcategoryId, this.productCategoryId, this.productSubcategoryName, userId);
	}
}
