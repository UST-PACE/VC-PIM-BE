package com.ust.retail.store.common.model;

import com.ust.retail.store.pim.common.catalogs.ProductUnitCatalog;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "units_of_measure")
@Getter
@NoArgsConstructor
public class UnitOfMeasureModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "unit_id")
	private Long unitId;
	private Double equivalence;
	private boolean baseUnit;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "catalog_unit_id")
	private CatalogModel unit;

	@OneToOne
	@JoinColumn(name = "unit_type_id")
	private CatalogModel unitType;

	public UnitOfMeasureModel(Long unitId, Long unitTypeId, String unitName, Double equivalence, boolean baseUnit) {
		this.unitId = unitId;
		this.unitType = new CatalogModel(unitTypeId);
		this.equivalence = equivalence;
		this.baseUnit = baseUnit;

		this.unit = new CatalogModel(unitId);
		this.unit.setValues(ProductUnitCatalog.CATALOG_NAME, unitName);
	}
}
