package com.ust.retail.store.pim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ApplicationException {
	private static final long serialVersionUID = 1L;

	private String resourceName;
	private String fieldName;
	private Object fieldValue;
	private final String errorCode;

	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.errorCode = "PIM-RNF-001";
	}

	public ResourceNotFoundException(String resourceName, String firstFieldName, String secondFieldName, Object firstFieldValue, Object secondFieldValue) {
		super(String.format("%s not found with %s : '%s' and  %s : '%s'", resourceName, firstFieldName, firstFieldValue, secondFieldName, secondFieldValue));
		this.errorCode = "PIM-RNF-001";
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
