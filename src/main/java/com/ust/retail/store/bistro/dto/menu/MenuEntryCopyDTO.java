package com.ust.retail.store.bistro.dto.menu;

import com.ust.retail.store.pim.common.annotations.OnCopy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntryCopyDTO {

	@NotNull(message = "menuEntryId is Mandatory.", groups = {OnCopy.class})
	private Long menuEntryId;

	@NotNull(message = "storeNumId is Mandatory.", groups = {OnCopy.class})
	private Long storeNumId;

}
