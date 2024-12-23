package com.ust.retail.store.pim.dto.menuconfigurator;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorWeekModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class MenuConfiguratorWeekDTO {

	@NotNull(message = "week is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long weekId;
	private String weekName;

	@NotEmpty(message = "menu days is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	List<MenuConfiguratorDayDTO> menuDays;

	public MenuConfiguratorWeekDTO(MenuConfiguratorWeekModel model) {
		this.weekId = model.getWeek().getCatalogId();
		this.weekName = model.getWeek().getCatalogOptions();
		this.menuDays = model.getMenuConfiguratorDays().stream().map(d -> new MenuConfiguratorDayDTO(d)).collect(Collectors.toList());
	}

}
