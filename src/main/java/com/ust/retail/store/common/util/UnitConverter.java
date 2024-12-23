package com.ust.retail.store.common.util;

import com.ust.retail.store.bistro.exception.UnitConvertException;
import com.ust.retail.store.common.dto.UnitOfMeasureDTO;
import com.ust.retail.store.common.service.UnitOfMeasureService;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class UnitConverter {
	private final UnitOfMeasureService unitOfMeasureService;

	public UnitConverter(UnitOfMeasureService unitOfMeasureService) {
		this.unitOfMeasureService = unitOfMeasureService;
	}

	public Double convert(Long unitFromId, Long unitToId, Double value) {
		Double sanitizedValue = Optional.ofNullable(value).orElse(0d);

		if (Objects.equals(unitFromId, unitToId)) {
			return sanitizedValue;
		}

		List<UnitOfMeasureDTO> units = unitOfMeasureService.findConvertableUnitsFor(unitFromId);

		if (units.isEmpty()) {
			log.info("No convertable units found for ID [{}]", unitFromId);
			throw new UnitConvertException(unitFromId, unitToId);
		}

		Optional<UnitOfMeasureDTO> unitFromOpt = units.stream()
				.filter(u -> Objects.equals(u.getCatalogUnitId(), unitFromId)).findFirst();
		Optional<UnitOfMeasureDTO> unitToOpt = units.stream()
				.filter(u -> Objects.equals(u.getCatalogUnitId(), unitToId)).findFirst();

		if (unitFromOpt.isEmpty() || unitToOpt.isEmpty()) {
			log.info("Destination unit ID [{}] is not convertable", unitToId);
			throw new UnitConvertException(unitFromId, unitToId);
		}

		double valueInBaseUnit = sanitizedValue / unitFromOpt.get().getEquivalence();

		return unitToOpt.get().getEquivalence() * valueInBaseUnit;
	}

	public List<CatalogDTO> findConvertableUnitsFor(Long unitId) {
		return unitOfMeasureService.findConvertableCatalogUnitsFor(unitId);
	}

	public List<CatalogDTO> getConvertableUnits() {
		return unitOfMeasureService.getConvertableCatalogUnits();
	}
}
