package com.ust.retail.store.bistro.repository.recipes;

import com.ust.retail.store.bistro.model.recipes.DrinkConfMasterDetailSizeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkConfMasterDetailSizeRepository extends JpaRepository<DrinkConfMasterDetailSizeModel, Long> {

    List<DrinkConfMasterDetailSizeModel> findByDrinkConfMasterDrinkConfMasterId(Long drinkConfMasterId);
}
