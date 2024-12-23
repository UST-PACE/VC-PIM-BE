package com.ust.retail.store.pim.dto.inventory.returns.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class InventoryReturnSumaryDTO {

	private Map<String,List<InventoryReturnItemDetailDTO>> sumary;
	
	
	public void add(String category,InventoryReturnItemDetailDTO sumaryItem) {
		if(sumary==null) sumary = new HashMap<String, List<InventoryReturnItemDetailDTO>>();
		
		if(sumary.get(category)==null) {
			
			List<InventoryReturnItemDetailDTO> ls = new ArrayList<InventoryReturnItemDetailDTO>();
			ls.add(sumaryItem);
			sumary.put(category,ls);
			
		}else {
			sumary.get(category).add(sumaryItem);
			
		}
	}
}
