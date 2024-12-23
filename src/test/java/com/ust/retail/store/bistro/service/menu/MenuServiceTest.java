package com.ust.retail.store.bistro.service.menu;

import java.util.List;
import java.util.Optional;

import com.ust.retail.store.bistro.dto.menu.MenuEntryDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryFiltersDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryFiltersResponseDTO;
import com.ust.retail.store.bistro.model.menu.MenuEntryDayModel;
import com.ust.retail.store.bistro.model.menu.MenuEntryModel;
import com.ust.retail.store.bistro.repository.menu.MenuEntryRepository;
import com.ust.retail.store.pim.repository.catalog.StoreNumberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private MenuEntryRepository mockMenuEntryRepository;
	@Mock
	private StoreNumberRepository mockStoreNumberRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private MenuService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(MenuServiceTest.class);
	}

	@Test
	void saveReturnsExpected() {
		MenuEntryDTO request = fixtureLoader.getObject("saveRequest", MenuEntryDTO.class).orElse(new MenuEntryDTO());
		when(mockMenuEntryRepository.save(any())).then(invocation -> {
			MenuEntryModel argument = invocation.getArgument(0);
			ReflectionTestUtils.setField(argument, "menuEntryId", 1L);
			return argument;
		});

		MenuEntryDTO result = service.save(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		MenuEntryDTO request = fixtureLoader.getObject("updateRequest", MenuEntryDTO.class).orElse(new MenuEntryDTO());
		Optional<MenuEntryModel> menuModel = fixtureLoader.getObject("menuModel", MenuEntryModel.class);
		when(mockMenuEntryRepository.findById(1L)).thenReturn(menuModel);
		when(mockMenuEntryRepository.save(any())).then(invocation -> invocation.getArgument(0));

		MenuEntryDTO result = service.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<MenuEntryModel> menuEntryModel = fixtureLoader.getObject("menuModel", MenuEntryModel.class);
		when(mockMenuEntryRepository.findById(1L)).thenReturn(menuEntryModel);

		MenuEntryDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenResourceNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void findByFiltersReturnsExpected() {
		MenuEntryFiltersDTO request = new MenuEntryFiltersDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");
		MenuEntryDayModel menuEntryDayModel = fixtureLoader.getObject("menuModelNoDetail", MenuEntryDayModel.class).orElse(new MenuEntryDayModel());

		when(mockMenuEntryRepository.findByFilters(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(menuEntryDayModel)));

		Page<MenuEntryFiltersResponseDTO> result = service.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}
}
