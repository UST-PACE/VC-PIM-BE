package com.ust.retail.store.bistro.dto.recipes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.bistro.dto.recipes.RecipeDrinkConfDTO.DrinkDiary;
import com.ust.retail.store.bistro.dto.recipes.RecipeDrinkConfDTO.DrinkFlavour;
import com.ust.retail.store.bistro.dto.recipes.RecipeDrinkConfDTO.DrinkSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrinksConfExternalApiDTO {

    private Boolean enabledDrinkConf;

    private List<DrinkSize> sizes;

    private List<DrinkFlavour> flavours;

    private List<DrinkDiary> diaries;
    
}