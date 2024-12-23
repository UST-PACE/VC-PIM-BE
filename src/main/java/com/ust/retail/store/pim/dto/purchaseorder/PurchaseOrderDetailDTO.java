package com.ust.retail.store.pim.dto.purchaseorder;

import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseDTO;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseOrderDetailDTO extends BaseDTO {
	@Null(message = "Purchase Order Detail ID should not be present when creating.", groups = OnCreate.class)
	@NotNull(message = "Purchase Order Detail ID is mandatory when updating.", groups = OnUpdate.class)
	private Long purchaseOrderDetailId;

	@Null(message = "Purchase Order ID should not be present when creating.", groups = OnCreate.class)
	@NotNull(message = "Purchase Order ID is mandatory when updating.", groups = OnUpdate.class)
	private Long purchaseOrderId;

	@NotNull(message = "Product Number ID is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long upcMasterId;

	private String productName;
	private String principalUpc;
	private String vendorSku;
	private Integer productShelfLife;
	private String productType;
	private String productUnit;
	private Double productCost;

	@NotNull(message = "# Cases is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Integer caseNum;

	private Integer unitsPerCase;

	@NotNull(message = "# Pallets is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Integer palletNum;

	private Integer unitsPerPallet;

	@NotNull(message = "Total Qty is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Integer totalAmount;

	private Double originalCost;
	private Double discount;
	private Double finalCost;
	private Double productMoq;
	private boolean lineItemUnderMoq;
	private Double promotionQty;
	private Long promotionTypeId;
	private Long itemStatusId;

	public PurchaseOrderDetailDTO(
			Long purchaseOrderDetailId,
			Long purchaseOrderId,
			Long upcMasterId,
			String productName,
			String principalUpc,
			String vendorSku,
			Integer productShelfLife,
			String productType,
			String productUnit,
			Double productCost,
			Integer caseNum,
			Integer unitsPerCase,
			Integer palletNum,
			Integer unitsPerPallet,
			Integer totalAmount,
			Double originalCost,
			Double discount,
			Double finalCost,
			Double productMoq,
			boolean lineItemUnderMoq) {
		this.purchaseOrderDetailId = purchaseOrderDetailId;
		this.purchaseOrderId = purchaseOrderId;
		this.upcMasterId = upcMasterId;
		this.productName = productName;
		this.principalUpc = principalUpc;
		this.vendorSku = vendorSku;
		this.productShelfLife = productShelfLife;
		this.productType = productType;
		this.productUnit = productUnit;
		this.productCost = productCost;
		this.caseNum = caseNum;
		this.unitsPerCase = unitsPerCase;
		this.palletNum = palletNum;
		this.unitsPerPallet = unitsPerPallet;
		this.totalAmount = totalAmount;
		this.originalCost = originalCost;
		this.discount = discount;
		this.finalCost = finalCost;
		this.productMoq = productMoq;
		this.lineItemUnderMoq = lineItemUnderMoq;
	}

	public PurchaseOrderDetailDTO parseToDTO(PurchaseOrderDetailModel detail) {
		UpcMasterModel product = detail.getUpcMaster();
		PurchaseOrderDetailDTO purchaseOrderDetailDTO = new PurchaseOrderDetailDTO(
				detail.getPurchaseOrderDetailId(),
				detail.getPurchaseOrder().getPurchaseOrderId(),
				product.getUpcMasterId(),
				product.getProductName(),
				product.getPrincipalUpc(),
				detail.getVendorSku(),
				product.getShelfLifeWh(),
				product.getProductType().getCatalogOptions(),
				product.getProductTypeBuyingUnit().getCatalogOptions(),
				detail.getProductCost(),
				detail.getCaseNum(),
				detail.getUnitsPerCase(),
				detail.getPalletNum(),
				detail.getUnitsPerPallet(),
				detail.getTotalAmount(),
				detail.getOriginalCost(),
				detail.getDiscount(),
				detail.getFinalCost(),
				detail.getProductMoq(),
				detail.isLineItemUnderMoq());

		Optional.ofNullable(detail.getPromotionType())
				.ifPresent(type -> {
					purchaseOrderDetailDTO.promotionTypeId = type.getCatalogId();
					purchaseOrderDetailDTO.promotionQty = detail.getPromotionQty();
				});

		Optional.ofNullable(detail.getItemStatus())
				.ifPresent(status -> purchaseOrderDetailDTO.itemStatusId = status.getCatalogId());

		return purchaseOrderDetailDTO;
	}

}
