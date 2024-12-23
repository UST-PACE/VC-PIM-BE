package com.ust.retail.store.pim.dto.external;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorWeekModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalMenuConfiguratorWeekDTO {

	private Long weekId;
	private String weekName;

	List<ExternalMenuConfiguratorDayDTO> menuDays;
	
	public ExternalMenuConfiguratorWeekDTO(MenuConfiguratorWeekModel model) {
		this.weekId = model.getWeek().getCatalogId();
		this.weekName = model.getWeek().getCatalogOptions();
		this.menuDays = model.getMenuConfiguratorDays().stream().map(d -> new ExternalMenuConfiguratorDayDTO(d)).collect(Collectors.toList());
	}
}
