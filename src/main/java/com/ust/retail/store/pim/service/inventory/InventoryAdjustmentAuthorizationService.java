package com.ust.retail.store.pim.service.inventory;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.catalogs.InventoryAdjustmentStatusCatalog;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationDetailDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationFilterDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationFilterResultDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationRequestDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationRequestLineDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationResultDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationResultLineDTO;
import com.ust.retail.store.pim.engine.inventory.InventoryAdjustment;
import com.ust.retail.store.pim.engine.inventory.InventoryEngine;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentModel;
import com.ust.retail.store.pim.repository.inventory.InventoryAdjustmentRepository;

@Service
public class InventoryAdjustmentAuthorizationService {
	private final InventoryAdjustmentRepository inventoryAdjustmentRepository;
	private final InventoryService inventoryService;

	public InventoryAdjustmentAuthorizationService(
			InventoryAdjustmentRepository inventoryAdjustmentRepository,
			InventoryService inventoryService) {
		this.inventoryAdjustmentRepository = inventoryAdjustmentRepository;
		this.inventoryService = inventoryService;
	}

	public Page<InventoryAdjustmentAuthorizationFilterResultDTO> getInventoryAdjustmentsByFilters(InventoryAdjustmentAuthorizationFilterDTO dto) {
		return inventoryAdjustmentRepository.findByFilters(
						dto.getBrandOwnerId(),
						dto.getProductTypeId(),
						dto.getProductGroupId(),
						dto.getProductCategoryId(),
						dto.getProductName(),
						dto.getStatusId(),
						dto.createPageable())
				.map(m -> new InventoryAdjustmentAuthorizationFilterResultDTO().parseToDTO(m));
	}
	
	public InventoryAdjustmentAuthorizationDTO findById(Long adjustmentId) {
		
		InventoryAdjustmentModel adjustmentModel = inventoryAdjustmentRepository.findById(adjustmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Inventory Adjustment", "adjustmentId", adjustmentId));
		
		InventoryAdjustmentAuthorizationDTO dto = new InventoryAdjustmentAuthorizationDTO().parseToDTO(adjustmentModel);
		
		dto.setDetails(getAdjustmentDetailWithStatus(adjustmentId));
		
		return dto;
	}

	

	public InventoryAdjustmentAuthorizationResultDTO authorize(InventoryAdjustmentAuthorizationRequestDTO dto) {
		
		InventoryAdjustmentAuthorizationResultDTO result = new InventoryAdjustmentAuthorizationResultDTO();

		for (InventoryAdjustmentAuthorizationRequestLineDTO line : dto.getLines()) {
			result.getResults().add(new InventoryAdjustmentAuthorizationResultLineDTO(
					line.getTxnNum(),
					line.getAdjustmentDetailId(),
					inventoryService.authorizeByTxnNumber(line.getTxnNum()),
					null));
		}

		result.setStatusId(updateAdjustmentStatus(dto.getAdjustmentId()));
		return result;
	}

	public InventoryAdjustmentAuthorizationResultDTO decline(InventoryAdjustmentAuthorizationRequestDTO dto) {
		InventoryAdjustmentAuthorizationResultDTO result = new InventoryAdjustmentAuthorizationResultDTO();

		for (InventoryAdjustmentAuthorizationRequestLineDTO line : dto.getLines()) {
			result.getResults().add(new InventoryAdjustmentAuthorizationResultLineDTO(
					line.getTxnNum(),
					line.getAdjustmentDetailId(),
					inventoryService.rejectByTxnNumber(line.getTxnNum()),
					null));
		}

		result.setStatusId(updateAdjustmentStatus(dto.getAdjustmentId()));
		return result;
	}

	private List<InventoryAdjustmentAuthorizationDetailDTO> getAdjustmentDetailWithStatus(Long adjustmentId) {
		return inventoryAdjustmentRepository.findAdjustmentDetailWithStatus(adjustmentId, InventoryAdjustment.OPERATION_MODULE, InventoryEngine.OPERATION_TYPE_SET);
	}

	private long updateAdjustmentStatus(Long adjustmentId) {
		long adjustmentStatusId = getAdjustmentDetailWithStatus(adjustmentId).stream()
				.anyMatch(d -> Objects.equals(d.getStatusId(), InventoryEngine.AUTHORIZATION_STATUS_PENDING)) ?
				InventoryAdjustmentStatusCatalog.INVENTORY_ADJUSTMENT_STATUS_PARTIAL_REVIEW :
				InventoryAdjustmentStatusCatalog.INVENTORY_ADJUSTMENT_STATUS_REVIEWED;

		inventoryAdjustmentRepository.findById(adjustmentId)
				.ifPresent(adjustment -> {
					adjustment.setStatus(adjustmentStatusId);
					inventoryAdjustmentRepository.save(adjustment);
				});

		return adjustmentStatusId;
	}

}
