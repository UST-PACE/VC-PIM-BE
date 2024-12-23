package com.ust.retail.store.pim.service.external;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.dto.external.store.ExternalStoreDTO;
import com.ust.retail.store.pim.repository.catalog.StoreNumberRepository;

@Service
public class ExternalStoreNumberService {
	private final StoreNumberRepository storeNumberRepository;

	public ExternalStoreNumberService(StoreNumberRepository storeNumberRepository) {
		this.storeNumberRepository = storeNumberRepository;
	}

	public List<ExternalStoreDTO> loadCatalog() {
		return storeNumberRepository.findAll().stream()
				.map(m -> new ExternalStoreDTO().parseToDTO(m))
				.collect(Collectors.toUnmodifiableList());
	}
}
