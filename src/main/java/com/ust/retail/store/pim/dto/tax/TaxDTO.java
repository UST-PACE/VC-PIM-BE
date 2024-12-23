package com.ust.retail.store.pim.dto.tax;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.tax.TaxModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaxDTO {
	@Null(message = "Tax ID should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "Tax ID is mandatory when updating.", groups = {OnUpdate.class})
	private Long taxId;

	@NotNull(message = "Store Num ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long storeNumId;
	private String storeName;

	@NotNull(message = "Product Group ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long productGroupId;
	private String productGroupName;

	private Long productCategoryId;
	private String productCategoryName;

	private Long productSubcategoryId;
	private String productSubcategoryName;

	@NotNull(message = "Tax Type ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long taxTypeId;
	private String taxTypeName;

	@NotNull(message = "Percentage is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Double percentage;

	private Date createdAt;

	public TaxDTO parseToDTO(TaxModel model) {
		this.taxId = model.getTaxId();
		this.taxTypeId = model.getTaxType().getCatalogId();
		this.taxTypeName = model.getTaxType().getCatalogOptions();
		this.storeNumId = model.getStoreNumber().getStoreNumId();
		this.storeName = model.getStoreNumber().getStoreName();
		this.productGroupId = model.getProductGroup().getProductGroupId();
		this.productGroupName = model.getProductGroup().getProductGroupName();
		Optional.ofNullable(model.getProductCategory()).ifPresent(pc -> {
			this.productCategoryId = pc.getProductCategoryId();
			this.productCategoryName = pc.getProductCategoryName();
		});
		Optional.ofNullable(model.getProductSubcategory()).ifPresent(ps -> {
			this.productSubcategoryId = ps.getProductSubcategoryId();
			this.productSubcategoryName = ps.getProductSubcategoryName();
		});
		this.percentage = model.getPercentage();
		this.createdAt = model.getCreatedAt();
		return this;
	}

	public TaxModel createModel(Long userId) {
		return new TaxModel(this, userId);
	}
}
