package com.ust.retail.store.pim.dto.inventory.adjustment.authorization;

import java.util.Date;
import java.util.stream.Collectors;

import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentDetailModel;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InventoryAdjustmentAuthorizationFilterResultDTO {
	private Long adjustmentId;
	private String userName;
	private String categories;
	private Date adjustmentStartDate;
	private Date adjustmentEndDate;
	private Long productCount;
	private String status;

	public InventoryAdjustmentAuthorizationFilterResultDTO parseToDTO(InventoryAdjustmentModel m) {
		this.adjustmentId = m.getInventoryAdjustmentId();
		this.userName = m.getUserCreate().getNameDesc();
		this.adjustmentStartDate = m.getStartTime();
		this.adjustmentEndDate = m.getEndTime();
		this.productCount = (long) m.getDetails().size();
		this.status = m.getStatus().getCatalogOptions();
		this.categories = m.getDetails().stream()
				.map(InventoryAdjustmentDetailModel::getUpcMaster)
				.map(UpcMasterModel::getProductCategory)
				.map(ProductCategoryModel::getProductCategoryName)
				.sorted()
				.distinct()
				.collect(Collectors.joining(", "));
		return this;
	}
}
