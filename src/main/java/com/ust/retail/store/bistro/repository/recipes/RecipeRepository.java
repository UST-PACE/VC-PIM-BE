package com.ust.retail.store.bistro.repository.recipes;

import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.pim.dto.report.RecipeAddOnCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.RecipeCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.RecipeIngredientCsvDumpDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeModel, Long> {

	@Query(value = "SELECT a"
			+ " FROM RecipeModel a"
			+ " WHERE a.relatedUpcMaster.productGroup.displayExternally = true"
			+ " AND   (:recipeName = NULL OR a.relatedUpcMaster.productName LIKE %:recipeName%)"
			+ " AND   (:foodClassificationId = NULL OR a.foodClassification.catalogId = :foodClassificationId)"
			+ " AND   (:preparationAreaId = NULL OR a.preparationArea.catalogId = :preparationAreaId)"
			+ " AND   (:mealTemperatureId = NULL OR a.mealTemperature.catalogId = :mealTemperatureId)")
	Page<RecipeModel> findByFilters(String recipeName,
									Long foodClassificationId,
									Long mealTemperatureId,
									Long preparationAreaId,
									Pageable pageable);

	List<RecipeModel> findByFoodClassificationCatalogId(Long categoryId);

	List<RecipeModel> findByRelatedUpcMasterProductNameContaining(String recipeName);

	@Query(value = "SELECT r"
			+ " FROM MenuEntryDayModel d"
			+ "   JOIN d.menuEntry m"
			+ "   JOIN m.recipe r"
			+ "   JOIN r.relatedUpcMaster p"
			+ "   JOIN p.upcSellingChannels sc"
			+ "   JOIN p.storePrices sp"
			+ "   JOIN m.store sn"
			+ " WHERE sn.storeNumId = :storeId"
			+ "   AND d.day = :weekDay"
			+ "   AND sp.storeNumber.storeNumId = :storeId"
			+ "   AND :time BETWEEN d.start AND d.end"
			+ "   AND r.displayExternally = true"
			+ "   AND sc.channel.catalogId = :channelId"
			+ "   AND sc.enabled = true"
			+ "   AND p.productGroup.displayExternally = true"
			+ "   AND (:searchKey = NULL OR (p.productName LIKE %:searchKey% OR p.productDescription LIKE %:searchKey% OR p.principalUpc LIKE %:searchKey%))"
			+ "   AND (:groupId = NULL OR p.productGroup.productGroupId = :groupId)"
			+ "   AND (:categoryId = NULL OR p.productCategory.productCategoryId = :categoryId)"
			+ "   AND (:subcategoryId = NULL OR p.productSubcategory.productSubcategoryId = :subcategoryId)"
			+ " GROUP BY r"
	)
	Page<RecipeModel> findByStoreAndFilters(Long storeId,
											Integer weekDay,
											Integer time,
											String searchKey,
											Long groupId,
											Long categoryId,
											Long subcategoryId,
											Long channelId,
											Pageable pageable);

	@Query(value = "SELECT r"
			+ " FROM MenuEntryDayModel d"
			+ "   JOIN d.menuEntry m"
			+ "   JOIN m.recipe r"
			+ "   JOIN r.relatedUpcMaster p"
			+ "   JOIN p.storePrices sp"
			+ "   JOIN m.store sn"
			+ " WHERE sn.storeNumId = :storeId"
			+ "   AND sp.storeNumber.storeNumId = :storeId"
			+ "   AND d.day = :weekDay"
			+ "   AND :time BETWEEN d.start AND d.end"
			+ "   AND r.displayExternally = true"
			+ "   AND p.productGroup.displayExternally = true"
			+ " GROUP BY r"
	)
	List<RecipeModel> findByStore(Long storeId,
								  Integer weekDay,
								  Integer time);

	@Query(value = "SELECT r.recipeId"
			+ " FROM MenuEntryDayModel d"
			+ "   JOIN d.menuEntry m"
			+ "   JOIN m.recipe r"
			+ "   JOIN r.relatedUpcMaster p"
			+ "   JOIN p.storePrices sp"
			+ "   JOIN m.store sn"
			+ " WHERE sn.storeNumId = :storeId"
			+ "   AND sp.storeNumber.storeNumId = :storeId"
			+ "   AND d.day = :weekDay"
			+ "   AND :time BETWEEN d.start AND d.end"
			+ "   AND r.displayExternally = true"
			+ "   AND p.productGroup.displayExternally = true"
			+ "   AND p.principalUpc IN :upcList"
			+ " GROUP BY r"
	)
	List<Long> findAvailableForSaleByStoreAndUpcList(Long storeId,
													  Integer weekDay,
													  Integer time,
													  List<String> upcList);

	@Query(value = "SELECT r"
			+ " FROM MenuEntryModel m"
			+ "   JOIN m.recipe r"
			+ "   JOIN r.relatedUpcMaster p"
			+ "   JOIN m.store sn"
			+ " WHERE sn.storeNumId = :storeId"
			+ "   AND p.principalUpc IN :upcList"
			+ " GROUP BY r"
	)
	List<RecipeModel> findByStoreAndUpcList(Long storeId,
											List<String> upcList);

	Optional<RecipeModel> findByRelatedUpcMasterUpcMasterId(Long upcMasterId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.RecipeCsvDumpDTO("
			+ "p,"
			+ "p.principalUpc,"
			+ "p.sku,"
			+ "p.productName,"
			+ "pg.productGroupName,"
			+ "pc.productCategoryName,"
			+ "ps.productSubcategoryName,"
			+ "bo.brandOwnerName,"
			+ "st.catalogOptions,"
			+ "p.productDescription,"
			+ "p.contentPerUnit,"
			+ "cpu.catalogOptions,"
			+ "r.sellingPrice,"
			+ "p.taxPercentage,"
			+ "p.ageRestricted,"
			+ "mt.catalogOptions"
			+ ")"
			+ " FROM RecipeModel r"
			+ "   JOIN r.mealTemperature mt"
			+ "   JOIN r.relatedUpcMaster p"
			+ "   JOIN p.productSubcategory ps"
			+ "   JOIN p.productCategory pc"
			+ "   JOIN p.productGroup pg"
			+ "   JOIN p.brandOwner bo"
			+ "   JOIN p.contentPerUnitUom cpu"
			+ "   JOIN p.upcMasterStatus st"
	)
	List<RecipeCsvDumpDTO> getRecipeCsvDump();

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.RecipeIngredientCsvDumpDTO("
			+ "rp.principalUpc,"
			+ "rp.productName,"
			+ "ip.principalUpc,"
			+ "ip.productName,"
			+ "vm.vendorName,"
			+ "i.qty,"
			+ "i.unit.catalogOptions,"
			+ "bo.brandOwnerName,"
			+ "ip.upcMasterId,"
			+ "ip.contentPerUnit,"
			+ "ip.contentPerUnitUom.catalogId,"
			+ "i.unit.catalogId,"
			+ "i.topping"
			+ ")"
			+ " FROM RecipeDetailModel i"
			+ "   JOIN i.recipe r"
			+ "   JOIN i.upcMaster ip"
			+ "   JOIN ip.brandOwner bo"
			+ "   JOIN r.relatedUpcMaster rp"
			+ "   LEFT JOIN UpcVendorDetailsModel uvd ON uvd.upcMaster = ip AND uvd.defaultVendor = true"
			+ "   LEFT JOIN VendorMasterModel vm ON uvd.vendorMaster = vm"
	)
	List<RecipeIngredientCsvDumpDTO> getRecipeIngredientCsvDump();

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.report.RecipeAddOnCsvDumpDTO("
			+ "rp.principalUpc,"
			+ "rp.productName,"
			+ "ap.principalUpc,"
			+ "ap.productName,"
			+ "vm.vendorName,"
			+ "a.qty,"
			+ "ap.contentPerUnitUom.catalogOptions,"
			+ "a.sellingPrice,"
			+ "bo.brandOwnerName,"
			+ "st.catalogOptions,"
			+ "ap.upcMasterId"
			+ ")"
			+ " FROM RecipeAddonModel a"
			+ "   JOIN a.recipe r"
			+ "   JOIN a.upcMaster ap"
			+ "   JOIN ap.brandOwner bo"
			+ "   JOIN r.relatedUpcMaster rp"
			+ "   JOIN rp.upcMasterStatus st"
			+ "   LEFT JOIN UpcVendorDetailsModel uvd ON uvd.upcMaster = ap AND uvd.defaultVendor = true"
			+ "   LEFT JOIN VendorMasterModel vm ON uvd.vendorMaster = vm"
	)
	List<RecipeAddOnCsvDumpDTO> getRecipeAddOnCsvDump();

	@Query(value = "SELECT r"
			+ " FROM MenuEntryDayModel d"
			+ "   JOIN d.menuEntry m"
			+ "   JOIN m.recipe r"
			+ "   JOIN r.relatedUpcMaster p"
			+ "   JOIN m.store sn"
			+ " WHERE sn.storeNumId = :storeId"
			+ "   AND (:recipeName = NULL OR p.productName LIKE %:recipeName%)"
			+ "   AND d.day = :weekDay"
			+ "   AND :time BETWEEN d.start AND d.end"
			+ "   AND p.productGroup.displayExternally = true"
			+ " GROUP BY r"
	)
	Page<RecipeModel> findCurrentByStoreAndRecipeName(Long storeId,
													  String recipeName,
													  Integer weekDay,
													  Integer time,
													  Pageable pageable);
}
