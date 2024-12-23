package com.ust.retail.store.bistro.repository.recipes;


import com.ust.retail.store.bistro.model.recipes.DrinkDiaryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkDiaryRepository extends JpaRepository<DrinkDiaryModel, Long> {
    @Query(value = "select c from DrinkDiaryModel as c"
            + " where (?1 = null or c.diaryName like %?1%)"
            + " and (?2 = null or c.status.catalogId = ?2)")
    Page<DrinkDiaryModel> findByFilters(String diaryName, Long statusId, Pageable pageable);

    List<DrinkDiaryModel> findByStatusCatalogId(Long statusId);
}
