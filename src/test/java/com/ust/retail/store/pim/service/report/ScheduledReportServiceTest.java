package com.ust.retail.store.pim.service.report;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.util.ReflectionTestUtils;

import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduledReportServiceTest {
	@Mock
	private ReportService mockReportService;
	@Mock
	private StoreNumberService mockStoreNumberService;
	@Mock
	private EmailSend mockEmailSend;

	@InjectMocks
	private ScheduledReportService service;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(service, "customer", "UST");
		ReflectionTestUtils.setField(service, "host", "https://example.com");
		ReflectionTestUtils.setField(service, "customerBgColor", "black");
		ReflectionTestUtils.setField(service, "customerFgColor", "white");
		ReflectionTestUtils.setField(service, "contactAddress", "info@example.com");
	}

	@Test
	void runCsvDumpCompletesSuccessfully() {
		when(mockStoreNumberService.load()).thenReturn(List.of(new StoreNumberDTO(), new StoreNumberDTO()));
		FileSystemResource mockResource = mock(FileSystemResource.class);
		File mockFile = mock(File.class);
		when(mockResource.getFile()).thenReturn(mockFile);
		when(mockReportService.getUpcCsvDumpForExport(any())).thenReturn(mockResource);
		when(mockReportService.getRecipeCsvDumpForExport()).thenReturn(mockResource);
		when(mockReportService.getRecipeIngredientCsvDumpForExport()).thenReturn(mockResource);
		when(mockReportService.getRecipeAddOnCsvDumpForExport()).thenReturn(mockResource);

		assertDoesNotThrow(() -> service.runCsvDump());
	}
}
