package com.ust.retail.store.pim.service.tax;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.tax.TaxDTO;
import com.ust.retail.store.pim.dto.tax.TaxFilterDTO;
import com.ust.retail.store.pim.dto.tax.TaxFilterResultDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.tax.TaxModel;
import com.ust.retail.store.pim.repository.tax.TaxRepository;

@Service
public class TaxService {
	private final TaxRepository taxRepository;
	private final AuthenticationFacade authenticationFacade;

	public TaxService(TaxRepository taxRepository,
					  AuthenticationFacade authenticationFacade) {
		this.taxRepository = taxRepository;
		this.authenticationFacade = authenticationFacade;
	}

	@Transactional
	public TaxDTO saveOrUpdate(@Valid TaxDTO taxDTO) {
		if (Objects.isNull(taxDTO.getTaxId()) && taxRepository.existsByUniqueKey(taxDTO.getStoreNumId(), taxDTO.getTaxTypeId(), taxDTO.getProductGroupId(), taxDTO.getProductCategoryId(), taxDTO.getProductSubcategoryId())) {
			throw new DataIntegrityViolationException("Tax exists", new ConstraintViolationException("", new SQLException(), "tax_by_store_and_type"));
		}
		TaxModel taxModel = taxRepository.save(taxDTO.createModel(this.authenticationFacade.getCurrentUserId()));
		return taxDTO.parseToDTO(taxModel);
	}

	public TaxDTO findById(Long taxId) {
		return taxRepository.findById(taxId)
				.map(m -> new TaxDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("Tax", "id", taxId));
	}

	@Transactional
	public void deleteById(Long taxId) {
		taxRepository.deleteById(taxId);
	}

	public Page<TaxFilterResultDTO> getTaxesByFilters(TaxFilterDTO filterDTO) {
		return taxRepository.findByFilters(
						filterDTO.getStoreNumId(),
						filterDTO.getTaxTypeId(),
						filterDTO.getProductGroupId(),
						filterDTO.getProductCategoryId(),
						filterDTO.getProductSubcategoryId(),
						filterDTO.createPageable())
				.map(m -> new TaxFilterResultDTO().parseToDTO(m));
	}

	public Set<TaxDTO> findApplicableTaxesForProductAtStore(Long upcMasterId, Long storeNumId) {
		return taxRepository.findProductTaxes(upcMasterId, storeNumId).stream()
				.map(m -> new TaxDTO().parseToDTO(m))
				.collect(Collectors.toSet());
	}
}
