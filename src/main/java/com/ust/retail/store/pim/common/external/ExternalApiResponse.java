package com.ust.retail.store.pim.common.external;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExternalApiResponse<T> {
	private T response;
	public ExternalApiResponse(T response) {
		this.response = response;
	}
}
