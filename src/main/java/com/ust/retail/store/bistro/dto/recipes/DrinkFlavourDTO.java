package com.ust.retail.store.bistro.dto.recipes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class DrinkFlavourDTO {

    private Long drinkFlavourId;
    private String flavourName;
    private Long statusId;
    private String statusDesc;

}
