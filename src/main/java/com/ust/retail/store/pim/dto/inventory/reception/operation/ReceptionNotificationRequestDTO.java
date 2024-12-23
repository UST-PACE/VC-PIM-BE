package com.ust.retail.store.pim.dto.inventory.reception.operation;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnNotifyReception;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceptionNotificationRequestDTO {
	@NotNull(message = "Purchase OrderDetail Id is Mandatory.", groups = { OnNotifyReception.class})
	private Long purchaseOrderId;

	private String driverName;
	private String driverEmail;
}
