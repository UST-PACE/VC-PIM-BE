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

import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bistro_recipe_addons", uniqueConstraints = {@UniqueConstraint(columnNames = { "recipe_id", "upc_master_id" }) })
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class RecipeAddonModel extends Audits implements Serializable {

	private static final long serialVersionUID = -7626991907599406481L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_addon_id")
	private Long recipeAddonId;

	@Setter
	@Column(name = "qty", nullable = false)
	private Double qty;

	@Setter
	@Column(name = "selling_price")
	private Double sellingPrice;

	@Setter
	@Column(name = "max_requests")
	private Integer maxRequests;

	@ManyToOne
	@JoinColumn(name = "recipe_id")
	private RecipeModel recipe;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id")
	private UpcMasterModel upcMaster;
	
	public RecipeAddonModel(Long userId) {
		super.userCreate = new UserModel(userId);
	}

	public RecipeAddonModel(RecipeModel recipe,
							Long recipeAddonId,
							Long upcMasterId,
							Double qty,
							Double sellingPrice,
							Integer maxRequests,
							Long userCreated) {
		this.recipeAddonId = recipeAddonId;
		this.qty = qty;
		this.recipe = recipe;
		this.upcMaster = new UpcMasterModel(upcMasterId);
		this.sellingPrice = sellingPrice;
		this.maxRequests = maxRequests;
		super.userCreate = new UserModel(userCreated);
	}
}
