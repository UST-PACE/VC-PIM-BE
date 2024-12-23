package com.ust.retail.store.pim.dto.productreturn.authorization;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class ProductReturnAuthorizationResultDTO {
	@Setter
	private Long statusId;
	private final List<ProductReturnAuthorizationResultLineDTO> results = new ArrayList<>();
}
