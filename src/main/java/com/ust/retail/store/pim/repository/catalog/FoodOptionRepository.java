package com.ust.retail.store.pim.repository.catalog;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ust.retail.store.pim.dto.catalog.FoodOptionDTO;
import com.ust.retail.store.pim.model.catalog.FoodOptionModel;

@Repository
public interface FoodOptionRepository extends JpaRepository<FoodOptionModel, Long>{
	
	@Query(value = "SELECT f"
			+ " FROM FoodOptionModel f "
			+ " WHERE (f.deleted = false) AND (?1 IS NULL OR f.foodOptionCatalogueName LIKE %?1%) AND (?2 IS NULL OR f.foodOptionStatus.catalogId = ?2)")
	Page<FoodOptionModel> findByFilters(String foodOptionName, Long foodOptionStatusId, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.FoodOptionDTO(i.foodOptionId, i.foodOptionCatalogueName)"
			+ " FROM FoodOptionModel i "
			+ " WHERE (i.deleted = false) AND (?1 = NULL OR i.foodOptionCatalogueName LIKE %?1%)")
	List<FoodOptionDTO> getAutocompleteList(String catalogueName);
		
	@Query("SELECT DISTINCT fo FROM FoodOptionModel fo "
			+ "JOIN fo.foodOptionCatalogueUpcs um "
			+ "WHERE (:id IS NULL "
			+ "AND um.upcMasterId IN :upcMasterIds AND fo.deleted = false) "
			+ "OR (:id IS NOT NULL "
			+ "AND fo.foodOptionId = :id "
			+ "AND "
			+ "(CASE "
			+ "WHEN (SELECT COUNT(um.upcMasterId) FROM fo.foodOptionCatalogueUpcs um WHERE um.upcMasterId IN :upcMasterIds AND fo.deleted = false) = :upcMasterIdsSize "
			+ "THEN 0 "
			+ "ELSE "
			+ "CASE "
			+ "WHEN (SELECT COUNT(fo) FROM FoodOptionModel fo "
			+ "JOIN fo.foodOptionCatalogueUpcs um "
			+ "WHERE (um.upcMasterId IN :newUpcIds AND fo.deleted = false)) > 0"
			+ "THEN 1 "
			+ "ELSE 0 "
			+ "END "
			+ "END) = 1)")
		List<FoodOptionModel> findMatchingFoodOptions(@Param("id") Long id, 
		                                         @Param("upcMasterIds") List<Long> upcMasterIds,
		                                         @Param("upcMasterIdsSize") Long upcMasterIdsSize,
		                                         @Param("newUpcIds") List<Long> newUpcIds);
	
	List<FoodOptionModel> findAllByDeletedFalse();
	
	@Query(value = "SELECT f FROM FoodOptionModel f WHERE f.foodOptionCatalogueName = ?1 AND (?2 IS NULL OR f.foodOptionId != ?2) AND f.deleted = false")
	FoodOptionModel existsByCatalogueName(String catalogueName, Long Id);
	
}
