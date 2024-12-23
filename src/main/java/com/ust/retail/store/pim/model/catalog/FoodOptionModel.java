package com.ust.retail.store.pim.model.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "food_option")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class FoodOptionModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "food_option_id")
	private Long foodOptionId;

	@Column(name = "food_option_catalogue_name", nullable = false, length = 200)
	private String foodOptionCatalogueName;

	@OneToMany(mappedBy = "foodOption", fetch = FetchType.LAZY)
	private List<UpcMasterModel> foodOptionCatalogueUpcs = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_option_status_id", referencedColumnName = "catalog_id")
	private CatalogModel foodOptionStatus;
	
	@Column(name = "deleted")
	private boolean deleted;

	public FoodOptionModel(String catalogueName, Long foodOptionStatusId,Long userId, Long foodOptionId) {
		this.foodOptionId = foodOptionId;
		this.foodOptionCatalogueName = catalogueName;
		this.userCreate = new UserModel(userId);
		this.foodOptionStatus = new CatalogModel(foodOptionStatusId);
	}

	public void setCatalogueUpcs(List<UpcMasterModel> foodOptionCatalogueUpcs) {
		if (!foodOptionCatalogueUpcs.isEmpty())
			foodOptionCatalogueUpcs.forEach(p -> p.setFoodOption(this));
		this.foodOptionCatalogueUpcs = foodOptionCatalogueUpcs;
	}
	
	public void setDeleted(boolean value) {
		this.deleted = value;
	}
}
