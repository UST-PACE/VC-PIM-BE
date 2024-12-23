package com.ust.retail.store.pim.dto.catalog;

import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import com.ust.retail.store.pim.model.catalog.ProductItemModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductItemDTO extends BaseFilterDTO {
	@Null(message = "Product Item ID should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "Product Item ID is mandatory when updating.", groups = {OnUpdate.class})
	private Long productItemId;

	private Long productGroupId;
	private String productGroupName;

	private Long productCategoryId;
	private String productCategoryName;

	@NotNull(message = "Product Subcategory ID is mandatory.", groups = {OnUpdate.class})
	private Long productSubcategoryId;

	private String productSubcategoryName;

	@NotNull(message = "Product Item Name is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private String productItemName;

	private String picture;

	public ProductItemDTO(Long productItemId, Long productGroupId, Long productCategoryId, Long productSubcategoryId, String productSubcategoryName, String productItemName) {
		this.productItemId = productItemId;
		this.productGroupId = productGroupId;
		this.productCategoryId = productCategoryId;
		this.productSubcategoryName = productSubcategoryName;
		this.productSubcategoryId = productSubcategoryId;
		this.productItemName = productItemName;
	}

	public ProductItemDTO parseToDTO(ProductItemModel model) {
		this.productItemId = model.getProductItemId();
		Optional<ProductCategoryModel> productCategoryOpt = Optional.ofNullable(model.getProductSubcategory().getProductCategory());
		if (productCategoryOpt.isPresent()) {
			ProductCategoryModel productCategory = productCategoryOpt.get();
			if (Optional.ofNullable(productCategory.getProductGroup()).isPresent()) {
				this.productGroupId = productCategory.getProductGroup().getProductGroupId();
				this.productGroupName = productCategory.getProductGroup().getProductGroupName();
			}
			this.productCategoryId = productCategory.getProductCategoryId();
			this.productCategoryName = productCategory.getProductCategoryName();
		}
		this.productSubcategoryId = model.getProductSubcategory().getProductSubcategoryId();
		this.productSubcategoryName = model.getProductSubcategory().getProductSubcategoryName();
		this.productItemName = model.getProductItemName();
		this.picture = model.getPictureUrl();
		return this;
	}

	public ProductItemModel createModel(Long userId) {
		return new ProductItemModel(this.productItemId, this.productSubcategoryId, this.productItemName, userId);
	}
}
