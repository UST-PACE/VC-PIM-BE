package com.ust.retail.store.pim.repository.vendormaster;

import com.ust.retail.store.pim.model.vendormaster.VendorMasterStoreModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterStorePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorMasterStoreRepository extends JpaRepository<VendorMasterStoreModel, VendorMasterStorePK> {

	List<VendorMasterStoreModel> findByPkVendorMasterVendorMasterId(Long vendorMasterId);
}
