package com.ust.retail.store.pim.service.catalog;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.BrandOwnerModel;
import com.ust.retail.store.pim.repository.catalog.BrandOwnerRepository;

@Service
public class BrandOwnerService extends BaseService {

	private final BrandOwnerRepository brandOwnerRepository;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public BrandOwnerService(BrandOwnerRepository brandOwnerRepository,AuthenticationFacade authenticationFacade) {
		super();
		this.brandOwnerRepository = brandOwnerRepository;
		this.authenticationFacade = authenticationFacade;
	}

	public BrandOwnerDTO saveOrUpdate(BrandOwnerDTO brandOwnerDTO) {
		BrandOwnerModel brandOwnerModel = this.brandOwnerRepository.save(brandOwnerDTO.createModel(this.authenticationFacade.getCurrentUserId()));
		return brandOwnerDTO.parseToDTO(brandOwnerModel); 
	}

	public BrandOwnerDTO findById(Long brandOwnerId) {
		BrandOwnerModel brandOwnerModel = this.brandOwnerRepository.findById(brandOwnerId)
				.orElseThrow(() -> new ResourceNotFoundException("Brand Owner", "id",brandOwnerId));
		
		return new BrandOwnerDTO().parseToDTO(brandOwnerModel);
	}
	
	public Page<BrandOwnerDTO> getBrandOwnersByFilters(@Valid BrandOwnerDTO brandOwnerDTO) {
		return brandOwnerRepository.findByFilters(brandOwnerDTO.getBrandOwnerName(), brandOwnerDTO.createPageable());
	}
	
	public List<BrandOwnerDTO> getAutocomplete(@Valid String brandOwnerName) {
		return brandOwnerRepository.getAutocompleteList(brandOwnerName);
	}

	public List<BrandOwnerDTO> load() {
		return brandOwnerRepository.findAll().stream()
				.map(m -> new BrandOwnerDTO().parseToDTO(m))
				.collect(Collectors.toUnmodifiableList());
	}

}
