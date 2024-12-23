package com.ust.retail.store.pim.dto.external;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorDayModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class ExternalMenuConfiguratorDayDTO {

	private Long dayId;
	private String dayName;

	private List<ExternalMenuConfiguratorProductDTO> menuProducts;
	
	public ExternalMenuConfiguratorDayDTO(MenuConfiguratorDayModel model) {
		this.dayId = model.getDay().getCatalogId();
		this.dayName = model.getDay().getCatalogOptions();
		this.menuProducts = model.getMenuConfiguratorProducts().stream().map(p -> new ExternalMenuConfiguratorProductDTO(p.getMenuConfiguratorProduct())).collect(Collectors.toList());
	}
}
