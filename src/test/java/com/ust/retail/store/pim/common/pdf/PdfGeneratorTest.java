package com.ust.retail.store.pim.common.pdf;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;

import com.ust.retail.store.PimAPI;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDTO;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDetailDTO;
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorContactDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class PdfGeneratorTest {
	@Test
	@Tag("Slow")
	void generatePdfFile() throws Exception {
		TemplateEngine pdfTemplateEngine = new PimAPI().pdfTemplateEngine();
		pdfTemplateEngine.setDialect(new SpringStandardDialect());
		PdfGenerator generator = new PdfGenerator(pdfTemplateEngine);
		PurchaseOrderDTO po = new PurchaseOrderDTO(
				1L,
				1L,
				"Vendor Name",
				1L,
				"Draft",
				1L,
				"Store Name",
				new UserDTO(),
				"A-0000000001-001",
				1000.0,
				10.0,
				0.0,
				0.0,
				990.0,
				null,
				null,
				new Date(),
				false,
				null,
				null,
				false,
				null,
				LongStream.rangeClosed(1, 7).mapToObj(this::getDetail).collect(Collectors.toList())
		);


		File file = generator.generatePdfFile(
				Map.of(
						"host", "https://pim-qa.example.com",
						"po", po,
						"vendor", new VendorMasterDTO(),
						"store", new StoreNumberDTO(),
						"user", new UserDTO(),
						"vendorContact", new VendorContactDTO()
				),
				"A-0000000001-001",
				"purchaseOrder");

		MatcherAssert.assertThat(file, is(notNullValue()));
	}

	private PurchaseOrderDetailDTO getDetail(Long index) {
		double productCost = 50.0 + index;
		int caseNum = 1 + index.intValue();
		int palletNum = 1 + index.intValue();
		int totalAmount = (caseNum * 10) + (palletNum * 20);
		double originalCost = totalAmount * productCost;
		return new PurchaseOrderDetailDTO(
				index,
				index,
				index,
				"My Product " + index,
				String.valueOf(112233445566L + index),
				String.valueOf(665544332211L + index),
				Math.toIntExact(10 + index),
				"FG",
				"Pz",
				productCost,
				caseNum,
				null,
				palletNum,
				null,
				totalAmount,
				originalCost,
				0.0,
				originalCost,
				50.0,
				false);
	}
}
