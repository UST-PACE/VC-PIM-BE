package com.ust.retail.store.pim.model.vendormaster;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.ust.retail.store.pim.model.catalog.StoreNumberModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class VendorMasterStorePK implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "store_num_id", referencedColumnName = "store_num_id")
	StoreNumberModel storeNum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_master_id",referencedColumnName = "vendor_master_id")
	private VendorMasterModel vendorMaster;
	
	
	public VendorMasterStorePK(Long storeNumId,Long vendorMasterId) {
		this.storeNum = new StoreNumberModel(storeNumId);
		this.vendorMaster = new VendorMasterModel(vendorMasterId);
	}
	
	public VendorMasterStorePK(StoreNumberModel storeNum,VendorMasterModel vendorMaster) {
		this.storeNum = storeNum;
		this.vendorMaster = vendorMaster;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VendorMasterStorePK)) return false;
		VendorMasterStorePK that = (VendorMasterStorePK) o;
		return Objects.equals(storeNum.getStoreNumId(), that.storeNum.getStoreNumId())
				&& Objects.equals(vendorMaster.getVendorMasterId(), that.vendorMaster.getVendorMasterId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(storeNum.getStoreNumId(), vendorMaster.getVendorMasterId());
	}
}
