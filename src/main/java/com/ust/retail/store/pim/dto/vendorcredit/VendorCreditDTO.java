package com.ust.retail.store.pim.dto.vendorcredit;

import java.util.Objects;

import com.ust.retail.store.pim.model.vendorcredits.VendorCreditModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VendorCreditDTO {
	private Long vendorCreditId;
	private Double availableCredit;

	public VendorCreditDTO parseToDTO(VendorCreditModel model) {
		if (Objects.nonNull(model)) {
			this.vendorCreditId = model.getVendorCreditId();
			this.availableCredit = model.getAvailableCredit();
		}
		return this;
	}
}
