package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SecurityChallengeException extends ApplicationException {
	private static final long serialVersionUID = 1L;

	public SecurityChallengeException(String securityCode) {
		super(String.format("Invalid Verification Code: '%s'", securityCode), "PIM-SEC-01");
	}

	public String getErrorCode() {
		return errorCode;
	}

}
