package com.ust.retail.store.pim.repository.promotion;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ust.retail.store.pim.dto.promotion.PromotionFilterResultDTO;
import com.ust.retail.store.pim.model.promotion.PromotionModel;

public interface PromotionRepository extends JpaRepository<PromotionModel, Long> {

	String FIND_BY_FILTERS_FROM_CLAUSE = " FROM PromotionModel AS p"
			+ "     JOIN p.brandOwner"
			+ "     JOIN p.vendorMaster"
			+ "     JOIN p.productGroup"
			+ "     JOIN p.promotionType"
			+ "     LEFT JOIN p.productCategory"
			+ "     LEFT JOIN p.productSubcategory"
			+ "     LEFT JOIN p.productItem"
			+ "     LEFT JOIN p.upcMaster";
	String FIND_BY_FILTERS_WHERE_CLAUSE = " WHERE (?1 = NULL OR p.brandOwner.brandOwnerId = ?1)"
			+ "    AND (?2 = NULL OR p.vendorMaster.vendorMasterId = ?2)"
			+ "    AND (?3 = NULL OR p.vendorMaster.vendorCode LIKE %?3%)"
			+ "    AND (?4 = NULL OR p.vendorMaster.vendorName LIKE %?4%)"
			+ "    AND (?5 = NULL OR p.productGroup.productGroupId = ?5)"
			+ "    AND (?6 = NULL OR p.productCategory.productCategoryId = ?6)"
			+ "    AND (?7 = NULL OR p.productSubcategory.productSubcategoryId = ?7)"
			+ "    AND (?8 = NULL OR p.productItem.productItemId = ?8)"
			+ "    AND (?9 = NULL OR p.upcMaster.upcMasterId = ?9)"
			+ "    AND (?10 = NULL OR p.promotionType.catalogId = ?10)"
			+ "    AND ((?11 = NULL AND ?12 = NULL) OR p.endDate BETWEEN ?11 AND ?12)";

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.promotion.PromotionFilterResultDTO("
			+ "p.promotionId, "
			+ "p.vendorMaster.vendorCode, "
			+ "p.vendorMaster.vendorName, "
			+ "p.productGroup.productGroupName, "
			+ "p.productCategory.productCategoryName, "
			+ "p.productSubcategory.productSubcategoryName, "
			+ "p.productItem.productItemName, "
			+ "p.upcMaster.productName, "
			+ "p.promotionType.catalogOptions, "
			+ "p.startDate, "
			+ "p.endDate, "
			+ "p.discount, "
			+ "p.createdAt)"
			+ FIND_BY_FILTERS_FROM_CLAUSE
			+ FIND_BY_FILTERS_WHERE_CLAUSE,
			countQuery = "SELECT count(p)"
					+ FIND_BY_FILTERS_FROM_CLAUSE
					+ FIND_BY_FILTERS_WHERE_CLAUSE
	)
	Page<PromotionFilterResultDTO> findByFilters(Long brandOwnerId, Long vendorMasterId, String vendorCode, String vendorName, Long productGroupId, Long productCategoryId, Long productSubcategoryId, Long productItemId, Long upcMasterId, Long promotionTypeId, Date expirationDateStart, Date expirationDateEnd, Pageable pageable);

	@Query(value = "SELECT p"
			+ " FROM PromotionModel p, UpcMasterModel upcm, UpcVendorDetailsModel upcv"
			+ " WHERE upcv.vendorMaster.vendorMasterId = p.vendorMaster.vendorMasterId and upcm.upcMasterId = upcv.upcMaster.upcMasterId"
			+ "   AND p.vendorMaster.vendorMasterId = ?1"
			+ "   AND upcm.upcMasterId = ?2"
			+ "   AND p.endDate >= ?3"
			+ "   AND ("
			+ "     (upcm.productGroup = p.productGroup and upcm.productCategory = p.productCategory and upcm.productSubcategory = p.productSubcategory and upcm.productItem = p.productItem and upcm = p.upcMaster)"
			+ "     OR (upcm.productGroup = p.productGroup and upcm.productCategory = p.productCategory and upcm.productSubcategory = p.productSubcategory and upcm.productItem = p.productItem and p.upcMaster is null)"
			+ "     OR (upcm.productGroup = p.productGroup and upcm.productCategory = p.productCategory and upcm.productSubcategory = p.productSubcategory and p.productItem is null and p.upcMaster is null)"
			+ "     OR (upcm.productGroup = p.productGroup and upcm.productCategory = p.productCategory and p.productSubcategory is null and p.productItem is null and p.upcMaster is null)"
			+ "     OR (upcm.productGroup = p.productGroup and p.productCategory is null and p.productSubcategory is null and p.productItem is null and p.upcMaster is null)"
			+ "   )"
			+ " ORDER BY"
			+ "   p.upcMaster.upcMasterId NULLS LAST,"
			+ "   p.productItem.productItemId NULLS LAST,"
			+ "   p.productSubcategory.productSubcategoryId NULLS LAST,"
			+ "   p.productCategory.productCategoryId NULLS LAST,"
			+ "   p.productGroup.productGroupId NULLS LAST"
	)
	List<PromotionModel> findActiveProductPromotions(Long vendorMasterId, Long upcMasterId, Date endDate);
}
