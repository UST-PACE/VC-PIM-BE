package com.ust.retail.store.pim.repository.tax;

import com.ust.retail.store.pim.model.tax.TaxModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaxRepository extends JpaRepository<TaxModel, Long> {
	@Query(value = "SELECT t FROM TaxModel t"
			+ " WHERE (?1 is null OR t.storeNumber.storeNumId = ?1)"
			+ "   AND (?2 is null OR t.taxType.catalogId = ?2)"
			+ "   AND (?3 is null OR t.productGroup.productGroupId = ?3)"
			+ "   AND (?4 is null OR t.productCategory.productCategoryId = ?4)"
			+ "   AND (?5 is null OR t.productSubcategory.productSubcategoryId = ?5)"
	)
	Page<TaxModel> findByFilters(Long storeNumId,
								 Long taxTypeId,
								 Long productGroupId,
								 Long productCategoryId,
								 Long productSubcategoryId,
								 Pageable pageable);

	@Query(value = "SELECT t"
			+ " FROM UpcStorePriceModel p"
			+ "   JOIN p.upcMaster upc"
			+ "   JOIN TaxModel t"
			+ "     ON t.storeNumber = p.storeNumber"
			+ "       AND ("
			+ "            (upc.productGroup = t.productGroup and upc.productCategory = t.productCategory and upc.productSubcategory = t.productSubcategory)"
			+ "         OR (upc.productGroup = t.productGroup and upc.productCategory = t.productCategory and t.productSubcategory is null)"
			+ "         OR (upc.productGroup = t.productGroup and t.productCategory is null and t.productSubcategory is null)"
			+ "       )"
			+ " WHERE t.storeNumber.storeNumId = ?2"
			+ "   AND upc.upcMasterId = ?1"
			+ " ORDER BY t.storeNumber.storeNumId, t.taxType.catalogId,"
			+ "   t.productSubcategory.productSubcategoryId NULLS LAST,"
			+ "   t.productCategory.productCategoryId NULLS LAST,"
			+ "   t.productGroup.productGroupId"
	)
	List<TaxModel> findProductTaxes(Long upcMasterId, Long storeNumId);

	@Query("SELECT (count(t) > 0)"
			+ " FROM TaxModel t"
			+ " WHERE t.storeNumber.storeNumId = ?1"
			+ "   and t.taxType.catalogId = ?2"
			+ "   and t.productGroup.productGroupId = ?3"
			+ "   and (t.productCategory.productCategoryId = ?4 or t.productCategory.productCategoryId is null)"
			+ "   and (t.productSubcategory.productSubcategoryId = ?5 or t.productSubcategory.productSubcategoryId is null)")
	boolean existsByUniqueKey(Long storeNumId, Long catalogId, Long productGroupId, Long productCategoryId, Long productSubcategoryId);
}
