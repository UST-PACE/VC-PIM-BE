package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.dto.catalog.ProductItemDTO;
import com.ust.retail.store.pim.model.catalog.ProductItemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItemModel, Long> {
	@Query(value = "SELECT i"
			+ " FROM ProductItemModel i JOIN i.productSubcategory s"
			+ " WHERE (?1 = NULL OR s.productSubcategoryId = ?1)"
			+ "   AND (?2 = NULL OR i.productItemName LIKE %?2%)")
	Page<ProductItemModel> findByFilters(Long productSubcategoryId, String productItemName, Pageable pageable);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.catalog.ProductItemDTO(i.productItemId, s.productCategory.productGroup.productGroupId, s.productCategory.productCategoryId, s.productSubcategoryId, s.productSubcategoryName, i.productItemName)"
			+ " FROM ProductItemModel i JOIN i.productSubcategory s"
			+ " WHERE (?1 = 0L OR s.productSubcategoryId = ?1)"
			+ "   AND (?2 = NULL OR i.productItemName LIKE %?2%)")
	List<ProductItemDTO> getAutocompleteList(Long productSubcategoryId, String productItemName);

	List<ProductItemModel> findByProductSubcategoryProductSubcategoryId(Long productSubcategoryId);
}
