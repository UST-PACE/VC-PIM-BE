package com.ust.retail.store.pim.service.inventory;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.dto.inventory.InventoryProductDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryTransferencesDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryTransferencesFiltersDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.InventoryTransferModel;
import com.ust.retail.store.pim.repository.inventory.InventoryTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class InventoryTransferenceService extends BaseService {

	private final InventoryTransferRepository inventoryTransferRepository;
	private final InventoryService inventoryService;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public InventoryTransferenceService(InventoryService inventoryService,
										InventoryTransferRepository inventoryTransferRepository,
										AuthenticationFacade authenticationFacade) {
		super();
		this.inventoryTransferRepository = inventoryTransferRepository;
		this.inventoryService = inventoryService;
		this.authenticationFacade = authenticationFacade;
	}

	@Transactional
	public InventoryTransferencesDTO saveOrUpdate(InventoryTransferencesDTO transferenceDTO) {

		InventoryProductDTO inventoryProduct = inventoryService.findInventoryByCodeAndStoreLocation(
				transferenceDTO.getPrincipalUpc(), transferenceDTO.getStoreLocationFromId());

		InventoryTransferModel transferenceModel = this.inventoryTransferRepository.save(transferenceDTO.createModel(
				inventoryProduct.getUpcMasterModel().getUpcMasterId(), this.authenticationFacade.getCurrentUserId()));

		inventoryService.trasferInventory(inventoryProduct.getUpcMasterModel().getPrincipalUpc(),
				transferenceDTO.getStoreLocationFromId(), transferenceDTO.getStoreLocationToId(),
				transferenceDTO.getQty(), transferenceModel.getInventoryTransferId());

		return transferenceDTO.parseToDTO(transferenceModel, inventoryProduct.getUpcMasterModel().getPrincipalUpc(),
				inventoryProduct.getUpcMasterModel().getUpcMasterId());
	}

	public InventoryTransferencesDTO findById(Long inventoryTransferId) {
		InventoryTransferModel transferenceModel = this.inventoryTransferRepository.findById(inventoryTransferId)
				.orElseThrow(() -> new ResourceNotFoundException("Transference", "id", inventoryTransferId));

		return new InventoryTransferencesDTO().parseToDTO(transferenceModel);
	}

	public Page<InventoryTransferencesFiltersDTO> getTransferenceByFilters(
			InventoryTransferencesFiltersDTO inventoryTransferencesFiltersDTO) {

		return inventoryTransferRepository.findByFilters(inventoryTransferencesFiltersDTO.getPrincipalUpc(),
				inventoryTransferencesFiltersDTO.getStoreLocationToFromId(),
				inventoryTransferencesFiltersDTO.getStoreLocationToId(),
				inventoryTransferencesFiltersDTO.createPageable());
	}

}
