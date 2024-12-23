package com.ust.retail.store.pim.dto.external.report.sale;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExternalInventoryReportRequest {
	private Long storeNumberId;
	private List<String> upcList;
}
