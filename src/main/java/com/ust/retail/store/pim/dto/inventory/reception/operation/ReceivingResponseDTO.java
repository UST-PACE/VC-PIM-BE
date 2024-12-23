package com.ust.retail.store.pim.dto.inventory.reception.operation;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceivingResponseDTO {

	private Long purchaseOrderDetailId;
	private Long poReceiveDetailId;
	
	
}
