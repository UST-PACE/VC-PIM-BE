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
@Table(name = "product_categories", uniqueConstraints = {@UniqueConstraint(columnNames = {"product_group_id", "product_category_name"})})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class ProductCategoryModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_category_id")
	private Long productCategoryId;

	@Column(name = "product_category_name", nullable = false, length = 75)
	private String productCategoryName;

	@Column(name = "picture_url")
	private String pictureUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_group_id", referencedColumnName = "product_group_id")
	private ProductGroupModel productGroup;

	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
	private List<ProductSubcategoryModel> productSubcategories;

	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
	private List<UpcMasterModel> products;

	public ProductCategoryModel(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public ProductCategoryModel(Long productCategoryId, Long productGroupId, String productCategoryName, Long userCreateId) {
		this.productCategoryId = productCategoryId;
		this.productCategoryName = productCategoryName;
		this.productGroup = new ProductGroupModel(productGroupId);
		this.userCreate = new UserModel(userCreateId);
	}

	public void updatePictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
}
