package com.ust.retail.store.pim.dto.menuconfigurator;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class MenuConfiguratorDTO {

	@NotNull(message = "menu configurator id is mandatory.", groups = { OnUpdate.class })
	private Long menuConfiguratorId;

	@NotNull(message = "year is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long year;

	@NotNull(message = "quarter is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long quarterId;
	private String quarterName;

	@NotNull(message = "month is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long monthId;
	private String monthName;

	@NotEmpty(message = "menu weeks is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private List<MenuConfiguratorWeekDTO> menuWeeks;

	public MenuConfiguratorDTO parse(MenuConfiguratorModel model) {
		this.menuConfiguratorId = model.getMenuConfiguratorId();
		this.year = model.getYear();
		this.quarterId = model.getQuarter().getCatalogId();
		this.quarterName = model.getQuarter().getCatalogOptions();
		this.monthId = model.getMonth().getCatalogId();
		this.monthName = model.getQuarter().getCatalogOptions();
		return this;
	}

	public MenuConfiguratorDTO(MenuConfiguratorModel model) {
		this.menuConfiguratorId = model.getMenuConfiguratorId();
		this.year = model.getYear();
		this.quarterId = model.getQuarter().getCatalogId();
		this.quarterName = model.getQuarter().getCatalogOptions();
		this.monthId = model.getMonth().getCatalogId();
		this.monthName = model.getMonth().getCatalogOptions();
		this.menuWeeks = model.getMenuConfiguratorWeeks().stream().map(w -> new MenuConfiguratorWeekDTO(w)).collect(Collectors.toList());
	}

}
