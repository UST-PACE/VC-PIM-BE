package com.ust.retail.store.pim.dto.productreturn.authorization;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ust.retail.store.pim.common.annotations.OnAuthorize;
import com.ust.retail.store.pim.common.annotations.OnReject;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductReturnAuthorizationDTO {
	@NotNull(message = "Return ID is mandatory.", groups = {OnAuthorize.class, OnReject.class})
	private Long returnId;

	@NotEmpty(message = "Add at list one line.", groups = {OnAuthorize.class, OnReject.class})
	private List<@Valid ProductReturnAuthorizationLineDTO> lines;
}
