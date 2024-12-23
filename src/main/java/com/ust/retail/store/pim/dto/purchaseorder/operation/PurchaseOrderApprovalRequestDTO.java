package com.ust.retail.store.pim.dto.purchaseorder.operation;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseOrderApprovalRequestDTO {
	@NotNull
	private String username;
	@NotNull
	private String password;

	public PurchaseOrderApprovalRequestDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
