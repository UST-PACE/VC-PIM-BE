package com.ust.retail.store.bistro.dto.menu;

import com.ust.retail.store.bistro.model.menu.MenuEntryModel;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntryDTO {

	@Null(message = "menuEntryId is not valid for creation.", groups = {OnCreate.class})
	@NotNull(message = "menuEntryId is Mandatory.", groups = {OnUpdate.class})
	private Long menuEntryId;

	@NotNull(message = "recipeId is Mandatory.", groups = {OnCreate.class})
	private Long recipeId;
	private String recipeName;

	@NotNull(message = "storeNumId is Mandatory.", groups = {OnCreate.class})
	private Long storeNumId;
	private String storeName;

	@Valid
	@NotNull(message = "weekDays is Mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private List<MenuEntryDayDTO> weekDays;

	public MenuEntryDTO parseToDTO(MenuEntryModel m) {
		this.menuEntryId = m.getMenuEntryId();
		this.recipeId = m.getRecipe().getRecipeId();
		this.recipeName = Optional.ofNullable(m.getRecipe().getRelatedUpcMaster())
				.map(UpcMasterModel::getProductName).orElse(null);
		this.storeNumId = m.getStore().getStoreNumId();
		this.storeName = m.getStore().getStoreName();
		this.weekDays = m.getWeekDays().stream()
				.map(d -> new MenuEntryDayDTO(d.getDay(), d.getStart(), d.getEnd()))
				.collect(Collectors.toUnmodifiableList());
		return this;
	}

	public MenuEntryModel createModel(Long userId) {
		MenuEntryModel model = new MenuEntryModel(
				this.menuEntryId,
				this.storeNumId,
				this.recipeId,
				userId);

		this.weekDays.forEach(d -> model.addWeekDay(d.createModel(userId)));

		return model;
	}
}
