package com.ust.retail.store.pim.model.inventory;

import com.ust.retail.store.pim.common.catalogs.InventoryAdjustmentStatusCatalog;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "inventory_adjustments")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class InventoryAdjustmentModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_adjustment_id")
	private Long inventoryAdjustmentId;

	@Column(name="start_time",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name="end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_location_id", referencedColumnName = "store_location_id", nullable = false)
	private StoreLocationModel storeLocation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_adjustment_status_id", referencedColumnName = "catalog_id")
	private CatalogModel status;

	@OneToMany(mappedBy = "inventoryAdjustment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InventoryAdjustmentCategoryModel> categories;
	
	@OneToMany(mappedBy = "inventoryAdjustment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InventoryAdjustmentShrinkageModel> shrinkages;
	
	@OneToMany(mappedBy = "inventoryAdjustment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InventoryAdjustmentDetailModel> details;
	
	public InventoryAdjustmentModel(Long storeLocationId,Long userCreatedId) {
		super();
		super.userCreate = new UserModel(userCreatedId);
		this.startTime = new Date();
		this.storeLocation = new StoreLocationModel(storeLocationId);
		this.status = new CatalogModel(InventoryAdjustmentStatusCatalog.INVENTORY_ADJUSTMENT_STATUS_PENDING_REVIEW);
	}
	
	public InventoryAdjustmentModel(Long inventoryAdjustmentId) {
		super();
		this.inventoryAdjustmentId = inventoryAdjustmentId;
	}

	
	public void addSubcategory(Long productSubcategoryId,Long dailyCountStatusId) {
		if(categories==null) categories = new ArrayList<>();
		
		categories.add(new InventoryAdjustmentCategoryModel(this, productSubcategoryId, dailyCountStatusId));
	}

	public void addShrinkage(Long upcMasterId, Long shrinkageReasonId, double qty, String evidence, Long userCreatedId) {
		if (getShrinkages() == null) shrinkages = new ArrayList<>();

		shrinkages.add(new InventoryAdjustmentShrinkageModel(this,
				upcMasterId,
				shrinkageReasonId,
				qty,
				evidence,
				userCreatedId));
	}

	public void removeUpcShrinkages(Long upcMasterId) {
		shrinkages.removeIf(shrinkage -> Objects.equals(shrinkage.getUpcMaster().getUpcMasterId(), upcMasterId));
	}

	public void addDetail(Long inventoryAdjustmentDetailId, Long upcMasterId, Double qty, Long userCreatedId) {

		if (getDetails() == null) details = new ArrayList<>();

		if (inventoryAdjustmentDetailId != null) {
			details.stream()
					.filter(detail -> Objects.equals(detail.getInventoryAdjustmentDetailId(), inventoryAdjustmentDetailId))
					.findFirst()
					.ifPresent(detail -> detail.updateInformation(qty, userCreatedId));
		} else {
			details.add(new InventoryAdjustmentDetailModel(this, inventoryAdjustmentDetailId, upcMasterId, qty, userCreatedId));
		}
	}


	public void finishAdjustment() {
		this.endTime = new Date();
		this.status = new CatalogModel(InventoryAdjustmentStatusCatalog.INVENTORY_ADJUSTMENT_STATUS_PENDING_REVIEW);
	}

	public void setStatus(Long statusId) {
		this.status = new CatalogModel(statusId);
	}
}
