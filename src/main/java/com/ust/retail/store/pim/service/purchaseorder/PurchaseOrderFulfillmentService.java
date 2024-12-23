package com.ust.retail.store.pim.service.purchaseorder;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.pim.common.catalogs.PoStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.dto.purchaseorder.operation.FulfillmentCandidateDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderFulfillmentRequestDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderModifyLineItemResultDTO;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderFulfillmentRepository;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PurchaseOrderFulfillmentService {

	private final PurchaseOrderFulfillmentRepository purchaseOrderFulfillmentRepository;
	private final PurchaseOrderService purchaseOrderService;
	private final StoreNumberService storeNumberService;
	private final AutoFulfillmentHelper autoFulfillmentHelper;
	private final EmailSend emailSend;

	private final String serviceUser;
	private final String customer;
	private final String host;
	private final String customerBgColor;
	private final String customerFgColor;
	private final String contactAddress;
	private final String websiteUrl;
	private final String basePath;

	public PurchaseOrderFulfillmentService(
			PurchaseOrderFulfillmentRepository purchaseOrderFulfillmentRepository,
			PurchaseOrderService purchaseOrderService,
			StoreNumberService storeNumberService,
			AutoFulfillmentHelper autoFulfillmentHelper,
			EmailSend emailSend,
			@Value("${pim.fulfillment.serviceUser}") String serviceUser,
			@Value("${pim.customer-name}") String customer,
			@Value("${email.host}") String host,
			@Value("${pim.customer-bg-color}") String customerBgColor,
			@Value("${pim.customer-fg-color}") String customerFgColor,
			@Value("${email.website-url}") String websiteUrl,
			@Value("${email.contact-address}") String contactAddress,
			@Value("${pim.fulfillment.poBasePath}") String basePath) {
		this.purchaseOrderFulfillmentRepository = purchaseOrderFulfillmentRepository;
		this.purchaseOrderService = purchaseOrderService;
		this.storeNumberService = storeNumberService;
		this.autoFulfillmentHelper = autoFulfillmentHelper;
		this.emailSend = emailSend;
		this.serviceUser = serviceUser;
		this.customer = customer;
		this.host = host;
		this.websiteUrl = websiteUrl;
		this.contactAddress = contactAddress;
		this.customerBgColor = customerBgColor;
		this.customerFgColor = customerFgColor;
		this.basePath = basePath;
	}

	@Scheduled(cron = "${pim.instruction.schedule.time}", zone = "${pim.schedule.time-zone}")
	@Transactional
	@Async
	public void runInventoryAutoFulfillment() {
		log.info("Starting Inventory Automatic Fulfillment");
		List<FulfillmentCandidateDTO> reorderCandidates = purchaseOrderFulfillmentRepository.getReorderCandidates(PoStatusCatalog.PO_STATUS_DRAFT, UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE);
		if (reorderCandidates.isEmpty()) {
			log.info("No fulfillment required. Terminating...");
			return;
		}

		log.info("Found {} candidate product(s)", reorderCandidates.size());

		Authentication authentication = new UsernamePasswordAuthenticationToken(serviceUser, "");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Set<PurchaseOrderModifyLineItemResultDTO> autoFulfillmentOrders = createAutoFulfillmentOrders(reorderCandidates);

		if (!autoFulfillmentOrders.isEmpty()) {
			log.info("Created or updated {} order(s)", autoFulfillmentOrders.size());
			autoFulfillmentOrders.stream()
					.collect(Collectors.groupingBy(PurchaseOrderModifyLineItemResultDTO::getStoreNumId))
					.forEach((storeNumId, orders) -> {
						String storeName = orders.get(0).getStoreName();
						log.info("Sending emails for store '{}'", storeName);
						emailSend.send(
								storeNumberService.getStoreManagerEmailList(storeNumId),
								String.format("[Store %s] Inventory Auto Fulfillment [%s]",
										storeName,
										new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date())),
								Map.of(
										"username", "Store Manager",
										"basePath", basePath,
										"customer", this.customer,
										"host", this.host,
										"customerBgColor", this.customerBgColor,
										"customerFgColor", this.customerFgColor,
										"websiteUrl", this.websiteUrl,
										"email", this.contactAddress,
										"orders", autoFulfillmentOrders),
								"autoFulfillment");
					});
		}
		log.info("Auto Fulfillment finished...");
	}

	private Set<PurchaseOrderModifyLineItemResultDTO> createAutoFulfillmentOrders(List<FulfillmentCandidateDTO> reorderCandidates) {

		Set<PurchaseOrderModifyLineItemResultDTO> list = new TreeSet<>(Comparator.comparing(PurchaseOrderModifyLineItemResultDTO::getPurchaseOrderNum));
		Map<String, PurchaseOrderModifyLineItemResultDTO> map = new HashMap<>();

		Map<String, Long> newOrdersMap = new HashMap<>();
		for (FulfillmentCandidateDTO candidate : reorderCandidates) {
			PurchaseOrderFulfillmentRequestDTO request = autoFulfillmentHelper.evaluateCandidate(candidate, newOrdersMap);

			PurchaseOrderModifyLineItemResultDTO result;
			if (request.isDetailToRemove()) {
				result = purchaseOrderService.removeLineItem(request.getPurchaseOrderDetailId());
			} else if (request.isNewOrder()) {
				result = purchaseOrderService.createOrderWithLineItem(autoFulfillmentHelper.getNewOrderLineItem(request));

				newOrdersMap.put(
						String.format("%d_%d", request.getVendorMasterId(), request.getStoreNumId()),
						result.getPurchaseOrderId());
			} else if (request.isDetailToUpdate()) {
				result = purchaseOrderService.updateLineItem(autoFulfillmentHelper.getUpdateDetailLineItem(request));
			} else if (!request.isToFulfill()) {
				log.info("Product '{}' at store '{}' (vendor '{}') has order '{}' with detail '{}' for '{}' units plus '{}' units in inventory", candidate.getUpcMasterId(), candidate.getStoreNumId(), candidate.getVendorMasterId(), request.getPurchaseOrderId(), candidate.getPurchaseOrderDetailId(), candidate.getTotalAmount(), candidate.getQtyInUnits());
				continue;
			} else {
				result = purchaseOrderService.appendLineItemToOrder(autoFulfillmentHelper.getAppendDetailLineItem(request));
			}
			map.put(result.getPurchaseOrderNum(), result);
		}
		list.addAll(map.values());
		return list;
	}

}
