package com.ust.retail.store.pim.common.email;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;
import java.util.Map;

class EmailSendTest {
	private static final String EMAIL_TEMPLATE_CUSTOMER = "customer";
	private static final String EMAIL_TEMPLATE_HOST = "host";
	private static final String EMAIL_TEMPLATE_WEBSITE_URL = "websiteUrl";
	private static final String EMAIL_TEMPLATE_CONTACT_ADDRESS = "email";
	private static final String EMAIL_TEMPLATE_USER_NAME = "username";
	public static final String EMAIL_TEMPLATE_CUSTOMER_BG_COLOR = "customerBgColor";
	public static final String EMAIL_TEMPLATE_CUSTOMER_FG_COLOR = "customerFgColor";


	@Tag("IntegrationTest")
	@Test
	void emailSentCorrectly() {
		ITemplateResolver resolver = templateResolver();
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(resolver);
		EmailSend emailSend = new EmailSend();
		ReflectionTestUtils.setField(emailSend, "host", "smtp.gmail.com");
		ReflectionTestUtils.setField(emailSend, "port", "465");
		ReflectionTestUtils.setField(emailSend, "userName", "no-reply@jedediahs.com");
		ReflectionTestUtils.setField(emailSend, "password", "pgiaoqepyqoctvib");
		ReflectionTestUtils.setField(emailSend, "bccEnabled", false);
		ReflectionTestUtils.setField(emailSend, "templateEngine", templateEngine);


		emailSend.send(
				List.of("cesar.romero@e-voluta.com", "cromeroz@gmail.com"),
				"Prueba de correo",
				Map.of(
						EMAIL_TEMPLATE_CUSTOMER, "Jedediah's",
						EMAIL_TEMPLATE_HOST, "https://pim.jedediahs.com",
						EMAIL_TEMPLATE_CUSTOMER_BG_COLOR, "#d85129;",
						EMAIL_TEMPLATE_CUSTOMER_FG_COLOR, "white;",
						EMAIL_TEMPLATE_WEBSITE_URL, "https://www.jedediahs.com",
						EMAIL_TEMPLATE_CONTACT_ADDRESS, "cs@jedediahs.com",
						EMAIL_TEMPLATE_USER_NAME, "Sales Rep"
				),
				EmailSend.TEMPLATE_NEW_PURCHASE_ORDER
		);
	}


	private ITemplateResolver templateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

		templateResolver.setPrefix("emailTemplates/");

		templateResolver.setSuffix(".html");

		templateResolver.setTemplateMode(TemplateMode.HTML);

		return templateResolver;

	}

}
