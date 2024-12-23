package com.ust.retail.store.pim.service.purchaseorder;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.catalogs.PoStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.common.email.EmailSend;
import com.ust.retail.store.pim.common.pdf.PdfGenerator;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.PurchaseOrdersDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionDTO;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDTO;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDetailDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.*;
import com.ust.retail.store.pim.dto.purchaseorder.screens.PurchaseOrderPendingFulfillmentDTO;
import com.ust.retail.store.pim.dto.purchaseorder.screens.PurchaseOrderUpcInformationDTO;
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorContactDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.exceptions.*;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderDetailRepository;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderFulfillmentRepository;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcVendorDetailsRepository;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.service.promotion.PromotionService;
import com.ust.retail.store.pim.service.security.RoleService;
import com.ust.retail.store.pim.service.security.UserService;
import com.ust.retail.store.pim.service.vendormaster.VendorMasterService;
import com.ust.retail.store.pim.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseOrderService {

	private static final List<Long> UPDATABLE_STATUS_LIST = List.of(
			PoStatusCatalog.PO_STATUS_DRAFT,
			PoStatusCatalog.PO_STATUS_PENDING_AUTHORIZATION,
			PoStatusCatalog.PO_STATUS_ORDERED);
	private static final String PURCHASE_ORDER_RESOURCE_NAME = "Purchase Order";
	private static final String RESOURCE_ID_FIELD = "id";
	private static final int INCREASE = 1;
	private static final int DECREASE = -1;
	private static final String EMAIL_TEMPLATE_CUSTOMER = "customer";
	private static final String EMAIL_TEMPLATE_HOST = "host";
	private static final String EMAIL_TEMPLATE_WEBSITE_URL = "websiteUrl";
	private static final String EMAIL_TEMPLATE_CONTACT_ADDRESS = "email";
	private static final String EMAIL_TEMPLATE_USER_NAME = "username";
	private static final String EMAIL_TEMPLATE_REQUESTING_USER = "requestingUser";
	private static final String EMAIL_TEMPLATE_PURCHASE_ORDER = "order";
	private static final String EMAIL_TEMPLATE_PURCHASE_ORDER_NUM = "purchaseOrderNum";
	private static final String EMAIL_TEMPLATE_PURCHASE_ORDER_BASE_PATH = "basePath";
	private static final String EMAIL_TEMPLATE_VENDOR_NAME = "vendorName";
	private static final String EMAIL_TEMPLATE_VENDOR_CREDIT_MODIFIED = "vendorCreditModified";
	private static final String EMAIL_TEMPLATE_CREDIT_AMOUNT = "creditAmount";
	public static final String EMAIL_TEMPLATE_CUSTOMER_BG_COLOR = "customerBgColor";
	public static final String EMAIL_TEMPLATE_CUSTOMER_FG_COLOR = "customerFgColor";

	private final PurchaseOrderRepository purchaseOrderRepository;
	private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
	private final PurchaseOrderFulfillmentRepository purchaseOrderFulfillmentRepository;
	private final UpcMasterRepository upcMasterRepository;
	private final VendorMasterService vendorMasterService;
	private final StoreNumberService storeNumberService;
	private final InventoryService inventoryService;
	private final PromotionService promotionService;
	private final UserService userService;
	private final AuthenticationFacade authenticationFacade;
	private final PurchaseOrderNumberGenerator purchaseOrderNumberGenerator;
	private final PdfGenerator pdfGenerator;
	private final EntityManager entityManager;
	private final String customer;
	private final String host;
	private final String customerBgColor;
	private final String customerFgColor;
	private final String websiteUrl;
	private final String contactAddress;
	private final String poBasePath;
	private final EmailSend emailSend;
	private final UpcVendorDetailsRepository upcVendorDetailsRepository;

	public PurchaseOrderService(
			PurchaseOrderRepository purchaseOrderRepository,
			PurchaseOrderDetailRepository purchaseOrderDetailRepository,
			PurchaseOrderFulfillmentRepository purchaseOrderFulfillmentRepository,
			UpcMasterRepository upcMasterRepository,
			VendorMasterService vendorMasterService,
			StoreNumberService storeNumberService,
			InventoryService inventoryService,
			PromotionService promotionService,
			UserService userService,
			AuthenticationFacade authenticationFacade,
			PurchaseOrderNumberGenerator purchaseOrderNumberGenerator,
			PdfGenerator pdfGenerator,
			EntityManager entityManager,
			@Value("${pim.customer-name}") String customer,
			@Value("${email.host}") String host,
			@Value("${pim.customer-bg-color}") String customerBgColor,
			@Value("${pim.customer-fg-color}") String customerFgColor,
			@Value("${email.website-url}") String websiteUrl,
			@Value("${email.contact-address}") String contactAddress,
			@Value("${pim.fulfillment.poBasePath}") String poBasePath,
			EmailSend emailSend,
			UpcVendorDetailsRepository upcVendorDetailsRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
		this.purchaseOrderDetailRepository = purchaseOrderDetailRepository;
		this.purchaseOrderFulfillmentRepository = purchaseOrderFulfillmentRepository;
		this.upcMasterRepository = upcMasterRepository;
		this.vendorMasterService = vendorMasterService;
		this.storeNumberService = storeNumberService;
		this.inventoryService = inventoryService;
		this.promotionService = promotionService;
		this.userService = userService;
		this.authenticationFacade = authenticationFacade;
		this.purchaseOrderNumberGenerator = purchaseOrderNumberGenerator;
		this.pdfGenerator = pdfGenerator;
		this.entityManager = entityManager;
		this.customer = customer;
		this.host = host;
		this.customerBgColor = customerBgColor;
		this.customerFgColor = customerFgColor;
		this.websiteUrl = websiteUrl;
		this.contactAddress = contactAddress;
		this.poBasePath = poBasePath;
		this.emailSend = emailSend;
		this.upcVendorDetailsRepository = upcVendorDetailsRepository;
	}

	public PurchaseOrderDTO findById(Long purchaseOrderId) {
		PurchaseOrderDTO result = purchaseOrderRepository.findById(purchaseOrderId)
				.map(m -> new PurchaseOrderDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException(PURCHASE_ORDER_RESOURCE_NAME, RESOURCE_ID_FIELD, purchaseOrderId));

		result.setApprovableByUser(currentUserCanApprove());

		return result;
	}

	public Page<PurchaseOrderDetailDTO> findDetailsById(PurchaseOrderFindDetailsByIdRequestDTO dto) {
		return purchaseOrderRepository.getOrderDetailPage(dto.getPurchaseOrderId(), dto.createPageable())
				.map(m -> new PurchaseOrderDetailDTO().parseToDTO(m));
	}

	public Page<PurchaseOrderFilterResultDTO> findOrdersByFilters(PurchaseOrderFilterRequestDTO dto) {
		return purchaseOrderRepository.findByFilters(
				dto.getVendorMasterId(),
				dto.getVendorMasterCode(),
				dto.getVendorMasterName(),
				dto.getStoreNumId(),
				dto.getPurchaseOrderNum(),
				dto.getPrincipalUpc(),
				dto.getProductName(),
				dto.getStatusId(),
				DateUtils.atStartOfDay(dto.getStartSendAt()),
				DateUtils.atEndOfDay(dto.getEndSendAt()),
				DateUtils.atStartOfDay(dto.getStartCreateAt()),
				DateUtils.atEndOfDay(dto.getEndCreateAt()),
				dto.createPageable());
	}

	public List<PurchaseOrderPendingFulfillmentDTO> findOrdersPendingFulfillmentByProduct(Long upcMasterId) {
		return purchaseOrderFulfillmentRepository.findByStatusAndProduct(PoStatusCatalog.PO_STATUS_ORDERED, upcMasterId);
	}

	@Transactional
	public synchronized PurchaseOrderModifyLineItemResultDTO createOrderWithLineItem(PurchaseOrderAddProductRequestDTO dto) {
		try {
			PurchaseOrderDetailModel detailModel = getDetailModelFromRequest(dto);

			PurchaseOrderModel model = purchaseOrderRepository.saveAndFlush(createNewModel(detailModel, dto));
			entityManager.refresh(model);

			return new PurchaseOrderModifyLineItemResultDTO().parseToDTO(model);
		} catch (UpcNotActiveException e) {
			PurchaseOrderModifyLineItemResultDTO errorResult = new PurchaseOrderModifyLineItemResultDTO();
			String upc = upcMasterRepository.findById(dto.getUpcMasterId()).map(UpcMasterModel::getPrincipalUpc).orElse(null);
			errorResult.getErrors().add(new PurchaseOrderModifyLineItemResultDTO.LineItemError(upc, e.getErrorCode()));
			return errorResult;
		}
	}

	@Transactional
	public synchronized PurchaseOrderModifyLineItemResultDTO createOrUpdateOrderWithMultipleLineItems(PurchaseOrderAddBulkProductRequestDTO dto) {
		AtomicReference<PurchaseOrderModifyLineItemResultDTO> resultRef = new AtomicReference<>();
		List<PurchaseOrderAddBulkProductRequestDTO.BulkLineItem> bulkLineItemList = dto.getProducts();

		Set<PurchaseOrderModifyLineItemResultDTO.LineItemError> errors = new HashSet<>();

		bulkLineItemList.stream()
				.map(lineItem -> new PurchaseOrderAddProductRequestDTO(
						dto.getPurchaseOrderId(),
						dto.getVendorMasterId(),
						dto.getStoreNumId(),
						lineItem.getUpcMasterId(),
						lineItem.getCaseNum(),
						lineItem.getPalletNum(),
						lineItem.getTotalAmount(),
						false))
				.forEach(addRequest -> {
					PurchaseOrderModifyLineItemResultDTO lineItemResultDTO;
					if (dto.getPurchaseOrderId() == null) {
						lineItemResultDTO = createOrderWithLineItem(addRequest);
						errors.addAll(lineItemResultDTO.getErrors());
						dto.setPurchaseOrderId(lineItemResultDTO.getPurchaseOrderId());
					} else {
						lineItemResultDTO = appendLineItemToOrder(addRequest);
						errors.addAll(lineItemResultDTO.getErrors());
					}
					resultRef.set(lineItemResultDTO);
				});

		PurchaseOrderModifyLineItemResultDTO result = resultRef.get();
		result.getErrors().addAll(errors);
		return result;
	}

	@Transactional
	public synchronized PurchaseOrderModifyLineItemResultDTO appendLineItemToOrder(PurchaseOrderAddProductRequestDTO dto) {
		PurchaseOrderModel purchaseOrder = getPurchaseOrderById(dto.getPurchaseOrderId());

		try {
			PurchaseOrderDetailModel detail = getDetailModelFromRequest(dto);
			purchaseOrder.addDetail(detail);
			String initialPurchaseOrderNum = purchaseOrder.getPurchaseOrderNum();
			boolean revisionUpdated = updateModel(purchaseOrder);

			boolean creditRestored = false;
			if (revisionUpdated) {
				creditRestored = modifyCredit(purchaseOrder, INCREASE);
			}

			purchaseOrderDetailRepository.save(detail);
			entityManager.merge(purchaseOrder);
			entityManager.flush();
			entityManager.refresh(purchaseOrder);

			return new PurchaseOrderModifyLineItemResultDTO()
					.parseToDTO(purchaseOrder)
					.setRevisionUpdated(revisionUpdated, initialPurchaseOrderNum)
					.setVendorCreditModified(creditRestored, purchaseOrder.getAppliedVendorCredit());
		} catch (UpcNotActiveException e) {
			PurchaseOrderModifyLineItemResultDTO errorResult = new PurchaseOrderModifyLineItemResultDTO();
			errorResult.setPurchaseOrderId(dto.getPurchaseOrderId());
			String upc = upcMasterRepository.findById(dto.getUpcMasterId()).map(UpcMasterModel::getPrincipalUpc).orElse(null);
			errorResult.getErrors().add(new PurchaseOrderModifyLineItemResultDTO.LineItemError(upc, e.getErrorCode()));
			return errorResult;
		}
	}

	@Transactional
	public synchronized PurchaseOrderModifyLineItemResultDTO updateLineItem(PurchaseOrderModifyLineItemRequestDTO lineItem) {
		PurchaseOrderModel purchaseOrder = getPurchaseOrderByDetailId(lineItem.getPurchaseOrderDetailId());

		PurchaseOrderDetailModel detailToUpdate = purchaseOrder.getDetails()
				.get(purchaseOrder.getDetails().indexOf(new PurchaseOrderDetailModel(lineItem.getPurchaseOrderDetailId())));

		int caseNum = lineItem.getCaseNum();
		int palletNum = lineItem.getPalletNum();
		int totalUnits = (detailToUpdate.getUnitsPerCase() * caseNum) + (detailToUpdate.getUnitsPerPallet() * palletNum);
		if (totalUnits != lineItem.getTotalAmount()) {
			totalUnits = lineItem.getTotalAmount();
			caseNum = 0;
			palletNum = 0;
		}

		if (Objects.nonNull(detailToUpdate.getPromotionType())) {
			PromotionDTO promotionDTO = new PromotionDTO().withBasicInfo(detailToUpdate.getPromotionType().getCatalogId(), detailToUpdate.getPromotionQty());
			DiscountStrategy strategy = DiscountHelper.getDiscountStrategyFor(promotionDTO.getPromotionTypeId());
			Double discount = strategy.calculateDiscount(detailToUpdate.getProductCost(), totalUnits, promotionDTO.getDiscount());
			detailToUpdate.setDiscountInformation(discount, promotionDTO);
		}

		Double originalCost = totalUnits * detailToUpdate.getProductCost();
		Double finalCost = originalCost - detailToUpdate.getDiscount();
		detailToUpdate.updateCalculations(caseNum, palletNum, totalUnits, originalCost, finalCost);
		String initialPurchaseOrderNum = purchaseOrder.getPurchaseOrderNum();
		boolean revisionUpdated = updateModel(purchaseOrder);

		boolean creditRestored = false;
		if (revisionUpdated) {
			creditRestored = modifyCredit(purchaseOrder, INCREASE);
		}

		PurchaseOrderModel model = purchaseOrderRepository.saveAndFlush(purchaseOrder);
		entityManager.refresh(model);

		return new PurchaseOrderModifyLineItemResultDTO()
				.parseToDTO(model)
				.setRevisionUpdated(revisionUpdated, initialPurchaseOrderNum)
				.setVendorCreditModified(creditRestored, purchaseOrder.getAppliedVendorCredit());
	}

	@Transactional
	public synchronized PurchaseOrderModifyLineItemResultDTO removeLineItem(Long purchaseOrderDetailId) {
		PurchaseOrderModel purchaseOrder = getPurchaseOrderByDetailId(purchaseOrderDetailId);

		purchaseOrder.removeDetail(new PurchaseOrderDetailModel(purchaseOrderDetailId));
		String initialPurchaseOrderNum = purchaseOrder.getPurchaseOrderNum();
		boolean revisionUpdated = updateModel(purchaseOrder);

		boolean creditRestored = false;
		if (revisionUpdated) {
			creditRestored = modifyCredit(purchaseOrder, INCREASE);
		}

		PurchaseOrderModel model = purchaseOrderRepository.saveAndFlush(purchaseOrder);
		entityManager.refresh(model);

		return new PurchaseOrderModifyLineItemResultDTO()
				.parseToDTO(model)
				.setRevisionUpdated(revisionUpdated, initialPurchaseOrderNum)
				.setVendorCreditModified(creditRestored, purchaseOrder.getAppliedVendorCredit());
	}

	@Transactional
	public synchronized PurchaseOrderModifyStatusResultDTO saveDraft(PurchaseOrderSaveRequestDTO dto) {
		PurchaseOrderModel purchaseOrder = getPurchaseOrderById(dto.getPurchaseOrderId());

		String initialPurchaseOrderNum = purchaseOrder.getPurchaseOrderNum();
		boolean revisionUpdated = updateRevision(purchaseOrder);

		boolean creditRestored = false;
		if (revisionUpdated) {
			creditRestored = modifyCredit(purchaseOrder, INCREASE);
		}
		purchaseOrder.setDraftInformation(dto);

		PurchaseOrderModel model = purchaseOrderRepository.saveAndFlush(purchaseOrder);
		entityManager.refresh(model);

		return new PurchaseOrderModifyStatusResultDTO()
				.parseToDTO(model)
				.setRevisionUpdated(revisionUpdated, initialPurchaseOrderNum)
				.setVendorCreditModified(creditRestored, purchaseOrder.getAppliedVendorCredit());
	}

	@Transactional
	public synchronized PurchaseOrderModifyStatusResultDTO processPurchaseOrder(PurchaseOrderSaveRequestDTO dto) {
		PurchaseOrderModel order = getPurchaseOrderById(dto.getPurchaseOrderId());
		order.setProcessedInformation(dto, new Date());
		boolean shouldAuthorize = order.getFinalCost() > 1000d;

		boolean creditApplied = false;
		if (shouldAuthorize) {
			order.setPendingAuthorizationInformation(dto);
		} else {
			creditApplied = modifyCredit(order, DECREASE);
		}

		PurchaseOrderModel model = purchaseOrderRepository.saveAndFlush(order);
		entityManager.refresh(model);

		return new PurchaseOrderModifyStatusResultDTO()
				.parseToDTO(model)
				.setVendorCreditModified(creditApplied, order.getAppliedVendorCredit())
				.setToAuthorize(shouldAuthorize);
	}

	@Transactional
	public synchronized PurchaseOrderModifyStatusResultDTO authorizePurchaseOrder(PurchaseOrderSaveRequestDTO dto) {
		if (!currentUserCanApprove()) {
			throw new PurchaseOrderAuthorizationException();
		}

		PurchaseOrderModel order = getPurchaseOrderById(dto.getPurchaseOrderId());
		order.setProcessedInformation(dto, new Date());

		boolean creditApplied = modifyCredit(order, DECREASE);

		PurchaseOrderModel model = purchaseOrderRepository.saveAndFlush(order);
		entityManager.refresh(model);

		return new PurchaseOrderModifyStatusResultDTO()
				.parseToDTO(model)
				.setVendorCreditModified(creditApplied, order.getAppliedVendorCredit());
	}

	@Transactional
	public synchronized PurchaseOrderModifyLineItemResultDTO deleteById(Long purchaseOrderId) {
		PurchaseOrderModel purchaseOrder = getPurchaseOrderById(purchaseOrderId);

		String initialPurchaseOrderNum = purchaseOrder.getPurchaseOrderNum();
		boolean firstRevision = purchaseOrderNumberGenerator.isFirstRevision(initialPurchaseOrderNum);
		boolean revisionUpdated = updateModel(purchaseOrder);
		PurchaseOrderModifyLineItemResultDTO resultDTO = new PurchaseOrderModifyLineItemResultDTO()
				.parseToDTO(purchaseOrder)
				.setRevisionUpdated(revisionUpdated, initialPurchaseOrderNum)
				.setEscalationWarning(!firstRevision);

		purchaseOrderRepository.delete(purchaseOrder);

		return resultDTO;
	}

	@Async
	@Transactional
	public void sendOrderInvalidatedEmailToSalesRepresentative(Long vendorMasterId, String previousPurchaseOrderNum) {
		vendorMasterService.getVendorSalesRepresentative(vendorMasterId).ifPresent(salesRep ->
				emailSend.send(
						List.of(salesRep.getEmail()),
						String.format("Purchase Order [%s] from %s Cancellation / Modification", previousPurchaseOrderNum, customer),
						Map.of(
								EMAIL_TEMPLATE_CUSTOMER, this.customer,
								EMAIL_TEMPLATE_HOST, this.host,
								EMAIL_TEMPLATE_CUSTOMER_BG_COLOR, this.customerBgColor,
								EMAIL_TEMPLATE_CUSTOMER_FG_COLOR, this.customerFgColor,
								EMAIL_TEMPLATE_WEBSITE_URL, this.websiteUrl,
								EMAIL_TEMPLATE_CONTACT_ADDRESS, this.contactAddress,
								EMAIL_TEMPLATE_USER_NAME, salesRep.getVendorContactName(),
								EMAIL_TEMPLATE_PURCHASE_ORDER_NUM, previousPurchaseOrderNum
						),
						EmailSend.TEMPLATE_INVALIDATE_ORDER));
	}

	@Async
	@Transactional
	public void sendOrderInvalidatedEmailToEscalationContact(Long vendorMasterId, String previousPurchaseOrderNum) {
		vendorMasterService.getVendorEscalationContact(vendorMasterId).ifPresent(escalationContact ->
				emailSend.send(
						List.of(escalationContact.getEmail()),
						String.format("Purchase Order [%s] from %s was invalidated", previousPurchaseOrderNum, customer),
						Map.of(
								EMAIL_TEMPLATE_CUSTOMER, this.customer,
								EMAIL_TEMPLATE_HOST, this.host,
								EMAIL_TEMPLATE_CUSTOMER_BG_COLOR, this.customerBgColor,
								EMAIL_TEMPLATE_CUSTOMER_FG_COLOR, this.customerFgColor,
								EMAIL_TEMPLATE_WEBSITE_URL, this.websiteUrl,
								EMAIL_TEMPLATE_CONTACT_ADDRESS, this.contactAddress,
								EMAIL_TEMPLATE_USER_NAME, escalationContact.getVendorContactName(),
								EMAIL_TEMPLATE_PURCHASE_ORDER_NUM, previousPurchaseOrderNum
						),
						EmailSend.TEMPLATE_INVALIDATE_ORDER));
	}

	@Async
	@Transactional
	public void sendOrderInvalidatedEmailToStoreManager(Long storeNumId, String previousPurchaseOrderNum, boolean vendorCreditModified, Long vendorMasterId, Double creditAmount) {
		List<String> storeManagerEmailList = storeNumberService.getStoreManagerEmailList(storeNumId);

		if (!storeManagerEmailList.isEmpty()) {
			String subject = String.format("Purchase Order [%s] was invalidated", previousPurchaseOrderNum);
			String vendorName = vendorMasterService.findById(vendorMasterId).getVendorName();

			if (vendorCreditModified) {
				subject = String.format("%s. %s was restored to vendor's %s credit", subject, NumberFormat.getCurrencyInstance().format(creditAmount), vendorName);
			}
			Map<String, Object> templateKeyValues = new HashMap<>();
			templateKeyValues.put(EMAIL_TEMPLATE_CUSTOMER, this.customer);
			templateKeyValues.put(EMAIL_TEMPLATE_HOST, this.host);
			templateKeyValues.put(EMAIL_TEMPLATE_CUSTOMER_BG_COLOR, this.customerBgColor);
			templateKeyValues.put(EMAIL_TEMPLATE_CUSTOMER_FG_COLOR, this.customerFgColor);
			templateKeyValues.put(EMAIL_TEMPLATE_WEBSITE_URL, this.websiteUrl);
			templateKeyValues.put(EMAIL_TEMPLATE_CONTACT_ADDRESS, this.contactAddress);
			templateKeyValues.put(EMAIL_TEMPLATE_USER_NAME, "Store Manager");
			templateKeyValues.put(EMAIL_TEMPLATE_PURCHASE_ORDER_NUM, previousPurchaseOrderNum);
			templateKeyValues.put(EMAIL_TEMPLATE_VENDOR_NAME, vendorName);
			templateKeyValues.put(EMAIL_TEMPLATE_VENDOR_CREDIT_MODIFIED, vendorCreditModified);
			templateKeyValues.put(EMAIL_TEMPLATE_CREDIT_AMOUNT, creditAmount);

			emailSend.send(
					storeManagerEmailList,
					subject,
					templateKeyValues,
					EmailSend.TEMPLATE_INVALIDATE_ORDER_STORE_MANAGER);
		}
	}

	@Async
	@Transactional
	public void sendNewOrderEmailToSalesRepresentative(Long vendorMasterId, Long purchaseOrderId, String purchaseOrderNum, String sendingUser) {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(sendingUser, ""));

		vendorMasterService.getVendorSalesRepresentative(vendorMasterId).ifPresent(salesRep -> {
			File orderPdf = getPurchaseOrderPdf(purchaseOrderId);

			emailSend.send(
					List.of(salesRep.getEmail()),
					String.format("New Purchase Order [%s] from %s", purchaseOrderNum, customer),
					Map.of(
							EMAIL_TEMPLATE_CUSTOMER, this.customer,
							EMAIL_TEMPLATE_HOST, this.host,
							EMAIL_TEMPLATE_CUSTOMER_BG_COLOR, this.customerBgColor,
							EMAIL_TEMPLATE_CUSTOMER_FG_COLOR, this.customerFgColor,
							EMAIL_TEMPLATE_WEBSITE_URL, this.websiteUrl,
							EMAIL_TEMPLATE_CONTACT_ADDRESS, this.contactAddress,
							EMAIL_TEMPLATE_USER_NAME, salesRep.getVendorContactName()
					),
					EmailSend.TEMPLATE_NEW_PURCHASE_ORDER,
					List.of(orderPdf));
			orderPdf.delete();
		});
	}

	@Async
	@Transactional
	public void sendNewOrderEmailToStoreManager(PurchaseOrderModifyStatusResultDTO dto) {
		List<String> storeManagerEmailList = storeNumberService.getStoreManagerEmailList(dto.getStoreNumId());

		if (!storeManagerEmailList.isEmpty()) {
			String subject = String.format("New Purchase Order [%s] from %s", dto.getPurchaseOrderNum(), customer);
			String vendorName = vendorMasterService.findById(dto.getVendorMasterId()).getVendorName();

			if (dto.isVendorCreditModified()) {
				subject = String.format("%s. %s was deducted from the credit for vendor %s", subject, NumberFormat.getCurrencyInstance().format(dto.getCreditAmount()), vendorName);
			}

			Map<String, Object> templateKeyValues = new HashMap<>();
			templateKeyValues.put(EMAIL_TEMPLATE_CUSTOMER, this.customer);
			templateKeyValues.put(EMAIL_TEMPLATE_HOST, this.host);
			templateKeyValues.put(EMAIL_TEMPLATE_CUSTOMER_BG_COLOR, this.customerBgColor);
			templateKeyValues.put(EMAIL_TEMPLATE_CUSTOMER_FG_COLOR, this.customerFgColor);
			templateKeyValues.put(EMAIL_TEMPLATE_WEBSITE_URL, this.websiteUrl);
			templateKeyValues.put(EMAIL_TEMPLATE_CONTACT_ADDRESS, this.contactAddress);
			templateKeyValues.put(EMAIL_TEMPLATE_USER_NAME, "Store Manager");
			templateKeyValues.put(EMAIL_TEMPLATE_PURCHASE_ORDER_NUM, dto.getPurchaseOrderNum());
			templateKeyValues.put(EMAIL_TEMPLATE_VENDOR_NAME, vendorName);
			templateKeyValues.put(EMAIL_TEMPLATE_VENDOR_CREDIT_MODIFIED, dto.isVendorCreditModified());
			templateKeyValues.put(EMAIL_TEMPLATE_CREDIT_AMOUNT, dto.getCreditAmount());

			emailSend.send(
					storeManagerEmailList,
					subject,
					templateKeyValues,
					EmailSend.TEMPLATE_NEW_PURCHASE_ORDER_STORE_MANAGER);
		}
	}

	@Async
	@Transactional
	public void sendOrderAuthorizationEmail(PurchaseOrderModifyStatusResultDTO dto, Long currentUserId) {
		List<String> approverEmailList = storeNumberService.getPoApproverEmailList(dto.getStoreNumId());
		log.info("Sending Order authorization email to: {}", approverEmailList);

		if (!approverEmailList.isEmpty()) {
			String subject = String.format("Purchase Order [%s] waiting for approval", dto.getPurchaseOrderNum());
			String vendorName = vendorMasterService.findById(dto.getVendorMasterId()).getVendorName();
			UserDTO requestingUser = userService.findById(currentUserId);

			emailSend.send(
					approverEmailList,
					subject,
					Map.of(
							EMAIL_TEMPLATE_CUSTOMER, this.customer,
							EMAIL_TEMPLATE_HOST, this.host,
							EMAIL_TEMPLATE_CUSTOMER_BG_COLOR, this.customerBgColor,
							EMAIL_TEMPLATE_CUSTOMER_FG_COLOR, this.customerFgColor,
							EMAIL_TEMPLATE_WEBSITE_URL, this.websiteUrl,
							EMAIL_TEMPLATE_CONTACT_ADDRESS, this.contactAddress,
							EMAIL_TEMPLATE_REQUESTING_USER, requestingUser.getNameDesc(),
							EMAIL_TEMPLATE_PURCHASE_ORDER, dto,
							EMAIL_TEMPLATE_PURCHASE_ORDER_BASE_PATH, poBasePath,
							EMAIL_TEMPLATE_VENDOR_NAME, vendorName
					),
					EmailSend.TEMPLATE_PURCHASE_ORDER_AUTHORIZATION);
		}
	}

	public File getPurchaseOrderPdf(Long purchaseOrderId) {
		PurchaseOrderModel order = getPurchaseOrderById(purchaseOrderId, false);
		UserDTO user = new UserDTO().parseToDTO(authenticationFacade.getCurrentUserDetails());

		VendorContactDTO vendorContact = vendorMasterService.getVendorSalesRepresentative(order.getVendorMaster().getVendorMasterId())
				.map(dto -> new VendorContactDTO(
						dto.getVendorContactId(),
						dto.getVendorContactName(),
						dto.getPhone(),
						dto.getCellPhone(),
						dto.getEmail(),
						dto.getVendorType().getCatalogId()))
				.orElse(new VendorContactDTO());

		try {
			return pdfGenerator.generatePdfFile(
					Map.of(
							"host", this.host,
							"po", new PurchaseOrderDTO().parseToDTO(order, order.getDetails()),
							"vendor", new VendorMasterDTO().parseToDTO(order.getVendorMaster()),
							"store", new StoreNumberDTO().parseToDTO(order.getStoreNumber()),
							"user", user,
							"vendorContact", vendorContact
					),
					order.getPurchaseOrderNum(),
					PdfGenerator.PURCHASE_ORDER_TEMPLATE);
		} catch (IOException e) {
			log.error("Error generating Purchase Order PDF", e);
			throw new PurchaseOrderPrintException("Error generating print version");
		}
	}

	public PurchaseOrderModel findPurchaseOrderByNumber(String purchaseOrderNumber) {
		return purchaseOrderRepository.findByPurchaseOrderNum(purchaseOrderNumber)
				.orElseThrow(() -> new InvalidPurchaseOrderException(purchaseOrderNumber));
	}

	public List<PurchaseOrdersDTO> findPurchaseOrderByDate(Date date, List<Long> purchaseStatusIds) {
		return purchaseOrderRepository.findByDate(DateUtils.atStartOfDay(date), DateUtils.atEndOfDay(date), purchaseStatusIds);
	}

	public PurchaseOrderUpcInformationDTO loadProductByPurchaseOrderAndUpc(Long purchaseOrderId, String upc) {
		PurchaseOrderModel order = getPurchaseOrderById(purchaseOrderId, false);
		Long candidateUpcMasterId = upcMasterRepository.findByPrincipalUpcAndVendor(upc, order.getVendorMaster().getVendorMasterId(), order.getStoreNumber().getStoreNumId())
				.map(UpcMasterModel::getUpcMasterId)
				.orElse(null);

		order.getDetails().stream()
				.filter(upcMaster -> Objects.equals(upcMaster.getUpcMaster().getUpcMasterId(), candidateUpcMasterId))
				.findFirst()
				.ifPresent(detail -> {
					throw new UpcExistsInPurchaseOrderException(
							upc,
							detail.getUpcMaster().getProductName(),
							purchaseOrderId,
							detail.getPurchaseOrderDetailId());
				});

		return loadProduct(purchaseOrderId, upc, order.getVendorMaster().getVendorMasterId(), order.getStoreNumber().getStoreNumId());
	}

	public PurchaseOrderUpcInformationDTO loadProductByUpcSuppliedByVendor(String upc, Long vendorMasterId, Long storeNumId) {
		return loadProduct(null, upc, vendorMasterId, storeNumId);
	}

	public List<PurchaseOrderUpcInformationDTO> loadProductsByVendor(Long vendorMasterId, Long storeNumId, Long purchaseOrderId) {
		List<PurchaseOrderUpcInformationDTO> result = upcVendorDetailsRepository.findByVendorMasterIdAndStoreNumId(vendorMasterId, storeNumId).stream()
				.map(upcByVendor -> new PurchaseOrderUpcInformationDTO(null, null, storeNumId, upcByVendor.getUpcMaster(), upcByVendor, 0d, 0d))
				.collect(Collectors.toList());
		Optional.ofNullable(purchaseOrderId).map(id -> getPurchaseOrderById(id, false)).ifPresent(order -> {
			List<Long> upcsInOrder = order.getDetails().stream().map(detail -> detail.getUpcMaster().getUpcMasterId()).collect(Collectors.toList());
			result.removeIf(dto -> upcsInOrder.contains(dto.getUpcMasterId()));
		});
		return result;
	}

	public List<PurchaseOrderFilterResultDTO> findByPurchaseOrderNumber(String poNumber) {
		return purchaseOrderRepository.findByPurchaseOrderNumContaining(poNumber).stream()
				.map(m -> new PurchaseOrderFilterResultDTO(m.getPurchaseOrderId(), m.getPurchaseOrderNum()))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<PurchaseOrdersDTO> findOrdersByNumberAndStatus(String purchaseOrderNumber, List<Long> poStatusList) {
		return purchaseOrderRepository.findOrdersByNumberAndStatus(purchaseOrderNumber, poStatusList);
	}

	private boolean modifyCredit(PurchaseOrderModel order, int operation) {
		if (order.getAppliedVendorCredit() > 0) {
			order.getVendorMaster().getVendorCredit().updateAvailableCredit(order.getAppliedVendorCredit() * operation);
			return true;
		}
		return false;
	}

	private PurchaseOrderUpcInformationDTO loadProduct(Long purchaseOrderId, String upc, Long vendorMasterId, Long storeNumId) {
		UpcMasterModel upcMasterModel = upcMasterRepository.findByPrincipalUpcAndVendor(upc, vendorMasterId, storeNumId)
				.orElseThrow(() -> new UpcNotSuppliedByVendorException(upc, vendorMasterId));

		if (!Objects.equals(upcMasterModel.getUpcMasterStatus().getCatalogId(), UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE)) {
			throw new UpcNotActiveException(upc);
		}

		UpcVendorDetailsModel upcVendorDetailsModel =
				upcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(upcMasterModel.getUpcMasterId(), vendorMasterId)
						.orElseThrow(() -> new UpcNotSuppliedByVendorException(upc, vendorMasterId));

		List<InventoryModel> inventoryInStore = inventoryService.getItemInventoryDetailPerStoreNumber(upcMasterModel.getUpcMasterId(), storeNumId);

		PurchaseOrderUpcInformationDTO upcInformationDTO = new PurchaseOrderUpcInformationDTO(
				null,
				purchaseOrderId,
				storeNumId,
				upcMasterModel,
				upcVendorDetailsModel,
				inventoryInStore.stream().mapToDouble(InventoryModel::getQty).sum(),
				0.0);

		promotionService.findApplicablePromotionForProduct(vendorMasterId, upcMasterModel.getUpcMasterId())
				.ifPresent(upcInformationDTO::setDiscountInformation);

		return upcInformationDTO;
	}

	private PurchaseOrderModel getPurchaseOrderById(Long purchaseOrderId) {
		return getPurchaseOrderById(purchaseOrderId, true);
	}

	private PurchaseOrderModel getPurchaseOrderById(Long purchaseOrderId, boolean validate) {
		PurchaseOrderModel purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new ResourceNotFoundException(PURCHASE_ORDER_RESOURCE_NAME, RESOURCE_ID_FIELD, purchaseOrderId));
		if (validate) {
			verifyOrderModifiable(purchaseOrder);
		}
		return purchaseOrder;
	}

	private PurchaseOrderModel getPurchaseOrderByDetailId(Long purchaseOrderDetailId) {
		PurchaseOrderModel purchaseOrder = purchaseOrderRepository.findByDetailsPurchaseOrderDetailId(purchaseOrderDetailId)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase Order Detail", "purchaseOrderDetailId",
						purchaseOrderDetailId));

		verifyOrderModifiable(purchaseOrder);

		return purchaseOrder;
	}

	private void verifyOrderModifiable(PurchaseOrderModel purchaseOrder) {
		CatalogModel status = purchaseOrder.getStatus();
		if (!UPDATABLE_STATUS_LIST.contains(status.getCatalogId())) {
			throw new PurchaseOrderUpdateException(purchaseOrder.getPurchaseOrderNum(), status.getCatalogOptions());
		}
	}

	private PurchaseOrderModel createNewModel(PurchaseOrderDetailModel detailModel, PurchaseOrderAddProductRequestDTO dto) {
		VendorMasterDTO vendor = vendorMasterService.findById(dto.getVendorMasterId());
		Date suggestedEta = EtaHelper.getEta(new Date(), vendor.getCutOffDay(), vendor.getShipmentDay(), vendor.getEta());

		return new PurchaseOrderModel(
				dto,
				detailModel,
				vendor,
				authenticationFacade.getCurrentUserId(),
				purchaseOrderNumberGenerator.generateNumber(),
				suggestedEta
		);
	}

	private boolean updateModel(PurchaseOrderModel originalModel) {
		VendorMasterModel vendor = originalModel.getVendorMaster();

		boolean revisionUpdated = updateRevision(originalModel);

		originalModel.updatePurchaseOrderRevision(
				originalModel.getPurchaseOrderNum(),
				EtaHelper.getEta(new Date(), vendor.getCutOffDay(), vendor.getShipmentDay(), vendor.getEta()));

		return revisionUpdated;
	}

	private boolean updateRevision(PurchaseOrderModel originalModel) {
		if (Objects.equals(PoStatusCatalog.PO_STATUS_ORDERED, originalModel.getStatus().getCatalogId())) {
			originalModel.updatePurchaseOrderRevision(
					purchaseOrderNumberGenerator.generateRevision(originalModel.getPurchaseOrderNum()),
					originalModel.getSuggestedEta());
			return true;
		}
		return false;
	}

	private PurchaseOrderDetailModel getDetailModelFromRequest(PurchaseOrderAddProductRequestDTO dto) {
		UpcMasterModel upcMasterModel = upcMasterRepository.findById(dto.getUpcMasterId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", RESOURCE_ID_FIELD, dto.getUpcMasterId()));

		if (!Objects.equals(upcMasterModel.getUpcMasterStatus().getCatalogId(), UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE)) {
			throw new UpcNotActiveException(upcMasterModel.getPrincipalUpc());
		}

		UpcVendorDetailsModel upcVendorDetailsModel = upcVendorDetailsRepository
				.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(dto.getUpcMasterId(), dto.getVendorMasterId())
				.orElseThrow(() -> new ResourceNotFoundException("Vendor Details", RESOURCE_ID_FIELD, dto.getUpcMasterId()));

		PurchaseOrderDetailModel model = new PurchaseOrderDetailModel(upcMasterModel, upcVendorDetailsModel, dto, this.authenticationFacade.getCurrentUserId());

		Optional<PromotionDTO> promotionOptional = promotionService.findApplicablePromotionForProduct(
				dto.getVendorMasterId(),
				upcMasterModel.getUpcMasterId());

		if (promotionOptional.isPresent()) {
			PromotionDTO promotionDTO = promotionOptional.get();
			DiscountStrategy strategy = DiscountHelper.getDiscountStrategyFor(promotionDTO.getPromotionTypeId());
			Double discount = strategy.calculateDiscount(model.getProductCost(), model.getTotalAmount(), promotionDTO.getDiscount());
			model.setDiscountInformation(discount, promotionDTO);
		}

		return model;
	}

	private boolean currentUserCanApprove() {
		UserModel user = authenticationFacade.getCurrentUserDetails();
		boolean isAdmin = userService.userHasRole(user.getUserId(), RoleService.ADMIN_ROLE_ID);
		return user.isApprover() || isAdmin;
	}
}
