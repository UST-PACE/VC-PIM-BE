package com.ust.retail.store.pim.dto.productreturn;

import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnAuthorize;
import com.ust.retail.store.pim.common.annotations.OnRecover;

import lombok.Getter;

@Getter
public class CreditInfoDTO {

	@NotNull(message = "vendorMasterId is mandatory when calculating credits.", groups = {OnAuthorize.class, OnRecover.class})
	private Long vendorMasterId;
	
	@NotNull(message = "upcMasterId is mandatory when calculating credits")
	private Long upcMasterId;
	
	@NotNull(message = "returnDetailId is mandatory when calculating credits")
	private Long returnDetailId;
	
	private Double credit;

	public void setCredit(Double credit) {
		this.credit = credit;
	}
	
	
}
