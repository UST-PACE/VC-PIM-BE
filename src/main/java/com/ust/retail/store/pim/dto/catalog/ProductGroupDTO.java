package com.ust.retail.store.pim.dto.catalog;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.ProductGroupModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductGroupDTO extends BaseFilterDTO {
	@Null(message = "Product Group ID should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "Product Group ID is mandatory when updating.", groups = {OnUpdate.class})
	private Long productGroupId;

	@NotNull(message = "Product Group Name is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private String productGroupName;

	private String picture;

	private boolean displayExternally;

	public ProductGroupDTO(Long productGroupId, String productGroupName) {
		this.productGroupId = productGroupId;
		this.productGroupName = productGroupName;
	}

	public ProductGroupDTO parseToDTO(ProductGroupModel model) {
		this.productGroupId = model.getProductGroupId();
		this.productGroupName = model.getProductGroupName();
		this.picture = model.getPictureUrl();
		this.displayExternally = model.isDisplayExternally();
		return this;
	}

	public ProductGroupModel createModel(Long userId) {
		return new ProductGroupModel(this.productGroupId, this.productGroupName, this.displayExternally, userId);
	}
}
