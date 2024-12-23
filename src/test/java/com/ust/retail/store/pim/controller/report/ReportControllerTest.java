package com.ust.retail.store.pim.controller.report;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.ust.retail.store.pim.dto.report.RecipeAddOnCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.RecipeCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.RecipeIngredientCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.UpcCsvDumpDTO;
import com.ust.retail.store.pim.service.report.ReportService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {
	@Mock
	private ReportService mockReportService;

	@InjectMocks
	private ReportController controller;

	@Test
	void generatedPurchaseOrdersReportReturnsExpected() {
		when(mockReportService.getGeneratedPurchaseOrdersReport(any(), any())).thenReturn(new ByteArrayResource(new byte[0]));

		ResponseEntity<Resource> result = controller.generatedPurchaseOrdersReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void receivingReportReturnsExpected() {
		when(mockReportService.getReceivingReport(any(), any())).thenReturn(new ByteArrayResource(new byte[0]));

		ResponseEntity<Resource> result = controller.receivingReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void returnsReportReturnsExpected() {
		when(mockReportService.getReturnsReport(any(), any())).thenReturn(new ByteArrayResource(new byte[0]));

		ResponseEntity<Resource> result = controller.returnsReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void inventoryMovementReportReturnsExpected() {
		when(mockReportService.getInventoryMovementReport(any(), any())).thenReturn(new ByteArrayResource(new byte[0]));

		ResponseEntity<Resource> result = controller.inventoryMovementReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void productionWasteReportReturnsExpected() {
		when(mockReportService.getProductionWasteReport(any(), any())).thenReturn(new ByteArrayResource(new byte[0]));

		ResponseEntity<Resource> result = controller.productionWasteReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void unsoldProductReportReturnsExpected() {
		when(mockReportService.getUnsoldProductReport(any(), any())).thenReturn(new ByteArrayResource(new byte[0]));

		ResponseEntity<Resource> result = controller.unsoldProductReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getUpcCsvDumpFileReturnsExpected() throws IOException {
		FileSystemResource mockResource = mock(FileSystemResource.class);
		when(mockResource.contentLength()).thenReturn(1L);
		when(mockReportService.getUpcCsvDumpForExport(1L)).thenReturn(mockResource);

		ResponseEntity<Resource> result = controller.getUpcCsvDumpFile(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getUpcCsvDumpReturnsExpected() {
		List<UpcCsvDumpDTO> result = controller.getUpcCsvDump(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeCsvDumpFileReturnsExpected() throws IOException {
		FileSystemResource mockResource = mock(FileSystemResource.class);
		when(mockResource.contentLength()).thenReturn(1L);
		when(mockReportService.getRecipeCsvDumpForExport()).thenReturn(mockResource);

		ResponseEntity<Resource> result = controller.getRecipeCsvDumpFile();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeCsvDumpReturnsExpected() {
		List<RecipeCsvDumpDTO> result = controller.getRecipeCsvDump();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeIngredientCsvDumpFileReturnsExpected() throws IOException {
		FileSystemResource mockResource = mock(FileSystemResource.class);
		when(mockResource.contentLength()).thenReturn(1L);
		when(mockReportService.getRecipeIngredientCsvDumpForExport()).thenReturn(mockResource);

		ResponseEntity<Resource> result = controller.getRecipeIngredientCsvDumpFile();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeIngredientCsvDumpReturnsExpected() {
		List<RecipeIngredientCsvDumpDTO> result = controller.getRecipeIngredientCsvDump();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeAddOnCsvDumpFileReturnsExpected() throws IOException {
		FileSystemResource mockResource = mock(FileSystemResource.class);
		when(mockResource.contentLength()).thenReturn(1L);
		when(mockReportService.getRecipeAddOnCsvDumpForExport()).thenReturn(mockResource);

		ResponseEntity<Resource> result = controller.getRecipeAddOnCsvDumpFile();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeAddOnCsvDumpReturnsExpected() {
		List<RecipeAddOnCsvDumpDTO> result = controller.getRecipeAddOnCsvDump();

		assertThat(result, is(notNullValue()));
	}
}
