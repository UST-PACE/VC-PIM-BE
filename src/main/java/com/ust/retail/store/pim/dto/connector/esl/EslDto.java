package com.ust.retail.store.pim.dto.connector.esl;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EslDto {

	private String storeCode;
	private String customerStoreCode;
	private String batchNo;

	private List<EslItemDto> items;
}
