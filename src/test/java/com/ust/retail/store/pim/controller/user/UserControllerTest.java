package com.ust.retail.store.pim.controller.user;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.catalogs.UserStatusCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.security.StoreNumberInfoDTO;
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.dto.security.UserSearchResponseDTO;
import com.ust.retail.store.pim.exceptions.InvalidUserModificationException;
import com.ust.retail.store.pim.model.security.RoleModel;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.service.security.RoleService;
import com.ust.retail.store.pim.service.security.UserService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private UserService mockUserService;
	@Mock
	private UserStatusCatalog mockUserStatusCatalog;
	@Mock
	private RoleService mockRoleService;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private UserController controller;

	@BeforeAll
	static void beforeAll() {
		fixtureLoader = new FixtureLoader();
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockUserService.findById(1L)).thenReturn(new UserDTO());

		UserDTO result = controller.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByUserNameReturnsExpected() {
		when(mockUserService.findUserByUserName("ANY")).thenReturn(new UserDTO());

		UserDTO result = controller.findByUserName("ANY");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void createReturnsExpected() {
		UserDTO request = new UserDTO();
		UserDTO user = fixtureLoader.getObjectFromString("{}", UserDTO.class).orElse(new UserDTO());
		when(mockUserService.createUser(request)).thenReturn(user);

		UserDTO result = controller.create(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateUserReturnsExpected() {
		UserDTO request = new UserDTO();
		when(mockUserService.updateUser(request)).thenReturn(new UserDTO());

		UserDTO result = controller.updateUser(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateUserProfileReturnsExpected() {
		UserDTO request = new UserDTO(1L, null, null, null, null, null, null, null, null, null, null, null, false);
		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(new UserModel(1L));
		when(mockUserService.updateUser(request)).thenReturn(new UserDTO());

		UserDTO result = controller.updateUserProfile(request);

		assertThat(result, is(notNullValue()));
	}


	@Test
	void updateUserProfileThrowsExceptionWhenDifferentUser() {
		UserDTO request = new UserDTO(1L, null, null, null, null, null, null, null, null, null, null, null, false);
		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(new UserModel(2L));

		assertThrows(InvalidUserModificationException.class, () -> controller.updateUserProfile(request));
	}

	@Test
	void updatePasswordReturnsExpected() {
		UserDTO request = new UserDTO();

		ResponseEntity<Void> result = controller.updatePassword(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateUserPasswordReturnsExpected() {
		UserDTO request = new UserDTO(1L, null, null, null, null, null, null, null, null, null, null, null, false);
		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(new UserModel(1L));

		ResponseEntity<Void> result = controller.updateUserPassword(request);

		assertThat(result, is(notNullValue()));
	}


	@Test
	void updateUserPasswordThrowsExceptionWhenDifferentUser() {
		UserDTO request = new UserDTO(1L, null, null, null, null, null, null, null, null, null, null, null, false);
		when(mockAuthenticationFacade.getCurrentUserDetails()).thenReturn(new UserModel(2L));

		assertThrows(InvalidUserModificationException.class, () -> controller.updateUserPassword(request));
	}

	@Test
	void listUserByFiltersReturnsExpected() {
		UserDTO request = new UserDTO();
		when(mockUserService.getUsersByFilters(request)).thenReturn(Page.empty());

		Page<UserDTO> result = controller.listUserByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findAutoCompleteOptionsReturnsExpected() {
		when(mockUserService.getAutocomplete("ANY")).thenReturn(List.of());

		List<UserSearchResponseDTO> result = controller.findAutoCompleteOptions("ANY");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getUserStatusCatalogReturnsExpected() {
		when(mockUserStatusCatalog.getCatalogOptions()).thenReturn(List.of());

		List<CatalogDTO> result = controller.getUserStatusCatalog();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getStoreNumberByUserReturnsExpected() {
		when(mockAuthenticationFacade.getUserStoreNumber()).thenReturn(new StoreNumberInfoDTO(1L, "ANY"));
		StoreNumberInfoDTO result = controller.getStoreNumberByUser();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadRoles() {
		when(mockRoleService.findAllRoles()).thenReturn(List.of());

		List<RoleModel> result = controller.loadRoles();

		assertThat(result, is(notNullValue()));
	}
}
