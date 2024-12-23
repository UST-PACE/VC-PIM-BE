package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.dto.catalog.ProductSubcategoryDTO;
import com.ust.retail.store.pim.model.catalog.ProductSubcategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductSubcategoryRepository extends JpaRepository<ProductSubcategoryModel, Long> {
	@Query(value = "SELECT s"
			+ " FROM ProductSubcategoryModel s JOIN s.productCategory c"
			+ " WHERE (?1 = NULL OR c.productCategoryId = ?1)"
			+ "   AND (?2 = NULL OR s.productSubcategoryName LIKE %?2%)")
	Page<ProductSubcategoryModel> findByFilters(Long productCategoryId, String productSubcategoryName, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.ProductSubcategoryDTO(s.productSubcategoryId, c.productGroup.productGroupId, c.productCategoryId, c.productCategoryName, s.productSubcategoryName)"
			+ " FROM ProductSubcategoryModel s JOIN s.productCategory c"
			+ " WHERE (?1 = 0L OR c.productCategoryId = ?1)"
			+ "   AND (?2 = NULL OR s.productSubcategoryName LIKE %?2%)")
	List<ProductSubcategoryDTO> getAutocompleteList(Long productCategoryId, String productSubcategoryName);

	List<ProductSubcategoryModel> findByProductCategoryProductCategoryId(Long productCategoryId);

	@Query("SELECT DISTINCT sc"
			+ " FROM ProductSubcategoryModel sc"
			+ "   JOIN FETCH sc.products p"
			+ "   JOIN InventoryModel i ON p.upcMasterId = i.upcMaster.upcMasterId AND i.qty > 0"
			+ "   JOIN i.storeLocation sl ON sl.storeNumber.storeNumId = :storeNumberId "
			+ " WHERE sc.productCategory.productCategoryId = :productCategoryId"
			+ "   AND (:productTypeId = NULL OR p.upcMasterType.catalogId = :productTypeId)"
			+ "   AND p.productType.catalogId = 4002"
			+ "   AND p.upcMasterStatus.catalogId <> 30002"
			+ " ORDER BY sc.productSubcategoryId"
	)
	List<ProductSubcategoryModel> findByProductTypeWithStock(Long productCategoryId, Long storeNumberId, Long productTypeId);
}
