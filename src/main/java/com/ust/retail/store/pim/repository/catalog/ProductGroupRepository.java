package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.dto.catalog.ProductGroupDTO;
import com.ust.retail.store.pim.model.catalog.ProductGroupModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductGroupRepository extends JpaRepository<ProductGroupModel, Long> {
	@Query(value = "SELECT a"
			+ " FROM ProductGroupModel a"
			+ " WHERE ?1 = NULL OR a.productGroupName LIKE %?1%")
	Page<ProductGroupModel> findByFilters(String productGroupName, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.ProductGroupDTO(a.productGroupId, a.productGroupName)"
			+ " FROM ProductGroupModel a"
			+ " WHERE ?1 = NULL OR a.productGroupName LIKE %?1%")
	List<ProductGroupDTO> getAutocompleteList(String productGroupName);

	@Query("SELECT DISTINCT g"
			+ " FROM ProductGroupModel g"
			+ "   JOIN FETCH g.products p"
			+ "   JOIN InventoryModel i ON p.upcMasterId = i.upcMaster.upcMasterId AND i.qty > 0"
			+ "   JOIN i.storeLocation sl ON sl.storeNumber.storeNumId = :storeNumberId "
			+ " WHERE g.displayExternally = true"
			+ "   AND (:productTypeId = NULL OR p.upcMasterType.catalogId = :productTypeId)"
			+ "   AND p.productType.catalogId = 4002"
			+ "   AND p.upcMasterStatus.catalogId <> 30002"
			+ " ORDER BY g.productGroupId"
	)
	List<ProductGroupModel> findByProductTypeWithStock(Long storeNumberId, Long productTypeId);
}
