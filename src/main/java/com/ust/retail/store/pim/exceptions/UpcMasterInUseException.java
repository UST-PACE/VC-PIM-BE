package com.ust.retail.store.pim.exceptions;

import lombok.Getter;

public class UpcMasterInUseException extends ApplicationException {
	
	private static final long serialVersionUID = 1L;

	@Getter
	private final String usedIn;

	public enum ChangedField {
		PRODUCT_TYPE("Product type", "PIM-IUC-001"),
		CONTENT_PER_UNIT("Content per unit", "PIM-IUC-002"),
		CONTENT_PER_UNIT_UOM("Content per unit UOM", "PIM-IUC-003"),
		INVENTORY_UNIT("Inventory unit", "PIM-IUC-004");

		private final String fieldName;
		private final String errorCode;

		ChangedField(String fieldName, String errorCode) {
			this.fieldName = fieldName;
			this.errorCode = errorCode;
		}
	}

	public enum Usage {
		RECIPE("Currently present in Recipes"),
		INVENTORY("Currently has On Hand inventory");

		private final String usageMessage;

		Usage(String usageMessage) {
			this.usageMessage = usageMessage;
		}
	}

	public UpcMasterInUseException(ChangedField changedField, Usage usage) {
		super(String.format("Can not change field %s. %s", changedField.fieldName, usage.usageMessage));
		this.usedIn = usage.toString();
		this.errorCode = changedField.errorCode;
	}
}
