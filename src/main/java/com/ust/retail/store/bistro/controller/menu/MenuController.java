package com.ust.retail.store.bistro.controller.menu;

import com.ust.retail.store.bistro.dto.menu.MenuEntryCopyDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryFiltersDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryFiltersResponseDTO;
import com.ust.retail.store.bistro.service.menu.MenuService;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCopy;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/bistro/p/menu")
@Validated
public class MenuController extends BaseController {

	private final MenuService menuService;

	@Autowired
	public MenuController(MenuService menuService) {
		this.menuService = menuService;
	}


	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public MenuEntryDTO create(@Valid @RequestBody MenuEntryDTO menuEntryDTO) {
		return menuService.save(menuEntryDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public MenuEntryDTO update(@Valid @RequestBody MenuEntryDTO menuEntryDTO) {
		return menuService.update(menuEntryDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/copy")
	@Validated(OnCopy.class)
	public MenuEntryDTO copy(@Valid @RequestBody MenuEntryCopyDTO menuEntryDTO) {
		return menuService.copy(menuEntryDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@DeleteMapping("/delete/{menuEntryId}")
	@Validated(OnCopy.class)
	public GenericResponse delete(@PathVariable Long menuEntryId) {
		menuService.delete(menuEntryId);
		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "SUCCESS");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/find/id/{menuEntryId}")
	public MenuEntryDTO findMenuById(@Valid @PathVariable(value = "menuEntryId") Long menuEntryId) {
		return menuService.findById(menuEntryId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PostMapping("/filters")
	@Validated(OnCreate.class)
	public Page<MenuEntryFiltersResponseDTO> findByFilters(@Valid @RequestBody MenuEntryFiltersDTO filtersDTO) {
		return menuService.findByFilters(filtersDTO);
	}
}
