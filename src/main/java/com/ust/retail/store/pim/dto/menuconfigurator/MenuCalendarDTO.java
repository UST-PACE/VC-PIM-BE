package com.ust.retail.store.pim.dto.menuconfigurator;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class MenuCalendarDTO {

	private List<MenuConfiguratorFilterDTO> years;

	private List<MenuConfiguratorFilterDTO> quarters;

	private List<MenuConfiguratorFilterDTO> months;

	private List<MenuConfiguratorFilterDTO> weeks;

	private List<MenuConfiguratorFilterDTO> days;

}
