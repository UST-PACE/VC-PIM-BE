package com.ust.retail.store.pim.service.external;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.dto.external.ExternalComboDTO;
import com.ust.retail.store.pim.repository.upcmaster.ComboRepository;

@Service
public class ExternalComboService {

	private final ComboRepository comboRepository;

	public ExternalComboService(ComboRepository comboRepository) {
		this.comboRepository = comboRepository;
	}

	public List<ExternalComboDTO> loadByCombo() {
		return comboRepository.findAllByDeletedFalse().stream().map(m -> new ExternalComboDTO(m))
				.collect(Collectors.toList());

	}

}
