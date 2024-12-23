package com.ust.retail.store.pim.dto.productreturn;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CalculateCreditsDTO {

	@NotNull(message = "creditsInfo is mandatory when calculating credits.")
	@Valid
	List<CreditInfoDTO> creditsInfo;
}
