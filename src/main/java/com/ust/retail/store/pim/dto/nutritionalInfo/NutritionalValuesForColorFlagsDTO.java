package com.ust.retail.store.pim.dto.nutritionalInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class NutritionalValuesForColorFlagsDTO {
	public static final String GREEN_COLOR= "Green";
	public static final String ORANGE_COLOR= "Orange";
	public static final String RED_COLOR= "Red";

	private static final Long GREEN_ID = 1L;
	private static final Long ORANGE_ID = 2L;
	private static final Long RED_ID = 3L;

	private String colorDesc;
	private Double range;
	private Long colorId;

	public NutritionalValuesForColorFlagsDTO(String flagType,Double range){
		switch (flagType) {
			case GREEN_COLOR:
				this.colorDesc = GREEN_COLOR;
				this.range = range;
				this.colorId = GREEN_ID;
				break;

			case ORANGE_COLOR:
				this.colorDesc = ORANGE_COLOR;
				this.range = range;
				this.colorId = ORANGE_ID;
				break;

			case RED_COLOR:
				this.colorDesc = RED_COLOR;
				this.range = range;
				this.colorId = RED_ID;
				break;

		}

	}

}
