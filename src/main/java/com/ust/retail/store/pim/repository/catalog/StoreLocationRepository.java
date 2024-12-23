package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.dto.catalog.StoreLocationDTO;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreLocationRepository extends JpaRepository<StoreLocationModel, Long> {

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.StoreLocationDTO(a.storeLocationId,a.storeLocationName,a.storeNumber.storeNumId,a.storeNumber.storeName)"
			+ " FROM StoreLocationModel a"
			+ " WHERE (?1 = NULL OR a.storeLocationName LIKE %?1%)"
			+ " AND (?2 = NULL OR a.storeNumber.storeNumId=?2)")
	Page<StoreLocationDTO> findByFilters(String storeLocationName, Long storeNumId, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.StoreLocationDTO("
			+ "a.storeLocationId, "
			+ "a.storeLocationName, "
			+ "a.storeNumber.storeNumId, "
			+ "a.storeNumber.storeName) "
			+ "FROM StoreLocationModel a "
			+ "WHERE ?1 = NULL OR a.storeNumber.storeNumId = ?1")
	List<StoreLocationDTO> findByStoreNumId(Long storeNumId);

	StoreLocationModel findByFrontSaleTrueAndStoreNumberStoreNumId(Long storeNumId);


}
