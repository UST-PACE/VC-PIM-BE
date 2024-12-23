package com.ust.retail.store.pim.controller.inventory;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.annotations.OnNotifyReception;
import com.ust.retail.store.pim.common.annotations.OnReceive;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.catalogs.PoStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.ReceptionWarningCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.*;
import com.ust.retail.store.pim.dto.inventory.reception.screens.ItemDetailsDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.PoOrderMasterDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.PurchaseOrdersDTO;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.inventory.PoReceiveDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.service.inventory.ReceiveDetailService;
import com.ust.retail.store.pim.service.purchaseorder.PurchaseOrderDetailService;
import com.ust.retail.store.pim.service.purchaseorder.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/inventory/p/receiving/")
@Validated
public class InventoryReceivingController extends BaseController {

	private final PurchaseOrderDetailService purchaseOrderDetailService;
	private final PurchaseOrderService purchaseOrderService;
	private final InventoryService inventoryService;
	private final ReceiveDetailService receiveDetailService;
	private final ReceptionWarningCatalog receptionWarningCatalog;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public InventoryReceivingController(PurchaseOrderDetailService purchaseOrderDetailService,
										PurchaseOrderService purchaseOrderService, InventoryService inventoryService,
										ReceiveDetailService receiveDetailService, ReceptionWarningCatalog receptionWarningCatalog,
										AuthenticationFacade authenticationFacade) {

		super();
		this.purchaseOrderDetailService = purchaseOrderDetailService;
		this.purchaseOrderService = purchaseOrderService;
		this.inventoryService = inventoryService;
		this.receiveDetailService = receiveDetailService;
		this.receptionWarningCatalog = receptionWarningCatalog;
		this.authenticationFacade = authenticationFacade;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/load/order/{purchaseOrderNumber}")
	public PoOrderMasterDTO loadOrder(@Valid @PathVariable("purchaseOrderNumber") String purchaseOrderNumber) {

		PurchaseOrderModel po = purchaseOrderService.findPurchaseOrderByNumber(purchaseOrderNumber);

		return new PoOrderMasterDTO(po, authenticationFacade.getCurrentUserDetails().getNameDesc())
				.addReceptionDetails(receiveDetailService.getReceivingSumary(po.getPurchaseOrderId()));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/find/order/{purchaseOrderNumber}/status/{status}")
	public List<PurchaseOrdersDTO> findOrder(@Valid @PathVariable("purchaseOrderNumber") String purchaseOrderNumber,
											 @Valid @PathVariable("status") SearchStatus status) {

		return purchaseOrderService.findOrdersByNumberAndStatus(purchaseOrderNumber, status.poStatusList);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/load/item/upc/{principalUpc}/po/id/{poId}/vm/{vendorMasterId}/update/{isUpdate}")
	public ItemDetailsDTO loadItem(@Valid @PathVariable("principalUpc") String principalUpc,
								   @PathVariable("poId") Long purchaseOrderId, @PathVariable("vendorMasterId") Long vendorMasterId,
								   @PathVariable("isUpdate") Boolean isUpdate) {

		PurchaseOrderDetailModel purchaseOrderDetail = purchaseOrderDetailService.getPendingItemByCode(principalUpc,
				purchaseOrderId, vendorMasterId, isUpdate);

		PoReceiveDetailModel receiveDetail = receiveDetailService
				.findReceiveDetailsByPoOrderDetailId(purchaseOrderDetail.getPurchaseOrderDetailId());

		List<InventoryModel> inventoryPerStoreLocation = inventoryService.getItemInventoryDetailPerStoreNumber(
				purchaseOrderDetail.getUpcMaster().getUpcMasterId(),
				purchaseOrderDetail.getPurchaseOrder().getStoreNumber().getStoreNumId());

		return new ItemDetailsDTO(receiveDetail, purchaseOrderDetail, inventoryPerStoreLocation);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/receive")
	@Validated(OnReceive.class)
	public ReceivingResponseDTO receiveInventory(@Valid @RequestBody ReceivingRequestDTO receivingDTO) {
		return receiveDetailService.receiveInventory(receivingDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public ReceivingResponseDTO updateReceiveInventory(@Valid @RequestBody ReceivingRequestUpdateDTO receivingDTO) {
		return receiveDetailService.updateReceiveInventory(receivingDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/resume/po/id/{purchaseOrderId}")
	@Validated(OnReceive.class)
	public ReceivingResumeResponseDTO receivingResume(@Valid @PathVariable("purchaseOrderId") Long purchaseOrderId) {
		return receiveDetailService.getReceivingSumary(purchaseOrderId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/po/date/{poDate}/status/pending")
	@Validated(OnReceive.class)
	public List<PurchaseOrdersDTO> loadPendingPurchaseOrders(@Valid @PathVariable("poDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date poDate) {
		return purchaseOrderService.findPurchaseOrderByDate(poDate, SearchStatus.PENDING.poStatusList);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/po/date/{poDate}/status/received")
	@Validated(OnReceive.class)
	public List<PurchaseOrdersDTO> loadReceivedPurchaseOrders(@Valid @PathVariable("poDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date poDate) {
		return purchaseOrderService.findPurchaseOrderByDate(poDate, SearchStatus.RECEIVED.poStatusList);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PutMapping("/finish/po/id/{purchaseOrderId}")
	@Validated(OnReceive.class)
	public boolean finishReception(@Valid @PathVariable("purchaseOrderId") Long purchaseOrderId) {
		return receiveDetailService.finishReception(purchaseOrderId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/load/receptionwarnings")
	public List<CatalogDTO> loadReceptionWarnings() {
		return receptionWarningCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PostMapping("/notifyreception")
	@Validated(OnNotifyReception.class)
	public boolean notifyReception(@Valid @RequestBody ReceptionNotificationRequestDTO request) {
		receiveDetailService.notifyReception(request);
		return true;
	}

	enum SearchStatus {
		PENDING(List.of(PoStatusCatalog.PO_STATUS_ORDERED, PoStatusCatalog.PO_STATUS_IN_RECEPTION)),
		RECEIVED(List.of(PoStatusCatalog.PO_STATUS_COMPLETED, PoStatusCatalog.PO_STATUS_INCOMPLETE));

		private final List<Long> poStatusList;

		SearchStatus(List<Long> poStatusList) {
			this.poStatusList = poStatusList;
		}
	}
}
