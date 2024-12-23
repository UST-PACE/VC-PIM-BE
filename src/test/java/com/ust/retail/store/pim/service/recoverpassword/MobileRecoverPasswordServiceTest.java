package com.ust.retail.store.pim.service.recoverpassword;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.SecurityChallengeException;
import com.ust.retail.store.pim.exceptions.UserNameDoesNotExistsException;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.repository.security.UserRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MobileRecoverPasswordServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private UserRepository mockUserRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;
	@Mock
	private EmailSend mockEmailSend;

	private MobileRecoverPasswordService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(MobileRecoverPasswordServiceTest.class);
	}

	@BeforeEach
	void setUp() {
		service = new MobileRecoverPasswordService(mockUserRepository,
				mockEmailSend,
				mockAuthenticationFacade,
				"customer",
				"host",
				"black",
				"white",
				"address",
				"address");
	}

	@Test
	void sendPasswordRecoveryEmailCompletesSuccessfully() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockUserRepository.findByUserNameAndDeletedIsFalse("userName")).thenReturn(userModel);

		assertDoesNotThrow(() -> service.sendPasswordRecoveryEmail("userName"));
	}

	@Test
	void sendPasswordRecoveryEmailThrowsExceptionWhenUserNotFound() {
		assertThrows(UserNameDoesNotExistsException.class, () -> service.sendPasswordRecoveryEmail("userName"));
	}

	@Test
	void sendPasswordRecoveryEmailThrowsExceptionWhenUserNameEmpty() {
		assertThrows(UserNameDoesNotExistsException.class, () -> service.sendPasswordRecoveryEmail(""));
	}

	@Test
	void changeUserPasswordWithRecoveryCodeCompletesSuccessfully() {
		Optional<UserModel> userModelWithRecovery = fixtureLoader.getObject("userModelWithRecovery", UserModel.class);
		when(mockUserRepository.findByUserNameAndDeletedIsFalse("userName")).thenReturn(userModelWithRecovery);

		assertDoesNotThrow(() -> service.changeUserPassword("userName", "YYY", "123456"));
	}

	@Test
	void changeUserPasswordWithRecoveryCodeThrowsExceptionWhenUserDoesNotExist() {
		assertThrows(UserNameDoesNotExistsException.class, () -> service.changeUserPassword("userName", "YYY", "123456"));
	}

	@Test
	void changeUserPasswordWithRecoveryCodeThrowsExceptionWhenInvalidCode() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockUserRepository.findByUserNameAndDeletedIsFalse("userName")).thenReturn(userModel);

		assertThrows(SecurityChallengeException.class, () -> service.changeUserPassword("userName", "YYY", "123456"));
	}

	@Test
	void changeUserPasswordWithRecoveryCodeThrowsExceptionWhenDifferentCode() {
		Optional<UserModel> userModelWithRecovery = fixtureLoader.getObject("userModelWithRecovery", UserModel.class);
		when(mockUserRepository.findByUserNameAndDeletedIsFalse("userName")).thenReturn(userModelWithRecovery);

		assertThrows(SecurityChallengeException.class, () -> service.changeUserPassword("userName", "YYY", "654321"));
	}

	@Test
	void changePasswordCompletesSuccessfully() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		when(mockAuthenticationFacade.getPrincipal()).thenReturn("userName");
		when(mockUserRepository.findByUserNameAndDeletedIsFalse("userName")).thenReturn(userModel);

		assertDoesNotThrow(() -> service.changeUserPassword("YYYY"));
	}

	@Test
	void changeUserPasswordThrowsExceptionWhenUserNotFound() {
		when(mockAuthenticationFacade.getPrincipal()).thenReturn("userName");

		assertThrows(ResourceNotFoundException.class, () -> service.changeUserPassword("YYY"));
	}
}
