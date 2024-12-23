package com.ust.retail.store.pim.common;

import com.ust.retail.store.pim.exceptions.ApplicationException;

public class ErrorResponse {

	private String errorMsg;
	private String exceptionName;
	private String errorCode;
	private ApplicationException exception;

	public ErrorResponse(String errorMsg, String exceptionName, String errorCode) {
		super();
		this.errorMsg = errorMsg;
		this.exceptionName = exceptionName;
		this.errorCode = errorCode;
	}

	public ErrorResponse(String errorMsg, String exceptionName, String errorCode, ApplicationException exception) {
		super();
		this.errorMsg = errorMsg;
		this.exceptionName = exceptionName;
		this.errorCode = errorCode;
		this.exception = exception;
		this.exception.setStackTrace(new StackTraceElement[0]);
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public ApplicationException getException() {
		return exception;
	}
}
