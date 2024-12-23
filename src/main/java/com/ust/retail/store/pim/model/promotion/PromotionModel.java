package com.ust.retail.store.pim.model.promotion;

import com.ust.retail.store.pim.dto.promotion.PromotionDTO;
import com.ust.retail.store.pim.model.catalog.*;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(
		name = "promotions",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"vendor_master_id", "product_group_id", "product_category_id", "product_subcategory_id", "product_item_id", "upc_master_id", "start_date", "end_date"})
		})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class PromotionModel extends Audits implements Serializable {

	private static final long serialVersionUID = -5373296679368910942L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "promotion_id")
	private Long promotionId;

	@Column(name = "start_date", nullable = false)
	private Date startDate;

	@Column(name = "end_date", nullable = false)
	private Date endDate;

	@Column(name = "discount", nullable = false)
	private Double discount;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_owner_id", referencedColumnName = "brand_owner_id", nullable = false)
	private BrandOwnerModel brandOwner;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_master_id", referencedColumnName = "vendor_master_id", nullable = false)
	private VendorMasterModel vendorMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_group_id", referencedColumnName = "product_group_id", nullable = false)
	private ProductGroupModel productGroup;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_category_id", referencedColumnName = "product_category_id")
	private ProductCategoryModel productCategory;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_subcategory_id", referencedColumnName = "product_subcategory_id")
	private ProductSubcategoryModel productSubcategory;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_item_id", referencedColumnName = "product_item_id")
	private ProductItemModel productItem;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id")
	private UpcMasterModel upcMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "promotion_type_id", referencedColumnName = "catalog_id", nullable = false)
	private CatalogModel promotionType;

	public PromotionModel(PromotionDTO promotionDTO, Long userId) {
		this.promotionId = promotionDTO.getPromotionId();
		this.brandOwner = new BrandOwnerModel(promotionDTO.getBrandOwnerId());
		this.vendorMaster = new VendorMasterModel(promotionDTO.getVendorMasterId());
		this.productGroup = new ProductGroupModel(promotionDTO.getProductGroupId());
		this.promotionType = new CatalogModel(promotionDTO.getPromotionTypeId());
		this.userCreate = new UserModel(userId);
		Optional.ofNullable(promotionDTO.getProductCategoryId()).ifPresent(id -> this.productCategory = new ProductCategoryModel(id));
		Optional.ofNullable(promotionDTO.getProductSubcategoryId()).ifPresent(id -> this.productSubcategory = new ProductSubcategoryModel(id));
		Optional.ofNullable(promotionDTO.getProductItemId()).ifPresent(id -> this.productItem = new ProductItemModel(id));
		Optional.ofNullable(promotionDTO.getUpcMasterId()).ifPresent(id -> this.upcMaster = new UpcMasterModel(id));
		this.startDate = promotionDTO.getStartDate();
		this.endDate = promotionDTO.getEndDate();
		this.discount = promotionDTO.getDiscount();

	}
}
