package com.ust.retail.store.pim.service.inventory;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.dto.inventory.InventoryFiltersDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryProductDTO;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventoryAdjustmentDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventoryProductReturnDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventorySalesDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestUpdateDTO;
import com.ust.retail.store.pim.dto.inventory.reception.screens.ItemCurrentInventory;
import com.ust.retail.store.pim.dto.inventory.returns.screen.ReturnItemDTO;
import com.ust.retail.store.pim.engine.inventory.*;
import com.ust.retail.store.pim.exceptions.*;
import com.ust.retail.store.pim.model.inventory.InventoryHistoryModel;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.inventory.InventoryHistoryRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InventoryService {

	private static final List<Item> NOT_REQUIRED = null;
	private static final Boolean IS_AUTHORIZE = true;
	private final ReceiveInventory receiveInventoryService;
	private final ReturnMerchandiseInventory returnMerchandiseInventory;
	private final InventoryAdjustment inventoryAdjustmentService;
	private final VendorCredits vendorCreditsService;
	private final SaleItemInventory inventorySalesService;
	private final BistroReduceInventory bistroReduceInventory;
	private final TransferIncreaseInventory transferIncreaseService;
	private final TransferReduceInventory transferReduceService;
	private final UpcDepleteInventory upcDepleteInventory;
	private final InventoryHistoryRepository inventoryHistoryRepository;
	private final InventoryRepository inventoryRepository;
	private final UpcMasterRepository upcMasterRepository;
	private final UnitConverter unitConverter;

	@Autowired
	public InventoryService(ReceiveInventory receiveInventoryService,
							InventoryAdjustment inventoryAdjustmentService,
							VendorCredits vendorCreditsService,
							InventoryHistoryRepository inventoryHistoryRepository,
							InventoryRepository inventoryRepository,
							SaleItemInventory inventorySalesService,
							BistroReduceInventory bistroReduceInventory,
							TransferIncreaseInventory transferIncreaseService,
							TransferReduceInventory transferReduceService,
							ReturnMerchandiseInventory returnMerchandiseInventory,
							UpcDepleteInventory upcDepleteInventory,
							UpcMasterRepository upcMasterRepository,
							UnitConverter unitConverter) {
		super();
		this.receiveInventoryService = receiveInventoryService;
		this.inventoryAdjustmentService = inventoryAdjustmentService;
		this.vendorCreditsService = vendorCreditsService;
		this.inventoryHistoryRepository = inventoryHistoryRepository;
		this.inventoryRepository = inventoryRepository;
		this.inventorySalesService = inventorySalesService;
		this.bistroReduceInventory = bistroReduceInventory;
		this.transferIncreaseService = transferIncreaseService;
		this.transferReduceService = transferReduceService;
		this.returnMerchandiseInventory = returnMerchandiseInventory;
		this.upcDepleteInventory = upcDepleteInventory;
		this.upcMasterRepository = upcMasterRepository;
		this.unitConverter = unitConverter;
	}

	@Transactional
	public void receiveInventory(ReceivingRequestDTO receivingDTO, Long referenceId) {

		Item item = receivingDTO.getItem();
		Long upcMasterId = item.getUpcMasterId();
		UpcMasterModel upcMasterModel = upcMasterRepository.findById(upcMasterId).orElseThrow();

		double receivedQty = item.getQty() * getConvertedContentPerUnit(upcMasterModel);

		List<Item> items = List.of(new Item(receivedQty, upcMasterId, item.getStoreLocationId()));

		receiveInventoryService.execute(items, NOT_REQUIRED, referenceId);
	}

	@Transactional
	public void reduceBistroInventory(List<Item> items, Long referenceId) {

		bistroReduceInventory.execute(items, NOT_REQUIRED, referenceId);
	}

	@Transactional
	public void updateReceiveInventory(ReceivingRequestUpdateDTO receivingDTO, Long referenceId) {

		List<Item> items = new ArrayList<>();

		Double currentQty = receivingDTO.getItem().getQty();
		Double prevQty = receivingDTO.getPrevQty() * -1;

		Long currentStoreLocation = receivingDTO.getItem().getStoreLocationId();

		receivingDTO.getItem().changeItemQty(prevQty);
		receivingDTO.getItem().changeStoreLocation(receivingDTO.getPrevStoreLocation());

		items.add(receivingDTO.getItem());

		receiveInventoryService.execute(items, NOT_REQUIRED, referenceId);
		items.get(0)
				.changeItemQty(currentQty)
				.changeStoreLocation(currentStoreLocation);

		receiveInventoryService.execute(items, NOT_REQUIRED, referenceId);
	}

	@Transactional
	public void invetoryAdjustment(InventoryAdjustmentDTO inventoryAdjustmentDTO, Long referenceId) {
		List<Item> items = new ArrayList<>();
		items.add(inventoryAdjustmentDTO.getItem());

		List<InventoryHistoryModel> inventoryHistoryRecords = this.findInventoryHistoryRecord(
				inventoryAdjustmentDTO.getInventoryAdjustmentId(), InventoryAdjustment.OPERATION_MODULE,
				inventoryAdjustmentDTO.getItem().getUpcMasterId());

		if (!inventoryHistoryRecords.isEmpty()) {
			InventoryHistoryModel historyRecord = inventoryHistoryRecords.get(0);

			inventoryHistoryRepository.deleteByTxnNum(historyRecord.getTxnNum());
		}

		inventoryAdjustmentService.execute(items, inventoryAdjustmentDTO.getShrinkageItems(), referenceId);
	}

	@Transactional
	public void vendorCredits(ReturnItemDTO returnItemDTO, Long referenceId) {

		Item item = returnItemDTO.getItem();
		Long upcMasterId = item.getUpcMasterId();
		UpcMasterModel upcMasterModel = upcMasterRepository.findById(upcMasterId).orElseThrow();

		double returnQty = item.getQty() * getConvertedContentPerUnit(upcMasterModel);

		List<Item> items = List.of(new Item(returnQty, upcMasterId, item.getStoreLocationId()));

		List<InventoryHistoryModel> inventoryHistoryRecords = this.findInventoryHistoryRecord(
				referenceId, VendorCredits.OPERATION_MODULE,
				item.getUpcMasterId());

		if (!inventoryHistoryRecords.isEmpty()) {
			InventoryHistoryModel historyRecord = inventoryHistoryRecords.get(0);

			inventoryHistoryRepository.deleteByTxnNum(historyRecord.getTxnNum());
		}

		vendorCreditsService.execute(items, NOT_REQUIRED, referenceId);
	}

	@Transactional
	public void inventorySales(InventorySalesDTO inventorySales) {
		inventorySalesService.execute(inventorySales.getItems(), NOT_REQUIRED,
				inventorySales.getExternalReferenceId());
	}

	@Transactional
	public void customerReturnInventory(InventoryProductReturnDTO inventoryReturns) {
		returnMerchandiseInventory.execute(inventoryReturns.getItems(), NOT_REQUIRED,
				inventoryReturns.getExternalReferenceId());
	}

	@Transactional
	public boolean authorizeByTxnNumber(Long txnNum) {
		List<InventoryHistoryModel> inventoryToUpdate = inventoryHistoryRepository
				.findByTxnNumAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(txnNum,
						InventoryEngine.AUTHORIZATION_STATUS_PENDING)
				.orElseThrow(() -> new InvalidTxnNumberAuthorizationException(txnNum));

		return processAuthorization(inventoryToUpdate, IS_AUTHORIZE);
	}

	@Transactional
	public boolean authorize(Long referenceId, Long operationModuleId) {

		List<InventoryHistoryModel> inventoryToUpdate = inventoryHistoryRepository
				.findByReferenceIdAndAuthorizationStatusCatalogIdAndOperationModuleCatalogIdOrderByInventoryHistoryIdDesc(
						referenceId, InventoryEngine.AUTHORIZATION_STATUS_PENDING, operationModuleId)
				.orElseThrow(() -> new InvalidTxnNumberAuthorizationException(referenceId));

		return processAuthorization(inventoryToUpdate, IS_AUTHORIZE);
	}

	@Transactional
	public boolean rejectByTxnNumber(Long txnNum) {
		List<InventoryHistoryModel> inventoryToUpdate = inventoryHistoryRepository
				.findByTxnNumAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(txnNum,
						InventoryEngine.AUTHORIZATION_STATUS_PENDING)
				.orElseThrow(() -> new InvalidTxnNumberRejectException(txnNum));

		return processAuthorization(inventoryToUpdate, !IS_AUTHORIZE);
	}

	@Transactional
	public boolean rejectByReferenceId(Long referenceId, Long operationModuleId) {

		List<InventoryHistoryModel> inventoryToUpdate = inventoryHistoryRepository
				.findByReferenceIdAndAuthorizationStatusCatalogIdAndOperationModuleCatalogIdOrderByInventoryHistoryIdDesc(
						referenceId, InventoryEngine.AUTHORIZATION_STATUS_PENDING, operationModuleId)
				.orElseThrow(() -> new InvalidTxnNumberRejectException(referenceId));

		return processAuthorization(inventoryToUpdate, !IS_AUTHORIZE);
	}

	public List<InventoryModel> getItemInventoryDetailPerStoreNumber(Long upcMasterId, Long storeNumberId) {

		List<InventoryModel> inventory = inventoryRepository
				.findByUpcMasterUpcMasterIdAndStoreLocationStoreNumberStoreNumId(upcMasterId, storeNumberId);
		inventory.forEach(model -> model.setQty(model.getQty() / getConvertedContentPerUnit(model.getUpcMaster())));
		return inventory;
	}

	public InventoryProductDTO findInventoryByCodeAndStoreLocation(String code, Long storeLocationId) {

		UpcMasterModel product = upcMasterRepository.findByPrincipalUpc(code)
				.orElseThrow(() -> new InvalidUPCException(code));

		if (Objects.equals(product.getUpcMasterStatus().getCatalogId(), UpcMasterStatusCatalog.UPC_MASTER_STATUS_DISCONTINUE_TRADING)) {
			throw new UpcNotActiveException(code);
		}

		InventoryModel inventoryItem = inventoryRepository
				.findByUpcMasterUpcMasterIdAndStoreLocationStoreLocationId(product.getUpcMasterId(), storeLocationId)
				.orElseThrow(() -> new InvalidUPCException(code));

		return new InventoryProductDTO(inventoryItem, product);
	}

	public List<ItemCurrentInventory> findInventoryByUpcMasterId(Long upcMasterId) {
		return inventoryRepository.findByUpcMasterUpcMasterId(upcMasterId).stream()
				.map(inventory ->
						new ItemCurrentInventory(
								inventory.getStoreLocation().getStoreNumber().getStoreNumId(),
								inventory.getStoreLocation().getStoreNumber().getStoreName(),
								inventory.getStoreLocation().getStoreLocationId(),
								inventory.getStoreLocation().getStoreLocationName(),
								inventory.getQty() / getConvertedContentPerUnit(inventory.getUpcMaster())))
				.collect(Collectors.toList());
	}

	public boolean upcHasInventory(Long upcMasterId) {
		return findInventoryByUpcMasterId(upcMasterId).stream()
				.map(ItemCurrentInventory::getItems)
				.mapToDouble(Double::doubleValue).sum() > 0d;
	}

	private boolean processAuthorization(List<InventoryHistoryModel> inventoryToUpdate, Boolean isAuthorize) {

		Long operationModuleId = null;

		for (InventoryHistoryModel currentItem : inventoryToUpdate) {

			if (isAuthorize) {
				currentItem.authorize();
				operationModuleId = currentItem.getOperationModule().getCatalogId();
			} else {
				currentItem.reject();
			}

		}

		inventoryHistoryRepository.saveAll(inventoryToUpdate);

		if (isAuthorize) {
			InventoryModel currentInventory = inventoryRepository
					.findById(inventoryToUpdate.get(0).getInventory().getInventoryId())
					.orElseThrow(() -> new ResourceNotFoundException("Inventory", "inventoryId",
							inventoryToUpdate.get(0).getInventory().getInventoryId()));

			Double finalQty;

			if (Objects.equals(InventoryAdjustment.OPERATION_MODULE, operationModuleId)) {
				finalQty = inventoryToUpdate.get(0).getFinalQty();
			} else if (Objects.equals(ReceiveInventory.OPERATION_MODULE, operationModuleId)) {
				finalQty = currentInventory.getQty() + inventoryToUpdate.get(0).getOperationQty();
			} else {
				finalQty = currentInventory.getQty() - inventoryToUpdate.get(0).getOperationQty();
			}

			inventoryToUpdate.get(0).getInventory().setQty(finalQty);

			inventoryRepository.save(inventoryToUpdate.get(0).getInventory());
		}

		return true;
	}

	public boolean authorizeByInventoryHistoryId(Long inventoryHistoryId) {
		List<InventoryHistoryModel> inventoryToUpdate = inventoryHistoryRepository
				.findByInventoryHistoryIdAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(
						inventoryHistoryId, InventoryEngine.AUTHORIZATION_STATUS_PENDING)
				.orElseThrow(() -> new InvalidInventoryHistoryTransactionException(inventoryHistoryId));

		return processAuthorization(inventoryToUpdate, IS_AUTHORIZE);
	}

	@Transactional
	public boolean rejectByInventoryHistoryId(Long inventoryHistoryId) {
		List<InventoryHistoryModel> inventoryToUpdate = inventoryHistoryRepository
				.findByInventoryHistoryIdAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(
						inventoryHistoryId, InventoryEngine.AUTHORIZATION_STATUS_PENDING)
				.orElseThrow(() -> new InvalidInventoryHistoryTransactionException(inventoryHistoryId));

		return processAuthorization(inventoryToUpdate, !IS_AUTHORIZE);
	}

	@Transactional
	public void clearInterruptedTransactions(Long referenceId, Long operationModuleId) {

		List<InventoryHistoryModel> historyListToClear = inventoryHistoryRepository
				.findByReferenceIdAndOperationModuleCatalogId(referenceId, operationModuleId);
		inventoryHistoryRepository.deleteAll(historyListToClear);
	}

	@Transactional
	public void trasferInventory(String code, Long storeLocationFromId, Long storeLocationToId, Double qty,
								 Long referenceId) {

		InventoryProductDTO inventoryProductToDTO = findInventoryByCodeAndStoreLocation(code, storeLocationFromId);

		Item itemToReduce = new Item(qty, inventoryProductToDTO.getUpcMasterModel().getUpcMasterId(),
				storeLocationFromId);
		transferReduceService.execute(List.of(itemToReduce), NOT_REQUIRED, referenceId);

		Item itemToIncrease = new Item(qty, inventoryProductToDTO.getUpcMasterModel().getUpcMasterId(),
				storeLocationToId);
		transferIncreaseService.execute(List.of(itemToIncrease), NOT_REQUIRED, referenceId);

	}

	public Page<InventoryFiltersDTO> findInventory(@Valid InventoryFiltersDTO inventoryFilterDTO) {
		return inventoryRepository.findByFilters(inventoryFilterDTO.getStoreNumberId(),
				inventoryFilterDTO.getStoreLocationId(), inventoryFilterDTO.getPrincipalUpc(),
				inventoryFilterDTO.getProductName(), inventoryFilterDTO.createPageable());
	}

	public Page<InventoryFiltersDTO> findInventorySummary(@Valid InventoryFiltersDTO inventoryFilterDTO) {
		return inventoryRepository.findByFiltersSummary(inventoryFilterDTO.getStoreNumberId(),
				inventoryFilterDTO.getStoreLocationId(), inventoryFilterDTO.getPrincipalUpc(),
				inventoryFilterDTO.getProductName(), inventoryFilterDTO.createPageable());
	}

	public List<InventoryHistoryModel> findInventoryHistoryRecord(Long referenceId, Long operationModuleId,
																  Long upcMasterId) {
		return inventoryHistoryRepository.findByReferenceIdAndOperationModuleCatalogIdAndInventoryUpcMasterUpcMasterId(
				referenceId, operationModuleId, upcMasterId);
	}

	public void depleteUpcInventory(Long upcMasterId) {
		List<Item> itemList = findInventoryByUpcMasterId(upcMasterId).stream()
				.map(itemCurrentInventory -> new Item(0d, upcMasterId, itemCurrentInventory.getStoreLocationId()))
				.collect(Collectors.toUnmodifiableList());
		upcDepleteInventory.execute(itemList, NOT_REQUIRED, upcMasterId);
	}


	private Double getConvertedContentPerUnit(UpcMasterModel upcMasterModel) {
		Double contentPerUnit = upcMasterModel.getContentPerUnit();
		Long contentPerUnitUomId = upcMasterModel.getContentPerUnitUom().getCatalogId();
		Long inventoryUnitId = upcMasterModel.getInventoryUnit().getCatalogId();
		return unitConverter.convert(contentPerUnitUomId, inventoryUnitId, contentPerUnit);
	}

}
