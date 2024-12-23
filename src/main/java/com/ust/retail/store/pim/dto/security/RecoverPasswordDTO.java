package com.ust.retail.store.pim.dto.security;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecoverPasswordDTO {
//TODO validar lo obligatorio
	private String username;
	private String password;
	private String token;
}
