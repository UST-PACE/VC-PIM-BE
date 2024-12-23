package com.ust.retail.store.bistro.dto.recipes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrinkDiaryRequestDTO {
    @NotNull(message = "drinkDiaryId is mandatory.", groups = { OnUpdate.class })
    private Long drinkDiaryId;

    @NotNull(message = "diaryName is mandatory.", groups = { OnCreate.class, OnUpdate.class })
    @Size(min = 3, max = 35, message = "diaryName must have between 3 and 35 characters", groups = { OnCreate.class, OnUpdate.class })
    private String diaryName;

    @NotNull(message = "statusId is mandatory.", groups = { OnCreate.class, OnUpdate.class })
    private Long statusId;
}
