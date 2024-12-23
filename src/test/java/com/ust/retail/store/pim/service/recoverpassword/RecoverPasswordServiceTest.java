package com.ust.retail.store.pim.service.recoverpassword;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.exceptions.UserNameDoesNotExistsException;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.repository.security.UserRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecoverPasswordServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private UserRepository mockUserRepository;
	@Mock
	private EmailSend mockEmailSend;

	private RecoverPasswordService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(RecoverPasswordServiceTest.class);
	}

	@BeforeEach
	void setUp() {
		service = new RecoverPasswordService(mockUserRepository,
				mockEmailSend,
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

		assertDoesNotThrow(() -> service.sendPasswordResetEmail("userName"));
	}

	@Test
	void sendPasswordRecoveryEmailThrowsExceptionWhenUserNotFound() {
		assertThrows(UserNameDoesNotExistsException.class, () -> service.sendPasswordResetEmail("userName"));
	}

	@Test
	void sendPasswordRecoveryEmailThrowsExceptionWhenUserNameEmpty() {
		assertThrows(UserNameDoesNotExistsException.class, () -> service.sendPasswordResetEmail(""));
	}

	@Test
	void changeUserPasswordWithRecoveryCodeCompletesSuccessfully() {
		Optional<UserModel> userModel = fixtureLoader.getObject("userModel", UserModel.class);
		String tokenRequest = service.generateToken("userName");
		when(mockUserRepository.findByUserNameAndDeletedIsFalse("userName")).thenReturn(userModel);

		assertDoesNotThrow(() -> service.changeUserPassword("userName", "YYY", tokenRequest));
	}

	@Test
	void changeUserPasswordWithRecoveryCodeThrowsExceptionWhenUserDoesNotExist() {
		assertThrows(UserNameDoesNotExistsException.class, () -> service.changeUserPassword("userName", "YYY", "123456"));
	}

	@Test
	void changeUserPasswordWithRecoveryCodeThrowsExceptionWhenInvalidCode() {
		String tokenRequest = service.generateToken("userName");

		assertThrows(UserNameDoesNotExistsException.class, () -> service.changeUserPassword("userName", "YYY", tokenRequest));
	}
}
