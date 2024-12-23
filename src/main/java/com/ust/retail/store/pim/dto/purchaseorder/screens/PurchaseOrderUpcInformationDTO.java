package com.ust.retail.store.pim.dto.purchaseorder.screens;

import java.util.Objects;
import java.util.stream.Stream;

import com.ust.retail.store.pim.common.bases.BaseDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseOrderUpcInformationDTO extends BaseDTO {
	private Long purchaseOrderDetailId;
	private Long purchaseOrderId;
	private Long upcMasterId;
	private String productName;
	private String principalUpc;
	private Integer productShelfLife;
	private String productType;
	private String productUnit;
	private Double onHand;
	private Double productStockMin;
	private Integer unitsPerCase;
	private Integer unitsPerPallet;
	private Double productCost;
	private Double discount;
	private Integer caseNum;
	private Integer palletNum;
	private Integer totalAmount;
	private Double originalCost;
	private Double finalCost;
	private Long promotionTypeId;
	private Double promotionQty;

	public PurchaseOrderUpcInformationDTO(Long purchaseOrderDetailId,
										  Long purchaseOrderId,
										  Long storeNumId,
										  UpcMasterModel product,
										  UpcVendorDetailsModel upcVendorDetailsModel,
										  Double onHand,
										  Double discount) {
		this.purchaseOrderDetailId = purchaseOrderDetailId;
		this.purchaseOrderId = purchaseOrderId;
		this.upcMasterId = product.getUpcMasterId();
		this.productName = product.getProductName();
		this.principalUpc = product.getPrincipalUpc();
		this.productShelfLife = product.getShelfLifeWh();
		this.productType = product.getProductType().getCatalogOptions();
		this.productUnit = product.getProductTypeBuyingUnit().getCatalogOptions();
		this.onHand = onHand;
		this.productStockMin = product.getStockMin();
		this.unitsPerCase = upcVendorDetailsModel.getUnitsPerCase();
		this.unitsPerPallet = Stream.of(upcVendorDetailsModel.getUnitsPerCase(), upcVendorDetailsModel.getCasesPerPallet()).noneMatch(Objects::isNull) ?
				upcVendorDetailsModel.getUnitsPerCase() * upcVendorDetailsModel.getCasesPerPallet() :
				null;
		this.productCost = upcVendorDetailsModel.getProductCost(storeNumId);
		this.discount = discount;
		this.caseNum = 0;
		this.palletNum = 0;
		this.totalAmount = 0;
		this.originalCost = 0.0;
		this.finalCost = 0.0;
	}

	public PurchaseOrderUpcInformationDTO setDiscountInformation(PromotionDTO promotionDTO) {
		this.promotionTypeId = promotionDTO.getPromotionTypeId();
		this.promotionQty = promotionDTO.getDiscount();
		return this;
	}
}
