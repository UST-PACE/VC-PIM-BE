package com.ust.retail.store.bistro.dto.recipes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDrinkConfDTO {


    @NotNull(message = "recipeId is Mandatory.", groups = { OnUpdate.class })
    private Long recipeId;

    private Long upcMasterId;

    @NotNull(message = "enabledDrinkConf is Mandatory.", groups = { OnUpdate.class })
    private Boolean enabledDrinkConf;

    @NotNull(message = "storeNumberId is Mandatory.", groups = { OnUpdate.class })
    private Long storeNumberId;

    private List<DrinkSize> sizes;

    private List<DrinkFlavour> flavours;

    private List<DrinkDiary> diaries;

    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DrinkSize {
        private Long drinkConfMasterDetailSizeId;

        @NotNull(message = "price is Mandatory.", groups = { OnUpdate.class })
        private Double price;

        @NotNull(message = "drinkSizeId is Mandatory.", groups = { OnUpdate.class })
        private Long drinkSizeId;

        private String sizeName;

        @NotNull(message = "isDefault is Mandatory.", groups = { OnUpdate.class })
        private Boolean isDefault;

        @NotNull(message = "hasEnabled is Mandatory.", groups = { OnUpdate.class })
        private Boolean hasEnabled;
    }

    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DrinkFlavour {

        private Long drinkConfMasterDetailFlavourId;

        @NotNull(message = "drinkFlavourId is Mandatory.", groups = { OnUpdate.class })
        private Long drinkFlavourId;

        private String flavourName;

        @NotNull(message = "price is Mandatory.", groups = { OnUpdate.class })
        private Double price;

        @NotNull(message = "hasEnabled is Mandatory.", groups = { OnUpdate.class })
        private Boolean hasEnabled;
    }


    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DrinkDiary{

        private Long drinkConfMasterDetailDiaryId;

        @NotNull(message = "drinkDiaryId is Mandatory.", groups = { OnUpdate.class })
        private Long drinkDiaryId;

        private String diaryName;

        @NotNull(message = "price is Mandatory.", groups = { OnUpdate.class })
        private Double price;

        @NotNull(message = "hasEnabled is Mandatory.", groups = { OnUpdate.class })
        private Boolean hasEnabled;

        @NotNull(message = "isDefault is Mandatory.", groups = { OnUpdate.class })
        private Boolean isDefault;

    }

}
