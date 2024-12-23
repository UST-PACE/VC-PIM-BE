package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO;
import com.ust.retail.store.pim.model.catalog.BrandOwnerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandOwnerRepository extends JpaRepository<BrandOwnerModel, Long> {

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO(a.brandOwnerId,a.brandOwnerName)"
			+ " FROM BrandOwnerModel a"
			+ " WHERE ?1 = NULL OR a.brandOwnerName LIKE %?1%")
	Page<BrandOwnerDTO> findByFilters(String storeName, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO(a.brandOwnerId,a.brandOwnerName)"
			+ " FROM BrandOwnerModel a"
			+ " WHERE ?1 = NULL OR a.brandOwnerName LIKE %?1%")
	List<BrandOwnerDTO> getAutocompleteList(String storeName);
}
