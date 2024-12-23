package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UpcMasterAliasFilterDTO extends BaseFilterDTO {
    private Long aliasId;

    private Long upcMasterId;

    private String productName;

    private String principalUpc;

    private String productCategoryName;

    private String upcProductType;

    private Date updatedAt;

    private String productImage1;

    private String productImage2;

    private String productImage3;

    private String productImage4;

    private String packageColor;

    private String upcAlias;

    private String testResults;

    private Long productCategoryId;

    private Long upcProductTypeId;

    public UpcMasterAliasFilterDTO(Long aliasId,
                                   Long upcMasterId,
                                   String productName,
                                   String principalUpc,
                                   String productCategoryName,
                                   String upcProductType,
                                   Date updatedAt,
                                   String productImage1,
                                   String productImage2,
                                   String productImage3,
                                   String productImage4,
                                   String packageColor,
                                   String upcAlias,
                                   String testResults) {
        this.aliasId=aliasId;
        this.upcMasterId = upcMasterId;
        this.productName = productName;
        this.principalUpc = principalUpc;
        this.productCategoryName = productCategoryName;
        this.upcProductType = upcProductType;
        this.updatedAt = updatedAt;
        this.productImage1 = productImage1;
        this.productImage2 = productImage2;
        this.productImage3 = productImage3;
        this.productImage4 = productImage4;
        this.packageColor = packageColor;
        this.upcAlias = upcAlias;
        this.testResults =testResults;

    }
}
