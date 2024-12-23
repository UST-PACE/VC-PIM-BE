package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FoodOptionException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public FoodOptionException (String message) {
		super(message);
	}

}
