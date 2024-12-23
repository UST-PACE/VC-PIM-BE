package com.ust.retail.store.pim.model.inventory;

import com.ust.retail.store.pim.engine.inventory.InventoryEngine;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "inventory_history")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class InventoryHistoryModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_history_id")
	private Long inventoryHistoryId;

	@Column(name = "previews_qty",nullable = false)
	private Double previewsQty;
	
	@Column(name = "operation_qty", nullable = false)
	private Double operationQty;
	
	@Column(name = "final_qty", nullable = false)
	private Double finalQty;
	
	@Column(name = "reference_id", nullable = false)
	private Long referenceId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operation_module_id", referencedColumnName = "catalog_id",nullable = false)
	private CatalogModel operationModule;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opertation_type_id", referencedColumnName = "catalog_id",nullable = false)
	private CatalogModel operationType;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opertation_result_id", referencedColumnName = "catalog_id",nullable = false)
	private CatalogModel operationResult;
	
	@Column(name = "txn_num", nullable = false)
	private Long txnNum;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "authorization_status_id", referencedColumnName = "catalog_id",nullable = false)
	private CatalogModel authorizationStatus;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_id", referencedColumnName = "inventory_id",nullable = false)
	private InventoryModel inventory;

	public InventoryHistoryModel( Double previewsQty, Double operationQty,
			Double finalQty, Long referenceId, Long operationTypeId, Long operationResultId, 
			Long operationModuleId, Long inventoryId, Long authorizationStatusId,Long txnNum, Long userCreatedId) {
		super();
		this.previewsQty = previewsQty;
		this.operationQty = operationQty;
		this.finalQty = finalQty;
		this.referenceId = referenceId;
		this.operationType = new CatalogModel(operationTypeId);
		this.operationResult= new CatalogModel(operationResultId);
		this.operationModule = new CatalogModel(operationModuleId);
		this.inventory = new InventoryModel(inventoryId);
		this.authorizationStatus= new CatalogModel(authorizationStatusId);
		this.txnNum = txnNum;
		this.userCreate = new UserModel(userCreatedId);
	}
	
	public void authorize() {
		this.authorizationStatus = new CatalogModel(InventoryEngine.AUTHORIZATION_STATUS_AUTHORIZED);
	}

	public void reject() {
		this.authorizationStatus = new CatalogModel(InventoryEngine.AUTHORIZATION_STATUS_REJECTED);;
	}
	
}
