package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpcAdditionalFeeFilterDTO extends BaseFilterDTO {
	private Long upcMasterId;
	private Long storeNumId;
	private Long additionalFeeId;

}
