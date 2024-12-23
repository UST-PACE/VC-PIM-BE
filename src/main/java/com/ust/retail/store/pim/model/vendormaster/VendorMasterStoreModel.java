package com.ust.retail.store.pim.model.vendormaster;

import com.ust.retail.store.pim.model.general.Audits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "vendor_master_store")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class VendorMasterStoreModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VendorMasterStorePK pk;

	public VendorMasterStoreModel(Long vendorMasterId, Long storeNumId) {
		super();
		this.pk  = new VendorMasterStorePK(storeNumId,vendorMasterId);

	}
	
}
