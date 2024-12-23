package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.dto.catalog.DistributorDTO;
import com.ust.retail.store.pim.model.catalog.DistributorModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributorRepository extends JpaRepository<DistributorModel, Long> {

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.DistributorDTO(a.distributorId,a.distributorName)"
			+ " FROM DistributorModel a"
			+ " WHERE ?1 = NULL OR a.distributorName LIKE %?1%")
	Page<DistributorDTO> findByFilters(String distributorName, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.DistributorDTO(a.distributorId,a.distributorName)"
			+ " FROM DistributorModel a"
			+ " WHERE ?1 = NULL OR a.distributorName LIKE %?1%")
	List<DistributorDTO> getAutocompleteList(String distributorName);
}
