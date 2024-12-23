package com.ust.retail.store.pim.dto.upcmaster;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UpcMasterAliasDTO {
    
    private Long upcMasterId;

    private Long aliasId;

    private String alias;

    private String aliasDisplay;

    private String principalUpc;

    private String productName;

    private String productCategoryName;

    private String productImage1;

    private String productImage2;

    private String productImage3;

    private String productImage4;

    private String testResults;

    private Date updateAt;

    private String packageColor;

    private Long  upcProductTypeId;

    public UpcMasterAliasDTO(Long upcMasterId, Long aliasId, String alias, String aliasDisplay,
                             String principalUpc, String productName,
                             String productCategoryName, String productImage1,
                             String productImage2, String productImage3,
                             String productImage4, String testResults, Date updateAt,
                             String packageColor, Long upcProductTypeId) {
        this.upcMasterId = upcMasterId;
        this.aliasId = aliasId;
        this.alias = alias;
        this.aliasDisplay = aliasDisplay;
        this.principalUpc = principalUpc;
        this.productName = productName;
        this.productCategoryName = productCategoryName;
        this.productImage1 = productImage1;
        this.productImage2 = productImage2;
        this.productImage3 = productImage3;
        this.productImage4 = productImage4;
        this.testResults = testResults;
        this.updateAt = updateAt;
        this.packageColor = packageColor;
        this.upcProductTypeId = upcProductTypeId;
    }
}
