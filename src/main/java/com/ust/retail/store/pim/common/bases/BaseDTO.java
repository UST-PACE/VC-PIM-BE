package com.ust.retail.store.pim.common.bases;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;
import java.util.Optional;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDTO {

	protected String photoBase64;

	protected String convertToBase64(byte[] content) {
		this.photoBase64 = Optional.ofNullable(content)
				.map(bytes -> Base64.getEncoder().encode(content))
				.map(String::new)
				.orElse(null);
		return photoBase64;
	}

}
