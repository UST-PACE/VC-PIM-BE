package com.ust.retail.store.pim.service.security;

import java.util.List;
import java.util.Optional;

import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.common.catalogs.UserStatusCatalog;
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.dto.security.UserSearchResponseDTO;
import com.ust.retail.store.pim.exceptions.EmailExistsException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UserNameExistsException;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.repository.security.UserRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private UserRepository mockUserRepository;
	@Mock
	private StoreNumberService mockStoreNumberService;
	@Mock
	private RoleService mockRoleService;

	@InjectMocks
	private UserService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(UserServiceTest.class);
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockUserRepository.findById(1L)).thenReturn(userModel);

		UserDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenRecordNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void createUserReturnsExpected() {
		UserDTO request = fixtureLoader.getObject("saveRequest", UserDTO.class).orElse(new UserDTO());
		when(mockStoreNumberService.findById(1L)).thenReturn(new StoreNumberDTO());
		when(mockUserRepository.save(any())).then(invocation -> invocation.getArgument(0));

		UserDTO result = service.createUser(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void createUserThrowsExceptionWhenEmailExists() {
		UserDTO request = fixtureLoader.getObject("saveRequest", UserDTO.class).orElse(new UserDTO());

		when(mockStoreNumberService.findById(1L)).thenReturn(new StoreNumberDTO());
		when(mockUserRepository.findByEmail(any())).thenReturn(Optional.of(new UserModel(2L)));

		assertThrows(EmailExistsException.class, () -> service.createUser(request));
	}

	@Test
	void createUserThrowsExceptionWhenUserNameExists() {
		UserDTO request = fixtureLoader.getObject("saveRequest", UserDTO.class).orElse(new UserDTO());

		when(mockStoreNumberService.findById(1L)).thenReturn(new StoreNumberDTO());
		when(mockUserRepository.findByUserName(any())).thenReturn(Optional.of(new UserModel()));

		assertThrows(UserNameExistsException.class, () -> service.createUser(request));
	}

	@Test
	void updateUserReturnsExpected() {
		UserDTO request = fixtureLoader.getObject("saveRequest", UserDTO.class).orElse(new UserDTO());
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockUserRepository.findById(1L)).thenReturn(userModel);
		when(mockStoreNumberService.findById(1L)).thenReturn(new StoreNumberDTO());

		UserDTO result = service.updateUser(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateUserThrowsExceptionWhenRecordNotFound() {
		UserDTO request = fixtureLoader.getObject("saveRequest", UserDTO.class).orElse(new UserDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.updateUser(request));
	}

	@Test
	void updateUserThrowsExceptionWhenEmailExists() {
		UserDTO request = fixtureLoader.getObject("saveRequest", UserDTO.class).orElse(new UserDTO());
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockStoreNumberService.findById(1L)).thenReturn(new StoreNumberDTO());
		when(mockUserRepository.findById(1L)).thenReturn(userModel);
		when(mockUserRepository.findByEmail(any())).thenReturn(Optional.of(new UserModel(2L)));

		assertThrows(EmailExistsException.class, () -> service.updateUser(request));
	}

	@Test
	void updateUserThrowsExceptionWhenUserNameExists() {
		UserDTO request = fixtureLoader.getObject("saveRequest", UserDTO.class).orElse(new UserDTO());
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockStoreNumberService.findById(1L)).thenReturn(new StoreNumberDTO());
		when(mockUserRepository.findById(1L)).thenReturn(userModel);
		when(mockUserRepository.findByUserName(any())).thenReturn(Optional.of(new UserModel(2L)));

		assertThrows(UserNameExistsException.class, () -> service.updateUser(request));
	}

	@Test
	void getAutocompleteReturnsExpected() {
		UserSearchResponseDTO user = new UserSearchResponseDTO(1L, "ANY", "NAME");
		when(mockUserRepository.findUserAutocomplete("ANY")).thenReturn(List.of(user));

		List<UserSearchResponseDTO> result = service.getAutocomplete("ANY");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByUserNameReturnsExpected() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockUserRepository.findByUserName("user")).thenReturn(userModel);

		UserDTO result = service.findUserByUserName("user");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByUserNameThrowsExceptionWhenRecordNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findUserByUserName("user"));
	}

	@Test
	void findUserModelByUserNameReturnsExpected() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockUserRepository.findByUserName("user")).thenReturn(userModel);

		UserModel result = service.findUserModelByUserName("user");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findUserModelByUserNameThrowsExceptionWhenRecordNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findUserModelByUserName("user"));
	}

	@Test
	void getUsersByFiltersReturnsExpected() {
		UserDTO request = new UserDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");

		when(mockUserRepository.getUsersByFilters(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(new UserDTO())));
		Page<UserDTO> result = service.getUsersByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void changeUserPasswordReturnsExpected() {
		UserDTO request = fixtureLoader.getObject("saveRequest", UserDTO.class).orElse(new UserDTO());
		UserModel userModel = fixtureLoader.getObject("userModel", UserModel.class).orElse(new UserModel());
		when(mockUserRepository.getById(any())).thenReturn(userModel);
		when(mockUserRepository.save(any())).then(invocation -> invocation.getArgument(0));

		assertDoesNotThrow(() -> service.changeUserPassword(request));
	}

	@Test
	void logInvalidCredentialsForUpdatesLoginAttempts() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockUserRepository.findByUserName(any())).thenReturn(userModel);
		when(mockUserRepository.save(any())).then(invocation -> invocation.getArgument(0));

		service.logInvalidCredentialsFor("user");

		UserModel user = userModel.get();
		assertThat(user.getLoginAttempts(), is(1));
		assertThat(user.getStatus().getCatalogId(), is(UserStatusCatalog.USER_STATUS_ACTIVE));
	}

	@Test
	void logInvalidCredentialsForUpdatesStatusAfterThresholdReached() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModelBeforeDeactivation", UserModel.class);
		when(mockUserRepository.findByUserName(any())).thenReturn(userModel);
		when(mockUserRepository.save(any())).then(invocation -> invocation.getArgument(0));

		service.logInvalidCredentialsFor("user");

		UserModel user = userModel.get();
		assertThat(user.getLoginAttempts(), is(5));
		assertThat(user.getStatus().getCatalogId(), is(UserStatusCatalog.USER_STATUS_INACTIVE));
	}

	@Test
	void clearLoginAttemptsUpdatesLoginAttempts() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockUserRepository.findByUserName(any())).thenReturn(userModel);
		when(mockUserRepository.save(any())).then(invocation -> invocation.getArgument(0));

		service.clearLoginAttempts("user");

		UserModel user = userModel.get();
		assertThat(user.getLoginAttempts(), is(0));
	}
}
