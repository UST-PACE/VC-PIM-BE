package com.ust.retail.store.pim.controller.report;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.report.PurchaseOrderDraftedDTO;
import com.ust.retail.store.pim.dto.report.PurchaseOrderOrderedDTO;
import com.ust.retail.store.pim.dto.report.PurchaseOrderPendingApprovalDTO;
import com.ust.retail.store.pim.dto.report.PurchaseOrderReceptionDTO;
import com.ust.retail.store.pim.dto.report.PurchaseOrderReportDTO;
import com.ust.retail.store.pim.dto.report.PurchaseOrderReportRequestDTO;
import com.ust.retail.store.pim.dto.report.RecipeAddOnCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.RecipeCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.RecipeIngredientCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.UpcCsvDumpDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.service.report.ReportService;

@RestController
@RequestMapping(path = "/api/reports/p")
public class ReportController extends BaseController {
	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/generatedpurchaseorders/sd/{startDate}/ed/{endDate}")
	public ResponseEntity<Resource> generatedPurchaseOrdersReport(
			@PathVariable(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		return getResponse(reportService.getGeneratedPurchaseOrdersReport(startDate, endDate), Optional.empty());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/receiving/sd/{startDate}/ed/{endDate}")
	public ResponseEntity<Resource> receivingReport(
			@PathVariable(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		return getResponse(reportService.getReceivingReport(startDate, endDate), Optional.empty());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/returns/sd/{startDate}/ed/{endDate}")
	public ResponseEntity<Resource> returnsReport(
			@PathVariable(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		return getResponse(reportService.getReturnsReport(startDate, endDate), Optional.empty());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/inventorymovement/sd/{startDate}/ed/{endDate}")
	public ResponseEntity<Resource> inventoryMovementReport(
			@PathVariable(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		return getResponse(reportService.getInventoryMovementReport(startDate, endDate), Optional.empty());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/productionwaste/sd/{startDate}/ed/{endDate}")
	public ResponseEntity<Resource> productionWasteReport(
			@PathVariable(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		return getResponse(reportService.getProductionWasteReport(startDate, endDate), Optional.empty());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/unsoldproduct/sd/{startDate}/ed/{endDate}")
	public ResponseEntity<Resource> unsoldProductReport(
			@PathVariable(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		return getResponse(reportService.getUnsoldProductReport(startDate, endDate), Optional.empty());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/upcmaster/sn/{storeNumberId}/csv")
	public ResponseEntity<Resource> getUpcCsvDumpFile(@PathVariable Long storeNumberId) throws IOException {
		return getResponse(reportService.getUpcCsvDumpForExport(storeNumberId), Optional.of(String.format("upc_master_data_sn_%d_%tF", storeNumberId, new Date())));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/upcmaster/sn/{storeNumberId}")
	public List<UpcCsvDumpDTO> getUpcCsvDump(@PathVariable Long storeNumberId) {
		return reportService.getUpcCsvDump(storeNumberId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/upcmaster/csv")
	public ResponseEntity<Resource> getUpcMasterDumpFile() throws IOException {
		return getResponse(reportService.getUpcMasterDumpForExport(), Optional.of(String.format("upc_master_data_%tF", new Date())));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/upcmaster")
	public List<UpcMasterDTO> getUpcMasterDump() {
		return reportService.getUpcMasterDump();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/recipe/csv")
	public ResponseEntity<Resource> getRecipeCsvDumpFile() throws IOException {
		return getResponse(reportService.getRecipeCsvDumpForExport(), Optional.of(String.format("recipe_master_data_%tF", new Date())));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/recipe")
	public List<RecipeCsvDumpDTO> getRecipeCsvDump() {
		return reportService.getRecipeCsvDump();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/recipe/ingredients/csv")
	public ResponseEntity<Resource> getRecipeIngredientCsvDumpFile() throws IOException {
		return getResponse(reportService.getRecipeIngredientCsvDumpForExport(), Optional.of(String.format("recipe_ingredients_data_%tF", new Date())));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/recipe/ingredients")
	public List<RecipeIngredientCsvDumpDTO> getRecipeIngredientCsvDump() {
		return reportService.getRecipeIngredientCsvDump();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/recipe/addons/csv")
	public ResponseEntity<Resource> getRecipeAddOnCsvDumpFile() throws IOException {
		return getResponse(reportService.getRecipeAddOnCsvDumpForExport(), Optional.of(String.format("recipe_addon_data_%tF", new Date())));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/recipe/addons")
	public List<RecipeAddOnCsvDumpDTO> getRecipeAddOnCsvDump() {
		return reportService.getRecipeAddOnCsvDump();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/purchaseorder/sn/{storeNumberId}")
	public PurchaseOrderReportDTO getPurchaseOrderReport(@PathVariable Long storeNumberId) {
		return reportService.getPurchaseOrderReport(storeNumberId, false);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/purchaseorder/drafted")
	public Page<PurchaseOrderDraftedDTO> getDraftedPurchaseOrders(@RequestBody PurchaseOrderReportRequestDTO request) {
		return reportService.getDraftedPurchaseOrders(request);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/purchaseorder/pending")
	public Page<PurchaseOrderPendingApprovalDTO> getPendingPurchaseOrders(@RequestBody PurchaseOrderReportRequestDTO request) {
		return reportService.getPendingPurchaseOrders(request);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/purchaseorder/ordered")
	public Page<PurchaseOrderOrderedDTO> getOrderedPurchaseOrders(@RequestBody PurchaseOrderReportRequestDTO request) {
		return reportService.getOrderedPurchaseOrders(request);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/purchaseorder/reception")
	public Page<PurchaseOrderReceptionDTO> getPurchaseOrderReceptions(@RequestBody PurchaseOrderReportRequestDTO request) {
		return reportService.getPurchaseOrderReception(request);
	}

	private ResponseEntity<Resource> getResponse(ByteArrayResource resource, Optional<String> reportFileName) {
		String fileName = reportFileName.orElse("report");
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
		headers.add(HttpHeaders.PRAGMA, "no-cache");
		headers.add(HttpHeaders.EXPIRES, "0");
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".csv\"");

		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(resource.contentLength())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}

	private ResponseEntity<Resource> getResponse(FileSystemResource resource, Optional<String> reportFileName) throws IOException {
		String fileName = reportFileName.orElse("report");
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
		headers.add(HttpHeaders.PRAGMA, "no-cache");
		headers.add(HttpHeaders.EXPIRES, "0");
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".csv\"");

		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(resource.contentLength())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}

}
