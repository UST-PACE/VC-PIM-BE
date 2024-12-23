package com.ust.retail.store.pim.model.upcmaster;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ust.retail.store.pim.dto.upcmaster.ComboDTO;
import com.ust.retail.store.pim.dto.upcmaster.ComboProductCategoryDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "combo", uniqueConstraints = { @UniqueConstraint(columnNames = { "combo_id", "combo_name" })})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class ComboModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "combo_id")
	private Long comboId;

	@Column(name = "combo_name", nullable = false, length = 50)
	private String comboName;

	@Column(name = "price")
	private double price;

	@ManyToOne
	@JoinColumn(name = "combo_status_id", referencedColumnName = "catalog_id")
	private CatalogModel comboStatus;

	@Column(name = "principal_upc")
	private String principalUpc;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "tax_percentage_active")
	private Boolean taxPercentageActive;

	@OneToMany(mappedBy = "combo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ComboProductCategoryModel> comboCategories = new LinkedHashSet<>();
	
	@Column(name = "deleted")
	private boolean deleted;

	public ComboModel(ComboDTO dto) {
		super.userCreate = new UserModel(dto.getCreatedUserId());
		this.comboId = dto.getComboId();
		this.comboName = dto.getComboName();
		this.price = dto.getPrice();
		this.comboStatus = new CatalogModel(dto.getComboStatusId());
		this.principalUpc = dto.getPrincipalUPC();
		this.taxPercentage = dto.getTaxPercentage();
		this.taxPercentageActive = dto.getTaxPercentageActive();
		this.comboCategories = addProductCategories(dto.getComboCategories());
	}

	private Set<ComboProductCategoryModel> addProductCategories(Set<ComboProductCategoryDTO> comboProductCategoryDTOs) {
		comboCategories.clear();
		return comboProductCategoryDTOs.stream().map(i -> new ComboProductCategoryModel(i, this))
				.collect(Collectors.toSet());
	}
	
	public Map<Long, Integer> getCategoryIdAndQuantity(){
		return comboCategories.stream().collect(Collectors.toMap(ComboProductCategoryModel::getProductCategoryId, ComboProductCategoryModel::getQuantity));
	}

}
