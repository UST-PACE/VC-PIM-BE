package com.ust.retail.store.pim.model.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "product_groups")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class ProductGroupModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_group_id")
	private Long productGroupId;

	@Column(name = "product_group_name", nullable = false, unique = true, length = 75)
	private String productGroupName;

	@Column(name = "picture_url")
	private String pictureUrl;

	@JsonIgnore
	@Column(name = "display_externally", columnDefinition = "boolean default true")
	private boolean displayExternally;

	@OneToMany(mappedBy = "productGroup", fetch = FetchType.LAZY)
	private List<ProductCategoryModel> productCategories;

	@OneToMany(mappedBy = "productGroup", fetch = FetchType.LAZY)
	private List<UpcMasterModel> products;

	public ProductGroupModel(Long productGroupId) {
		this.productGroupId = productGroupId;
	}

	public ProductGroupModel(Long productGroupId, String productGroupName, boolean displayExternally, Long userCreateId) {
		this.productGroupId = productGroupId;
		this.productGroupName = productGroupName;
		this.displayExternally = displayExternally;
		this.userCreate = new UserModel(userCreateId);
	}

	public void updatePictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
}
