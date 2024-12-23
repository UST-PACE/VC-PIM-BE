package com.ust.retail.store.pim.dto.menuconfigurator;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorDayModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class MenuConfiguratorDayDTO {

	@NotNull(message = "day is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long dayId;
	private String dayName;

	@NotEmpty(message = "menu products should not be empty", groups = { OnCreate.class, OnUpdate.class })
	private List<MenuConfiguratorProductDTO> menuProducts;

	public MenuConfiguratorDayDTO(MenuConfiguratorDayModel model) {
		this.dayId = model.getDay().getCatalogId();
		this.dayName = model.getDay().getCatalogOptions();
		this.menuProducts = model.getMenuConfiguratorProducts().stream().map(p -> new MenuConfiguratorProductDTO(p)).collect(Collectors.toList());
	}
}
