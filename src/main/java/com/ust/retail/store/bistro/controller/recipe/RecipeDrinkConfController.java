package com.ust.retail.store.bistro.controller.recipe;

import com.ust.retail.store.bistro.dto.recipes.RecipeDrinkConfDTO;

import com.ust.retail.store.bistro.service.recipes.RecipeDrinkConfService;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/bistro/p/recipe/p/drink/p/")
@Validated
@AllArgsConstructor
public class RecipeDrinkConfController {

    private final RecipeDrinkConfService recipeDrinkConfService;

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PutMapping("/update")
    @Validated(OnUpdate.class)
    public void updateConf(@Valid @RequestBody RecipeDrinkConfDTO drinkConfRequestDTO) {

        recipeDrinkConfService.updateDrinkConf(drinkConfRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @GetMapping("/find/sn/{storeNumberId}/recipe/{recipeId}")
    public RecipeDrinkConfDTO findConf(@Valid @PathVariable(value = "storeNumberId") Long storeNumberId,
                                       @Valid @PathVariable(value = "recipeId") Long recipeId) {

        return recipeDrinkConfService.loadInfoRecipeDrinkConf(recipeId, storeNumberId);
    }
}
