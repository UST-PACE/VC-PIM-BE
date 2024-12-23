package com.ust.retail.store.pim.controller.external;

import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.report.sale.ExternalAdjustmentReportDTO;
import com.ust.retail.store.pim.dto.external.report.sale.ExternalInventoryReportDTO;
import com.ust.retail.store.pim.dto.external.report.sale.ExternalInventoryReportRequest;
import com.ust.retail.store.pim.service.external.ExternalReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/external/p/reports/sales")
@Validated
public class ExternalReportController {

	private final ExternalReportService externalReportService;

	public ExternalReportController(ExternalReportService externalReportService) {
		this.externalReportService = externalReportService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@PostMapping("/inventory/grocery")
	public ExternalApiResponse<List<ExternalInventoryReportDTO>> getGroceryOnHandInventory(@RequestBody @Valid ExternalInventoryReportRequest request) {
		return externalReportService.getGroceryOnHandInventory(request);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@PostMapping("/inventory/bistro")
	public ExternalApiResponse<List<ExternalInventoryReportDTO>> getBistroOnHandInventory(@RequestBody @Valid ExternalInventoryReportRequest request) {
		return externalReportService.getBistroOnHandInventory(request);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EXTERNAL_APIS')")
	@GetMapping("/adjustment/{reportDate}")
	public ExternalApiResponse<List<ExternalAdjustmentReportDTO>> getShrinkageReportForDates(@PathVariable("reportDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date reportDate) {
		return externalReportService.getShrinkageReportForDate(reportDate);
	}


}
