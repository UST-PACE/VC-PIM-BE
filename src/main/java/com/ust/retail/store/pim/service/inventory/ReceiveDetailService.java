package com.ust.retail.store.pim.service.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.catalogs.ItemStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.PoStatusCatalog;
import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestUpdateDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResponseDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResumeResponseDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingResumeResponseDetailDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceptionNotificationRequestDTO;
import com.ust.retail.store.pim.exceptions.ItemReceivingException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.PoReceiveDetailModel;
import com.ust.retail.store.pim.model.inventory.PoReceiveWarningModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;
import com.ust.retail.store.pim.model.vendormaster.VendorContactModel;
import com.ust.retail.store.pim.repository.inventory.PoReceiveDetailRepository;
import com.ust.retail.store.pim.repository.inventory.PoReceiveWarningRepository;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderDetailRepository;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderRepository;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import com.ust.retail.store.pim.service.vendormaster.VendorMasterService;

@Service
public class ReceiveDetailService {

	private final PoReceiveDetailRepository poReceiveDetailRepository;
	private final PoReceiveWarningRepository poReceiveWarningRepository;
	private final PurchaseOrderRepository purchaseOrderRepository;
	private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
	private final InventoryService inventoryService;
	private final VendorMasterService vendorMasterService;
	private final StoreNumberService storeNumberService;
	private final AuthenticationFacade authenticationFacade;
	private final EmailSend emailSend;
	private final String customer;
	private final String host;
	private final String customerBgColor;
	private final String customerFgColor;
	private final String websiteUrl;
	private final String contactAddress;

	@Autowired
	public ReceiveDetailService(PoReceiveDetailRepository poReceiveDetailRepository,
								InventoryService inventoryService,
								PoReceiveWarningRepository poReceiveWarningRepository,
								PurchaseOrderRepository purchaseOrderRepository,
								PurchaseOrderDetailRepository purchaseOrderDetailRepository,
								VendorMasterService vendorMasterService,
								StoreNumberService storeNumberService,
								AuthenticationFacade authenticationFacade,
								EmailSend emailSend,
								@Value("${pim.customer-name}") String customer,
								@Value("${email.host}") String host,
								@Value("${pim.customer-bg-color}") String customerBgColor,
								@Value("${pim.customer-fg-color}") String customerFgColor,
								@Value("${email.website-url}") String websiteUrl,
								@Value("${email.contact-address}") String contactAddress) {
		super();
		this.poReceiveDetailRepository = poReceiveDetailRepository;
		this.poReceiveWarningRepository = poReceiveWarningRepository;
		this.purchaseOrderRepository = purchaseOrderRepository;
		this.inventoryService = inventoryService;
		this.purchaseOrderDetailRepository = purchaseOrderDetailRepository;
		this.vendorMasterService = vendorMasterService;
		this.storeNumberService = storeNumberService;
		this.authenticationFacade = authenticationFacade;
		this.emailSend = emailSend;
		this.customer = customer;
		this.host = host;
		this.websiteUrl = websiteUrl;
		this.customerBgColor = customerBgColor;
		this.customerFgColor = customerFgColor;
		this.contactAddress = contactAddress;
	}

	@Transactional
	public ReceivingResponseDTO receiveInventory(ReceivingRequestDTO receivingDTO) {

		PoReceiveDetailModel receiveDetail = poReceiveDetailRepository
				.save(receivingDTO.createModel(authenticationFacade.getCurrentUserId()));

		PurchaseOrderDetailModel poDetail = purchaseOrderDetailRepository
				.findById(receivingDTO.getPurchaseOrderDetailId())
				.orElseThrow(() -> new ResourceNotFoundException("Receive Inventory", "Purchase Order Detail id", receivingDTO.getPurchaseOrderDetailId()));

		if (poDetail.getItemStatus().getCatalogId() == ItemStatusCatalog.ITEM_STATUS_RECEIVED) {
			throw new ItemReceivingException(poDetail.getUpcMaster().getPrincipalUpc());
		}

		inventoryService.receiveInventory(receivingDTO, receiveDetail.getPoReceiveDetailId());

		poDetail.changeStatusReceived();
		poDetail.getPurchaseOrder().setPurchaseOrderStatus(PoStatusCatalog.PO_STATUS_IN_RECEPTION);

		purchaseOrderDetailRepository.save(poDetail);

		return new ReceivingResponseDTO(receivingDTO.getPurchaseOrderDetailId(), receiveDetail.getPoReceiveDetailId());
	}

	@Transactional
	public ReceivingResponseDTO updateReceiveInventory(ReceivingRequestUpdateDTO receivingUpdateDTO) {

		PoReceiveDetailModel receiveDetail = poReceiveDetailRepository.findById(receivingUpdateDTO.getPoReceiveDetailId())
				.orElseThrow(() -> new ResourceNotFoundException("Receive Inventory", "id", receivingUpdateDTO.getPoReceiveDetailId()));

		receiveDetail.getReceptionWarnings().clear();
		receiveDetail.updateInformation(receivingUpdateDTO, authenticationFacade.getCurrentUserId());

		poReceiveDetailRepository.save(receiveDetail);

		inventoryService.updateReceiveInventory(receivingUpdateDTO, receiveDetail.getPoReceiveDetailId());

		return new ReceivingResponseDTO(receivingUpdateDTO.getPurchaseOrderDetailId(),
				receiveDetail.getPoReceiveDetailId());
	}

