package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.upcmaster.UpcAdditionalFeeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@NoArgsConstructor
@Getter
public class UpcAdditionalFeeDTO {
	@Null(message = "upcAdditionalFeeId should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "upcAdditionalFeeId is mandatory.", groups = {OnUpdate.class})
	private Long upcAdditionalFeeId;

	@NotNull(message = "storeNumId is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long storeNumId;

	@NotNull(message = "upcMasterId is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long upcMasterId;

	@NotNull(message = "additionalFeeId is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long additionalFeeId;

	@NotNull(message = "price is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Double price;

	public UpcAdditionalFeeDTO parseToDTO(UpcAdditionalFeeModel model) {
		this.upcAdditionalFeeId = model.getUpcAdditionalFeeId();
		this.storeNumId = model.getStoreNumber().getStoreNumId();
		this.upcMasterId = model.getUpcMaster().getUpcMasterId();
		this.additionalFeeId = model.getAdditionalFee().getAdditionalFeeId();
		this.price = model.getPrice();

		return this;
	}

	public UpcAdditionalFeeModel createModel(Long userId) {
		return new UpcAdditionalFeeModel(
				upcAdditionalFeeId,
				storeNumId,
				upcMasterId,
				additionalFeeId,
				price,
				userId);
	}
}
