package com.ust.retail.store.pim.repository.upcmaster;

import com.ust.retail.store.pim.dto.upcmaster.ListUpcAliasDTO;
import com.ust.retail.store.pim.dto.upcmaster.IUpcMasterAliasDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcAliasVerifyModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UpcAliasVerifyRepository extends JpaRepository<UpcAliasVerifyModel,Long > {

    String UPCMASTER_AND_ALIAS_EXECUTE_QUERY =
            "SELECT "
                + "b.alias_id as aliasId,"
                + "a.upc_master_id as upcMasterId,"
                + "a.product_name as productName,"
                + "a.principal_upc as principalUpc,"
                + "a.product_category_id as productCategoryId,"
                + "c.product_category_name as productCategoryName,"
                + "a.upc_product_type_id as upcProductTypeId,"
                + "d.catalog_options as upcProductType,"
                + "b.updated_at as updatedAt,"
                + "a.product_image_1 as productImage1,"
                + "a.product_image_2 as productImage2,"
                + "a.product_image_3 as productImage3,"
                + "a.product_image_4 as productImage4,"
                + "a.package_color as packageColor,"
                + "a.alias as upcAlias,"
                + "b.test_results as testResults "
            + "FROM upc_master a "
            + "LEFT JOIN upc_alias_verify b ON a.upc_master_id = b.upc_master_id "
            + "JOIN product_categories c on a.product_category_id =c.product_category_id "
            + "JOIN catalogs d on a.upc_product_type_id = d.catalog_id "
            + "WHERE (?3 is NULL OR a.product_category_id = ?3) "
            + "AND (?4 is NULL OR a.upc_product_type_id = ?4) "
            + "UNION "
            + "SELECT "
                + "b.alias_id as aliasId,"
                + "a.upc_master_id as upcMasterId,"
                + "a.product_name as productName,"
                + "a.principal_upc as principalUpc,"
                + "a.product_category_id as productCategoryId,"
                + "c.product_category_name as productCategoryName,"
                + "a.upc_product_type_id as upcProductTypeId,"
                + "d.catalog_options as upcProductType,"
                + "b.updated_at as updatedAt,"
                + "a.product_image_1 as productImage1,"
                + "a.product_image_2 as productImage2,"
                + "a.product_image_3 as productImage3,"
                + "a.product_image_4 as productImage4,"
                + "a.package_color as packageColor,"
                + "a.alias as upcAlias,"
                + "b.test_results as testResults "
            + "FROM upc_master a "
            + "RIGHT JOIN upc_alias_verify b ON a.upc_master_id = b.upc_master_id "
            + "JOIN product_categories c on a.product_category_id =c.product_category_id "
            + "JOIN catalogs d on a.upc_product_type_id = d.catalog_id "
            + "WHERE (?3 is NULL OR a.product_category_id = ?3) "
            + "AND (?4 is NULL OR a.upc_product_type_id = ?4)";


    @Query(value = "SELECT * FROM (" + UPCMASTER_AND_ALIAS_EXECUTE_QUERY + ") a WHERE (?1 is NULL OR principalUpc LIKE %?1%) And (?2 is NULL OR productName LIKE %?2%)",
            countQuery = "SELECT COUNT(*) FROM (" + UPCMASTER_AND_ALIAS_EXECUTE_QUERY + ") a WHERE (?1 is NULL OR principalUpc LIKE %?1%) And (?2 is NULL OR productName LIKE %?2%)",
            nativeQuery = true)
    Page<ListUpcAliasDTO> getProductsByFilters(String principalUpc,
                                               String productName,
                                               Long productCategoryId,
                                               Long upcProductTypeId,
                                               Pageable pageable);

    @Query(value = "SELECT "
            + "a.upc_master_id as upcMasterId,"
            + "b.alias_id as aliasId,"
            + "a.alias as alias,"
            + "a.principal_upc as principalUpc,"
            + "a.product_name as productName,"
            + "c.product_category_name as productCategoryName,"
            + "a.product_image_1 as productImage1,"
            + "a.product_image_2 as productImage2,"
            + "a.product_image_3 as productImage3,"
            + "a.product_image_4 as productImage4,"
            + "b.test_results as testResults,"
            + "b.updated_at as updatedAt,"
            + "a.package_color as packageColor,"
            + "a.upc_product_type_id as upcProductTypeId,"
            + "d.catalog_options as upcProductType "
            + "FROM upc_master a "
            + "LEFT JOIN upc_alias_verify b ON a.upc_master_id = b.upc_master_id "
            + "JOIN product_categories c on a.product_category_id =c.product_category_id "
            + "JOIN catalogs d on a.upc_product_type_id = d.catalog_id "
            + "WHERE a.upc_master_id = ?1 "
            + "UNION "
            + "SELECT "
            + "a.upc_master_id as upcMasterId,"
            + "b.alias_id as aliasId,"
            + "a.alias as alias,"
            + "a.principal_upc as principalUpc,"
            + "a.product_name as productName,"
            + "c.product_category_name as productCategoryName,"
            + "a.product_image_1 as productImage1,"
            + "a.product_image_2 as productImage2,"
            + "a.product_image_3 as productImage3,"
            + "a.product_image_4 as productImage4,"
            + "b.test_results as testResults,"
            + "b.updated_at as updatedAt,"
            + "a.package_color as packageColor,"
            + "a.upc_product_type_id as upcProductTypeId,"
            + "d.catalog_options as upcProductType "
            + "FROM upc_master a "
            + "RIGHT JOIN upc_alias_verify b ON a.upc_master_id = b.upc_master_id "
            + "JOIN product_categories c on a.product_category_id =c.product_category_id "
            + "JOIN catalogs d on a.upc_product_type_id = d.catalog_id "
            + "WHERE a.upc_master_id = ?1 ",
            nativeQuery = true)
    IUpcMasterAliasDTO findByUpcMasterId(Long upcMasterId);



}
