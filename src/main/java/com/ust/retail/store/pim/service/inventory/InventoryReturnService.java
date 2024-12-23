package com.ust.retail.store.pim.service.inventory;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.inventory.returns.operation.ReturnDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.InventoryReturnItemDetailDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.InventoryReturnSumaryDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.ReturnItemDTO;
import com.ust.retail.store.pim.exceptions.InvalidInventoryReturnReferenceException;
import com.ust.retail.store.pim.exceptions.InvalidUPCException;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnDetailModel;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.inventory.InventoryProductReturnRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
public class InventoryReturnService {

	private final InventoryProductReturnRepository inventoryReturnRepository;
	private final UpcMasterRepository upcMasterRepository;
	private final InventoryService inventoryService;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public InventoryReturnService(InventoryProductReturnRepository inventoryReturnRepository,
			InventoryService inventoryService, UpcMasterRepository upcMasterRepository,
			AuthenticationFacade authenticationFacade) {
		super();
		this.inventoryReturnRepository = inventoryReturnRepository;
		this.inventoryService = inventoryService;
		this.upcMasterRepository = upcMasterRepository;
		this.authenticationFacade = authenticationFacade;
	}

	@Transactional
	public ReturnDTO start(ReturnDTO returnDTO) {

		InventoryProductReturnModel returnModel = inventoryReturnRepository
				.save(returnDTO.createModel(authenticationFacade.getCurrentUserId()));

		return new ReturnDTO(returnModel.getInventoryProductReturnId());

	}

	@Transactional
	public Boolean returnItems(@Valid ReturnItemDTO returnItemDTO) {

		UpcMasterModel product = upcMasterRepository.findByPrincipalUpc(returnItemDTO.getCode())
				.orElseThrow(() -> new InvalidUPCException(returnItemDTO.getCode()));
		
		InventoryProductReturnModel currentReturnModel = inventoryReturnRepository
				.findById(returnItemDTO.getInventoryProductReturnId())
				.orElseThrow(() -> new InvalidInventoryReturnReferenceException(
						returnItemDTO.getInventoryProductReturnId()));

		currentReturnModel.add(returnItemDTO, product.getUpcMasterId());

		currentReturnModel = inventoryReturnRepository.save(currentReturnModel);

		inventoryService.vendorCredits(returnItemDTO, currentReturnModel.getInventoryProductReturnId());

		return true;
	}

	public InventoryReturnSumaryDTO getReturnSummary(Long inventoryReturnId) {

		InventoryReturnSumaryDTO summary = new InventoryReturnSumaryDTO();

		InventoryProductReturnModel returnModel = inventoryReturnRepository.findById(inventoryReturnId)
				.orElseThrow(() -> new InvalidInventoryReturnReferenceException(inventoryReturnId));

		for (InventoryProductReturnDetailModel currentDetail : returnModel.getReturnDetails()) {

			UpcMasterModel product = upcMasterRepository.findByPrincipalUpc(currentDetail.getUpcMaster().getPrincipalUpc())
					.orElseThrow(() -> new InvalidUPCException(currentDetail.getUpcMaster().getPrincipalUpc()));

			InventoryReturnItemDetailDTO adjustmentDetails = new InventoryReturnItemDetailDTO(
					currentDetail.getInventoryProductReturnDetailId(),
					product.getUpcMasterId(),
					product.getProductName(),
					product.getPrincipalUpc(),
					currentDetail.getBatchNumber(),
					currentDetail.getQty(),
					currentDetail.getReturnReason().getCatalogId(),
					currentDetail.getReturnReason().getCatalogOptions());

			summary.add(product.getProductCategory().getProductCategoryName(), adjustmentDetails);

		}

		return summary;
	}

	public boolean finish(Long inventoryProductReturnId) {

		InventoryProductReturnModel returnModel = inventoryReturnRepository.findById(inventoryProductReturnId)
				.orElseThrow(() -> new InvalidInventoryReturnReferenceException(inventoryProductReturnId));

		returnModel.finishReturn();

		inventoryReturnRepository.save(returnModel);

		return true;
	}
}
