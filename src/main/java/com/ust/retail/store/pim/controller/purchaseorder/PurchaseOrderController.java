package com.ust.retail.store.pim.controller.purchaseorder;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.catalogs.PoStatusCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.ItemCurrentInventory;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDTO;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDetailDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.*;
import com.ust.retail.store.pim.dto.purchaseorder.screens.PurchaseOrderPendingFulfillmentDTO;
import com.ust.retail.store.pim.dto.purchaseorder.screens.PurchaseOrderUpcInformationDTO;
import com.ust.retail.store.pim.exceptions.PurchaseOrderPrintException;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.service.purchaseorder.PurchaseOrderService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(path = "/api/purchaseorder/p")
@Validated
public class PurchaseOrderController extends BaseController {
	private final PurchaseOrderService purchaseOrderService;
	private final InventoryService inventoryService;
	private final PoStatusCatalog poStatusCatalog;
	private final AuthenticationFacade authenticationFacade;

	public PurchaseOrderController(PurchaseOrderService purchaseOrderService,
								   InventoryService inventoryService,
								   PoStatusCatalog poStatusCatalog,
								   AuthenticationFacade authenticationFacade) {
		this.purchaseOrderService = purchaseOrderService;
		this.inventoryService = inventoryService;
		this.poStatusCatalog = poStatusCatalog;
		this.authenticationFacade = authenticationFacade;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/loadupc/vm/{vendorMasterId}/sn/{storeNumId}/upc/{upc}")
	public PurchaseOrderUpcInformationDTO loadProductByUpc(
			@PathVariable(value = "vendorMasterId") Long vendorMasterId,
			@PathVariable(value = "storeNumId") Long storeNumId,
			@PathVariable(value = "upc") String upc) {
		return purchaseOrderService.loadProductByUpcSuppliedByVendor(upc, vendorMasterId, storeNumId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping({"/loadupc/vm/{vendorMasterId}/st/{storeNumId}", "/loadupc/vm/{vendorMasterId}/st/{storeNumId}/po/{purchaseOrderId}"})
	public List<PurchaseOrderUpcInformationDTO> loadProductByUpc(@PathVariable Long vendorMasterId,
																 @PathVariable Long storeNumId,
																 @PathVariable(required = false) Long purchaseOrderId) {
		return purchaseOrderService.loadProductsByVendor(vendorMasterId, storeNumId, purchaseOrderId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/loadupc/po/{purchaseOrderId}/upc/{upc}")
	public PurchaseOrderUpcInformationDTO loadProductByPurchaseOrderAndUpc(
			@PathVariable(value = "purchaseOrderId") Long purchaseOrderId,
			@PathVariable(value = "upc") String upc) {
		return purchaseOrderService.loadProductByPurchaseOrderAndUpc(purchaseOrderId, upc);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/addproduct")
	@Validated(OnCreate.class)
	public PurchaseOrderModifyLineItemResultDTO createOrderWithLineItem(@Valid @RequestBody PurchaseOrderAddProductRequestDTO dto) {
		return purchaseOrderService.createOrderWithLineItem(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/addproduct/bulk")
	@Validated(OnCreate.class)
	public PurchaseOrderModifyLineItemResultDTO createOrderWithMultipleLineItems(@Valid @RequestBody PurchaseOrderAddBulkProductRequestDTO dto) {
		return purchaseOrderService.createOrUpdateOrderWithMultipleLineItems(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/addproduct/bulk")
	@Validated(OnUpdate.class)
	public PurchaseOrderModifyLineItemResultDTO updateOrderWithMultipleLineItems(@Valid @RequestBody PurchaseOrderAddBulkProductRequestDTO dto) {
		return purchaseOrderService.createOrUpdateOrderWithMultipleLineItems(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/addproduct")
	@Validated(OnUpdate.class)
	public PurchaseOrderModifyLineItemResultDTO appendOrderWithProduct(@Valid @RequestBody PurchaseOrderAddProductRequestDTO dto) {
		PurchaseOrderModifyLineItemResultDTO result = purchaseOrderService.appendLineItemToOrder(dto);
		if (result.isRevisionUpdated()) {
			sendOrderInvalidatedEmails(result.getVendorMasterId(), result.getPreviousPurchaseOrderNum(), result.getStoreNumId(), result.getCreditAmount(), result.isVendorCreditModified());
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@DeleteMapping("/remove/detail/{purchaseOrderDetailId}")
	public PurchaseOrderModifyLineItemResultDTO removeLineItem(@PathVariable(value = "purchaseOrderDetailId") Long purchaseOrderDetailId) {
		PurchaseOrderModifyLineItemResultDTO result = purchaseOrderService.removeLineItem(purchaseOrderDetailId);
		if (result.isRevisionUpdated()) {
			sendOrderInvalidatedEmails(result.getVendorMasterId(), result.getPreviousPurchaseOrderNum(), result.getStoreNumId(), result.getCreditAmount(), result.isVendorCreditModified());
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/modify/lineitem")
	public PurchaseOrderModifyLineItemResultDTO modifyLineItem(@Valid @RequestBody PurchaseOrderModifyLineItemRequestDTO lineItemDTO) {
		PurchaseOrderModifyLineItemResultDTO result = purchaseOrderService.updateLineItem(lineItemDTO);
		if (result.isRevisionUpdated()) {
			sendOrderInvalidatedEmails(result.getVendorMasterId(), result.getPreviousPurchaseOrderNum(), result.getStoreNumId(), result.getCreditAmount(), result.isVendorCreditModified());
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/savedraft")
	public PurchaseOrderModifyStatusResultDTO saveDraft(@Valid @RequestBody PurchaseOrderSaveRequestDTO purchaseOrderSaveRequestDTO) {
		PurchaseOrderModifyStatusResultDTO result = purchaseOrderService.saveDraft(purchaseOrderSaveRequestDTO);
		if (result.isRevisionUpdated()) {
			sendOrderInvalidatedEmails(result.getVendorMasterId(), result.getPreviousPurchaseOrderNum(), result.getStoreNumId(), result.getCreditAmount(), result.isVendorCreditModified());
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/process")
	public PurchaseOrderModifyStatusResultDTO processPurchaseOrder(@Valid @RequestBody PurchaseOrderSaveRequestDTO purchaseOrderSaveRequestDTO) {
		PurchaseOrderModifyStatusResultDTO result = purchaseOrderService.processPurchaseOrder(purchaseOrderSaveRequestDTO);

		if (result.isToAuthorize()) {
			purchaseOrderService.sendOrderAuthorizationEmail(result, authenticationFacade.getCurrentUserId());
		} else {
			sendProcessedOrderEmails(result);
		}

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/authorize")
	public PurchaseOrderModifyStatusResultDTO authorizePurchaseOrder(@Valid @RequestBody PurchaseOrderSaveRequestDTO purchaseOrderSaveRequestDTO) {
		PurchaseOrderModifyStatusResultDTO result = purchaseOrderService.authorizePurchaseOrder(purchaseOrderSaveRequestDTO);

		sendProcessedOrderEmails(result);

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/resendorder/id/{purchaseOrderId}")
	public GenericResponse resendOrder(@PathVariable(value = "purchaseOrderId") Long purchaseOrderId) {
		PurchaseOrderDTO orderDTO = purchaseOrderService.findById(purchaseOrderId);

		purchaseOrderService.sendNewOrderEmailToSalesRepresentative(
				orderDTO.getVendorMasterId(),
				orderDTO.getPurchaseOrderId(),
				orderDTO.getPurchaseOrderNum(),
				authenticationFacade.getPrincipal());

		return new GenericResponse(GenericResponse.OP_TYPE_ACCEPT, GenericResponse.SUCCESS_CODE, "true");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@DeleteMapping("/delete/id/{id}")
	public GenericResponse deleteById(@PathVariable(value = "id") Long purchaseOrderId) {
		PurchaseOrderModifyLineItemResultDTO result = purchaseOrderService.deleteById(purchaseOrderId);

		if (result.isRevisionUpdated()) {
			purchaseOrderService.sendOrderInvalidatedEmailToSalesRepresentative(result.getVendorMasterId(), result.getPreviousPurchaseOrderNum());
			purchaseOrderService.sendOrderInvalidatedEmailToStoreManager(result.getStoreNumId(), result.getPreviousPurchaseOrderNum(), false, result.getVendorMasterId(), 0d);
		}

		if (result.isEscalationWarning()) {
			purchaseOrderService.sendOrderInvalidatedEmailToEscalationContact(result.getVendorMasterId(), result.getPreviousPurchaseOrderNum());
		}
		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "true");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/print/id/{id}")
	public ResponseEntity<Resource> printPurchaseOrder(@PathVariable(value = "id") Long purchaseOrderId) {
		File file = purchaseOrderService.getPurchaseOrderPdf(purchaseOrderId);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
			headers.add(HttpHeaders.PRAGMA, "no-cache");
			headers.add(HttpHeaders.EXPIRES, "0");
			headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", file.getName()));
			ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(resource.contentLength())
					.contentType(MediaType.APPLICATION_PDF)
					.body(resource);
		} catch (IOException e) {
			throw new PurchaseOrderPrintException("Error generating print version");
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/find/id/{id}")
	public PurchaseOrderDTO findById(@PathVariable(value = "id") Long purchaseOrderId) {
		return purchaseOrderService.findById(purchaseOrderId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/find/ponumber/{poNumber}")
	public List<PurchaseOrderFilterResultDTO> findByPoNumber(@PathVariable(value = "poNumber") String poNumber) {
		return purchaseOrderService.findByPurchaseOrderNumber(poNumber);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/details")
	@Validated(OnFilter.class)
	public Page<PurchaseOrderDetailDTO> findDetails(@Valid @RequestBody PurchaseOrderFindDetailsByIdRequestDTO dto) {
		return purchaseOrderService.findDetailsById(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<PurchaseOrderFilterResultDTO> findByFilters(@Valid @RequestBody PurchaseOrderFilterRequestDTO dto) {
		return purchaseOrderService.findOrdersByFilters(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/pendingfulfillment/product/{id}")
	public List<PurchaseOrderPendingFulfillmentDTO> findOrdersPendingFulfillmentByProduct(@PathVariable(value = "id") Long upcMasterId) {
		return purchaseOrderService.findOrdersPendingFulfillmentByProduct(upcMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/inventory/product/{id}")
	public List<ItemCurrentInventory> findProductByStore(@PathVariable(value = "id") Long upcMasterId) {
		return inventoryService.findInventoryByUpcMasterId(upcMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/load/statuscatalog")
	public List<CatalogDTO> loadStatusCatalog() {
		return poStatusCatalog.getCatalogOptions();
	}

	private void sendProcessedOrderEmails(PurchaseOrderModifyStatusResultDTO result) {
		purchaseOrderService.sendNewOrderEmailToSalesRepresentative(
				result.getVendorMasterId(),
				result.getPurchaseOrderId(),
				result.getPurchaseOrderNum(),
				authenticationFacade.getPrincipal());
		purchaseOrderService.sendNewOrderEmailToStoreManager(result);
	}

	private void sendOrderInvalidatedEmails(Long vendorMasterId, String previousPurchaseOrderNum, Long storeNumId, Double creditAmount, boolean vendorCreditModified) {
		purchaseOrderService.sendOrderInvalidatedEmailToSalesRepresentative(vendorMasterId, previousPurchaseOrderNum);
		purchaseOrderService.sendOrderInvalidatedEmailToStoreManager(storeNumId, previousPurchaseOrderNum, vendorCreditModified, vendorMasterId, creditAmount);
	}
}
