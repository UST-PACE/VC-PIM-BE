package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.model.upcmaster.UpcAliasVerifyModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class UpcAliasVerifyDTO {
    private Long aliasId;

    private Long upcMasterId;

    private Boolean resultT1;

    private Boolean resultT2;

    private Boolean resultT3;

    private Boolean resultT4;

    private String testResults;

    private Date updateAt;

    private String productsGeni;


    public UpcAliasVerifyDTO(Long aliasId, Long upcMasterId, Boolean resultT1,
                             Boolean resultT2, Boolean resultT3, Boolean resultT4,
                             String testResults,String productsGeni ,Date updateAt) {
        this.aliasId = aliasId;
        this.upcMasterId = upcMasterId;
        this.resultT1 = resultT1;
        this.resultT2 = resultT2;
        this.resultT3 = resultT3;
        this.resultT4 = resultT4;
        this.testResults = testResults;
        this.productsGeni =productsGeni;
        this.updateAt = updateAt;
    }

    public UpcAliasVerifyDTO parseToDTO(UpcAliasVerifyModel upcAliasVerifyModel) {
        this.aliasId = upcAliasVerifyModel.getAliasId();
        this.upcMasterId = upcAliasVerifyModel.getUpcMaster().getUpcMasterId();
        this.resultT1 = upcAliasVerifyModel.getResultT1();
        this.resultT2 = upcAliasVerifyModel.getResultT2();
        this.resultT3 = upcAliasVerifyModel.getResultT3();
        this.resultT4 = upcAliasVerifyModel.getResultT4();
        this.testResults = upcAliasVerifyModel.getTestResults();
        this.updateAt = upcAliasVerifyModel.getUpdatedAt();
        return this;
    }

    public UpcAliasVerifyModel parseToModel() {
        return new UpcAliasVerifyModel(
                this.getAliasId(),
                this.getUpcMasterId(),
                this.resultT1,
                this.resultT2,
                this.resultT3,
                this.resultT4,
                this.testResults,
                this.updateAt
        );
    }


}
