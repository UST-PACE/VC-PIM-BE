package com.ust.retail.store.pim.controller.menuconfigurator;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.dto.menuconfigurator.MenuCalendarDTO;
import com.ust.retail.store.pim.dto.menuconfigurator.MenuConfiguratorDTO;
import com.ust.retail.store.pim.dto.menuconfigurator.MenuConfiguratorFilterDTO;
import com.ust.retail.store.pim.dto.upcmaster.SimpleUpcDTO;
import com.ust.retail.store.pim.service.menuconfigurator.MenuConfiguratorService;

@RestController
@RequestMapping(path = "/api/menu/p/")
@Validated
public class MenuConfiguratorController {

	private final MenuConfiguratorService menuConfiguratorService;

	public MenuConfiguratorController(MenuConfiguratorService menuConfiguratorService) {
		this.menuConfiguratorService = menuConfiguratorService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public MenuConfiguratorDTO createMenu(@Valid @RequestBody MenuConfiguratorDTO dto) {
		return menuConfiguratorService.createAndUpdateMenu(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/update")
	@Validated(OnCreate.class)
	public MenuConfiguratorDTO updateMenu(@Valid @RequestBody MenuConfiguratorDTO dto) {
		return menuConfiguratorService.createAndUpdateMenu(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<MenuConfiguratorDTO> filterBySearch(@Valid @RequestBody MenuConfiguratorFilterDTO dto) {
		return menuConfiguratorService.findByFilters(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load")
	public MenuCalendarDTO getMenuFilterCalendar() {
		return menuConfiguratorService.getMenuFilterCalendar();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/calendar/load")
	public MenuCalendarDTO getMenuCalendar(@RequestParam(required = false) Long year, @RequestParam(required = false) Long quarterId,
			@RequestParam(required = false) Long monthId, @RequestParam(required = false) Long weekId, @RequestParam(required = false) boolean isFilter) {
		return menuConfiguratorService.getMenuCalendar(year, quarterId, monthId, weekId, isFilter);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/filter/category/products")
	public List<SimpleUpcDTO> getCategoryProducts(@RequestParam(required = true) Long year,
			@RequestParam(required = true) Long quarterId, @RequestParam(required = true) Long monthId,
			@RequestParam(required = true) Long weekId, @RequestParam(required = true) Long dayId,
			@RequestParam(required = true) Long categoryId) {
		return menuConfiguratorService.getCategoryProducts(year, quarterId, monthId, weekId, dayId, categoryId);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/delete/menu/{menuId}")
	public GenericResponse deleteByMenuConfiguratorId(@PathVariable(value = "menuId") Long menuId) {
		return menuConfiguratorService.deleteByMenuConfiguratorId(menuId);
	}
	
}
