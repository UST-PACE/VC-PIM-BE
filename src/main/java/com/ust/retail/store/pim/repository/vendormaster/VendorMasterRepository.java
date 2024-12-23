package com.ust.retail.store.pim.repository.vendormaster;

import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorMasterRepository extends JpaRepository<VendorMasterModel, Long> {

	@Query(value = "SELECT DISTINCT a"
			+ " FROM VendorMasterModel a"
			+ " LEFT OUTER JOIN a.vendorStoreNumbers vsn"
			+ " WHERE (?1 = NULL OR a.vendorName LIKE %?1%)"
			+ " AND (?2 = NULL OR a.vendorCode LIKE %?2%)"
			+ " AND (?3 = NULL OR vsn.pk.storeNum.storeNumId = ?3)"
			+ " AND (?4 = NULL OR a.distributor.distributorId = ?4)")
	Page<VendorMasterModel> findByFilters(String vendorName, String vendorCode, Long storeNumberId, Long distributorId, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO(a.vendorMasterId,a.vendorName,a.vendorCode)"
			+ " FROM VendorMasterModel a"
			+ " WHERE ?1 = NULL OR a.vendorName LIKE %?1%")
	List<VendorMasterDTO> getAutocompleteListVendorName(String vendorName);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO(a.vendorMasterId,a.vendorName,a.vendorCode)"
			+ " FROM VendorMasterModel a"
			+ " WHERE ?1 = NULL OR a.vendorCode LIKE %?1%")
	List<VendorMasterDTO> getAutocompleteListCodeName(String vendorCode);

}
