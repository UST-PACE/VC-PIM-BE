package com.ust.retail.store.common.repository;

import com.ust.retail.store.common.model.UnitOfMeasureModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasureModel, Long> {
	@Query(value = "SELECT u"
			+ " FROM UnitOfMeasureModel u"
			+ " WHERE (?1 = NULL OR u.unitType.catalogId = ?1)"
			+ " AND (?2 = NULL OR u.unit.catalogOptions LIKE %?2%)"
	)
	Page<UnitOfMeasureModel> findByFilters(Long unitTypeId, String unitName, Pageable pageable);

	@Query(value="SELECT u"
			+ " FROM UnitOfMeasureModel u"
			+ " WHERE ?1 = NULL OR u.unit.catalogOptions LIKE %?1%")
	List<UnitOfMeasureModel> getAutocompleteList(String storeName);

	Optional<UnitOfMeasureModel> findFirstByBaseUnitIsTrueAndUnitTypeCatalogId(Long unitTypeId);

	@Modifying
	@Query(value = "UPDATE UnitOfMeasureModel u set u.baseUnit = false WHERE u.unitType.catalogId = ?1")
	void removeBaseUnitForUnitTypeId(Long unitTypeId);

	@Query(value = "SELECT u.unitType.catalogId FROM UnitOfMeasureModel u WHERE u.unit.catalogId = ?1")
	Optional<Long> findUnitTypeForUnitCatalogId(Long unitCatalogId);

	List<UnitOfMeasureModel> findByUnitTypeCatalogId(Long unitTypeId);
}
