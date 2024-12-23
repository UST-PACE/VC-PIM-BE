package com.ust.retail.store.bistro.repository.recipes;

import com.ust.retail.store.bistro.model.recipes.RecipeAddonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeAddonRepository extends JpaRepository<RecipeAddonModel, Long>{

	Page<RecipeAddonModel> findByRecipeRecipeId(Long recipeId, Pageable pageable);

	List<RecipeAddonModel> findByRecipeRecipeId(Long recipeId);

	List<RecipeAddonModel> findByUpcMasterUpcMasterId(Long upcMasterId);
}
