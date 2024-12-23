package com.ust.retail.store.bistro.model.wastage;

import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "bistro_kitchen_wastage_detail")
@Entity
@Getter
@NoArgsConstructor
public class WastageDetailModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wastage_detail_id")
	private Long wastageDetailId;

	@Column(name = "wasted_amount")
	private Double wastedAmount;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wasted_unit_id")
	private CatalogModel wastedUnit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ingredient_upc_master_id")
	private UpcMasterModel ingredient;

	@ManyToOne
	@JoinColumn(name = "wastage_id")
	private WastageModel wastage;

	public WastageDetailModel(Long ingredientUpcMasterId, Double wastedAmount, Long wastedUnitId, WastageModel wastage) {
		this.ingredient = new UpcMasterModel(ingredientUpcMasterId);
		this.wastedAmount = wastedAmount;
		this.wastedUnit = new CatalogModel(wastedUnitId);
		this.wastage = wastage;
	}
}
