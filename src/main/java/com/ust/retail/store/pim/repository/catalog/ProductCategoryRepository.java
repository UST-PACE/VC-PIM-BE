package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.dto.catalog.ProductCategoryDTO;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryModel, Long> {
	@Query(value = "SELECT c"
			+ " FROM ProductCategoryModel c JOIN c.productGroup g"
			+ " WHERE (?1 = NULL OR g.productGroupId = ?1)"
			+ "   AND (?2 = NULL OR c.productCategoryName LIKE %?2%)")
	Page<ProductCategoryModel> findByFilters(Long productGroupId, String productCategoryName, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.ProductCategoryDTO(c.productCategoryId, g.productGroupId, g.productGroupName, c.productCategoryName)"
			+ " FROM ProductCategoryModel c JOIN c.productGroup g"
			+ " WHERE (?1 = 0L OR g.productGroupId = ?1)"
			+ "   AND (?2 = NULL OR c.productCategoryName LIKE %?2%)")
	List<ProductCategoryDTO> getAutocompleteList(Long productGroupId, String productCategoryName);

	List<ProductCategoryModel> findByProductGroupProductGroupId(Long productGroupId);

	@Query("SELECT DISTINCT c"
			+ " FROM ProductCategoryModel c"
			+ "   JOIN FETCH c.products p"
			+ "   JOIN InventoryModel i ON p.upcMasterId = i.upcMaster.upcMasterId AND i.qty > 0"
			+ "   JOIN i.storeLocation sl ON sl.storeNumber.storeNumId = :storeNumberId "
			+ " WHERE c.productGroup.productGroupId = :productGroupId"
			+ "   AND (:productTypeId = NULL OR p.upcMasterType.catalogId = :productTypeId)"
			+ "   AND p.productType.catalogId = 4002"
			+ "   AND p.upcMasterStatus.catalogId <> 30002"
			+ " ORDER BY c.productCategoryId"
	)
	List<ProductCategoryModel> findByProductTypeWithStock(Long productGroupId, Long storeNumberId, Long productTypeId);
}
