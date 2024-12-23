package com.ust.retail.store.pim.dto.upcmaster;

import java.util.Date;

public interface ListUpcAliasDTO {
      Long getAliasId();

      Long getUpcMasterId();

      String getProductName();

      String getPrincipalUpc();

      String getProductCategoryName();

      String getUpcProductType();

      Date getUpdatedAt();

      String getProductImage1();

      String getProductImage2();

      String getProductImage3();

      String getProductImage4();

      String getPackageColor();

      String getUpcAlias();

      String getTestResults();

      Long getProductCategoryId();

      Long getUpcProductTypeId();

}
