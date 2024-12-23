package com.ust.retail.store.common.cloud.storage;

import com.ust.retail.store.pim.exceptions.ApplicationException;

public class CloudStorageConfException extends ApplicationException{

	private static final long serialVersionUID = 5920891820594982803L;

	public CloudStorageConfException(String cloudSelection) {
		super("Cloud Service " + cloudSelection + "Is not available");
		this.errorCode = "PIM-CLOUD-CONF-001";
	}

}
