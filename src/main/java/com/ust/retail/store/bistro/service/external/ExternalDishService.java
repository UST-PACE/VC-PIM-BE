package com.ust.retail.store.bistro.service.external;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.ust.retail.store.bistro.dto.external.dish.ExternalDishByStoreAndSkuListRequest;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishByStoreRequest;
import com.ust.retail.store.bistro.dto.external.dish.ExternalDishDTO;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.bistro.service.recipes.RecipeDrinkConfService;
import com.ust.retail.store.pim.dto.upcmaster.ApplicableTaxDTO;
import com.ust.retail.store.pim.service.tax.TaxService;
import com.ust.retail.store.pim.util.DateUtils;

@Service
public class ExternalDishService {
	private final TaxService taxService;
	private final RecipeRepository recipeRepository;
	private final RecipeDrinkConfService recipeDrinkConfService;

	public ExternalDishService(TaxService taxService,
							   RecipeRepository recipeRepository,
							   RecipeDrinkConfService recipeDrinkConfService) {
		this.taxService = taxService;
		this.recipeRepository = recipeRepository;
		this.recipeDrinkConfService = recipeDrinkConfService;
	}

	public List<ExternalDishDTO> findByFilters(ExternalDishByStoreRequest request) {
		return recipeRepository.findByStoreAndFilters(
						request.getStoreId(),
						Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
						DateUtils.getCurrentTimeAsInt(),
						request.getSearchKey(),
						request.getGroupId(),
						request.getCategoryId(),
						request.getSubcategoryId(),
						request.getChannelId(),
						request.createSimplePageable())
				.map(m -> new ExternalDishDTO()
						.parseToDTO(m, request.getStoreId(), true)
						.setDrinksConfiguration(recipeDrinkConfService.loadDrinksInfoExternalApi(m.getRecipeId(), request.getStoreId()))
						.setApplicableTaxes(taxService.findApplicableTaxesForProductAtStore(m.getRelatedUpcMaster().getUpcMasterId(), request.getStoreId()).stream()
								.map(tax -> new ApplicableTaxDTO(
										tax.getTaxTypeName(),
										Stream.of(tax.getProductGroupName(), tax.getProductCategoryName(), tax.getProductSubcategoryName())
												.filter(Objects::nonNull)
												.collect(Collectors.joining("/")),
										tax.getPercentage()))
								.collect(Collectors.toUnmodifiableSet())))
				.getContent();
	}

	public List<ExternalDishDTO> findByStoreAndSkuList(ExternalDishByStoreAndSkuListRequest request) {
		List<Long> availableForSale = recipeRepository.findAvailableForSaleByStoreAndUpcList(
				request.getStoreId(),
				Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
				DateUtils.getCurrentTimeAsInt(),
				request.getSkuList());

		return recipeRepository.findByStoreAndUpcList(
						request.getStoreId(),
						request.getSkuList()).stream()
				.map(m -> new ExternalDishDTO()
						.parseToDTO(m, request.getStoreId(), availableForSale.contains(m.getRecipeId()))
						.setDrinksConfiguration(recipeDrinkConfService.loadDrinksInfoExternalApi(m.getRecipeId(), request.getStoreId()))
						.setApplicableTaxes(taxService.findApplicableTaxesForProductAtStore(m.getRelatedUpcMaster().getUpcMasterId(), request.getStoreId()).stream()
								.map(tax -> new ApplicableTaxDTO(
										tax.getTaxTypeName(),
										Stream.of(tax.getProductGroupName(), tax.getProductCategoryName(), tax.getProductSubcategoryName())
												.filter(Objects::nonNull)
												.collect(Collectors.joining("/")),
										tax.getPercentage()))
								.collect(Collectors.toUnmodifiableSet())))
				.collect(Collectors.toList());
	}
}
