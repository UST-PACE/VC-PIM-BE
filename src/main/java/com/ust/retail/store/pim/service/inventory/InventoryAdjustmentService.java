package com.ust.retail.store.pim.service.inventory;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.catalogs.DailyCountStatusCatalog;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.*;
import com.ust.retail.store.pim.engine.inventory.InventoryAdjustment;
import com.ust.retail.store.pim.exceptions.InvalidInventoryAdjustmentReferenceException;
import com.ust.retail.store.pim.exceptions.InvalidStatusChangeException;
import com.ust.retail.store.pim.exceptions.InvalidUPCException;
import com.ust.retail.store.pim.exceptions.InventoryAdjustmentProcessInProgressException;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentCategoryModel;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentDetailModel;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.inventory.InventoryAdjustmentRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryAdjustmentSubcategoryRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryAdjustmentService {

	private final InventoryAdjustmentRepository inventoryAdjustmentRepository;
	private final InventoryAdjustmentSubcategoryRepository inventoryAdjustmentSubcategoryRepository;
	private final UpcMasterRepository upcMasterRepository;
	private final InventoryService inventoryService;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public InventoryAdjustmentService(InventoryAdjustmentRepository inventoryAdjustmentRepository,
	                                  InventoryAdjustmentSubcategoryRepository inventoryAdjustmentSubcategoryRepository,
	                                  UpcMasterRepository upcMasterRepository,
	                                  InventoryService inventoryService,
	                                  AuthenticationFacade authenticationFacade) {
		super();
		this.inventoryAdjustmentRepository = inventoryAdjustmentRepository;
		this.inventoryAdjustmentSubcategoryRepository = inventoryAdjustmentSubcategoryRepository;
		this.inventoryService = inventoryService;
		this.upcMasterRepository = upcMasterRepository;
		this.authenticationFacade = authenticationFacade;
	}

	@Transactional
	public InventoryAdjustmentDTO start(StartDailyCountDTO startDailyCountDTO) {

		verifyActiveProcesses(startDailyCountDTO);

		InventoryAdjustmentModel adjustment = inventoryAdjustmentRepository
				.save(startDailyCountDTO.createModel(authenticationFacade.getCurrentUserId()));

		return new InventoryAdjustmentDTO(adjustment.getInventoryAdjustmentId());

	}

	private void verifyActiveProcesses(StartDailyCountDTO startDailyCountDTO) {
		List<Long> activeCategoriesWithCounts = inventoryAdjustmentSubcategoryRepository
				.getActiveCountsBySubcategory(startDailyCountDTO.getStoreLocationId(),
						startDailyCountDTO.getCategories().stream()
								.map(InventoryAdjustmentCategoryDTO::getProductCategoryId)
								.collect(Collectors.toUnmodifiableList()),
						DailyCountStatusCatalog.DAILY_COUNT_STATUS_IN_PROCESS).stream()
				.map(InventoryAdjustmentCategoryModel::getProductCategory)
				.map(ProductCategoryModel::getProductCategoryId)
				.collect(Collectors.toList());

		if (!activeCategoriesWithCounts.isEmpty()) {
			throw new InventoryAdjustmentProcessInProgressException(activeCategoriesWithCounts.stream()
					.distinct()
					.map(String::valueOf)
					.collect(Collectors.joining(",")));
		}
	}

	@Transactional
	public Boolean interrupt(@Valid StartDailyCountDTO startDailyCountDTO) {
		List<InventoryAdjustmentCategoryModel> existingCounts = inventoryAdjustmentSubcategoryRepository
				.getActiveCountsBySubcategory(startDailyCountDTO.getStoreLocationId(),
						startDailyCountDTO.getCategories().stream()
								.map(InventoryAdjustmentCategoryDTO::getProductCategoryId)
								.collect(Collectors.toUnmodifiableList()),
						DailyCountStatusCatalog.DAILY_COUNT_STATUS_IN_PROCESS);

		if (!existingCounts.isEmpty()) {
			existingCounts.forEach(count -> {
				count.interruptCount();
				inventoryAdjustmentSubcategoryRepository.save(count);

				inventoryService.clearInterruptedTransactions(
						count.getInventoryAdjustment().getInventoryAdjustmentId(),
						InventoryAdjustment.OPERATION_MODULE);
			});
		} else {
			throw new InvalidStatusChangeException();
		}

		return true;
	}

	@Transactional
	public Boolean adjustInventory(@Valid InventoryAdjustmentDTO inventoryAdjustmentDTO) {

		InventoryAdjustmentModel adjustment = inventoryAdjustmentRepository
				.findById(inventoryAdjustmentDTO.getInventoryAdjustmentId())
				.orElseThrow(() -> new InvalidInventoryAdjustmentReferenceException(
						inventoryAdjustmentDTO.getInventoryAdjustmentId()));

		adjustment.removeUpcShrinkages(inventoryAdjustmentDTO.getItem().getUpcMasterId());

		adjustment = inventoryAdjustmentRepository
				.save(inventoryAdjustmentDTO.updateModel(adjustment, authenticationFacade.getCurrentUserId()));

		inventoryService.invetoryAdjustment(inventoryAdjustmentDTO, adjustment.getInventoryAdjustmentId());

		return true;
	}

	public InventoryAdjustmentSumaryDTO getAdjustmentSumary(Long inventoryAdjustmentId) {

		InventoryAdjustmentSumaryDTO summary = new InventoryAdjustmentSumaryDTO();

		InventoryAdjustmentModel adjustment = inventoryAdjustmentRepository.findById(inventoryAdjustmentId)
				.orElseThrow(() -> new InvalidInventoryAdjustmentReferenceException(inventoryAdjustmentId));

		for (InventoryAdjustmentDetailModel currentDetail : adjustment.getDetails()) {

			UpcMasterModel product = upcMasterRepository.findById(currentDetail.getUpcMaster().getUpcMasterId())
					.orElseThrow(() -> new InvalidUPCException(inventoryAdjustmentId));

			InventoryAdjustmentItemDetailDTO adjustmentDetails = new InventoryAdjustmentItemDetailDTO(
					currentDetail.getInventoryAdjustmentDetailId(),
					product.getUpcMasterId(),
					product.getPrincipalUpc(), product.getProductName(), currentDetail.getSetQty(),
					currentDetail.getInventoryAdjustment().getShrinkages());

			summary.add(product.getProductCategory().getProductCategoryName(), adjustmentDetails);

		}

		return summary;
	}

	public boolean finish(Long inventoryAdjustmentId) {

		InventoryAdjustmentModel adjustment = inventoryAdjustmentRepository.findById(inventoryAdjustmentId)
				.orElseThrow(() -> new InvalidInventoryAdjustmentReferenceException(inventoryAdjustmentId));

		adjustment.finishAdjustment();

		adjustment.getCategories().forEach(InventoryAdjustmentCategoryModel::finishCount);
		inventoryAdjustmentRepository.save(adjustment);

		return true;
	}
}
