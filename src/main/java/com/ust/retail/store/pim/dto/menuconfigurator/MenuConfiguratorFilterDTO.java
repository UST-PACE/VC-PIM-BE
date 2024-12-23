package com.ust.retail.store.pim.dto.menuconfigurator;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.OrdinalSuffix;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class MenuConfiguratorFilterDTO extends BaseFilterDTO{

	private Long menuConfiguratorId;

	private Long year;
	
	private String yearName;

	private Long quarterId;
	
	private String quarterName;

	private Long monthId;
	
	private String monthName;

	private Long weekId;
	
	private String weekName;

	private Long dayId;
	
	private String dayName;
	
	public MenuConfiguratorFilterDTO parseYear(Long year) {
		this.year = year;
		this.yearName = String.valueOf(year);
		return this;
	}
	
	public MenuConfiguratorFilterDTO parseQuarter(CatalogDTO model) {
		this.quarterId = model.getCatalogId();
		this.quarterName = model.getCatalogOptions();
		return this;
	}

	public MenuConfiguratorFilterDTO parseMonth(CatalogDTO model) {
		this.monthId = model.getCatalogId();
		this.monthName = model.getCatalogOptions();
		return this;
	}
	
	public MenuConfiguratorFilterDTO parseWeek(CatalogDTO model, List<String> days) {
		this.weekId = model.getCatalogId();
		int size = days.size();
		this.weekName = model.getCatalogOptions() + String.format("(%s%s)", days.get(0), size == 1 ? "" : "-" + days.get(size - 1));
		return this;
	}
	
	public MenuConfiguratorFilterDTO parseDay(CatalogDTO model, String day) {
		this.dayId = model.getCatalogId();
		this.dayName = OrdinalSuffix.getOrdinalSuffixForDate(day) +"-"+model.getCatalogOptions();
		return this;
	}
}
