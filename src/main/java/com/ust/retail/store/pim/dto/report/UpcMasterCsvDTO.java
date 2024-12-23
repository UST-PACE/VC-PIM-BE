package com.ust.retail.store.pim.dto.report;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UpcMasterCsvDTO {

    private Long upcMasterId;
    private String principalUpc;
    private Double recipeNumber;
    private String productName;
    private String servedWith;
    private String productType;
    private String packageColor;
    private String coreName;
    private Integer portionPerServing;
    private String entryType;
    private Double calories;
    private Double kCalories;
    private String flagToDisplay;
    private String category;
    private String subCategory;
    private Date creationDate;
    private Date lastUpdateDate;

    public UpcMasterCsvDTO(Long upcMasterId, String principalUpc,
                           Double recipeNumber, String productName,
                           String servedWith, String productType,
                           String packageColor, String coreName,
                           Integer portionPerServing, String entryType,
                           Double calories, Double kCalories,
                           String flagToDisplay, String category,
                           String subCategory, Date creationDate,
                           Date lastUpdateDate) {

        this.upcMasterId = upcMasterId;
        this.principalUpc = principalUpc;
        this.recipeNumber = recipeNumber;
        this.productName = productName;
        this.servedWith = servedWith;
        this.productType = productType;
        this.packageColor = packageColor;
        this.coreName = coreName;
        this.portionPerServing = portionPerServing;
        this.entryType = entryType;
        this.calories = calories;
        this.kCalories = kCalories;
        this.flagToDisplay = flagToDisplay;
        this.category = category;
        this.subCategory = subCategory;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
    }
}
