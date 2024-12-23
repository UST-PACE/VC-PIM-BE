package com.ust.retail.store.pim.dto.productreturn.authorization;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnAuthorize;
import com.ust.retail.store.pim.common.annotations.OnRecover;
import com.ust.retail.store.pim.common.annotations.OnReject;
import com.ust.retail.store.pim.dto.productreturn.CreditInfoDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductReturnAuthorizationLineDTO {
	
	@NotNull(message = "History ID is mandatory.", groups = {OnAuthorize.class, OnReject.class})
	private Long inventoryHistoryId;
	
	@NotNull(message = "Return Detail ID is mandatory.", groups = {OnAuthorize.class, OnReject.class})
	private Long returnDetailId;
	
	@NotNull(message = "Vendor Master ID is mandatory.", groups = {OnRecover.class})
	private Long vendorMasterId;
	
	@Valid
	@NotNull(message = "creditInfo is mandatory.", groups = {OnAuthorize.class})
	private CreditInfoDTO creditInfo;
}
