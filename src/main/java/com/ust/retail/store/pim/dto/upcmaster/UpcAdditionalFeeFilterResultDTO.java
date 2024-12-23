package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.model.upcmaster.UpcAdditionalFeeModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UpcAdditionalFeeFilterResultDTO {
    private Long upcAdditionalFeeId;
    private String storeName;
    private String principalUpc;
    private String productName;
    private String additionalFee;
    private Double price;
    private Date createdAt;

    public UpcAdditionalFeeFilterResultDTO modelToDTO(UpcAdditionalFeeModel model) {
        this.upcAdditionalFeeId = model.getUpcAdditionalFeeId();
        this.storeName = model.getStoreNumber().getStoreName();
        this.principalUpc = model.getUpcMaster().getPrincipalUpc();
        this.productName = model.getUpcMaster().getProductName();
        this.additionalFee = model.getAdditionalFee().getFeeName();
        this.price = model.getPrice();
        this.createdAt = model.getCreatedAt();

        return this;
    }
}
