package com.ust.retail.store.bistro.repository.recipes;

import com.ust.retail.store.bistro.model.recipes.DrinkConfMasterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkConfMasterRepository extends JpaRepository<DrinkConfMasterModel, Long> {

    Optional<DrinkConfMasterModel> findByRelatedUpcMasterUpcMasterIdAndStoreNumberStoreNumId(Long upcMasterId, Long storeNumId);


    @Modifying
    @Query(value = "DELETE FROM DrinkConfMasterDetailSizeModel d WHERE d.drinkConfMasterDetailSizeId in ?1")
    void deleteDisabledSizesConf(List<Long> drinkConfMasterDetailSizeIds);


    @Modifying
    @Query(value = "DELETE FROM DrinkConfMasterDetailFlavourModel d WHERE d.drinkConfMasterDetailFlavourId in ?1")
    void deleteDisabledFlavourConf(List<Long> drinkConfMasterDetailFlavourIds);


    @Modifying
    @Query(value = "DELETE FROM DrinkConfMasterDetailDiaryModel d WHERE d.drinkConfMasterDetailDiaryId in ?1")
    void deleteDisabledDiaryConf(List<Long> drinkConfMasterDetailDiaryIds);


    @Modifying
    @Query(value = "UPDATE DrinkConfMasterModel d SET enabledDrinkConf = false WHERE d.relatedUpcMaster.upcMasterId = ?1 AND d.storeNumber.storeNumId = ?2")
	void disableDrinksConfiguration(Long upcMasterId, Long storeNumId);
    
    @Modifying
    @Query(value = "DELETE FROM DrinkConfMasterDetailDiaryModel d WHERE d.drinkDiary.drinkDiaryId = ?1")
    void deleteDiaryDetailConf(Long drinkDiaryId);
    
    @Modifying
    @Query(value = "DELETE FROM DrinkConfMasterDetailSizeModel d WHERE d.drinkSize.drinkSizeId = ?1")
    void deleteSizeDetailConf(Long drinkSizeId);
    
    @Modifying
    @Query(value = "DELETE FROM DrinkConfMasterDetailFlavourModel d WHERE d.drinkFlavour.drinkFlavourId = ?1")
    void deleteFlavourDetailConf(Long drinkFlavourId);

}
