package com.ust.retail.store.bistro.repository.menu;

import com.ust.retail.store.bistro.model.menu.MenuEntryDayModel;
import com.ust.retail.store.bistro.model.menu.MenuEntryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MenuEntryRepository extends JpaRepository<MenuEntryModel, Long> {
	@Query(value = "SELECT day"
			+ " FROM MenuEntryDayModel day"
			+ "   JOIN day.menuEntry entry"
			+ "   JOIN entry.recipe recipe"
			+ "   JOIN recipe.relatedUpcMaster recipeProduct"
			+ "   JOIN entry.store store"
			+ " WHERE (:storeNumId = NULL OR store.storeNumId = :storeNumId)"
			+ "   AND (:recipeName = NULL OR recipeProduct.productName LIKE %:recipeName%)"
			+ "   AND (:weekDay = NULL OR day.day = :weekDay)"
			+ "   AND (:start = NULL OR day.start >= :start)"
	)
	Page<MenuEntryDayModel> findByFilters(Long storeNumId, String recipeName, Integer weekDay, Integer start, Pageable pageable);

	Optional<MenuEntryModel> findByRecipeRecipeIdAndStoreStoreNumId(Long recipeId, Long storeNumId);
}
