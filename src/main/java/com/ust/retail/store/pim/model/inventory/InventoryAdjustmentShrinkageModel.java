package com.ust.retail.store.pim.model.inventory;

import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "inventory_adjustments_shrinkage")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class InventoryAdjustmentShrinkageModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_adjustment_shrinkage_id")
	private Long inventoryAdjustmentShrinkageId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_adjustment_id", referencedColumnName = "inventory_adjustment_id", nullable = false)
	private InventoryAdjustmentModel inventoryAdjustment;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id", nullable = false)
	private UpcMasterModel upcMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shrinkage_reason_id", referencedColumnName = "catalog_id", nullable = false)
	private CatalogModel shrinkageReason;

	@Column(name = "qty" )
	private Double qty;
	
	//TODO poner el correcto
	@Lob
	@Column(name ="evidence")
	private String evidence;
	
	
	public InventoryAdjustmentShrinkageModel(InventoryAdjustmentModel inventoryAdjustment, Long upcMasterId,
			Long shrinkageReasonId, double qty, String evidence,Long userCreatedId) {

		this.inventoryAdjustment = inventoryAdjustment;
		this.upcMaster =new UpcMasterModel(upcMasterId);
		this.shrinkageReason = new CatalogModel(shrinkageReasonId);
		this.qty = qty;
		this.evidence = evidence;
		super.userCreate = new UserModel(userCreatedId);
	}
	
}
