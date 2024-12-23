package com.ust.retail.store.pim.service.recoverpassword;

import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.exceptions.UserNameDoesNotExistsException;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.repository.security.UserRepository;
import com.ust.retail.store.pim.util.DateUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RecoverPasswordService {

	private static final String KEY_SECRET_TOKEN = "UST_PIM$2021";

	private final UserRepository userRepository;
	private final EmailSend emailSend;

	private final String customer;
	private final String host;
	private final String customerBgColor;
	private final String customerFgColor;
	private final String websiteUrl;
	private final String contactAddress;

	public RecoverPasswordService(UserRepository userRepository,
								  EmailSend emailSend,
								  @Value("${pim.customer-name}") String customer,
								  @Value("${email.host}") String host,
								  @Value("${pim.customer-bg-color}") String customerBgColor,
								  @Value("${pim.customer-fg-color}") String customerFgColor,
								  @Value("${email.website-url}") String websiteUrl,
								  @Value("${email.contact-address}") String contactAddress) {
		super();
		this.userRepository = userRepository;
		this.emailSend = emailSend;
		this.customer = customer;
		this.host = host;
		this.customerBgColor = customerBgColor;
		this.customerFgColor = customerFgColor;
		this.websiteUrl = websiteUrl;
		this.contactAddress = contactAddress;
	}

	public void sendPasswordResetEmail(String userName) {

		UserModel user = userRepository.findByUserNameAndDeletedIsFalse(userName)
				.orElseThrow(() -> new UserNameDoesNotExistsException(userName));

		this.emailSend.send(
				List.of(user.getEmail()),
				String.format("%s Password reset request", customer),
				Map.of(
						"customer", this.customer,
						"username", user.getNameDesc(),
						"host", this.host,
						"customerBgColor", this.customerBgColor,
						"customerFgColor", this.customerFgColor,
						"websiteUrl", this.websiteUrl,
						"email", this.contactAddress,
						"token", generateToken(userName)
				),
				EmailSend.TEMPLATE_RECOVER_PASSWORD_WEB);
	}

	@Transactional
	public void changeUserPassword(String userName, String newPassword, String tokenRequest) {

		if (generateToken(userName).equals(tokenRequest)) {
			UserModel user = userRepository.findByUserNameAndDeletedIsFalse(userName)
					.orElseThrow(() -> new UserNameDoesNotExistsException(userName));
			user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
			userRepository.save(user);
		} else {
			throw new UserNameDoesNotExistsException(userName);
		}

	}

	public String generateToken(String userName) {
		String currentDate = DateUtils.dateAmericanFormat(new Date());

		return DigestUtils.md5Hex(KEY_SECRET_TOKEN + currentDate + userName);
	}

}
