package com.ust.retail.store.bistro.controller.recipe;

import com.ust.retail.store.bistro.commons.catalogs.DrinkSizeFlavourDiaryStatusCatalog;
import com.ust.retail.store.bistro.dto.recipes.*;
import com.ust.retail.store.bistro.service.recipes.DrinkDiaryService;
import com.ust.retail.store.bistro.service.recipes.DrinkFlavourService;
import com.ust.retail.store.bistro.service.recipes.DrinkSizeService;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/bistro/p/recipe/p/drink/p/catalog/p")
@Validated
@AllArgsConstructor
public class RecipeDrinkCatalogsController {

    private final DrinkSizeService drinkSizeService;
    private final DrinkSizeFlavourDiaryStatusCatalog drinkSizeFlavourDiaryStatusCatalog;
    private final DrinkDiaryService drinkDiaryService;
    private final DrinkFlavourService drinkFlavourService;

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @GetMapping("/status")
    public List<CatalogDTO> getStatusCatalog() {
        return drinkSizeFlavourDiaryStatusCatalog.getCatalogOptions();
    }

    // drink size catalog
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PostMapping("/size")
    @Validated(OnCreate.class)
    public DrinkSizeDTO createSize(@Valid @RequestBody DrinkSizeRequestDTO drinkSizeRequest) {
        return drinkSizeService.create(drinkSizeRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PutMapping("/size")
    @Validated(OnUpdate.class)
    public DrinkSizeDTO updateSize(@Valid @RequestBody DrinkSizeRequestDTO drinkSizeRequest) {
        return drinkSizeService.update(drinkSizeRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @GetMapping("/size/{drinkSizeId}")
    public DrinkSizeDTO findSize(@Valid @PathVariable(value = "drinkSizeId") Long drinkSizeId) {
        return drinkSizeService.findById(drinkSizeId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @DeleteMapping("/size/{drinkSizeId}")
    public void deleteSize(@Valid @PathVariable(value = "drinkSizeId") Long drinkSizeId) {
        drinkSizeService.delete(drinkSizeId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PostMapping("/size/filter")
    @Validated(OnFilter.class)
    public Page<DrinkSizeDTO> filterSize(@Valid @RequestBody DrinkSizeRequestFilterDTO requestFilterDTO) {
        return drinkSizeService.findByFilters(requestFilterDTO);
    }

    // drink diary catalog
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PostMapping("/diary")
    @Validated(OnCreate.class)
    public DrinkDiaryDTO createDiary(@Valid @RequestBody DrinkDiaryRequestDTO diaryRequestDTO) {
        return drinkDiaryService.create(diaryRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PutMapping("/diary")
    @Validated(OnUpdate.class)
    public DrinkDiaryDTO updateDiary(@Valid @RequestBody DrinkDiaryRequestDTO diaryRequestDTO) {
        return drinkDiaryService.update(diaryRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @GetMapping("/diary/{drinkDiaryId}")
    public DrinkDiaryDTO findDiary(@Valid @PathVariable(value = "drinkDiaryId") Long drinkDiaryId) {
        return drinkDiaryService.findById(drinkDiaryId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @DeleteMapping("/diary/{drinkDiaryId}")
    public void deleteDiary(@Valid @PathVariable(value = "drinkDiaryId") Long drinkDiaryId) {
        drinkDiaryService.deleteById(drinkDiaryId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PostMapping("/diary/filter")
    @Validated(OnFilter.class)
    public Page<DrinkDiaryDTO> filterDiary(@Valid @RequestBody DrinkDiaryRequestFilterDTO requestFilterDTO) {
        return drinkDiaryService.findByFilters(requestFilterDTO);
    }

    // drink flavour catalog
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PostMapping("/flavour")
    @Validated(OnCreate.class)
    public DrinkFlavourDTO createFlavour(@Valid @RequestBody DrinkFlavourRequestDTO drinkFlavourRequestDTO) {
        return drinkFlavourService.create(drinkFlavourRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PutMapping("/flavour")
    @Validated(OnUpdate.class)
    public DrinkFlavourDTO updateFlavour(@Valid @RequestBody DrinkFlavourRequestDTO diaryRequestDTO) {
        return drinkFlavourService.update(diaryRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @GetMapping("/flavour/{drinkFlavourId}")
    public DrinkFlavourDTO findFlavour(@Valid @PathVariable(value = "drinkFlavourId") Long drinkFlavourId) {
        return drinkFlavourService.findById(drinkFlavourId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @DeleteMapping("/flavour/{drinkFlavourId}")
    public void deleteFlavour(@Valid @PathVariable(value = "drinkFlavourId") Long drinkFlavourId) {
        drinkFlavourService.deleteById(drinkFlavourId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_BISTRO_MANAGER')")
    @PostMapping("/flavour/filter")
    @Validated(OnFilter.class)
    public Page<DrinkFlavourDTO> filterFlavour(@Valid @RequestBody DrinkFlavourFilterRequestDTO filterRequestDTO) {
        return drinkFlavourService.findByFilters(filterRequestDTO);
    }
}
