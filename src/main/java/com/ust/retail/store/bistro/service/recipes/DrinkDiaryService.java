package com.ust.retail.store.bistro.service.recipes;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.bistro.commons.catalogs.DrinkSizeFlavourDiaryStatusCatalog;
import com.ust.retail.store.bistro.dto.recipes.DrinkDiaryDTO;
import com.ust.retail.store.bistro.dto.recipes.DrinkDiaryRequestDTO;
import com.ust.retail.store.bistro.dto.recipes.DrinkDiaryRequestFilterDTO;
import com.ust.retail.store.bistro.exception.InvalidStatusDrinkCatalogException;
import com.ust.retail.store.bistro.model.recipes.DrinkDiaryModel;
import com.ust.retail.store.bistro.repository.recipes.DrinkConfMasterRepository;
import com.ust.retail.store.bistro.repository.recipes.DrinkDiaryRepository;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.CatalogModel;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class DrinkDiaryService {

    private final EntityManager entityManager;
    private final DrinkDiaryRepository drinkDiaryRepository;
    private final DrinkConfMasterRepository drinkConfMasterRepository;

    public DrinkDiaryDTO create(DrinkDiaryRequestDTO diaryRequestDTO) {
        DrinkDiaryModel newDrinkDiary = DrinkDiaryModel.builder()
                .diaryName(diaryRequestDTO.getDiaryName())
                .status(new CatalogModel(diaryRequestDTO.getStatusId()))
                .build();

        validStatus(diaryRequestDTO.getStatusId());

        return modelToDTO(
                drinkDiaryRepository.save(newDrinkDiary)
        );
    }

    public DrinkDiaryDTO update(DrinkDiaryRequestDTO diaryRequestDTO) {

        DrinkDiaryModel currentDrinkDiary = drinkDiaryRepository.findById(diaryRequestDTO.getDrinkDiaryId())
                .orElseThrow(() -> new ResourceNotFoundException("DrinkDiary","id", diaryRequestDTO.getDrinkDiaryId()));

        validStatus(diaryRequestDTO.getStatusId());

        return modelToDTO(
                drinkDiaryRepository.save(currentDrinkDiary.update(diaryRequestDTO))
        );
    }

    public DrinkDiaryDTO findById(Long drinkDiaryId) {

        DrinkDiaryModel currentDrinkDiary = drinkDiaryRepository.findById(drinkDiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("DrinkDiary","id",drinkDiaryId));

        return modelToDTO(currentDrinkDiary);
    }

    public void deleteById(Long drinkDiaryId) {
        DrinkDiaryModel currentDrinkDiary = drinkDiaryRepository.findById(drinkDiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("DrinkDiary","id",drinkDiaryId));
        
        drinkConfMasterRepository.deleteDiaryDetailConf(drinkDiaryId);
        drinkDiaryRepository.delete(currentDrinkDiary);
    }

    public Page<DrinkDiaryDTO> findByFilters(DrinkDiaryRequestFilterDTO requestFilterDTO) {

        return drinkDiaryRepository.findByFilters(
                requestFilterDTO.getDiaryName(),
                requestFilterDTO.getStatusId(),
                requestFilterDTO.createPageable())
                .map(this::modelToDTO);
    }

    private DrinkDiaryDTO modelToDTO(DrinkDiaryModel drinkDiary) {

        entityManager.flush();
        entityManager.refresh(drinkDiary);

        return DrinkDiaryDTO.builder()
                .drinkDiaryId(drinkDiary.getDrinkDiaryId())
                .diaryName(drinkDiary.getDiaryName())
                .statusId(drinkDiary.getStatus().getCatalogId())
                .statusDesc(drinkDiary.getStatus().getCatalogOptions())
                .build();
    }

    private void validStatus(Long statusId) {

        if(!statusId.equals(DrinkSizeFlavourDiaryStatusCatalog.ENABLED) &&
                !statusId.equals(DrinkSizeFlavourDiaryStatusCatalog.DISABLED)) {
            throw new InvalidStatusDrinkCatalogException(statusId);
        }
    }
}
