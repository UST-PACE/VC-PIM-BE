package com.ust.retail.store.pim.service.recoverpassword;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.SecurityChallengeException;
import com.ust.retail.store.pim.exceptions.UserNameDoesNotExistsException;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.repository.security.UserRepository;

@Service
public class MobileRecoverPasswordService {

	private final UserRepository userRepository;
	private final AuthenticationFacade authenticationFacade;
	private final EmailSend emailSend;

	private final String customer;
	private final String host;
	private final String customerBgColor;
	private final String customerFgColor;
	private final String websiteUrl;
	private final String contactAddress;

	public MobileRecoverPasswordService(UserRepository userRepository,
										EmailSend emailSend,
										AuthenticationFacade authenticationFacade,
										@Value("${pim.customer-name}") String customer,
										@Value("${email.host}") String host,
										@Value("${pim.customer-bg-color}") String customerBgColor,
										@Value("${pim.customer-fg-color}") String customerFgColor,
										@Value("${email.website-url}") String websiteUrl,
										@Value("${email.contact-address}") String contactAddress) {
		super();
		this.userRepository = userRepository;
		this.emailSend = emailSend;
		this.authenticationFacade = authenticationFacade;
		this.customer = customer;
		this.host = host;
		this.customerBgColor = customerBgColor;
		this.customerFgColor = customerFgColor;
		this.websiteUrl = websiteUrl;
		this.contactAddress = contactAddress;
	}

	public void sendPasswordRecoveryEmail(String userName) {

		String recoveryCode = generateToken();
		String email = getUserEmail(userName, recoveryCode);

		this.emailSend.send(
				List.of(email),
				String.format("%s Password reset request", customer),
				Map.of(
						"customer", this.customer,
						"username", email,
						"host", this.host,
						"customerBgColor", this.customerBgColor,
						"customerFgColor", this.customerFgColor,
						"websiteUrl", this.websiteUrl,
						"email", this.contactAddress,
						"recoveryCode", recoveryCode
				),
				EmailSend.TEMPLATE_RECOVER_PASSWORD_MOBILE);

	}

	@Transactional
	public void changeUserPassword(String userName, String newPassword, String recoveryCode) {
		UserModel userModel = getUserWithValidCode(userName, recoveryCode);
		String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
		userModel.setPassword(encryptedPassword);
		userModel.setRecoveryCode(null);

		userRepository.save(userModel);
	}

	@Transactional
	public void changeUserPassword(String newPassword) {

		final String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
		final String userName = authenticationFacade.getPrincipal();

		UserModel user = userRepository.findByUserNameAndDeletedIsFalse(userName)
				.orElseThrow(() -> new ResourceNotFoundException("User", "UserName", userName));

		user.setPassword(encryptedPassword);
		userRepository.save(user);

	}

	private UserModel getUserWithValidCode(String userName, String recoveryCode) {
		UserModel userModel = userRepository.findByUserNameAndDeletedIsFalse(userName)
				.orElseThrow(() -> new UserNameDoesNotExistsException(userName));

		if (userModel.getRecoveryCode() == null) {
			throw new SecurityChallengeException("Not Available");
		}

		if (!userModel.getRecoveryCode().equals(recoveryCode)) {
			throw new SecurityChallengeException(recoveryCode);
		}
		return userModel;
	}

	private String getUserEmail(String userName, String recoveryCode) {

		String email;

		if (StringUtils.isBlank(userName)) {
			throw new UserNameDoesNotExistsException(userName);
		} else {
			email = checkEmailUser(userName, recoveryCode);
		}

		if (email == null)
			throw new UserNameDoesNotExistsException(userName);

		return email;
	}

	private String checkEmailUser(String userName, String recoveryCode) {

		String email = null;

		Optional<UserModel> user = userRepository.findByUserNameAndDeletedIsFalse(userName);

		if (user.isPresent()) {
			UserModel userModel = user.get();
			userModel.setRecoveryCode(recoveryCode);
			userRepository.save(userModel);
			email = userModel.getEmail();
		}
		return email;
	}

	private String generateToken() {
		return new Random().ints(0, 10)
				.limit(6)
				.mapToObj(String::valueOf)
				.collect(Collectors.joining(""));
	}
}
