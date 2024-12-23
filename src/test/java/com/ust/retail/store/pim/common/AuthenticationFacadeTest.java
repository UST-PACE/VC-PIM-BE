package com.ust.retail.store.pim.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.service.security.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationFacadeTest {
	private static final String ROLE = "ROLE_NAME";
	@Mock
	private UserService mockUserService;

	private Authentication authentication;

	@InjectMocks
	private AuthenticationFacade facade;

	@BeforeEach
	void setUp() {
		authentication = new TestingAuthenticationToken("user", "password", ROLE);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	void getCurrentUserIdReturnsExpected() {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(1L);
		when(mockUserService.findUserByUserName(authentication.getName())).thenReturn(userDTO);

		assertThat(facade.getCurrentUserId(), is(1L));
	}

	@Test
	void getCurrentUserDetailsReturnsExpected() {
		UserModel userModel = new UserModel(1L);
		when(mockUserService.findUserModelByUserName(authentication.getName())).thenReturn(userModel);

		assertThat(facade.getCurrentUserDetails(), is(userModel));
	}

	@Test
	void getPrincipalReturnsExpected() {
		assertThat(facade.getPrincipal(), is(authentication.getName()));
	}

	@Test
	void getAuthorityReturnsExpected() {
		assertThat(facade.getAuthority(), is(ROLE));
	}

	@Test
	void getUserStoreNumberReturnsExpected() {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(1L);
		userDTO.setStoreNumberId(1L);
		userDTO.setStoreNumberDesc("Store 1");

		when(mockUserService.findUserByUserName(authentication.getName())).thenReturn(userDTO);

		assertThat(facade.getUserStoreNumber(), allOf(
				hasProperty("storeNumberId", is(1L)),
				hasProperty("storeNumberName", is("Store 1"))
		));
	}

	@Test
	void logInvalidCredentialsCompletesSuccessfully() {
		assertDoesNotThrow(() -> facade.logInvalidCredentials("user"));
	}

	@Test
	void clearLoginAttemptsCompletesSuccessfully() {
		assertDoesNotThrow(() -> facade.clearLoginAttempts("user"));
	}
}
