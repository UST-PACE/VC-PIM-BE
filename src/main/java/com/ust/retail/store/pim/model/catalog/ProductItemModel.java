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
@Table(name = "product_items", uniqueConstraints = {@UniqueConstraint(columnNames = {"product_subcategory_id", "product_item_name"})})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class ProductItemModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_item_id")
	private Long productItemId;

	@Column(name = "product_item_name", nullable = false, length = 75)
	private String productItemName;

	@Column(name = "picture_url")
	private String pictureUrl;

	@OneToMany(mappedBy = "productItem", fetch = FetchType.LAZY)
	private List<UpcMasterModel> products;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_subcategory_id", referencedColumnName = "product_subcategory_id")
	private ProductSubcategoryModel productSubcategory;

	public ProductItemModel(Long productItemId) {
		this.productItemId = productItemId;
	}

	public ProductItemModel(Long productItemId, Long productSubcategoryId, String productItemName, Long userCreateId) {
		this.productItemId = productItemId;
		this.productItemName = productItemName;
		this.productSubcategory = new ProductSubcategoryModel(productSubcategoryId);
		this.userCreate = new UserModel(userCreateId);
	}

	public void updatePictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
}
