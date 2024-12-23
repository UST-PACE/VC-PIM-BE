package com.ust.retail.store.pim.dto.catalog;

import com.ust.retail.store.pim.model.catalog.AdditionalFeeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class AdditionalFeeFilterResultDTO {
	private Long additionalFeeId;
	private String feeName;
	private Date createdAt;

	public AdditionalFeeFilterResultDTO modelToDTO(AdditionalFeeModel model) {
		this.additionalFeeId = model.getAdditionalFeeId();
		this.feeName = model.getFeeName();
		this.createdAt = model.getCreatedAt();

		return this;
	}
}
