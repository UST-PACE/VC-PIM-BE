package com.ust.retail.store.pim.repository.upcmaster;

import com.ust.retail.store.pim.dto.report.IUpcCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.UpcMasterCsvDTO;
import com.ust.retail.store.pim.dto.upcmaster.DishJsonFileDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterFilterDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UpcMasterRepository extends JpaRepository<UpcMasterModel, Long> {

	Optional<UpcMasterModel> findByPrincipalUpc(String code);

	@Query(value = "SELECT a"
			+ " FROM UpcMasterModel a"
			+ "   JOIN UpcVendorDetailsModel b ON a.upcMasterId = b.upcMaster.upcMasterId"
			+ "   JOIN b.storeCosts sc"
			+ " WHERE a.principalUpc = ?1 "
			+ "   AND b.vendorMaster.vendorMasterId = ?2"
			+ "   AND sc.storeNumber.storeNumId = ?3"
	)
	Optional<UpcMasterModel> findByPrincipalUpcAndVendor(String code, Long vendorMasterId, Long storeNumId);

	@Query(value = "SELECT new com.ust.retail.store.pim.dto.upcmaster.UpcMasterFilterDTO("
			+ "a.upcMasterId,"
			+ "a.principalUpc,"
			+ "a.productName,"
			+ "a.productType.catalogId,"
			+ "a.upcMasterType.catalogOptions,"
			+ "a.brandOwner.brandOwnerId,"
			+ "a.productType.catalogOptions,"
			+ "a.brandOwner.brandOwnerName,"
			+ "a.upcMasterStatus.catalogId,"
			+ "a.upcMasterStatus.catalogOptions,"
			+ "a.createdAt,"
			+ "a.vcItem,"
			+ "a.coreName,"
			+ "a.recipeNumber,"
			+ "a.upcProductType.catalogOptions,"
			+ "a.productCategory.productCategoryName"
			+ ")"
			+ " FROM UpcMasterModel a"
			+ " WHERE (a.vcItem = ?6)"
			+ " AND (?1 = NULL OR a.principalUpc LIKE %?1%)"
			+ " AND (?2 = NULL OR a.productName LIKE %?2%)"
			+ " AND (?3 = NULL OR a.productType.catalogId = ?3)"
			+ " AND (?4 = NULL OR a.brandOwner.brandOwnerId = ?4)"
			+ " AND (?5 = NULL OR a.upcMasterType.catalogId = ?5)"
			+ " AND (?7 = NULL OR a.coreName LIKE %?7%)"
			+ " AND (?8 = NULL OR a.recipeNumber = ?8)"
			+ " AND (?9 = NULL OR a.upcMasterStatus.catalogId = ?9)"
			+ " AND (?10 = NULL OR a.upcProductType.catalogId = ?10)"
			+ " AND (?11 = NULL OR a.productCategory.productCategoryId = ?11)")

	Page<UpcMasterFilterDTO> findByFilters(String principalUpc,
										   String productName,
										   Long productType,
										   Long brandOwnerId,
										   Long upcMasterTypeId,
										   boolean vcItem,
										   String coreName,
										   Double recipeNumber,
										   Long upcMasterStatusId,
										   Long upcProductTypeId,
										   Long productCategoryId,
										   Pageable pageable);

	List<UpcMasterModel> findByProductNameContaining(String productName);

	@Query(value = "SELECT a FROM UpcMasterModel a"
			+ "   JOIN UpcVendorDetailsModel b ON a.upcMasterId = b.upcMaster.upcMasterId"
			+ " WHERE a.productItem.productItemId = ?1 and b.vendorMaster.vendorMasterId = ?2")
	List<UpcMasterModel> findByItemAndVendor(Long productItemId, Long vendorMasterId);

	List<UpcMasterModel> findByProductItemProductItemId(Long productItemId);

	@Query(value = "SELECT p"
			+ " FROM UpcMasterModel p "
			+ "   JOIN p.storePrices sp"
			+ "   JOIN p.upcSellingChannels sc"
			+ "   JOIN InventoryModel i ON p = i.upcMaster"
			+ "   JOIN i.storeLocation sl "
			+ "   JOIN sl.storeNumber sn"
			+ " WHERE sn.storeNumId = ?1"
			+ "   AND sl.frontSale = true"
			+ "   AND sc.channel.catalogId = ?6"
			+ "   AND sc.enabled = true"
			+ "   AND p.productGroup.displayExternally = true"
			+ "   AND sp.storeNumber = sn"
			+ "   AND (?2 = NULL OR (p.productName LIKE %?2% OR p.productDescription LIKE %?2% OR p.principalUpc LIKE %?2%))"
			+ "   AND (?3 = NULL OR p.productGroup.productGroupId = ?3)"
			+ "   AND (?4 = NULL OR p.productCategory.productCategoryId = ?4)"
			+ "   AND (?5 = NULL OR p.productSubcategory.productSubcategoryId = ?5)"
			+ "   AND p.upcMasterType.catalogId = ?7"
			+ "   AND p.productType.catalogId = ?8"
			+ "   AND p.upcMasterStatus.catalogId <> ?9"
			+ "   AND (?10 IS NULL OR p.vcItem = ?10)"
			+ " GROUP BY p"
	)
	Page<UpcMasterModel> findByStoreAndFilters(Long storeNumId,
											   String searchKey,
											   Long groupId,
											   Long categoryId,
											   Long subcategoryId,
											   Long channelId,
											   Long pimUpcMasterTypeId,
											   Long fgProductTypeId,
											   Long disableTradingStatusId,
											   Boolean vcItem,
											   Pageable pageable);

	@Query(value = "SELECT p"
			+ " FROM UpcMasterModel p "
			+ "   JOIN p.storePrices sp"
			+ "   JOIN p.upcSellingChannels sc"
			+ "   JOIN InventoryModel i ON p = i.upcMaster"
			+ "   JOIN i.storeLocation sl "
			+ "   JOIN sl.storeNumber sn"
			+ " WHERE sn.storeNumId = ?1"
			+ "   AND sl.frontSale = true"
			+ "   AND sc.channel.catalogId = ?2"
			+ "   AND sc.enabled = true"
			+ "   AND p.productGroup.displayExternally = true"
			+ "   AND sp.storeNumber = sn"
			+ "   AND (?3 = NULL OR p.principalUpc = ?3)"
			+ "   AND p.upcMasterType.catalogId = ?4"
			+ "   AND p.productType.catalogId = ?5"
			+ "   AND p.upcMasterStatus.catalogId <> ?6"
			+ " GROUP BY p"
	)
	Optional<UpcMasterModel> findProductByStore(Long storeNumId,
												Long channelId,
												String upc,
												Long pimUpcMasterTypeId,
												Long fgProductTypeId,
												Long disableTradingStatusId);

	List<UpcMasterModel> findByProductTypeCatalogIdAndUpcMasterStatusCatalogId(Long productTypeId, Long upcMasterStatusId);

	List<UpcMasterModel> findByProductTypeCatalogId(Long productTypeId);

	@Modifying
	@Query(value = "UPDATE UpcMasterModel p SET p.productGroup.productGroupId = ?2 WHERE p.productCategory.productCategoryId = ?1")
	void updateProductHierarchyByProductCategory(Long productCategoryId, Long productGroupId);

	@Modifying
	@Query(value = "UPDATE UpcMasterModel p SET p.productGroup.productGroupId = ?3, p.productCategory.productCategoryId = ?2 WHERE p.productSubcategory.productSubcategoryId = ?1")
	void updateProductHierarchyByProductSubcategory(Long productSubcategoryId, Long productCategoryId, Long productGroupId);

	@Modifying
	@Query(value = "UPDATE UpcMasterModel p SET p.productGroup.productGroupId = ?4, p.productCategory.productCategoryId = ?3, p.productSubcategory.productSubcategoryId = ?2 WHERE p.productItem.productItemId = ?1")
	void updateProductHierarchyByProductItem(Long productItemId, Long productSubcategoryId, Long productCategoryId, Long productGroupId);


	@Query(value = "SELECT"
			+ "     p as upcMaster,"
			+ "     l.storeNumber.storeNumId as storeNumId,"
			+ "     p.principalUpc as upc,"
			+ "     p.sku as sku,"
			+ "     p.productName as productName,"
			+ "     pi.productItemName as productItem,"
			+ "     pg.productGroupName as productGroup,"
			+ "     pc.productCategoryName as productCategory,"
			+ "     ps.productSubcategoryName as productSubcategory,"
			+ "     bo.brandOwnerName as brand,"
			+ "     st.catalogOptions as status,"
			+ "     p.productDescription as productDescription,"
			+ "     p.websiteImageUrl as pictureUrl,"
			+ "     ss.catalogOptions as storeSection,"
			+ "     p.productHeight as height,"
			+ "     p.productWidth as width,"
			+ "     p.productLength as depth,"
			+ "     p.contentPerUnit as contentPerUnit,"
			+ "     cpu.catalogOptions as contentPerUnitUom,"
			+ "     coo.catalogOptions as countryOfOrigin,"
			+ "     iu.catalogOptions as inventoryUnit,"
			+ "     sp.salePrice as salePrice,"
			+ "     p.taxPercentage as taxPercentage,"
			+ "     p.shelfLifeWh as shelfLifeWh,"
			+ "     p.shelfLifeShipment as shelfLifeShipment,"
			+ "     p.shelfLifeCustomer as shelfLifeCustomer,"
			+ "     p.planogramLocation as planogramLocation,"
			+ "     p.stockMin as stockMin,"
			+ "     p.expirationDateRequired as expirationDateRequired,"
			+ "     p.ageRestricted as ageRestricted,"
			+ "     p.imageTrained as imageTrained,"
			+ "     vm.vendorName as vendorName,"
			+ "     vc.email as vendorEmail,"
			+ "     sc.cost * uvd.unitsPerCase as supplierPriceCase,"
			+ "     sc.cost as supplierPriceUnit,"
			+ "     uvd.moq as vendorMoq,"
			+ "     uvd.unitsPerCase as unitsPerCase,"
			+ "     uvd.caseUpc as caseUpc,"
			+ "     uvd.caseWeight as caseWeight,"
			+ "     uvd.caseLength as caseLength,"
			+ "     uvd.caseWidth as caseHeight,"
			+ "     uvd.caseHeight as caseWidth,"
			+ "     uvd.casesPerPallet as casesPerPallet,"
			+ "     uvd.palletWeight as palletWeight,"
			+ "     uvd.palletLength as palletLength,"
			+ "     uvd.palletHeight as palletHeight,"
			+ "     uvd.palletWidth as palletWidth,"
			+ "     i.qty as inventoryOnHand"
			+ " FROM UpcMasterModel p"
			+ "   JOIN p.productItem pi"
			+ "   JOIN p.productSubcategory ps"
			+ "   JOIN p.productCategory pc"
			+ "   JOIN p.productGroup pg"
			+ "   JOIN p.brandOwner bo"
			+ "   JOIN p.contentPerUnitUom cpu"
			+ "   JOIN p.countryOfOrigin coo"
			+ "   JOIN p.mainEntryType ss"
			+ "   JOIN p.inventoryUnit iu"
			+ "   JOIN p.upcMasterStatus st"
			+ "   JOIN InventoryModel i ON i.upcMaster = p"
			+ "   LEFT JOIN UpcVendorDetailsModel uvd ON uvd.upcMaster = p"
			+ "   LEFT JOIN UpcVendorStoreCostModel sc ON sc.upcVendorDetail = uvd AND sc.storeNumber.storeNumId = ?2"
			+ "   LEFT JOIN p.storePrices sp ON sp.storeNumber.storeNumId = ?2"
			+ "   LEFT JOIN VendorMasterModel vm ON uvd.vendorMaster = vm"
			+ "   LEFT JOIN VendorContactModel vc ON vc.vendorMaster = vm AND vc.vendorType.catalogId = ?1"
			+ "   JOIN i.storeLocation l"
			+ " WHERE l.storeNumber.storeNumId = ?2"
			+ "   AND p.upcMasterType.catalogId = ?3"
			+ "   AND p.productType.catalogId = ?4"
	)
	List<IUpcCsvDumpDTO> getCsvDumpData(Long salesRepVendorType, Long storeNumberId, Long pimUpcMasterTypeId, Long fgProductTypeId);

	@Query("SELECT p FROM UpcMasterModel p WHERE p.upcMasterType.catalogId = ?1")
	List<UpcMasterModel> getDumpData(Long pimUpcMasterTypeId);
	
	@Query("SELECT new com.ust.retail.store.pim.dto.upcmaster.UpcMasterFilterDTO(u.productName, u.upcMasterId) " 
		      +"FROM UpcMasterModel u " 
		      +" WHERE "
		      +"	  (u.vcItem = true) " 
		      +"  AND (?1 IS NULL OR u.productName LIKE %?1%) " 
		      +"  AND (u.foodOption IS NULL)")
	List<UpcMasterFilterDTO> loadProducts(String searchKey, Pageable pageable);

	@Query("SELECT a "
		  +"FROM UpcMasterModel a "
		  + "LEFT JOIN a.upcSellingChannels usc ON usc.upcMaster = a "
		  +" WHERE " 
		  +"     (a.productCategory.productCategoryId = ?1) "
		  +" AND (a.upcProductType.catalogId = ?2) "
		  + "AND (a.upcMasterStatus.catalogId != ?3) "
		  + "AND (usc.channel.catalogId =?4 AND usc.enabled = true)"
		  +" AND (COALESCE(null, ?5) IS NULL OR a.upcMasterId NOT IN (?5))")
	List<UpcMasterModel> findByCategoryIdAndNotInProducts(Long categoryId, Long upcProductTypeId, Long discountinueStatus, Long vcUpcSellingChannel, List<Long> productIds);
	
	@Query("SELECT a "
			  +"FROM UpcMasterModel a "
			  + "LEFT JOIN a.upcSellingChannels usc ON usc.upcMaster = a "
			  +" WHERE " 
			  +"(a.upcProductType.catalogId != ?1) "
			  + "AND (a.upcMasterStatus.catalogId != ?2)"
			  + "AND (a.upcProductType.catalogId != ?4)"
			  + "AND (usc.channel.catalogId =?3 AND usc.enabled = true)")
	List<UpcMasterModel> findByUpcProductTypeCatalogIdNot(Long upcProductTypeId, Long discountinueStatus, Long vcUpcSellingChannel,Long modifyType);

	@Query("SELECT new com.ust.retail.store.pim.dto.upcmaster.DishJsonFileDTO(" +
			"p.upcMasterId, " +
			"p.productName," +
			"pc.productCategoryId," +
			"pc.productCategoryName," +
			"p.alias," +
			"p.servedWith," +
			"p.principalUpc," +
			"p.packageColor," +
			"p.upcProductType.catalogOptions) " +
			"FROM UpcMasterModel p " +
			"join ProductCategoryModel pc on  pc.productCategoryId = p.productCategory " +
			"Join UpcMasterSellingChannelModel sc on sc.upcMaster = p.upcMasterId " +
			"Where sc.channel = 40002 And sc.enabled = true")
	List<DishJsonFileDTO> getVerifiedMenu();

	@Query("SELECT a FROM UpcMasterModel a")
	List<UpcMasterModel> getUpcForVcCsvData();


}