	@Transactional
	public ReceivingResumeResponseDTO getReceivingSumary(Long purchaseOrderId) {

		List<ReceivingResumeResponseDetailDTO> details = poReceiveDetailRepository
				.findByPurchaseOrderDetailPurchaseOrderPurchaseOrderId(purchaseOrderId);

		for (ReceivingResumeResponseDetailDTO currentDetail : details) {
			currentDetail.addWarnings(
					poReceiveWarningRepository.getWarningByPoReceiveDetailId(currentDetail.getPoReceiveDetailId()));
		}

		return assembleResult(purchaseOrderId, details);
	}

	private ReceivingResumeResponseDTO assembleResult(Long purchaseOrderId, List<ReceivingResumeResponseDetailDTO> details) {
		boolean withEscalationContact = details.stream()
				.anyMatch(ReceivingResumeResponseDetailDTO::isHasError);

		PurchaseOrderModel orderModel = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", purchaseOrderId));
		Long vendorMasterId = orderModel.getVendorMaster().getVendorMasterId();

		VendorContactModel salesRepContact = vendorMasterService.getVendorSalesRepresentative(vendorMasterId)
				.orElseThrow(() -> new ResourceNotFoundException("VendorMaster", "id", vendorMasterId));

		VendorContactModel escalationContact = vendorMasterService.getVendorEscalationContact(vendorMasterId)
				.orElseThrow(() -> new ResourceNotFoundException("VendorMaster", "id", vendorMasterId));


		ReceivingResumeResponseDTO result = new ReceivingResumeResponseDTO(
				salesRepContact.getVendorContactName(),
				salesRepContact.getEmail(),
				withEscalationContact ? escalationContact.getVendorContactName() : null,
				withEscalationContact ? escalationContact.getEmail() : null,
				orderModel.getStoreNumber().getStoreNumId(),
				orderModel.getVendorMaster().getVendorName(),
				details.stream().map(ReceivingResumeResponseDetailDTO::getReceiver).distinct().findFirst().orElse(""),
				orderModel.getReceptionDate(),
				orderModel.getPurchaseOrderNum());

		result.setDetails(details);
		return result;
	}

	public boolean finishReception(@Valid Long purchaseOrderId) {

		List<PoReceiveWarningModel> incompleteReception = poReceiveWarningRepository
				.checkIncompleteReception(purchaseOrderId);

		PurchaseOrderModel po = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", purchaseOrderId));

		po.setPurchaseOrderStatus(incompleteReception.isEmpty() ? PoStatusCatalog.PO_STATUS_COMPLETED
				: PoStatusCatalog.PO_STATUS_INCOMPLETE);

		po.setReceptionDate(new Date());

		purchaseOrderRepository.save(po);

		return true;
	}

	public PoReceiveDetailModel findReceiveDetailsByPoOrderDetailId(Long purchaseOrderDetailId) {
		return poReceiveDetailRepository.findByPurchaseOrderDetailPurchaseOrderDetailId(purchaseOrderDetailId);
	}

	@Async
	@Transactional
	public void notifyReception(ReceptionNotificationRequestDTO request) {
		ReceivingResumeResponseDTO receivingSummary = getReceivingSumary(request.getPurchaseOrderId());
		List<String> recipients = new ArrayList<>(storeNumberService.getStoreManagerEmailList(receivingSummary.getStoreNumId()));
		recipients.add(receivingSummary.getSalesRepEmail());
		Optional.ofNullable(receivingSummary.getEscalationContactEmail()).ifPresent(recipients::add);
		Optional.ofNullable(request.getDriverEmail()).ifPresent(recipients::add);

		String purchaseOrderNum = receivingSummary.getPurchaseOrderNumber();
		emailSend.send(
				recipients,
				String.format("Purchase Order [%s] from %s was received", customer, purchaseOrderNum),
				Map.of(
						"customer", this.customer,
						"host", this.host,
						"websiteUrl", this.websiteUrl,
						"customerBgColor", this.customerBgColor,
						"customerFgColor", this.customerFgColor,
						"contactAddress", this.contactAddress,
						"driverName", Optional.ofNullable(request.getDriverName()).orElse("Vendor"),
						"po", receivingSummary,
						"complete", receivingSummary.getDetails().stream().filter(d -> !d.isHasError()).collect(Collectors.toUnmodifiableList()),
						"incomplete", receivingSummary.getDetails().stream().filter(ReceivingResumeResponseDetailDTO::isHasError).collect(Collectors.toUnmodifiableList())
				),
				EmailSend.TEMPLATE_NOTIFY_ORDER_RECEPTION);

	}
}
