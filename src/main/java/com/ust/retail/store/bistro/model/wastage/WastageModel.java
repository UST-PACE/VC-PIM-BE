package com.ust.retail.store.bistro.model.wastage;

import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bistro_kitchen_wastage")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class WastageModel extends Audits implements Serializable {

	private static final long serialVersionUID = 4436617601496670593L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wastage_id")
	private Long wastageId;

	@Column(name = "execution_date")
	@Temporal(TemporalType.DATE)
	private Date executionDate;

	@Column(name = "whole_dish")
	private boolean wholeDish;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe_id")
	private RecipeModel recipe;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wastage_reason_id")
	private CatalogModel wastageReason;

	@OneToMany(mappedBy = "wastage", cascade = CascadeType.ALL)
	private List<WastageDetailModel> details;

	public WastageModel(Long wastageId, Date executionDate, boolean wholeDish, Long recipeId, Long wastageReasonId, Long userId) {
		this.wastageId = wastageId;
		this.executionDate = executionDate;
		this.wholeDish = wholeDish;
		this.recipe = new RecipeModel(recipeId, userId);
		this.wastageReason = new CatalogModel(wastageReasonId);
		super.userCreate = new UserModel(userId);
	}

	public void addDetail(Long upcMasterId, Double wastedAmount, Long wastedUnitId) {
		if (details == null)
			details = new ArrayList<>();

		details.add(new WastageDetailModel(upcMasterId, wastedAmount, wastedUnitId, this));

	}
}
