package com.ust.retail.store.pim.dto.catalog;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.catalog.AdditionalFeeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class AdditionalFeeDTO {
	@Null(message = "additionalFeeId should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "additionalFeeId is mandatory.", groups = {OnUpdate.class})
	private Long additionalFeeId;

	@Size(min = 3, max = 50,
			message = "feeName must have between 3 and 50 characters",
			groups = {OnCreate.class, OnUpdate.class})
	@NotNull(message = "feeName is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private String feeName;

	public AdditionalFeeDTO parseToDTO(AdditionalFeeModel model) {
		this.additionalFeeId = model.getAdditionalFeeId();
		this.feeName = model.getFeeName();

		return this;
	}

	public AdditionalFeeModel createModel(Long userId) {
		return new AdditionalFeeModel(
				this.additionalFeeId,
				this.feeName,
				userId
		);
	}
}
