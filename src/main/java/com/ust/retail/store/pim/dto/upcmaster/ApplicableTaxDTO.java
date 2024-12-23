package com.ust.retail.store.pim.dto.upcmaster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApplicableTaxDTO {
	private String type;
	private String category;
	private Double percentage;
}
