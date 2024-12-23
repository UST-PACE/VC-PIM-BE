package com.ust.retail.store.pim.dto.promotion;

import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import com.ust.retail.store.pim.model.catalog.ProductItemModel;
import com.ust.retail.store.pim.model.catalog.ProductSubcategoryModel;
import com.ust.retail.store.pim.model.promotion.PromotionModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.util.deserializers.AmericanFormatDateDeserializer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PromotionDTO extends BaseFilterDTO {
	@Null(message = "Promotion ID should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "Promotion ID is mandatory when updating.", groups = {OnUpdate.class})
	private Long promotionId;

	@NotNull(message = "Brand Owner ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long brandOwnerId;

	@NotNull(message = "Vendor ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long vendorMasterId;

	@NotNull(message = "Product Group ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long productGroupId;

	private Long productCategoryId;

	private Long productSubcategoryId;

	private Long productItemId;

	private Long upcMasterId;

	@NotNull(message = "Promotion Type ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long promotionTypeId;

	@NotNull(message = "Start Date is mandatory.", groups = {OnCreate.class, OnUpdate.class})
//	@JsonDeserialize(using = MultiZoneTimeZoneDeserializer.class)
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date startDate;

	@NotNull(message = "End Date is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date endDate;

	@NotNull(message = "Discount is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Double discount;


	public PromotionDTO parseToDTO(PromotionModel model) {
		this.promotionId = model.getPromotionId();
		this.brandOwnerId = model.getBrandOwner().getBrandOwnerId();
		this.vendorMasterId = model.getVendorMaster().getVendorMasterId();
		this.productGroupId = model.getProductGroup().getProductGroupId();
		this.promotionTypeId = model.getPromotionType().getCatalogId();
		this.productCategoryId = Optional.ofNullable(model.getProductCategory()).map(ProductCategoryModel::getProductCategoryId).orElse(null);
		this.productSubcategoryId = Optional.ofNullable(model.getProductSubcategory()).map(ProductSubcategoryModel::getProductSubcategoryId).orElse(null);
		this.productItemId = Optional.ofNullable(model.getProductItem()).map(ProductItemModel::getProductItemId).orElse(null);
		this.upcMasterId = Optional.ofNullable(model.getUpcMaster()).map(UpcMasterModel::getUpcMasterId).orElse(null);
		this.startDate = model.getStartDate();
		this.endDate = model.getEndDate();
		this.discount = model.getDiscount();
		return this;
	}

	public PromotionModel createModel(Long userId) {
		return new PromotionModel(this, userId);
	}

	public PromotionDTO withBasicInfo(Long promotionTypeId, Double discount) {
		this.promotionTypeId = promotionTypeId;
		this.discount = discount;
		return this;
	}
}
