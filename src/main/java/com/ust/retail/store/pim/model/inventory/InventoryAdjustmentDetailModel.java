package com.ust.retail.store.pim.model.inventory;

import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "inventory_adjustments_details")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class InventoryAdjustmentDetailModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_adjustment__detail_id")
	private Long inventoryAdjustmentDetailId;

	@Column(name = "set_qty")
	private Double setQty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_adjustment_id", referencedColumnName = "inventory_adjustment_id", nullable = false)
	private InventoryAdjustmentModel inventoryAdjustment;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id", nullable = false)
	private UpcMasterModel upcMaster;

	public InventoryAdjustmentDetailModel(InventoryAdjustmentModel inventoryAdjustment,
										  Long inventoryAdjustmentDetailId,
										  Long upcMasterId,
										  Double setQty,
										  Long userCreatedId) {
		super();

		this.inventoryAdjustmentDetailId = inventoryAdjustmentDetailId;
		this.setQty = setQty;
		this.inventoryAdjustment = inventoryAdjustment;
		this.upcMaster = new UpcMasterModel(upcMasterId);
		super.userCreate = new UserModel(userCreatedId);
	}

	public void updateInformation(Double setQty, Long userCreatedId) {
		this.setQty = setQty;
		this.userCreate = new UserModel(userCreatedId);
	}
}
