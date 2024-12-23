package com.ust.retail.store.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.common.model.UnitOfMeasureModel;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UnitOfMeasureDTO {
	@Null(message = "Unit ID should not be present when creating.", groups = { OnCreate.class })
	@NotNull(message = "Unit ID is mandatory when updating.", groups = { OnUpdate.class })
	private Long unitId;

	@Null(message = "Catalog Unit ID should not be present when creating.", groups = { OnCreate.class })
	@NotNull(message = "Catalog Unit ID is mandatory when updating.", groups = { OnUpdate.class })
	private Long catalogUnitId;

	@NotNull(message = "Unit Name is mandatory.", groups = { OnCreate.class,OnUpdate.class })
	private String unitName;

	@NotNull(message = "Unit Type is mandatory.", groups = { OnCreate.class,OnUpdate.class })
	private Long unitTypeId;

	private String unitTypeName;

	private boolean baseUnit;

	@NotNull(message = "Equivalence is mandatory.", groups = { OnCreate.class,OnUpdate.class })
	private Double equivalence;


	public UnitOfMeasureDTO parseToDTO(UnitOfMeasureModel model) {
		this.unitId = model.getUnitId();
		this.catalogUnitId = model.getUnit().getCatalogId();
		this.unitName = model.getUnit().getCatalogOptions();
		this.unitTypeId = model.getUnitType().getCatalogId();
		this.unitTypeName = model.getUnitType().getCatalogOptions();
		this.baseUnit = model.isBaseUnit();
		this.equivalence = model.getEquivalence();

		return this;
	}

	public UnitOfMeasureDTO parseToSimpleDTO(UnitOfMeasureModel model) {
		this.unitId = model.getUnitId();
		this.unitName = model.getUnit().getCatalogOptions();

		return this;
	}

	public UnitOfMeasureModel createModel() {
		return new UnitOfMeasureModel(unitId, unitTypeId, unitName, equivalence, baseUnit);
	}
}
