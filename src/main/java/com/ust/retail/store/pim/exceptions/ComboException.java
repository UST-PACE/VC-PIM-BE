package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ComboException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	private String resourceName;
	private String fieldName;

	public ComboException(String resourceName, String fieldName) {
		super(String.format("%s already exist in %s", fieldName, resourceName));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
	}

	public ComboException(String message) {
		super(message);
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
