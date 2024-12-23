package com.ust.retail.store.pim.common.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
public class PdfGenerator {
	public static final String PURCHASE_ORDER_TEMPLATE = "purchaseOrder";

	private final TemplateEngine pdfTemplateEngine;

	public PdfGenerator(TemplateEngine pdfTemplateEngine) {
		this.pdfTemplateEngine = pdfTemplateEngine;
	}

	public File generatePdfFile(Map<String, Object> templateValues, String fileName, String templateName) throws IOException {
		PdfTemplateConfiguration templateConfiguration =
				new PdfTemplateConfiguration(templateName, templateValues, pdfTemplateEngine);

		String htmlContent = templateConfiguration.getHTMLTemplateMsg();

		Path path = Paths.get(System.getProperty("java.io.tmpdir"), String.format("%s.pdf", fileName));
		File outputFile = path.toFile();

		try (FileOutputStream os = new FileOutputStream(outputFile)) {
			new PdfRendererBuilder()
					.withHtmlContent(htmlContent, null)
					.toStream(os)
					.run();
			return outputFile;
		}
	}
}
