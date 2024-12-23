package com.ust.retail.store.pim.service.menuconfigurator;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.catalogs.MenuDayCalendarCatalog;
import com.ust.retail.store.pim.common.catalogs.MenuMonthCalendarCatalog;
import com.ust.retail.store.pim.common.catalogs.MenuQuarterCalendarCatalog;
import com.ust.retail.store.pim.common.catalogs.MenuWeekCalendarCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcProductTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcSellingChannelCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.menuconfigurator.MenuCalendarDTO;
import com.ust.retail.store.pim.dto.menuconfigurator.MenuConfiguratorDTO;
import com.ust.retail.store.pim.dto.menuconfigurator.MenuConfiguratorFilterDTO;
import com.ust.retail.store.pim.dto.upcmaster.SimpleUpcDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorModel;
import com.ust.retail.store.pim.repository.menuconfigurator.MenuConfiguratorRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuConfiguratorService {

	private final AuthenticationFacade authenticationFacade;

	private final MenuConfiguratorRepository menuConfiguratorRepository;
	
	private final UpcMasterRepository upcMasterRepository;
	
	private final MenuQuarterCalendarCatalog menuQuarterCalendarCatalog;
	
	private final MenuMonthCalendarCatalog menuMonthCalendarCatalog;
	
	private final MenuWeekCalendarCatalog menuWeekCalendarCatalog;
	
	private final MenuDayCalendarCatalog menuDayCalendarCatalog;
	
	public MenuConfiguratorDTO createAndUpdateMenu(MenuConfiguratorDTO menuDTO) {
		MenuConfiguratorModel model = new MenuConfiguratorModel();
		if (menuDTO.getMenuConfiguratorId() != null) {
			model = menuConfiguratorRepository.findById(menuDTO.getMenuConfiguratorId()).get();
			model.getMenuConfiguratorWeeks().clear();
		}
		model.createModel(menuDTO, this.authenticationFacade.getCurrentUserId());
		model = menuConfiguratorRepository.saveAndFlush(model);
		return new MenuConfiguratorDTO().parse(model);
	}
	
	public Page<MenuConfiguratorDTO> findByFilters(MenuConfiguratorFilterDTO dto) {
		return menuConfiguratorRepository
				.findByFilters(dto.getYear(), dto.getQuarterId(), dto.getMonthId(), dto.createPageable())
				.map(m -> new MenuConfiguratorDTO(m));
	}

	public MenuCalendarDTO getMenuCalendar(Long year, Long quarterId, Long monthId, Long weekId, boolean isFilter) {
		MenuCalendarDTO menuCalendarDTO = new MenuCalendarDTO();
		menuCalendarDTO.setYears(List.of(new MenuConfiguratorFilterDTO().parseYear(Long.valueOf(Year.now().getValue())),
				new MenuConfiguratorFilterDTO().parseYear(Long.valueOf(Year.now().getValue()) + 1)));
		
		if (year != null) {
			Long value = !isFilter ? 0L : year > Year.now().getValue() ? 0L : YearMonth.now().getMonthValue();
			List<MenuConfiguratorFilterDTO> quarters = MenuQuarterCalendarCatalog.quarters.entrySet().stream()
					.filter(q -> q.getKey() >= value).map(c -> new MenuConfiguratorFilterDTO().parseQuarter(menuQuarterCalendarCatalog.findCatalogById(c.getValue())))
					.sorted(Comparator.comparing(MenuConfiguratorFilterDTO::getQuarterId)).collect(Collectors.toList());
			menuCalendarDTO.setQuarters(quarters);
		}
		if (quarterId != null) {
			List<MenuConfiguratorFilterDTO> months = null;
			if (isFilter) {
				months = MenuMonthCalendarCatalog.quarterMonths.entrySet().stream()
						.filter(m -> m.getKey().equals(quarterId)).flatMap(m -> m.getValue().stream().map(c -> new MenuConfiguratorFilterDTO()
						.parseMonth(menuMonthCalendarCatalog.findCatalogById(c))))
						.sorted(Comparator.comparing(MenuConfiguratorFilterDTO::getMonthId)).collect(Collectors.toList());
				
				YearMonth yearOfMonth = Year.now().getValue() == year ? YearMonth.now() : null;
				if (yearOfMonth != null) {
					AtomicLong count = new AtomicLong(0);
					months.removeIf(m -> {
						if (count.get() == 1)
							return false;
						if (m.getMonthName().equalsIgnoreCase(yearOfMonth.getMonth().name())) {
							count.getAndIncrement();
							return false;
						}
						return true;
					});
				}
				months.removeIf(month -> menuConfiguratorRepository.findByYearAndDeletedFalse(year).stream()
						.anyMatch(menu -> menu.getMonth().getCatalogOptions().equalsIgnoreCase(month.getMonthName())));
			}
			else {
				months = menuMonthCalendarCatalog.getCatalogOptions().stream().map(c -> new MenuConfiguratorFilterDTO().parseMonth(c)).collect(Collectors.toList());
			}
			menuCalendarDTO.setMonths(months);
		}
		if (monthId != null) {
			List<CatalogDTO> menuWeeks = menuWeekCalendarCatalog.getCatalogOptions();
			List<String> days = new ArrayList<>();
			List<MenuConfiguratorFilterDTO> daysOfweek = new ArrayList<>();
			AtomicLong count = new AtomicLong(0);
			YearMonth yearMonth = YearMonth.of(year.intValue(),
					Month.valueOf(menuMonthCalendarCatalog.findCatalogById(monthId).getCatalogOptions().toUpperCase()));

			Stream.iterate(yearMonth.atDay(1), date -> date.plusDays(1)).limit(yearMonth.atEndOfMonth().getDayOfMonth()).forEach(day -> {
				days.add(String.valueOf(day.getDayOfMonth()));
				if (day.getDayOfWeek() == DayOfWeek.SATURDAY || day.equals(yearMonth.atEndOfMonth())) {
					count.incrementAndGet();
					daysOfweek.add(menuWeeks.stream()
							.filter(f -> f.getCatalogOptions().contains(String.valueOf(count.get())))
							.map(m -> new MenuConfiguratorFilterDTO().parseWeek(m, days)).findFirst().get());
					days.clear();
				}
			});
			menuCalendarDTO.setWeeks(daysOfweek);
		}
		
		if (weekId != null) {
			YearMonth yearMonth = YearMonth.of(year.intValue(),
					Month.valueOf(menuMonthCalendarCatalog.findCatalogById(monthId).getCatalogOptions().toUpperCase()));
			String week = menuWeekCalendarCatalog.findCatalogById(weekId).getCatalogOptions();
			AtomicLong count = new AtomicLong(0);
			List<MenuConfiguratorFilterDTO> daysOfweek = new ArrayList<>();
			List<Map<String, String>> days = new ArrayList<>();

			Stream.iterate(yearMonth.atDay(1), date -> date.plusDays(1)).limit(yearMonth.atEndOfMonth().getDayOfMonth()).forEach(day -> {
				days.add(Map.of(String.valueOf(day.getDayOfMonth()), day.getDayOfWeek().name()));
				if (day.getDayOfWeek() == DayOfWeek.SATURDAY || day.equals(yearMonth.atEndOfMonth())) {
					count.incrementAndGet();
					if (Long.valueOf(String.valueOf(week.charAt(week.length() - 1))) == count.get()) {
						days.stream().flatMap(f -> f.entrySet().stream()).forEach(dayMap -> {
							List<MenuConfiguratorFilterDTO> menuDays = menuDayCalendarCatalog.getCatalogOptions()
									.stream()
									.filter(d -> dayMap.getValue().contentEquals(d.getCatalogOptions().toUpperCase()))
									.map(m -> new MenuConfiguratorFilterDTO().parseDay(m, dayMap.getKey()))
									.collect(Collectors.toList());
							daysOfweek.addAll(menuDays);
						});
					}
					days.clear();
				}
			});
			menuCalendarDTO.setDays(daysOfweek);
		}
		return menuCalendarDTO;
	}
	
	public MenuCalendarDTO getMenuFilterCalendar() {
		MenuCalendarDTO menuCalendarDTO = new MenuCalendarDTO();
		menuCalendarDTO.setYears(List.of(new MenuConfiguratorFilterDTO().parseYear(Long.valueOf(Year.now().getValue())),
				new MenuConfiguratorFilterDTO().parseYear(Long.valueOf(Year.now().getValue()) + 1)));
		menuCalendarDTO.setQuarters(menuQuarterCalendarCatalog.getCatalogOptions().stream()
				.map(w -> new MenuConfiguratorFilterDTO().parseQuarter(w))
				.sorted(Comparator.comparing(MenuConfiguratorFilterDTO::getQuarterId)).collect(Collectors.toList()));
		menuCalendarDTO.setMonths(menuMonthCalendarCatalog.getCatalogOptions().stream()
				.map(w -> new MenuConfiguratorFilterDTO().parseMonth(w))
				.sorted(Comparator.comparing(MenuConfiguratorFilterDTO::getMonthId)).collect(Collectors.toList()));
		return menuCalendarDTO;
	}
	
	public List<SimpleUpcDTO> getCategoryProducts(Long year, Long quarterId, Long monthId, Long weekId, Long dayId,
			Long categoryId) {
		List<Long> upcIds = menuConfiguratorRepository.findByFilters(year, quarterId, monthId, weekId, dayId, categoryId);
		return upcMasterRepository
				.findByCategoryIdAndNotInProducts(categoryId, UpcProductTypeCatalog.ROTATING_TYPE, UpcMasterStatusCatalog.UPC_MASTER_STATUS_DISCONTINUE_TRADING, UpcSellingChannelCatalog.VC_UPC_SELLING_CHANNEL, upcIds).stream()
				.map(m -> new SimpleUpcDTO(m)).collect(Collectors.toList());
	}
	
	public GenericResponse deleteByMenuConfiguratorId(Long menuId) {
		MenuConfiguratorModel model = menuConfiguratorRepository.findById(menuId)
				.orElseThrow(() -> new ResourceNotFoundException("MenuConfigurator", "id", menuId));
		model.setDeleted(true);
		menuConfiguratorRepository.saveAndFlush(model);
		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "true");
	}
}
