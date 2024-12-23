package com.ust.retail.store.pim.model.tax;

import com.ust.retail.store.pim.dto.tax.TaxDTO;
import com.ust.retail.store.pim.model.catalog.*;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(
		name = "taxes",
		uniqueConstraints = {@UniqueConstraint(
				name = "tax_by_store_and_type",
				columnNames = {"store_num_id", "tax_type_id", "product_group_id", "product_category_id", "product_subcategory_id"})
		})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class TaxModel extends Audits {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tax_id")
	private Long taxId;

	@Column(name = "percentage", nullable = false)
	private Double percentage;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_num_id", referencedColumnName = "store_num_id", nullable = false)
	private StoreNumberModel storeNumber;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_type_id", referencedColumnName = "catalog_id", nullable = false)
	private CatalogModel taxType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_group_id", referencedColumnName = "product_group_id", nullable = false)
	private ProductGroupModel productGroup;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_category_id", referencedColumnName = "product_category_id")
	private ProductCategoryModel productCategory;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_subcategory_id", referencedColumnName = "product_subcategory_id")
	private ProductSubcategoryModel productSubcategory;

	public TaxModel(TaxDTO taxDTO, Long userId) {
		this.taxId = taxDTO.getTaxId();
		this.percentage = taxDTO.getPercentage();
		this.taxType = new CatalogModel(taxDTO.getTaxTypeId());
		this.userCreate = new UserModel(userId);
		this.storeNumber = new StoreNumberModel(taxDTO.getStoreNumId());
		this.productGroup = new ProductGroupModel(taxDTO.getProductGroupId());
		Optional.ofNullable(taxDTO.getProductCategoryId()).ifPresent(id -> this.productCategory = new ProductCategoryModel(id));
		Optional.ofNullable(taxDTO.getProductSubcategoryId()).ifPresent(id -> this.productSubcategory = new ProductSubcategoryModel(id));

	}
}
