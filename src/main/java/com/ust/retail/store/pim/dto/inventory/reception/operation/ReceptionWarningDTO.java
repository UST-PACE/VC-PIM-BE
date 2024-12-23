package com.ust.retail.store.pim.dto.inventory.reception.operation;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceptionWarningDTO {
	
	private Long warningId;
	private String warningDesc;
	private Double qty;

}
