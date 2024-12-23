package com.ust.retail.store.pim.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GenericResponse {

	public static final String SUCCESS_CODE = "PIM-001";
	public static final String ERROR_CODE = "PIM-002";

	public static final String OP_TYPE_REGISTER = "PIM-003";
	public static final String OP_TYPE_UPDATE = "PIM-004";
	public static final String OP_TYPE_DELETE = "PIM-005";
	public static final String OP_TYPE_ACCEPT = "PIM-006";
	public static final String OP_TYPE_REJECT = "PIM-007";


	private String operationType;

	private String code;

	private String msg;
}
