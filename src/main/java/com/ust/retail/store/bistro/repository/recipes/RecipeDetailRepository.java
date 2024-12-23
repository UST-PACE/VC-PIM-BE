package com.ust.retail.store.bistro.repository.recipes;

import com.ust.retail.store.bistro.model.recipes.RecipeDetailModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeDetailRepository extends JpaRepository<RecipeDetailModel, Long>{

	Page<RecipeDetailModel> findByRecipeRecipeId(Long recipeId, Pageable pageable);

	List<RecipeDetailModel> findByToExcludeTrueAndRecipeRecipeId(Long recipeId);

	List<RecipeDetailModel> findByRecipeRecipeId(Long recipeId);

	List<RecipeDetailModel> findByUpcMasterUpcMasterId(Long upcMasterId);

	List<RecipeDetailModel> findByRecipeRelatedUpcMasterUpcMasterId(Long upcMasterId);

	@Query(value = "SELECT i FROM RecipeDetailModel i WHERE i.recipe.recipeId = ?1 AND i.toExclude = true")
	Page<RecipeDetailModel> loadOptionalIngredientsByRecipeId(Long recipeId, Pageable pageable);

	@Query(value = "SELECT i FROM RecipeDetailModel i WHERE i.recipe.recipeId = ?1 AND i.toExclude = false")
	List<RecipeDetailModel> loadOptionalIngredientCandidatesByRecipeId(Long recipeId);
}
