package com.ust.retail.store.pim.service.catalog;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.repository.catalog.StoreLocationRepository;
import com.ust.retail.store.pim.repository.catalog.StoreNumberRepository;
import com.ust.retail.store.pim.repository.security.UserRepository;
import com.ust.retail.store.pim.service.security.RoleService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreNumberServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private StoreNumberRepository mockStoreNumberRepository;
	@Mock
	private StoreLocationRepository mockStoreLocationRepository;
	@Mock
	private UserRepository mockUserRepository;

	@InjectMocks
	private StoreNumberService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(StoreNumberServiceTest.class);
	}
	@Test
	void createReturnsExpected() {
		StoreNumberDTO dto = new StoreNumberDTO(null, "STORE NAME");
		when(mockStoreNumberRepository.save(any()))
				.thenAnswer(invocation -> {
					StoreNumberModel model = invocation.getArgument(0);
					return new StoreNumberModel(1L, model.getStoreName(), null, null, null, null);
				});

		StoreNumberDTO result = service.create(dto);

		assertThat(result, allOf(
				hasProperty("storeNumId", equalTo(1L)),
				hasProperty("storeName", equalTo("STORE NAME"))
		));
	}

	@Test
	void saveOrUpdateReturnsExpectedWhenUpdating() {
		StoreNumberDTO dto = new StoreNumberDTO(1L, "STORE NAME");
		when(mockStoreNumberRepository.findById(1L)).thenReturn(Optional.ofNullable(dto.createModel()));
		when(mockStoreNumberRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		StoreNumberDTO result = service.update(dto);

		assertThat(result, allOf(
				hasProperty("storeNumId", equalTo(1L)),
				hasProperty("storeName", equalTo("STORE NAME"))
		));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockStoreNumberRepository.findById(1L)).thenReturn(Optional.of(new StoreNumberModel(1L)));

		StoreNumberDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenStoreNumberNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void getStoreNumbersByFiltersReturnsExpected() {
		StoreNumberDTO request = new StoreNumberDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");

		when(mockStoreNumberRepository.findByFilters(any(), any())).thenReturn(new PageImpl<>(List.of()));

		Page<StoreNumberDTO> result = service.getStoreNumbersByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getAutocompleteReturnsExpected() {
		List<StoreNumberDTO> result = service.getAutocomplete("Store");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadReturnsExpected() {
		when(mockStoreNumberRepository.findAll()).thenReturn(List.of(new StoreNumberModel(1L)));

		List<StoreNumberDTO> result = service.load();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadByVendorMasterIdReturnsExpected() {
		when(mockStoreNumberRepository.findByVendorMasterId(1L)).thenReturn(List.of(new StoreNumberModel(1L)));

		List<StoreNumberDTO> result = service.loadByVendorMasterId(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getStoreManagerEmailListReturnsExpected() {
		when(mockUserRepository.findUsersWithRoleByStoreNumId(1L, RoleService.STORE_MANAGER_ROLE_ID))
				.thenReturn(List.of(new UserModel(1L, null, null, null, "user@example.com", null, null, 1L, 1L, 1L)));

		List<String> result = service.getStoreManagerEmailList(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getPoApproverEmailListReturnsExpected() {
		List<UserModel> storeManagerList = fixtureLoader.getObject("storeManagerList", new UserListReference()).orElse(List.of());
		List<UserModel> administratorList = fixtureLoader.getObject("administratorList", new UserListReference()).orElse(List.of());

		when(mockUserRepository.findUsersWithRoleByStoreNumId(eq(1L), any()))
				.thenAnswer(invocation -> Objects.equals(invocation.<Long>getArgument(1), RoleService.STORE_MANAGER_ROLE_ID) ? storeManagerList : administratorList);

		List<String> result = service.getPoApproverEmailList(1L);

		assertThat(result, hasSize(2));
	}

	private static class UserListReference extends TypeReference<List<UserModel>> {
	}
}
