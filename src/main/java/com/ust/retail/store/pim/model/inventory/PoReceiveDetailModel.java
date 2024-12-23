package com.ust.retail.store.pim.model.inventory;

import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceivingRequestUpdateDTO;
import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceptionWarningDTO;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "po_receiving_details")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class PoReceiveDetailModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "po_receive_detail_id")
	private Long poReceiveDetailId;

	@Column(name = "received_qty", nullable = false)
	private Double receivedQty;

	@Column(name = "batch_number")
	private String batchNumber;

	@Column(name = "is_error")
	private boolean error;

	@Column(name = "expiration_date")
	private Date expirationDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_detail_id", referencedColumnName = "purchase_order_detail_id", nullable = false)
	private PurchaseOrderDetailModel purchaseOrderDetail;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_location_id", referencedColumnName = "store_location_id", nullable = false)
	private StoreLocationModel storeLocation;

	@OneToMany(mappedBy = "pk.poReceiveDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PoReceiveWarningModel> receptionWarnings;

	public PoReceiveDetailModel(Long poReceiveDetailId, Long purchaseOrderDetailId,
								Long storeLocationId, Double receivedQty, String batchNumber, boolean error,
								Date expirationDate, Long userReceiverId) {

		super();

		this.poReceiveDetailId = poReceiveDetailId;
		this.purchaseOrderDetail = new PurchaseOrderDetailModel(purchaseOrderDetailId);
		this.storeLocation = new StoreLocationModel(storeLocationId);
		this.receivedQty = receivedQty;
		this.batchNumber = batchNumber;
		this.error = error;
		this.expirationDate = expirationDate;
		super.userCreate = new UserModel(userReceiverId);
	}

	public PoReceiveDetailModel(Long purchaseOrderDetailId,
								Long storeLocationId, Double receivedQty, String batchNumber, boolean error,
								Date expirationDate, Long userReceiverId) {

		super();

		this.purchaseOrderDetail = new PurchaseOrderDetailModel(purchaseOrderDetailId);
		this.storeLocation = new StoreLocationModel(storeLocationId);
		this.receivedQty = receivedQty;
		this.batchNumber = batchNumber;
		this.error = error;
		this.expirationDate = expirationDate;
		super.userCreate = new UserModel(userReceiverId);
	}

	public void add(ReceptionWarningDTO currentWarning) {

		if (receptionWarnings == null) receptionWarnings = new ArrayList<>();

		receptionWarnings.add(new PoReceiveWarningModel(this, currentWarning.getWarningId(), currentWarning.getQty()));
	}


	public void updateInformation(ReceivingRequestUpdateDTO dto, Long userReceiverId) {
		List<ReceptionWarningDTO> warnings = Optional.ofNullable(dto.getWarnings()).orElse(List.of());
		this.storeLocation = new StoreLocationModel(dto.getItem().getStoreLocationId());
		this.receivedQty = dto.getItem().getQty();
		this.batchNumber = dto.getItem().getBatchNumber();
		this.error = !warnings.isEmpty();
		this.expirationDate = dto.getItem().getExpirationDate();
		super.userCreate = new UserModel(userReceiverId);

		warnings.forEach(w -> receptionWarnings.add(new PoReceiveWarningModel(this, w.getWarningId(), w.getQty())));
	}
}
