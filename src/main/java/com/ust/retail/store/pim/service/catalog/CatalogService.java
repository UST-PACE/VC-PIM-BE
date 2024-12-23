package com.ust.retail.store.pim.service.catalog;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.repository.catalog.CatalogRepository;

@Service
public class CatalogService extends BaseService {

	private final CatalogRepository catalogRepository;

	public CatalogService(CatalogRepository catalogRepository) {
		super();
		this.catalogRepository = catalogRepository;
	}

	public List<CatalogDTO> findByCatalogName(String catalogName) {
		return catalogRepository.findByCatalogName(catalogName).stream()
				.map(model -> new CatalogDTO().parseToDTO(model))
				.collect(Collectors.toList());
	}

	public CatalogDTO findCatalogById(Long catalogId) {
		return catalogRepository.findById(catalogId)
				.map(model -> new CatalogDTO().parseToDTO(model))
				.orElseThrow(() -> new ResourceNotFoundException("Catalog", "id", catalogId));
	}

}
