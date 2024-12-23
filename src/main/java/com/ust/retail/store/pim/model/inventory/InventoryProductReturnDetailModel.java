package com.ust.retail.store.pim.model.inventory;

import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "inventory_product_returns_detail")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class InventoryProductReturnDetailModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_product_return_detail_id")
	private Long inventoryProductReturnDetailId;

	@Column(name = "qty")
	private Double qty;
	
	@Column(name = "credit")
	private Double credit;
	
	@Column(name = "batch_number")
	private String batchNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id", nullable = false)
	private UpcMasterModel upcMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "return_reason_id", referencedColumnName = "catalog_id", nullable = false)
	private CatalogModel returnReason;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_location_id", referencedColumnName = "store_location_id", nullable = false)
	private StoreLocationModel storeLocation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_master_id", referencedColumnName = "vendor_master_id")
	private VendorMasterModel vendorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_product_return_id", referencedColumnName = "inventory_product_return_id", nullable = false)
	private InventoryProductReturnModel productReturn;

	public InventoryProductReturnDetailModel(Long upcMasterId,
											 Long storeLocationId,
											 Double qty,
											 String batchNumber,
											 Long returnReasonId,
											 InventoryProductReturnModel inventoryProductReturnModel) {

		this.storeLocation = new StoreLocationModel(storeLocationId);
		this.qty = qty;
		this.upcMaster = new UpcMasterModel(upcMasterId);
		this.batchNumber = batchNumber;
		this.productReturn = inventoryProductReturnModel ;
		this.returnReason = new CatalogModel(returnReasonId);

	}

	public void setCredit(Double credit,Long vendorMasterId) {
		this.credit = credit;
		this.vendorMaster = new VendorMasterModel(vendorMasterId);
		
	}

	public void updateInformation(Double qty, String batchNum, Long returnReasonId) {
		this.qty = qty;
		this.batchNumber = batchNum;
		this.returnReason = new CatalogModel(returnReasonId);
	}
}
