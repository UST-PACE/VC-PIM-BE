package com.ust.retail.store.bistro.repository.recipes;

import com.ust.retail.store.bistro.model.recipes.RecipeSubstitutionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeSubstitutionRepository extends JpaRepository<RecipeSubstitutionModel, Long> {

	Page<RecipeSubstitutionModel> findByIngredientRecipeRecipeId(Long recipeId, Pageable pageable);

	List<RecipeSubstitutionModel> findByIngredientRecipeDetailId(Long recipeDetailId);

	@Modifying
	@Query("DELETE FROM RecipeSubstitutionModel s WHERE s.ingredient.recipeDetailId = :recipeDetailId")
	void deleteByIngredientRecipeDetailId(Long recipeDetailId);
}
