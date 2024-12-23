package com.ust.retail.store.pim.engine.inventory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.ust.retail.store.common.util.UnitConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.QtyOperationResultDTO;
import com.ust.retail.store.pim.model.inventory.InventoryHistoryModel;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.repository.inventory.InventoryHistoryRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryRepository;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public abstract class InventoryEngine {

	public final static Long AUTHORIZATION_STATUS_NOT_REQUIRED = 12000L;
	public final static Long AUTHORIZATION_STATUS_PENDING = 12001L;
	public final static Long AUTHORIZATION_STATUS_REJECTED = 12002L;
	public final static Long AUTHORIZATION_STATUS_AUTHORIZED = 12003L;

	public final static Long OPERATION_TYPE_SET = 11000L;
	public final static Long OPERATION_TYPE_INCREASE = 11001L;
	public final static Long OPERATION_TYPE_SHRINKAGE = 11002L;
	public final static Long OPERATION_TYPE_VENDOR_CREDIT = 11003L;
	public final static Long OPERATION_TYPE_SALE = 11004L;
	public final static Long OPERATION_TYPE_NO_CHANGE = 11005L;
	public final static Long OPERATION_TYPE_REDUCE = 11006L;
	public final static Long OPERATION_TYPE_WASTE_WHOLE_DISH = 11007L;
	public final static Long OPERATION_TYPE_TOSSING = 11008L;

	protected final static QtyOperationResultDTO NOT_ACTIVE = null;
	
	public final static List<Item> NO_SHRIKAGE_LIST = null;
	public final static Long DEFAULT_REFERENCE_ID = 1L;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private InventoryHistoryRepository inventoryHistoryRepository;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@Autowired
	protected UnitConverter unitConverter;

	@Transactional
	public void execute(List<Item> items, List<Item> shrinkageItems, Long referenceId) {

		Long txnNumber = new Date().getTime();

		Map<Long, Double> inventoryMap = processItems(items, txnNumber, referenceId);

		processShrinkageItems(inventoryMap, shrinkageItems, txnNumber, referenceId);
	}

	private Map<Long, Double> processItems(List<Item> items, Long txtNum, Long referenceId) {
		Map<Long, Double> inventoryMap = new HashMap<>();

		for (Item currentItem : items) {
			InventoryModel currentInventory = getInventoryItem(currentItem);

			QtyOperationResultDTO qtyOperationResult = getOperationResult(currentInventory, currentItem);

			processInventoryHistoryRecord(currentItem, currentInventory.getInventoryId(), qtyOperationResult, txtNum, referenceId);

			if (!isAuthorizationRequired()) {
				updateInventory(currentInventory, qtyOperationResult.getFinalQty());
			} else {
				inventoryMap.put(currentInventory.getInventoryId(), qtyOperationResult.getFinalQty());
			}
		}

		return inventoryMap;
	}

	private void processShrinkageItems(Map<Long, Double> inventoryMap, List<Item> shrinkageItems, Long txnNumber, Long referenceId) {

		if (shrinkageItems != null) {

			for (Item currentItem : shrinkageItems) {

				QtyOperationResultDTO qtyOperationResult;

				if (!isAuthorizationRequired()) {

					InventoryModel currentInventory = getInventoryItem(currentItem);

					qtyOperationResult = getItemFinalQty(currentInventory.getQty(), currentItem.getQty(),
							currentItem.isShrinkage());

					processInventoryHistoryRecord(currentItem, currentInventory.getInventoryId(), qtyOperationResult,
							txnNumber, referenceId);

					updateInventory(currentInventory, qtyOperationResult.getFinalQty());

				} else {

					Double currentInventoryQty = inventoryMap.get(currentItem.getInventoryId());

					qtyOperationResult = getItemFinalQty(currentInventoryQty, currentItem.getQty(),
							currentItem.isShrinkage());

					inventoryMap.put(currentItem.getInventoryId(), qtyOperationResult.getFinalQty());

					processInventoryHistoryRecord(currentItem, currentItem.getInventoryId(), qtyOperationResult, txnNumber, referenceId);

				}

			}
		}
	}

	private QtyOperationResultDTO getOperationResult(InventoryModel currentInventory, Item currentItem) {

		QtyOperationResultDTO qtyOperationResult = getItemFinalQty(currentInventory.getQty(), currentItem.getQty(),
				currentItem.isShrinkage());

		if (qtyOperationResult == NOT_ACTIVE) {

			qtyOperationResult = getItemFinalQty(currentInventory, currentItem.getQty());
		}

		return qtyOperationResult;
	}

	private InventoryModel getInventoryItem(Item item) {
		return inventoryRepository
				.findByUpcMasterUpcMasterIdAndStoreLocationStoreLocationId(item.getUpcMasterId(), item.getStoreLocationId())
				.orElseGet(() -> inventoryRepository.save(item.createModel(authenticationFacade.getCurrentUserId())));
	}

	private void processInventoryHistoryRecord(Item newInventoryInfo,
											   Long inventoryId,
											   QtyOperationResultDTO qtyOperationResult,
											   Long txnNumber,
											   Long referenceId) {

		InventoryHistoryModel inventoryHistoryModel = new InventoryHistoryModel(
				qtyOperationResult.getPreviewsQty(),
				newInventoryInfo.getQty(),
				qtyOperationResult.getFinalQty(),
				referenceId,
				getOperationType(newInventoryInfo),
				qtyOperationResult.getOperationResultId(),
				getOperationModule(),
				inventoryId,
				getDefaultAuthorizationStatus(),
				txnNumber,
				authenticationFacade.getCurrentUserId());

		inventoryHistoryRepository.save(inventoryHistoryModel);
	}

	private void updateInventory(InventoryModel currentInventory, Double finalQty) {

		currentInventory.setQty(finalQty);

		inventoryRepository.save(currentInventory);

	}

	protected abstract Long getOperationType(Item inventoryInfoDTO);

	protected abstract Long getOperationModule();

	protected abstract QtyOperationResultDTO getItemFinalQty(Double currentQty, Double newQty, boolean isShrinkage);

	protected abstract QtyOperationResultDTO getItemFinalQty(InventoryModel inventoryModel, Double newQty);

	protected abstract boolean isAuthorizationRequired();

	protected abstract Long getDefaultAuthorizationStatus();
}
