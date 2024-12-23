package com.ust.retail.store.pim.service.catalog;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.dto.catalog.StoreLocationDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;
import com.ust.retail.store.pim.repository.catalog.StoreLocationRepository;

@Service
public class StoreLocationService extends BaseService {

	private final StoreLocationRepository storeLocationRepository;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public StoreLocationService(StoreLocationRepository storeLocationRepository,
								AuthenticationFacade authenticationFacade) {
		super();
		this.storeLocationRepository = storeLocationRepository;
		this.authenticationFacade = authenticationFacade;
	}

	public StoreLocationDTO save(StoreLocationDTO storeLocationDTO) {
		StoreLocationModel storeLocationModel = this.storeLocationRepository.save(storeLocationDTO.createModel(this.authenticationFacade.getCurrentUserId()));
		return storeLocationDTO.parseToDTO(storeLocationModel); 
	}

	public StoreLocationDTO update(StoreLocationDTO dto) {
		StoreLocationModel model = this.storeLocationRepository.findById(dto.getStoreLocationId())
				.orElseThrow(() -> new ResourceNotFoundException("Store Location", "id", dto.getStoreLocationId()));

		return new StoreLocationDTO().parseToDTO(this.storeLocationRepository.save(model.updateValues(dto)));
	}

	public StoreLocationDTO findById(Long storeLocationId) {
		StoreLocationModel storeLocationModel = this.storeLocationRepository.findById(storeLocationId)
				.orElseThrow(() -> new ResourceNotFoundException("Store Location", "id",storeLocationId));
		
		return new StoreLocationDTO().parseToDTO(storeLocationModel);
	}

	public Page<StoreLocationDTO> getStoreLocationsByFilters(@Valid StoreLocationDTO storeLocationDTO) {
		return storeLocationRepository.findByFilters(storeLocationDTO.getStoreLocationName(), storeLocationDTO.getStoreNumId(),storeLocationDTO.createPageable());
	}
	
	public List<StoreLocationDTO> load(Long storeNumId) {
		return storeLocationRepository.findByStoreNumId(storeNumId);
	}

	public Long findStoreLocationForSales(Long storeNumId) {
		StoreLocationModel storeLocationForSales = storeLocationRepository.findByFrontSaleTrueAndStoreNumberStoreNumId(storeNumId);
		return storeLocationForSales.getStoreLocationId();
	}
	

}
