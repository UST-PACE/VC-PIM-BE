package com.ust.retail.store.pim.dto.external.report.sale;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExternalInventoryReportDTO {
	@JsonIgnore
	private Long upcMasterId;
	private String upc;
	private Double onHandInventory;
	private Double averageCost;

	public void updateAverageCost(Double averageCost) {
		this.averageCost = averageCost;
	}
}
