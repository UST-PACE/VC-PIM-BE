package com.ust.retail.store.pim.model.purchaseorder;

import com.ust.retail.store.pim.common.catalogs.ItemStatusCatalog;
import com.ust.retail.store.pim.dto.promotion.PromotionDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderAddProductRequestDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "purchase_order_detail", uniqueConstraints = @UniqueConstraint(columnNames = {"purchase_order_id", "upc_master_id"}))
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class PurchaseOrderDetailModel extends Audits implements Serializable {

	private static final long serialVersionUID = 4994116044270663392L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchase_order_detail_id")
	private Long purchaseOrderDetailId;

	@Column(name = "product_cost")
	private Double productCost;

	@Column(name = "vendor_sku")
	private String vendorSku;

	@Column(name = "case_num")
	private Integer caseNum;

	@Column(name = "units_per_case")
	private Integer unitsPerCase;

	@Column(name = "pallet_num")
	private Integer palletNum;

	@Column(name = "units_per_pallet")
	private Integer unitsPerPallet;

	@Column(name = "total_amount")
	private Integer totalAmount;

	@Column(name = "original_cost")
	private Double originalCost;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "final_cost")
	private Double finalCost;

	@Column(name = "product_moq")
	private Double productMoq;

	@Column(name = "line_item_under_moq")
	private boolean lineItemUnderMoq;

	@Column(name = "promotion_qty")
	private Double promotionQty;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id")
	private UpcMasterModel upcMaster;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "purchase_order_id", referencedColumnName = "purchase_order_id")
	@Setter(AccessLevel.PACKAGE)
	private PurchaseOrderModel purchaseOrder;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_status_id", referencedColumnName = "catalog_id")
	private CatalogModel itemStatus;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "promotion_type_id", referencedColumnName = "catalog_id")
	private CatalogModel promotionType;

	public PurchaseOrderDetailModel(Long purchaseOrderDetailId) {
		this.purchaseOrderDetailId = purchaseOrderDetailId;
	}

	public PurchaseOrderDetailModel(UpcMasterModel upcMaster, UpcVendorDetailsModel upcVendorDetail, PurchaseOrderAddProductRequestDTO dto, Long userId) {
		this.purchaseOrderDetailId = null;
		this.upcMaster = upcMaster;
		this.productCost = upcVendorDetail.getProductCost(dto.getStoreNumId());
		this.vendorSku = upcVendorDetail.getCaseUpc();
		this.caseNum = dto.getCaseNum();
		this.unitsPerCase = upcVendorDetail.getUnitsPerCase();
		this.palletNum = dto.getPalletNum();
		this.unitsPerPallet = upcVendorDetail.getUnitsPerCase() * upcVendorDetail.getCasesPerPallet();
		this.totalAmount = dto.getTotalAmount();
		this.originalCost = totalAmount * productCost;
		this.discount = 0d;
		this.finalCost = originalCost - discount;
		this.productMoq = upcVendorDetail.getMoq();
		this.lineItemUnderMoq = this.totalAmount < this.productMoq;
		this.itemStatus = new CatalogModel(ItemStatusCatalog.ITEM_STATUS_PENDING);
		this.userCreate = new UserModel(userId);
	}

	public void setDiscountInformation(Double discount, PromotionDTO promotionType) {
		this.discount = discount;
		this.finalCost -= discount;
		this.promotionType = new CatalogModel(promotionType.getPromotionTypeId());
		this.promotionQty = promotionType.getDiscount();
	}

	public void changeStatusReceived() {
		this.itemStatus = new CatalogModel(ItemStatusCatalog.ITEM_STATUS_RECEIVED);
	}

	public void updateCalculations(Integer caseNum, Integer palletNum, Integer totalAmount, Double originalCost, Double finalCost) {
		this.caseNum = caseNum;
		this.palletNum = palletNum;
		this.totalAmount = totalAmount;
		this.originalCost = originalCost;
		this.finalCost = finalCost;
		this.lineItemUnderMoq = this.totalAmount < this.productMoq;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		if (!(o instanceof PurchaseOrderDetailModel)) return false;
		PurchaseOrderDetailModel that = (PurchaseOrderDetailModel) o;
		return Objects.equals(purchaseOrderDetailId, that.purchaseOrderDetailId);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
