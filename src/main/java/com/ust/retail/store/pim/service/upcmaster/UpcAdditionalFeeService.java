package com.ust.retail.store.pim.service.upcmaster;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.upcmaster.UpcAdditionalFeeDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcAdditionalFeeFilterDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcAdditionalFeeFilterResultDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.upcmaster.UpcAdditionalFeeModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcAdditionalFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpcAdditionalFeeService {
	private final UpcAdditionalFeeRepository upcAdditionalFeeRepository;
	private final AuthenticationFacade authenticationFacade;

	@Transactional
	public UpcAdditionalFeeDTO saveOrUpdate(UpcAdditionalFeeDTO dto) {
		UpcAdditionalFeeModel additionalFee = upcAdditionalFeeRepository.save(
				dto.createModel(authenticationFacade.getCurrentUserId()));
		return new UpcAdditionalFeeDTO().parseToDTO(additionalFee);
	}

	@Transactional
	public void deleteById(Long upcAdditionalFeeId) {
		upcAdditionalFeeRepository.deleteById(upcAdditionalFeeId);
	}

	public UpcAdditionalFeeDTO findById(Long upcAdditionalFeeId) {
		return upcAdditionalFeeRepository.findById(upcAdditionalFeeId)
				.map(m -> new UpcAdditionalFeeDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("Upc Additional Fee", "id", upcAdditionalFeeId));
	}

	public Page<UpcAdditionalFeeFilterResultDTO> findByFilters(UpcAdditionalFeeFilterDTO dto) {
		return upcAdditionalFeeRepository.findByFilters(
						dto.getUpcMasterId(),
						dto.getStoreNumId(),
						dto.getAdditionalFeeId(),
						dto.createPageable())
				.map(af -> new UpcAdditionalFeeFilterResultDTO().modelToDTO(af));
	}
}
