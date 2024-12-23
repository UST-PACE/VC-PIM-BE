package com.ust.retail.store.pim.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.dto.nutritionalInfo.NutritionalValuesForColorFlagsDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Component
public class NutritionalValuesForColorFlags {
	
	@Value("${pim.nutritional.calories.flag.green}")
	private Double greenStartRange;
	
	@Value("${pim.nutritional.calories.flag.orange}")
	private Double orangeStartRange;
	
	@Value("${pim.nutritional.calories.flag.red}")
	private Double redStartRange;
	
	public List<NutritionalValuesForColorFlagsDTO> getValues(){
		List<NutritionalValuesForColorFlagsDTO> nutritionalValueForFlagsList = new ArrayList<NutritionalValuesForColorFlagsDTO>();
		nutritionalValueForFlagsList.add(new NutritionalValuesForColorFlagsDTO(NutritionalValuesForColorFlagsDTO.GREEN_COLOR,greenStartRange));
		nutritionalValueForFlagsList.add(new NutritionalValuesForColorFlagsDTO(NutritionalValuesForColorFlagsDTO.ORANGE_COLOR,orangeStartRange));
		nutritionalValueForFlagsList.add(new NutritionalValuesForColorFlagsDTO(NutritionalValuesForColorFlagsDTO.RED_COLOR,redStartRange));
		
		return nutritionalValueForFlagsList;
	}
}
