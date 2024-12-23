package com.ust.retail.store.pim.model.inventory;

import com.ust.retail.store.pim.model.catalog.StoreLocationModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "inventory",
uniqueConstraints = {
		@UniqueConstraint(columnNames = {"upc_master_id", "store_location_id"}),
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class InventoryModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_id")
	private Long inventoryId;

	@Column(name = "qty", nullable = false)
	private Double qty;

	@Column(name = "qty_transformation")
	private Double qtyTransformation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id", nullable = false)
	private UpcMasterModel upcMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_location_id", referencedColumnName = "store_location_id", nullable = false)
	private StoreLocationModel storeLocation;

	public InventoryModel(Long inventoryId, Double qty, Long upcMasterId, Long storeLocationId,Long currentUserId) {
		super();
		this.inventoryId = inventoryId;
		this.qty = qty;
		this.upcMaster = new UpcMasterModel(upcMasterId);
		this.storeLocation = new StoreLocationModel(storeLocationId);
		super.userCreate = new UserModel(currentUserId);
	}

	public InventoryModel(Long inventoryId) {
		super();
		this.inventoryId = inventoryId;
	}

	public void setQty(Double qty) {
		this.qty = qty;

	}

	public void setQtyTransformation(Double qtyTransformation) {
		this.qtyTransformation = qtyTransformation;
	}
	
	

}
