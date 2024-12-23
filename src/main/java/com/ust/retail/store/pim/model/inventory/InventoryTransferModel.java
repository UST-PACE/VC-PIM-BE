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
@Table(name = "inventory_transfer")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class InventoryTransferModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_trasfer_id")
	private Long inventoryTransferId;

	@Column(name = "qty", nullable = false)
	private Double qty;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id", nullable = false)
	private UpcMasterModel upcMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_location_id", referencedColumnName = "store_location_id", nullable = false)
	private StoreLocationModel storeLocationFrom;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_location_id", referencedColumnName = "store_location_id", nullable = false)
	private StoreLocationModel storeLocationTo;

	public InventoryTransferModel(Long inventoryTransferId, Double qty, Long upcMasterId, Long storeLocationFromId,Long storeLocationToId,Long currentUserId) {
		super();
		this.inventoryTransferId = inventoryTransferId;
		this.qty = qty;
		this.upcMaster = new UpcMasterModel(upcMasterId);
		this.storeLocationTo = new StoreLocationModel(storeLocationToId);
		this.storeLocationFrom = new StoreLocationModel(storeLocationFromId);
		super.userCreate = new UserModel(currentUserId);
	}

	public InventoryTransferModel(Long inventoryTransferId) {
		super();
		this.inventoryTransferId = inventoryTransferId;
	}


}
