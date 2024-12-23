package com.ust.retail.store.pim.dto.upcmaster;

import java.util.Date;


public interface IUpcMasterAliasDTO {

    Long getUpcMasterId();

    Long getAliasId();

    String getAlias();

    String getPrincipalUpc();

    String getProductName();

    String getProductCategoryName();

    String getProductImage1();

    String getProductImage2();

    String getProductImage3();

    String getProductImage4();

    String getTestResults();

    Date getUpdatedAt();

    String getPackageColor();

    Long getUpcProductTypeId();

    String getUpcProductType();



}
