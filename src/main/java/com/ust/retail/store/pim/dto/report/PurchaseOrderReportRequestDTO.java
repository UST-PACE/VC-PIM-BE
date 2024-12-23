package com.ust.retail.store.pim.dto.report;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseOrderReportRequestDTO extends BaseFilterDTO {
	private Long storeNumId;
}
