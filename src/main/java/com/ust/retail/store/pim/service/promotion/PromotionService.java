package com.ust.retail.store.pim.service.promotion;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.promotion.PromotionDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionFilterDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionFilterResultDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.promotion.PromotionModel;
import com.ust.retail.store.pim.repository.promotion.PromotionRepository;
import com.ust.retail.store.pim.util.DateUtils;

@Service
public class PromotionService {
	private final PromotionRepository promotionRepository;
	private final AuthenticationFacade authenticationFacade;

	public PromotionService(PromotionRepository promotionRepository, AuthenticationFacade authenticationFacade) {
		this.promotionRepository = promotionRepository;
		this.authenticationFacade = authenticationFacade;
	}

	@Transactional
	public PromotionDTO saveOrUpdate(@Valid PromotionDTO promotionDTO) {
		PromotionModel promotionModel = promotionRepository.save(
				promotionDTO.createModel(this.authenticationFacade.getCurrentUserId()));
		return promotionDTO.parseToDTO(promotionModel);
	}

	public PromotionDTO findById(Long promotionId) {
		return promotionRepository.findById(promotionId)
				.map(m -> new PromotionDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("Promotion", "id", promotionId));
	}

	@Transactional
	public void deleteById(Long promotionId) {
		promotionRepository.deleteById(promotionId);
	}

	public Page<PromotionFilterResultDTO> getPromotionsByFilters(PromotionFilterDTO filterDTO) {
		return promotionRepository.findByFilters(
				filterDTO.getBrandOwnerId(),
				filterDTO.getVendorMasterId(),
				filterDTO.getVendorCode(),
				filterDTO.getVendorName(),
				filterDTO.getProductGroupId(),
				filterDTO.getProductCategoryId(),
				filterDTO.getProductSubcategoryId(),
				filterDTO.getProductItemId(),
				filterDTO.getProductMasterId(),
				filterDTO.getPromotionTypeId(),
				filterDTO.getExpirationDateStart(),
				filterDTO.getExpirationDateEnd(),
				filterDTO.createPageable());
	}

	public Optional<PromotionDTO> findApplicablePromotionForProduct(Long vendorMasterId, Long upcMasterId) {
		return promotionRepository.findActiveProductPromotions(vendorMasterId, upcMasterId, DateUtils.atStartOfDay(new Date())).stream()
				.findFirst()
				.map(m -> new PromotionDTO().parseToDTO(m));
	}
}
