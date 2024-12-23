package com.ust.retail.store.pim.model.inventory;

import com.ust.retail.store.pim.common.catalogs.InventoryProductReturnStatusCatalog;
import com.ust.retail.store.pim.dto.inventory.returns.screen.ReturnItemDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
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
@Table(name = "inventory_product_returns")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class InventoryProductReturnModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_product_return_id")
	private Long inventoryProductReturnId;

	@Column(name = "finish_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date finishAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_product_return_status_id", referencedColumnName = "catalog_id")
	private CatalogModel status;

	@OneToMany(mappedBy = "productReturn", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InventoryProductReturnDetailModel> returnDetails;

	public InventoryProductReturnModel(Long userCreatedId) {
		super.userCreate = new UserModel(userCreatedId);
	}
	

	public void add(ReturnItemDTO returnItemDTO, Long upcMasterId) {
		if (getReturnDetails() == null) returnDetails = new ArrayList<>();

		if (returnItemDTO.getInventoryProductReturnDetailId() != null) {
			returnDetails.stream()
					.filter(detail -> Objects.equals(detail.getInventoryProductReturnDetailId(), returnItemDTO.getInventoryProductReturnDetailId()))
					.findFirst()
					.ifPresent(detail -> detail.updateInformation(
							returnItemDTO.getQty(),
							returnItemDTO.getBatchNum(),
							returnItemDTO.getReturnReasonId()
					));
		} else {
			returnDetails.add(new InventoryProductReturnDetailModel(
					upcMasterId,
					returnItemDTO.getStoreLocationId(),
					returnItemDTO.getQty(),
					returnItemDTO.getBatchNum(),
					returnItemDTO.getReturnReasonId(),
					this));
		}

	}
	
	public void finishReturn() {
		this.finishAt = new Date();
		this.status = new CatalogModel(InventoryProductReturnStatusCatalog.INVENTORY_PRODUCT_RETURN_STATUS_PENDING_REVIEW);
	}

	public void setStatus(Long statusId) {
		this.status = new CatalogModel(statusId);
	}
}
