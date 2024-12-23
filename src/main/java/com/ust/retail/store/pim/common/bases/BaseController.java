package com.ust.retail.store.pim.common.bases;

import com.ust.retail.store.bistro.exception.UnitConvertException;
import com.ust.retail.store.pim.common.ErrorResponse;
import com.ust.retail.store.pim.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public abstract class BaseController {

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(AccessDeniedException.class)
	public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
		log.error("Access Denied exception occurred", e);
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), "AHC0000");
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleGeneralException(Exception e) {
		log.error("Generic exception occurred", e);
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), "AHC0001");
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ApplicationException.class)
	public ErrorResponse handleApplicationException(ApplicationException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode(), e);
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(UnitConvertException.class)
	public ErrorResponse handleUnitConvertException(UnitConvertException e) {
		log.error("UnitConverter exception occurred", e);
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode(), e);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode());
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(EmailExistsException.class)
	public ErrorResponse handleEmailExistsException(EmailExistsException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode());
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(UserNameExistsException.class)
	public ErrorResponse handleUserNameExistsException(UserNameExistsException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode());
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(UserNameDoesNotExistsException.class)
	public ErrorResponse handleUserNameDoesNotExistsException(UserNameDoesNotExistsException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode());
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(InvalidOldPasswordException.class)
	public ErrorResponse handleInvalidOldPasswordException(InvalidOldPasswordException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode());
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(FileException.class)
	public ErrorResponse handleFileException(FileException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode());
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException e) {
		log.error("DataIntegrity exception occurred", e);

		if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
			return new ErrorResponse(e.getRootCause().getMessage(), e.getClass().getSimpleName(), "PIM001-DB");
		}

		return new ErrorResponse(e.getMessage(), e.getClass().toString(), "PIM002-DB");
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ErrorResponse handleMaxSizeException(MaxUploadSizeExceededException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), "ARC001");
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(PurchaseOrderAuthorizationException.class)
	public ErrorResponse handlePurchaseOrderAuthorizationException(PurchaseOrderAuthorizationException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode());
	}
	
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(FoodOptionException.class)
	public ErrorResponse handleFoodOptionException(FoodOptionException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getErrorCode());
	}
	
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(ComboException.class)
	public ErrorResponse handleComboException(ComboException e) {
		return new ErrorResponse(e.getMessage(), e.getClass().getSimpleName(), e.getFieldName());
	}
}
