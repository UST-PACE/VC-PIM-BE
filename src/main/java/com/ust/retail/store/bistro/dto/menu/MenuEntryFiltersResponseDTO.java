package com.ust.retail.store.bistro.dto.menu;

import com.ust.retail.store.bistro.model.menu.MenuEntryDayModel;
import com.ust.retail.store.bistro.model.menu.MenuEntryModel;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntryFiltersResponseDTO extends BaseFilterDTO {
	private Long menuEntryId;
	private String recipeName;
	private String storeName;
	private String weekDay;
	private String availability;

	public MenuEntryFiltersResponseDTO parseToDTO(MenuEntryDayModel m) {
		MenuEntryModel menuEntry = m.getMenuEntry();
		this.menuEntryId = menuEntry.getMenuEntryId();
		this.recipeName = menuEntry.getRecipe().getRelatedUpcMaster().getProductName();
		this.storeName = menuEntry.getStore().getStoreName();
		this.weekDay = DateUtils.getDayName(m.getDay());
		this.availability = String.format("%s - %s", DateUtils.getAmPmFormattedTime(m.getStart()), DateUtils.getAmPmFormattedTime(m.getEnd()));

		return this;
	}

}
