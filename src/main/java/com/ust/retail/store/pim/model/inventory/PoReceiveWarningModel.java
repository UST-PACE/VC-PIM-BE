package com.ust.retail.store.pim.model.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "po_receiving_warnings")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class PoReceiveWarningModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PoReceptionWarningPK pk;
	
	private Double qty;
	
	public PoReceiveWarningModel (PoReceiveDetailModel receiveDetail,Long warningReasonId, Double qty) {
		this.pk = new PoReceptionWarningPK(receiveDetail, warningReasonId);
		this.qty = qty;
		
	}
	

}
