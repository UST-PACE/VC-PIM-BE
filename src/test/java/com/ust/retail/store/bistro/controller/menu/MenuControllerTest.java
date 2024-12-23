package com.ust.retail.store.bistro.controller.menu;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.ust.retail.store.bistro.commons.catalogs.MealTimeCatalog;
import com.ust.retail.store.bistro.dto.menu.MenuEntryDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryFiltersDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryFiltersResponseDTO;
import com.ust.retail.store.bistro.service.menu.MenuService;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuControllerTest {
	@Mock
	private MenuService mockMenuService;
	@Mock
	private MealTimeCatalog mockMealTimeCatalog;

	@InjectMocks
	private MenuController controller;

	@Test
	void createReturnsExpected() {
		MenuEntryDTO request = new MenuEntryDTO();
		when(mockMenuService.save(request)).thenReturn(new MenuEntryDTO());

		MenuEntryDTO result = controller.create(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		MenuEntryDTO request = new MenuEntryDTO();
		when(mockMenuService.update(request)).thenReturn(new MenuEntryDTO());

		MenuEntryDTO result = controller.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findMenuByIdReturnsExpected() {
		when(mockMenuService.findById(1L)).thenReturn(new MenuEntryDTO());

		MenuEntryDTO result = controller.findMenuById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByFiltersReturnsExpected() {
		MenuEntryFiltersDTO request = new MenuEntryFiltersDTO();
		when(mockMenuService.findByFilters(request)).thenReturn(Page.empty());

		Page<MenuEntryFiltersResponseDTO> result = controller.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}
}
