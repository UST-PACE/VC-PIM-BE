package com.ust.retail.store.pim.model.inventory;

import com.ust.retail.store.pim.common.catalogs.DailyCountStatusCatalog;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Inventory_adjustments_category")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class InventoryAdjustmentCategoryModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_category_id")
	private Long inventoryCategoryId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_adjustment_id", referencedColumnName = "inventory_adjustment_id", nullable = false)
	private InventoryAdjustmentModel inventoryAdjustment;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_category_id", referencedColumnName = "product_category_id", nullable = false)
	private ProductCategoryModel productCategory;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "daily_count_status_id", referencedColumnName = "catalog_id", nullable = false)
	private CatalogModel dailyCountStatus;
	
	public InventoryAdjustmentCategoryModel(InventoryAdjustmentModel inventoryAdjustment,
			Long productCategoryId, Long dailyCountStatusId) {
		super();
		this.inventoryAdjustment = inventoryAdjustment;
		this.productCategory = new ProductCategoryModel(productCategoryId);
		this.dailyCountStatus = new CatalogModel(dailyCountStatusId);
	}

	public void interruptCount() {
		dailyCountStatus = new CatalogModel(DailyCountStatusCatalog.DAILY_COUNT_STATUS_INTERRUPTED);
	}
	
	public void finishCount() {
		dailyCountStatus = new CatalogModel(DailyCountStatusCatalog.DAILY_COUNT_STATUS_COMPLETE);
	}
	
	
}
