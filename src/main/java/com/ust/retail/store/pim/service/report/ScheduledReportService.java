package com.ust.retail.store.pim.service.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.dto.report.PurchaseOrderReportDTO;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScheduledReportService {

	private final ReportService reportService;
	private final StoreNumberService storeNumberService;
	private final EmailSend emailSend;
	private final String customer;
	private final List<String> recipients;
	private final String host;
	private final String customerBgColor;
	private final String customerFgColor;
	private final String websiteUrl;
	private final String contactAddress;


	public ScheduledReportService(ReportService reportService,
								  StoreNumberService storeNumberService,
								  EmailSend emailSend,
								  @Value("${pim.customer-name}") String customer,
								  @Value("${pim.report.recipients}") List<String> recipients,
								  @Value("${email.host}") String host,
								  @Value("${pim.customer-bg-color}") String customerBgColor,
								  @Value("${pim.customer-fg-color}") String customerFgColor,
								  @Value("${email.website-url}") String websiteUrl,
								  @Value("${email.contact-address}") String contactAddress) {
		this.reportService = reportService;
		this.storeNumberService = storeNumberService;
		this.emailSend = emailSend;
		this.customer = customer;
		this.recipients = recipients;
		this.host = host;
		this.customerBgColor = customerBgColor;
		this.customerFgColor = customerFgColor;
		this.websiteUrl = websiteUrl;
		this.contactAddress = contactAddress;
	}

	@Scheduled(cron = "${pim.report.schedule.time}", zone = "${pim.schedule.time-zone}")
	@Transactional
	@Async
	public void runCsvDump() {
		log.info("Starting CSV Dump Report");
		List<String> storeNames = new ArrayList<>();
		List<File> attachments = new ArrayList<>();

		storeNumberService.load()
				.forEach(storeNum -> {
					storeNames.add(String.format("%d - %s", storeNum.getStoreNumId(), storeNum.getStoreName()));
					attachments.add(reportService.getUpcCsvDumpForExport(storeNum.getStoreNumId()).getFile());
				});
		attachments.add(reportService.getRecipeCsvDumpForExport().getFile());
		attachments.add(reportService.getRecipeIngredientCsvDumpForExport().getFile());
		attachments.add(reportService.getRecipeAddOnCsvDumpForExport().getFile());
		emailSend.send(
				recipients,
				String.format("CSV Master Dump [%s]",
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date())),
				Map.of(
						"customer", this.customer,
						"username", "Store Manager",
						"host", this.host,
						"customerBgColor", this.customerBgColor,
						"customerFgColor", this.customerFgColor,
						"email", this.contactAddress,
						"storeNames", storeNames),
				"csvDump",
				attachments);

		log.info("CSV Dump Report complete...");
	}

	@Scheduled(cron = "${pim.report.schedule.time}", zone = "${pim.schedule.time-zone}")
	@Transactional
	@Async
	public void runPurchaseOrderReport() {
		log.info("Starting Purchase Order Report");
		List<PurchaseOrderReportDTO> reports = storeNumberService.load().stream()
						.map(storeNum -> reportService.getPurchaseOrderReport(storeNum.getStoreNumId()))
						.collect(Collectors.toUnmodifiableList());

		if (!reports.isEmpty()){
			emailSend.send(
					recipients,
					String.format("Purchase Order Reports [%s]",
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date())),
					Map.of(
							"customer", this.customer,
							"username", "Store Manager",
							"host", this.host,
							"customerBgColor", this.customerBgColor,
							"customerFgColor", this.customerFgColor,
							"websiteUrl", this.websiteUrl,
							"email", this.contactAddress,
							"reports", reports),
					"purchaseOrderReport");
		}

		log.info("Purchase Order Report complete...");
	}
}
