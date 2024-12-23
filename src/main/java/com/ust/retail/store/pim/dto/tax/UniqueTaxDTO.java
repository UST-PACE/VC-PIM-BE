package com.ust.retail.store.pim.dto.tax;

import com.ust.retail.store.pim.model.tax.TaxModel;

import java.util.Objects;

public class UniqueTaxDTO extends TaxDTO {
	@Override
	public UniqueTaxDTO parseToDTO(TaxModel model) {
		return (UniqueTaxDTO) super.parseToDTO(model);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStoreNumId(), getTaxTypeId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UniqueTaxDTO)) return false;
		UniqueTaxDTO that = (UniqueTaxDTO) o;

		return Objects.equals(getStoreNumId(), that.getStoreNumId())
				&& Objects.equals(getTaxTypeId(), that.getTaxTypeId());
	}
}
