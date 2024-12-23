package com.ust.retail.store.bistro.repository.recipes;

import com.ust.retail.store.bistro.model.recipes.DrinkFlavourModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkFlavourRepository extends JpaRepository<DrinkFlavourModel, Long> {
    @Query(value = "select c from DrinkFlavourModel as c"
            + " where (?1 = null or c.flavourName like %?1%)"
            + " and (?2 = null or c.status.catalogId = ?2)")
    Page<DrinkFlavourModel> findByFilters(String flavourName, Long statusId, Pageable pageable);


    List<DrinkFlavourModel> findByStatusCatalogId(Long statusId);

}
