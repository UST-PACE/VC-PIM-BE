package com.ust.retail.store.pim.common.pdf;

import com.evoluta.libraries.emailsender.conf.TemplateConfiguration;
import org.thymeleaf.TemplateEngine;

import java.util.Map;

public class PdfTemplateConfiguration extends TemplateConfiguration {
	public PdfTemplateConfiguration(String templateFileName, Map<String, Object> templateKeyValues, TemplateEngine templateEngine) {
		super(templateFileName, templateKeyValues, templateEngine);
	}
}
