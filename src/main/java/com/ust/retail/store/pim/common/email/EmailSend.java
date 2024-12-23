package com.ust.retail.store.pim.common.email;

import com.evoluta.libraries.emailsender.HtmlEmailSender;
import com.evoluta.libraries.emailsender.conf.EmailMessage;
import com.evoluta.libraries.emailsender.conf.SMTPConfiguration;
import com.evoluta.libraries.emailsender.conf.TemplateConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EmailSend {

	public static final String TEMPLATE_RECOVER_PASSWORD_MOBILE = "mobileRecoveryPassword";
	public static final String TEMPLATE_RECOVER_PASSWORD_WEB = "recoveryPassword";

	public static final String TEMPLATE_NEW_PURCHASE_ORDER = "newPurchaseOrder";
	public static final String TEMPLATE_NEW_PURCHASE_ORDER_STORE_MANAGER = "newPurchaseOrderSM";
	public static final String TEMPLATE_INVALIDATE_ORDER = "invalidatedOrder";
	public static final String TEMPLATE_INVALIDATE_ORDER_STORE_MANAGER = "invalidatedOrderSM";
	public static final String TEMPLATE_NOTIFY_ORDER_RECEPTION = "notifyOrderReception";
	public static final String TEMPLATE_PURCHASE_ORDER_AUTHORIZATION = "purchaseOrderAuthorization";

	private SMTPConfiguration smtpConfiguration;

	@Value("${a.virtual.email.send.host}")
	private String host;
	@Value("${a.virtual.email.send.port}")
	private String port;
	@Value("${a.virtual.email.send.userName}")
	private String userName;
	@Value("${a.virtual.email.send.password}")
	private String password;
	@Value("${a.virtual.email.send.bcc.enabled:true}")
	private boolean bccEnabled;
	@Autowired
	private TemplateEngine templateEngine;

	public void send(List<String> emailAddresses, String subject, Map<String, Object> templateKeyValues,
					 String templateEmail) {

		initSMTP();

		TemplateConfiguration templateConfiguration = new TemplateConfiguration(templateEmail, templateKeyValues,
				templateEngine);

		EmailMessage emailMessage = new EmailMessage(userName, subject, emailAddresses, EmailMessage.NO_ATTACHMENTS,
				templateConfiguration);

		HtmlEmailSender emailSender = new HtmlEmailSender(smtpConfiguration, emailMessage);

		try {
			emailSender.send();
		} catch (IOException | AddressException e) {
			log.error("IOException or AddressException", e);
		} catch (MessagingException e) {
			log.error("MessagingException", e);
		} catch (Exception e) {
			log.error("Generic Exception", e);
		}
	}

	public void send(List<String> emailAddresses, String subject, Map<String, Object> templateKeyValues,
					 String templateEmail, List<File> attachments) {

		initSMTP();

		TemplateConfiguration templateConfiguration = new TemplateConfiguration(templateEmail, templateKeyValues,
				templateEngine);

		EmailMessage emailMessage = new EmailMessage(userName, subject, emailAddresses, attachments,
				templateConfiguration);

		HtmlEmailSender emailSender = new HtmlEmailSender(smtpConfiguration, emailMessage);

		try {
			emailSender.send();
		} catch (IOException | AddressException e) {
			log.error("IOException or AddressException", e);
		} catch (MessagingException e) {
			log.error("MessagingException", e);
		} catch (Exception e) {
			log.error("Generic Exception", e);
		}
	}

	private void initSMTP() {
		if (smtpConfiguration == null)
			smtpConfiguration = new SMTPConfiguration(host, port, userName, password, bccEnabled);
	}

}
