package com.ust.retail.store.pim.model.catalog;

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
@Table(name = "product_subcategories", uniqueConstraints = {@UniqueConstraint(columnNames = {"product_category_id", "product_subcategory_name"})})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class ProductSubcategoryModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_subcategory_id")
	private Long productSubcategoryId;

	@Column(name = "product_subcategory_name", nullable = false, length = 75)
	private String productSubcategoryName;

	@Column(name = "picture_url")
	private String pictureUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_category_id", referencedColumnName = "product_category_id")
	private ProductCategoryModel productCategory;

	@OneToMany(mappedBy = "productSubcategory", fetch = FetchType.LAZY)
	private List<ProductItemModel> productItems;

	@OneToMany(mappedBy = "productSubcategory", fetch = FetchType.LAZY)
	private List<UpcMasterModel> products;

	public ProductSubcategoryModel(Long productSubcategoryId) {
		this.productSubcategoryId = productSubcategoryId;
	}

	public ProductSubcategoryModel(Long productSubcategoryId, Long productCategoryId, String productSubcategoryName, Long userCreateId) {
		this.productSubcategoryId = productSubcategoryId;
		this.productSubcategoryName = productSubcategoryName;
		this.productCategory = new ProductCategoryModel(productCategoryId);
		this.userCreate = new UserModel(userCreateId);
	}

	public void updatePictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
}
