package com.ust.retail.store.pim.dto.external;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnSimpleFilter;
import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class ExternalMenuConfiguratorDTO {

	private Long menuConfiguratorId;

	@NotNull(message = "year name is mandatory", groups = {OnSimpleFilter.class, OnFilter.class})
	private Long year;

	private String yearName;

	private Long quarterId;

	private String quarterName;

	private Long monthId;

	@NotNull(message = "month name is mandatory", groups = {OnSimpleFilter.class, OnFilter.class})
	private String monthName;
	
	
	@NotNull(message = "week name is mandatory", groups = {OnFilter.class})
	private String weekName;

	@NotNull(message = "day name is mandatory", groups = {OnFilter.class}) 
	private String dayName;
	
	private List<ExternalMenuConfiguratorWeekDTO> menuWeeks;

	public ExternalMenuConfiguratorDTO(MenuConfiguratorModel model) {
		this.menuConfiguratorId = model.getMenuConfiguratorId();
		this.year = model.getYear();
		this.quarterId = model.getQuarter().getCatalogId();
		this.quarterName = model.getQuarter().getCatalogOptions();
		this.monthId = model.getMonth().getCatalogId();
		this.monthName = model.getMonth().getCatalogOptions();
		this.menuWeeks = model.getMenuConfiguratorWeeks().stream().map(w -> new ExternalMenuConfiguratorWeekDTO(w)).collect(Collectors.toList());
	}
	
}
