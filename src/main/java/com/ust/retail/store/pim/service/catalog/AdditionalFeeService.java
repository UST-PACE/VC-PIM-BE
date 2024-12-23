package com.ust.retail.store.pim.service.catalog;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.catalog.AdditionalFeeDTO;
import com.ust.retail.store.pim.dto.catalog.AdditionalFeeFilterDTO;
import com.ust.retail.store.pim.dto.catalog.AdditionalFeeFilterResultDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.AdditionalFeeModel;
import com.ust.retail.store.pim.repository.catalog.AdditionalFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdditionalFeeService {
    private final AdditionalFeeRepository additionalFeeRepository;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public AdditionalFeeDTO saveOrUpdate(AdditionalFeeDTO dto) {
        AdditionalFeeModel additionalFee = additionalFeeRepository.save(
                dto.createModel(authenticationFacade.getCurrentUserId()));
        return new AdditionalFeeDTO().parseToDTO(additionalFee);
    }

    @Transactional
    public void deleteById(Long additionalFeeId) {
        additionalFeeRepository.deleteById(additionalFeeId);
    }

    public AdditionalFeeDTO findById(Long additionalFeeId) {
        return additionalFeeRepository.findById(additionalFeeId)
                .map(m -> new AdditionalFeeDTO().parseToDTO(m))
                .orElseThrow(() -> new ResourceNotFoundException("Upc Additional Fee", "id", additionalFeeId));
    }

    public Page<AdditionalFeeFilterResultDTO> findByFilters(AdditionalFeeFilterDTO dto) {
        return additionalFeeRepository.findByFilters(dto.getFeeName(), dto.createPageable())
                .map(af -> new AdditionalFeeFilterResultDTO().modelToDTO(af));
    }

    public List<AdditionalFeeDTO> load() {
        return additionalFeeRepository.findAll().stream()
                .map(af -> new AdditionalFeeDTO().parseToDTO(af))
                .collect(Collectors.toUnmodifiableList());
    }
}
