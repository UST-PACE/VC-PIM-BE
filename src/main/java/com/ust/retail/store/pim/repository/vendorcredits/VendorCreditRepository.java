package com.ust.retail.store.pim.repository.vendorcredits;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ust.retail.store.pim.model.vendorcredits.VendorCreditModel;

public interface VendorCreditRepository extends JpaRepository<VendorCreditModel, Long> {

	VendorCreditModel findByVendorMasterVendorMasterId(Long vendorMasterId);
}
