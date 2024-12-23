package com.ust.retail.store.pim.model.inventory;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.ust.retail.store.pim.model.catalog.CatalogModel;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@NoArgsConstructor
@Getter
public class PoReceptionWarningPK implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "po_receive_detail_id", referencedColumnName = "po_receive_detail_id")
	PoReceiveDetailModel poReceiveDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warning_reason_id", referencedColumnName = "catalog_id")
	CatalogModel warningReason;
	
	public PoReceptionWarningPK(PoReceiveDetailModel receiveDetail, Long warningReasonId) {
		poReceiveDetail = receiveDetail;
		warningReason = new CatalogModel(warningReasonId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PoReceptionWarningPK)) return false;
		PoReceptionWarningPK that = (PoReceptionWarningPK) o;
		return Objects.equals(poReceiveDetail.getPoReceiveDetailId(), that.poReceiveDetail.getPoReceiveDetailId())
				&& Objects.equals(warningReason.getCatalogId(), that.warningReason.getCatalogId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(poReceiveDetail.getPoReceiveDetailId(), warningReason.getCatalogId());
	}
}
