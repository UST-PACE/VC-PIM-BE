package com.ust.retail.store.bistro.service.recipes;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.bistro.commons.catalogs.DrinkSizeFlavourDiaryStatusCatalog;
import com.ust.retail.store.bistro.dto.recipes.DrinkFlavourDTO;
import com.ust.retail.store.bistro.dto.recipes.DrinkFlavourFilterRequestDTO;
import com.ust.retail.store.bistro.dto.recipes.DrinkFlavourRequestDTO;
import com.ust.retail.store.bistro.exception.InvalidStatusDrinkCatalogException;
import com.ust.retail.store.bistro.model.recipes.DrinkFlavourModel;
import com.ust.retail.store.bistro.repository.recipes.DrinkConfMasterRepository;
import com.ust.retail.store.bistro.repository.recipes.DrinkFlavourRepository;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.CatalogModel;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class DrinkFlavourService {

    private final DrinkFlavourRepository drinkFlavourRepository;
    private final DrinkConfMasterRepository drinkConfMasterRepository;
    private final EntityManager entityManager;

    public DrinkFlavourDTO create(DrinkFlavourRequestDTO drinkFlavourRequestDTO) {

        DrinkFlavourModel newDrinkFlavour = DrinkFlavourModel.builder()
                .flavourName(drinkFlavourRequestDTO.getFlavourName())
                .status(new CatalogModel(drinkFlavourRequestDTO.getStatusId()))
                .build();

        validStatus(drinkFlavourRequestDTO.getStatusId());

        return modelToDTO(
                drinkFlavourRepository.save(newDrinkFlavour)
        );
    }


    public DrinkFlavourDTO update(DrinkFlavourRequestDTO drinkFlavourRequestDTO) {

        DrinkFlavourModel currentDrinkFlavour = drinkFlavourRepository.findById(drinkFlavourRequestDTO.getDrinkFlavourId())
                .orElseThrow(() -> new ResourceNotFoundException("DrinkFlavour", "id", drinkFlavourRequestDTO.getDrinkFlavourId()));

        validStatus(drinkFlavourRequestDTO.getStatusId());

        return modelToDTO(
                drinkFlavourRepository.save(currentDrinkFlavour.update(drinkFlavourRequestDTO))
        );
    }

    @Transactional(readOnly = true)
    public DrinkFlavourDTO findById(Long drinkFlavourId) {

        DrinkFlavourModel currentDrinkFlavour = drinkFlavourRepository.findById(drinkFlavourId)
                .orElseThrow(() -> new ResourceNotFoundException("DrinkFlavour", "id", drinkFlavourId));

        return modelToDTO(currentDrinkFlavour);
    }


    public void deleteById(Long drinkFlavourId) {

        DrinkFlavourModel currentDrinkFlavour = drinkFlavourRepository.findById(drinkFlavourId)
                .orElseThrow(() -> new ResourceNotFoundException("DrinkFlavour", "id", drinkFlavourId));

        drinkConfMasterRepository.deleteFlavourDetailConf(drinkFlavourId);
        drinkFlavourRepository.delete(currentDrinkFlavour);
    }

    @Transactional(readOnly = true)
    public Page<DrinkFlavourDTO> findByFilters(DrinkFlavourFilterRequestDTO filterRequestDTO) {
        return this.drinkFlavourRepository.findByFilters(
                filterRequestDTO.getFlavourName(),
                filterRequestDTO.getStatusId(),
                filterRequestDTO.createPageable()
        ).map(this::modelToDTO);
    }

    private DrinkFlavourDTO modelToDTO(DrinkFlavourModel drinkFlavour) {

        entityManager.flush();
        entityManager.refresh(drinkFlavour);

        return DrinkFlavourDTO.builder()
                .drinkFlavourId(drinkFlavour.getDrinkFlavourId())
                .flavourName(drinkFlavour.getFlavourName())
                .statusId(drinkFlavour.getStatus().getCatalogId())
                .statusDesc(drinkFlavour.getStatus().getCatalogOptions())
                .build();
    }

    private void validStatus(Long statusId) {

        if(!statusId.equals(DrinkSizeFlavourDiaryStatusCatalog.ENABLED) &&
                !statusId.equals(DrinkSizeFlavourDiaryStatusCatalog.DISABLED)) {
            throw new InvalidStatusDrinkCatalogException(statusId);
        }
    }
}
