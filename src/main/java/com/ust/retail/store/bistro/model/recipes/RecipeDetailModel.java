package com.ust.retail.store.bistro.model.recipes;

import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "bistro_recipe_details", uniqueConstraints = {@UniqueConstraint(columnNames = { "recipe_id", "upc_master_id" }) })
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class RecipeDetailModel extends Audits implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_detail_id")
	private Long recipeDetailId;

	@Column(name = "qty", nullable = false)
	private Double qty; 

	@Column(name = "is_topping", nullable = false)
	private boolean topping;

	@Column(name = "to_exclude", nullable = false)
	private boolean toExclude;

	@Column(name = "exclude_name")
	private String excludeName;

	@ManyToOne
	@JoinColumn(name = "recipe_id")
	private RecipeModel recipe;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id")
	private UpcMasterModel upcMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ingredient_type_id", referencedColumnName = "catalog_id")
	private CatalogModel ingredientType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", referencedColumnName = "catalog_id")
	private CatalogModel unit;

	@OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RecipeSubstitutionModel> substitutions;

	public RecipeDetailModel(Long userId) {
		super.userCreate = new UserModel(userId);
	}

	public RecipeDetailModel(RecipeModel recipe,
							 Long recipeDetailId,
							 Double qty,
							 Long unitId,
							 Long ingredientTypeId,
							 boolean topping,
							 boolean toExclude,
							 String excludeName,
							 Long upcMasterId,
							 Long userCreatedId) {
		this.recipeDetailId = recipeDetailId;
		this.qty = qty;
		this.unit = new CatalogModel(unitId);
		this.ingredientType = new CatalogModel(ingredientTypeId);
		this.recipe = recipe;
		this.topping = topping;
		this.toExclude = toExclude;
		this.excludeName = excludeName;
		this.upcMaster = new UpcMasterModel(upcMasterId);
		super.userCreate = new UserModel(userCreatedId);
	}

	public void updateQty(Double qty) {
		this.qty = qty;
	}

	public void updateUnit(Long unitId) {
		this.unit = new CatalogModel(unitId);
	}

	public void updateIngredientType(Long ingredientTypeId) {
		this.ingredientType = new CatalogModel(ingredientTypeId);
	}

	public void setExclusionData(boolean toExclude, String excludeName) {
		this.toExclude = toExclude;
		this.excludeName = toExclude ? excludeName : null;
	}

}
