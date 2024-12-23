package com.ust.retail.store.bistro.dto.menu;

import com.ust.retail.store.bistro.model.menu.MenuEntryDayModel;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MenuEntryDayDTO {
	@NotNull(message = "day is Mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Integer day;
	@NotNull(message = "start is Mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Integer start;
	@NotNull(message = "end is Mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Integer end;

	public MenuEntryDayModel createModel(Long userId) {
		return new MenuEntryDayModel(null, this.day, this.start, this.end, userId);
	}
}
