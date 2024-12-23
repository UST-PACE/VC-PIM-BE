package com.ust.retail.store.pim.repository.upcmaster;

import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UpcVendorDetailsRepository extends JpaRepository<UpcVendorDetailsModel, Long> {
	Optional<UpcVendorDetailsModel> findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(Long upcMasterId, Long vendorMasterId);

	@Query(value = "SELECT a"
			+ " FROM UpcVendorDetailsModel a"
			+ " WHERE (?1 = NULL OR a.vendorMaster.vendorName LIKE %?1%)"
			+ " AND (?2 = NULL OR (a.upcMaster.principalUpc = ?2 OR COALESCE(a.upcMaster.sku, '') = ?2 OR COALESCE(a.caseUpc, '') = ?2))"
			+ " AND (?3 = NULL OR a.upcMaster.productName LIKE %?3%)")
	Page<UpcVendorDetailsModel> findByFilters(String vendorName, String code, String productName, Pageable pageable);

	Optional<UpcVendorDetailsModel> findByDefaultVendorTrueAndUpcMasterUpcMasterId(Long upcMasterId);

	List<UpcVendorDetailsModel> findByVendorMasterVendorMasterId(Long vendorMasterId);

	@Query(value = "SELECT u"
			+ " FROM UpcVendorDetailsModel u"
			+ "   JOIN u.storeCosts sc"
			+ " WHERE u.vendorMaster.vendorMasterId = ?1"
			+ " AND sc.storeNumber.storeNumId = ?2"
	)
	List<UpcVendorDetailsModel> findByVendorMasterIdAndStoreNumId(Long vendorMasterId, Long storeNumId);

	List<UpcVendorDetailsModel> findByUpcMasterUpcMasterId(Long upcMasterId);
}
