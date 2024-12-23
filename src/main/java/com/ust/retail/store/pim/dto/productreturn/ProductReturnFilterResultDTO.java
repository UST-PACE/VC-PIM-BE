package com.ust.retail.store.pim.dto.productreturn;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ust.retail.store.pim.model.inventory.InventoryProductReturnDetailModel;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductReturnFilterResultDTO {
	private Long returnId;
	private Double qty;
	private String vendors;
	private Date returnDate;
	private String status;
	private Double totalCredit;

	public ProductReturnFilterResultDTO parseToDTO(InventoryProductReturnModel m) {
		this.returnId = m.getInventoryProductReturnId();
		this.status = m.getStatus().getCatalogOptions();
		this.returnDate = m.getFinishAt();
		
		this.qty = m.getReturnDetails().stream()
				.map(InventoryProductReturnDetailModel::getQty)
				.reduce(0d, Double::sum);

		this.vendors = m.getReturnDetails().stream()
				.map(InventoryProductReturnDetailModel::getVendorMaster)
				.filter(Objects::nonNull)
				.map(VendorMasterModel::getVendorName)
				.distinct()
				.collect(Collectors.joining(", "));
		
		
			this.totalCredit = m.getReturnDetails().stream()
					.map(InventoryProductReturnDetailModel::getCredit)
					.filter(Objects::nonNull)
					.reduce(0d, Double::sum);

		if (this.vendors.isEmpty()) {
			this.vendors = "Not Assigned";
		}

		return this;
	}
}
