package com.ust.retail.store.bistro.repository.recipes;

import com.ust.retail.store.bistro.model.recipes.DrinkSizeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkSizeRepository  extends JpaRepository<DrinkSizeModel, Long> {

    @Query(value = "select c from DrinkSizeModel as c"
            + " where (?1 = null or c.sizeName like %?1%)"
            + " and (?2 = null or c.status.catalogId = ?2)")
    Page<DrinkSizeModel> findByFilters(String sizeName, Long statusId,Pageable pageable);


    List<DrinkSizeModel> findByStatusCatalogId(Long statusId);

}
