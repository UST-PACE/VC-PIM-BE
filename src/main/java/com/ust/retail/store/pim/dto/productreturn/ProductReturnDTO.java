package com.ust.retail.store.pim.dto.productreturn;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.ust.retail.store.pim.model.inventory.InventoryProductReturnDetailModel;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class ProductReturnDTO {
	private Long returnId;
	private Long vendorMasterId;
	private Date returnDate;
	private Long statusId;
	private String status;
	private Double totalCredit;
	@Setter
	private List<ProductReturnDetailDTO> details;

	public ProductReturnDTO parseToDTO(InventoryProductReturnModel m) {
		this.returnId = m.getInventoryProductReturnId();
		this.returnDate = m.getFinishAt();
		this.statusId = m.getStatus().getCatalogId();
		this.status = m.getStatus().getCatalogOptions();

		this.totalCredit = m.getReturnDetails().stream()
				.map(InventoryProductReturnDetailModel::getCredit)
				.filter(Objects::nonNull)
				.reduce(0d, Double::sum);
		long vendorCount = m.getReturnDetails().stream()
				.map(InventoryProductReturnDetailModel::getVendorMaster)
				.filter(Objects::nonNull)
				.distinct()
				.count();

		if (vendorCount == 1) {
			this.vendorMasterId = m.getReturnDetails().stream()
					.map(InventoryProductReturnDetailModel::getVendorMaster)
					.filter(Objects::nonNull)
					.map(VendorMasterModel::getVendorMasterId)
					.findFirst()
					.orElse(null); // Should not happen
		}

		return this;
	}
}
