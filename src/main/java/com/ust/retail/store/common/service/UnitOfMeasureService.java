package com.ust.retail.store.common.service;

import com.ust.retail.store.common.dto.UnitOfMeasureDTO;
import com.ust.retail.store.common.dto.UnitOfMeasureFilterRequestDTO;
import com.ust.retail.store.common.model.UnitOfMeasureModel;
import com.ust.retail.store.common.repository.UnitOfMeasureRepository;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitOfMeasureService {
	private final UnitOfMeasureRepository unitOfMeasureRepository;

	public UnitOfMeasureService(UnitOfMeasureRepository unitOfMeasureRepository) {
		this.unitOfMeasureRepository = unitOfMeasureRepository;
	}

	@Transactional
	public UnitOfMeasureDTO saveOrUpdate(UnitOfMeasureDTO unitOfMeasureDTO) {
		UnitOfMeasureModel model = unitOfMeasureDTO.createModel();

		if (unitOfMeasureDTO.isBaseUnit()) {
			unitOfMeasureRepository.removeBaseUnitForUnitTypeId(unitOfMeasureDTO.getUnitTypeId());
		}

		return new UnitOfMeasureDTO().parseToDTO(unitOfMeasureRepository.save(model));
	}

	public UnitOfMeasureDTO findById(Long unitOfMeasureId) {
		return this.unitOfMeasureRepository.findById(unitOfMeasureId)
				.map(model -> new UnitOfMeasureDTO().parseToDTO(model))
				.orElseThrow(() -> new ResourceNotFoundException("Unit of Measure", "id", unitOfMeasureId));
	}

	public Page<UnitOfMeasureDTO> getUnitOfMeasuresByFilters(UnitOfMeasureFilterRequestDTO request) {
		return unitOfMeasureRepository.findByFilters(
						request.getUnitTypeId(),
						request.getUnitName(),
						request.createPageable())
				.map(uom -> new UnitOfMeasureDTO().parseToDTO(uom));
	}

	public List<UnitOfMeasureDTO> getAutocomplete(String unitName) {
		return unitOfMeasureRepository.getAutocompleteList(unitName).stream()
				.map(uom -> new UnitOfMeasureDTO().parseToSimpleDTO(uom))
				.collect(Collectors.toUnmodifiableList());
	}

	public UnitOfMeasureDTO findBaseUnitFor(Long unitTypeId) {
		return unitOfMeasureRepository.findFirstByBaseUnitIsTrueAndUnitTypeCatalogId(unitTypeId)
				.map(uom -> new UnitOfMeasureDTO().parseToDTO(uom))
				.orElseThrow(() -> new ResourceNotFoundException("Base Unit of Measure", "unitTypeId", unitTypeId));
	}

	public List<CatalogDTO> getConvertableCatalogUnits() {
		return unitOfMeasureRepository.findAll().stream()
				.map(UnitOfMeasureModel::getUnit)
				.map(u -> new CatalogDTO().parseToDTO(u))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<CatalogDTO> findConvertableCatalogUnitsFor(Long catalogUnitId) {
		return unitOfMeasureRepository.findUnitTypeForUnitCatalogId(catalogUnitId)
				.map(unitTypeId -> unitOfMeasureRepository.findByUnitTypeCatalogId(unitTypeId).stream()
						.map(UnitOfMeasureModel::getUnit)
						.map(u -> new CatalogDTO().parseToDTO(u))
						.collect(Collectors.toUnmodifiableList()))
				.orElse(List.of());
	}

	public List<UnitOfMeasureDTO> findConvertableUnitsFor(Long catalogUnitId) {
		return unitOfMeasureRepository.findUnitTypeForUnitCatalogId(catalogUnitId)
				.map(unitTypeId -> unitOfMeasureRepository.findByUnitTypeCatalogId(unitTypeId).stream()
						.map(uom -> new UnitOfMeasureDTO().parseToDTO(uom))
						.collect(Collectors.toUnmodifiableList()))
				.orElse(List.of());
	}
}
