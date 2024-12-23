package com.ust.retail.store.bistro.model.recipes;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ust.retail.store.bistro.dto.recipes.SubstitutionDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bistro_recipe_substitutions", uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_detail_id", "substitute_upc_master_id"})})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class RecipeSubstitutionModel extends Audits implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_substitution_id")
	private Long recipeSubstitutionId;

	@Column(name = "display_name")
	@Setter
	private String displayName;

	@Column(name = "qty", nullable = false)
	@Setter
	private Double qty;

	@Column(name = "selling_price")
	@Setter
	private Double sellingPrice;

	@ManyToOne
	@JoinColumn(name = "recipe_detail_id")
	private RecipeDetailModel ingredient;

	@ManyToOne
	@JoinColumn(name = "substitution_unit_id")
	private CatalogModel unit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "substitute_upc_master_id", referencedColumnName = "upc_master_id")
	private UpcMasterModel substituteUpcMaster;

	public RecipeSubstitutionModel(Long userId) {
		super.userCreate = new UserModel(userId);
	}

	public RecipeSubstitutionModel(RecipeDetailModel ingredient,
								   Long recipeSubstitutionId,
								   Long substituteUpcMasterId,
								   String displayName,
								   Double qty,
								   Long unitId,
								   Double sellingPrice,
								   Long userCreated) {
		this.recipeSubstitutionId = recipeSubstitutionId;
		this.ingredient = ingredient;
		this.substituteUpcMaster = new UpcMasterModel(substituteUpcMasterId);
		this.displayName = displayName;
		this.qty = qty;
		this.unit = new CatalogModel(unitId);
		this.sellingPrice = sellingPrice;
		super.userCreate = new UserModel(userCreated);
	}

	public void updateWith(SubstitutionDTO dto) {
		this.qty = dto.getQty();
		this.sellingPrice = dto.getSellingPrice();
		this.displayName = dto.getSubstitutionDisplayName();
		this.unit = new CatalogModel(dto.getSubstitutionUnitId());
	}
}
