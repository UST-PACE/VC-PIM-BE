package com.ust.retail.store.pim.dto.inventory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnWasteInventory;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BistroWasteDTO {

	@NotNull(message = "referenceId is mandatory", groups = { OnWasteInventory.class })
	private Long referenceId;

	@Valid
	@NotNull(message = "Item details are Mandatory.", groups = { OnWasteInventory.class})
	private Item item;
	
	

}
