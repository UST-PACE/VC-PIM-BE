package com.ust.retail.store.pim.model.upcmaster;

import com.ust.retail.store.pim.dto.upcmaster.UpcAliasVerifyDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "upc_alias_verify")
@Getter
@NoArgsConstructor
public class UpcAliasVerifyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alias_id")
    private Long aliasId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id")
    private UpcMasterModel upcMaster;

    @Column(name = "result_t1")
    private Boolean resultT1;

    @Column(name = "result_t2")
    private Boolean resultT2;

    @Column(name = "result_t3")
    private Boolean resultT3;

    @Column(name = "result_t4")
    private Boolean resultT4;

    @Column(name = "test_results")
    private String testResults;

    @Column(nullable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public UpcAliasVerifyModel(Long aliasId, Long upcMasterId, Boolean resultT1, Boolean resultT2, Boolean resultT3, Boolean resultT4, String testResults, Date updatedAt) {
        this.aliasId = aliasId;
        this.upcMaster = new UpcMasterModel(upcMasterId);
        this.resultT1 = resultT1;
        this.resultT2 = resultT2;
        this.resultT3 = resultT3;
        this.resultT4 = resultT4;
        this.testResults = testResults;
        this.updatedAt = updatedAt;
    }
    public void updateUpcAlias(UpcAliasVerifyDTO dto){
        this.resultT1 =dto.getResultT1();
        this.resultT2 =dto.getResultT2();
        this.resultT3 =dto.getResultT3();
        this.resultT4 =dto.getResultT4();
        this.testResults =dto.getTestResults();
        this.updatedAt =dto.getUpdateAt();
    }
}
