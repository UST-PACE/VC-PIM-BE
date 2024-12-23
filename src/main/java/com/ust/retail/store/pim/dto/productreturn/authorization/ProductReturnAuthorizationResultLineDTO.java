package com.ust.retail.store.pim.dto.productreturn.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductReturnAuthorizationResultLineDTO {
	private Long historyId;
	private Long returnDetailId;
	private boolean success;
	private String message;
}
