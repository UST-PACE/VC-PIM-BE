package com.ust.retail.store.pim.model.upcmaster;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ust.retail.store.pim.dto.upcmaster.ComboProductCategoryDTO;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "combo_product_category")
@Getter
@NoArgsConstructor
public class ComboProductCategoryModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "combo_product_category_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "combo_id")
	private ComboModel combo;

	@ManyToOne
	@JoinColumn(name = "product_category_id")
	private ProductCategoryModel productCategory;

	@Column(name = "quantity")
	private int quantity;

	public ComboProductCategoryModel(ComboProductCategoryDTO dto,ComboModel comboModel) {
		this.productCategory = new ProductCategoryModel(dto.getProductCategoryId());
		this.quantity = dto.getQuantity();
		this.combo = comboModel;
	}
	
	public Long getProductCategoryId() {
		return this.productCategory.getProductCategoryId();
	}
	
}
