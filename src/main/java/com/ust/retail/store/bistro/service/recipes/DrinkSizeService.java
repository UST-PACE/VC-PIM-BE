package com.ust.retail.store.bistro.service.recipes;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.bistro.commons.catalogs.DrinkSizeFlavourDiaryStatusCatalog;
import com.ust.retail.store.bistro.dto.recipes.DrinkSizeDTO;
import com.ust.retail.store.bistro.dto.recipes.DrinkSizeRequestDTO;
import com.ust.retail.store.bistro.dto.recipes.DrinkSizeRequestFilterDTO;
import com.ust.retail.store.bistro.exception.InvalidStatusDrinkCatalogException;
import com.ust.retail.store.bistro.model.recipes.DrinkSizeModel;
import com.ust.retail.store.bistro.repository.recipes.DrinkConfMasterRepository;
import com.ust.retail.store.bistro.repository.recipes.DrinkSizeRepository;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.CatalogModel;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class DrinkSizeService {

    private final DrinkSizeRepository drinkSizeRepository;
    private final DrinkConfMasterRepository drinkConfMasterRepository;

    private final EntityManager entityManager;

    public DrinkSizeDTO create(DrinkSizeRequestDTO drinkSizeRequest) {

        DrinkSizeModel newDrinkSize = DrinkSizeModel.builder()
                .sizeName(drinkSizeRequest.getSizeName())
                .ounces(drinkSizeRequest.getOunces())
                .status(new CatalogModel(drinkSizeRequest.getStatusId()))
                .build();

        validStatus(drinkSizeRequest.getStatusId());

        return  modelToDTO(drinkSizeRepository.save(newDrinkSize));
    }

    public DrinkSizeDTO update(DrinkSizeRequestDTO drinkSizeRequest) {

        DrinkSizeModel currentDrinkSize = drinkSizeRepository.findById(drinkSizeRequest.getDrinkSizeId())
                .orElseThrow(() -> new ResourceNotFoundException("DrinkSize", "id", drinkSizeRequest.getDrinkSizeId()));

        validStatus(drinkSizeRequest.getStatusId());

        return  modelToDTO(
                drinkSizeRepository.save(currentDrinkSize.update(drinkSizeRequest))
        );
    }

    public void delete(Long drinkSizeId) {

        DrinkSizeModel currentDrinkSize = drinkSizeRepository.findById(drinkSizeId)
                .orElseThrow(() -> new ResourceNotFoundException("DrinkSize", "id", drinkSizeId));

        drinkConfMasterRepository.deleteSizeDetailConf(drinkSizeId);
        drinkSizeRepository.delete(currentDrinkSize);
    }

    @Transactional(readOnly = true)
    public DrinkSizeDTO findById(Long drinkSizeId) {

        DrinkSizeModel currentDrinkSize = drinkSizeRepository.findById(drinkSizeId)
                .orElseThrow(() -> new ResourceNotFoundException("DrinkSize", "id", drinkSizeId));

        return modelToDTO(currentDrinkSize);
    }

    @Transactional(readOnly = true)
    public Page<DrinkSizeDTO> findByFilters(DrinkSizeRequestFilterDTO requestFilterDTO) {

        return drinkSizeRepository.findByFilters(
                requestFilterDTO.getSizeName(),
                requestFilterDTO.getStatusId(),
                requestFilterDTO.createPageable()
        ).map(this::modelToDTO);
    }


    private DrinkSizeDTO modelToDTO(DrinkSizeModel drinkSize) {

        entityManager.flush();
        entityManager.refresh(drinkSize);

        return DrinkSizeDTO.builder()
                .drinkSizeId(drinkSize.getDrinkSizeId())
                .sizeName(drinkSize.getSizeName())
                .ounces(drinkSize.getOunces())
                .statusId(drinkSize.getStatus().getCatalogId())
                .statusDesc(drinkSize.getStatus().getCatalogOptions())
                .build();
    }

    private void validStatus(Long statusId) {

        if(!statusId.equals(DrinkSizeFlavourDiaryStatusCatalog.ENABLED) &&
                !statusId.equals(DrinkSizeFlavourDiaryStatusCatalog.DISABLED)) {
            throw new InvalidStatusDrinkCatalogException(statusId);
        }
    }

}
