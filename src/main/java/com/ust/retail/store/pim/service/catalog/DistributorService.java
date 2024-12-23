package com.ust.retail.store.pim.service.catalog;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.dto.catalog.DistributorDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.DistributorModel;
import com.ust.retail.store.pim.repository.catalog.DistributorRepository;

@Service
public class DistributorService extends BaseService {

	private final DistributorRepository distributorRepository;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public DistributorService(DistributorRepository distributorRepository,AuthenticationFacade authenticationFacade) {
		super();
		this.distributorRepository = distributorRepository;
		this.authenticationFacade = authenticationFacade;
	}

	public DistributorDTO saveOrUpdate(DistributorDTO distributorDTO) {
		DistributorModel distributorModel = this.distributorRepository.save(distributorDTO.createModel(this.authenticationFacade.getCurrentUserId()));
		return distributorDTO.parseToDTO(distributorModel); 
	}

	public DistributorDTO findById(Long distributorId) {
		DistributorModel distributorModel = this.distributorRepository.findById(distributorId)
				.orElseThrow(() -> new ResourceNotFoundException("Distributor", "id",distributorId));
		
		return new DistributorDTO().parseToDTO(distributorModel);
	}

	public Page<DistributorDTO> getDistributorsByFilters(@Valid DistributorDTO distributorDTO) {
		return distributorRepository.findByFilters(distributorDTO.getDistributorName(), distributorDTO.createPageable());
	}
	
	public List<DistributorDTO> getAutocomplete(@Valid String distributorName) {
		return distributorRepository.getAutocompleteList(distributorName);
	}
	
	public List<DistributorDTO> load() {
		return distributorRepository.findAll().stream()
				.map(m -> new DistributorDTO().parseToDTO(m))
				.collect(Collectors.toUnmodifiableList());
	}

}
