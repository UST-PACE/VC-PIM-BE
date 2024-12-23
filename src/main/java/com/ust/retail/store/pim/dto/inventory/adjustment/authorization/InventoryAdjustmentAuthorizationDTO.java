package com.ust.retail.store.pim.dto.inventory.adjustment.authorization;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentDetailModel;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentModel;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentShrinkageModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InventoryAdjustmentAuthorizationDTO {
	private Long adjustmentId;
	private Date adjustmentStartTime;
	private Date adjustmentEndTime;
	private Long storeId;
	private String storeName;
	private Long storeLocationId;
	private String storeLocationName;
	private Long statusId;
	private String status;
	private Double totalSetQty;
	private Double totalShrinkageQty;
	private List<InventoryAdjustmentAuthorizationDetailDTO> details;

	public InventoryAdjustmentAuthorizationDTO parseToDTO(InventoryAdjustmentModel m) {
		this.adjustmentId = m.getInventoryAdjustmentId();
		this.adjustmentStartTime = m.getStartTime();
		this.adjustmentEndTime = m.getEndTime();
		this.storeId = m.getStoreLocation().getStoreNumber().getStoreNumId();
		this.storeName = m.getStoreLocation().getStoreNumber().getStoreName();
		this.storeLocationId = m.getStoreLocation().getStoreLocationId();
		this.storeLocationName = m.getStoreLocation().getStoreLocationName();
		this.statusId = m.getStatus().getCatalogId();
		this.status = m.getStatus().getCatalogOptions();
		this.totalSetQty = m.getDetails().stream()
				.map(InventoryAdjustmentDetailModel::getSetQty)
				.reduce(0d, Double::sum);
		this.totalShrinkageQty = m.getShrinkages().stream()
				.map(InventoryAdjustmentShrinkageModel::getQty)
				.reduce(0d, Double::sum);
		return this;
	}

	public void setDetails(List<InventoryAdjustmentAuthorizationDetailDTO> details) {
		DecimalFormat df = new DecimalFormat("#.#");
		this.details = details.stream()
				.collect(Collectors.groupingBy(InventoryAdjustmentAuthorizationDetailDTO::getAdjustmentDetailId))
				.entrySet().stream()
				.map(entry -> {
					List<InventoryAdjustmentAuthorizationDetailDTO> detailList = entry.getValue();
					InventoryAdjustmentAuthorizationDetailDTO main = detailList.get(0);
					String shrinkage = detailList.stream()
							.filter(detail -> Objects.nonNull(detail.getShrinkageReason()) && Objects.nonNull(detail.getShrinkageQty()))
							.map(detail -> String.format(
									"%s: %s",
									detail.getShrinkageReason(),
									df.format(detail.getShrinkageQty())))
							.collect(Collectors.joining(System.lineSeparator()));
					String shrinkageEvidence = detailList.stream()
							.map(InventoryAdjustmentAuthorizationDetailDTO::getEvidence)
							.filter(Objects::nonNull)
							.findFirst()
							.orElse(null); // Shouldn't happen
					Double shrinkageQty = detailList.stream()
							.map(InventoryAdjustmentAuthorizationDetailDTO::getShrinkageQty)
							.filter(Objects::nonNull)
							.reduce(0d, Double::sum);
					return new InventoryAdjustmentAuthorizationDetailDTO(
							entry.getKey(),
							main.getCategoryName(),
							main.getProductName(),
							main.getPrincipalUpc(),
							main.getProductType(),
							main.getQty(),
							main.getInventoryUnit(),
							shrinkageQty,
							shrinkage,
							shrinkageEvidence,
							main.getStatusId(),
							main.getStatus(),
							main.getTxnNum(),
							main.getInventoryHistoryId());
				}).collect(Collectors.toUnmodifiableList());
	}
}
