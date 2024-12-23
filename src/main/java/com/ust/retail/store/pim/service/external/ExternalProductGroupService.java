package com.ust.retail.store.pim.service.external;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.catalogs.UpcMasterTypeCatalog;
import com.ust.retail.store.pim.dto.external.ExternalProductCategoryDTO;
import com.ust.retail.store.pim.dto.external.ExternalProductGroupDTO;
import com.ust.retail.store.pim.dto.external.ExternalProductSubcategoryDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.catalog.ProductCategoryRepository;
import com.ust.retail.store.pim.repository.catalog.ProductGroupRepository;
import com.ust.retail.store.pim.repository.catalog.ProductSubcategoryRepository;
import com.ust.retail.store.pim.util.DateUtils;

@Service
public class ExternalProductGroupService {
	private final ProductGroupRepository productGroupRepository;
	private final ProductCategoryRepository productCategoryRepository;
	private final ProductSubcategoryRepository productSubcategoryRepository;
	private final RecipeRepository recipeRepository;
	private final AuthenticationFacade authenticationFacade;
	
	public ExternalProductGroupService(ProductGroupRepository productGroupRepository,
									   ProductCategoryRepository productCategoryRepository,
									   ProductSubcategoryRepository productSubcategoryRepository,
									   RecipeRepository recipeRepository,
									   AuthenticationFacade authenticationFacade) {
		this.productGroupRepository = productGroupRepository;
		this.productCategoryRepository = productCategoryRepository;
		this.productSubcategoryRepository = productSubcategoryRepository;
		this.recipeRepository = recipeRepository;
		this.authenticationFacade = authenticationFacade;
	}

	public List<ExternalProductGroupDTO> loadCatalog(Long storeNumberId) {
		Long validatedStoreNumId = Optional.ofNullable(storeNumberId)
				.orElse(authenticationFacade.getUserStoreNumber().getStoreNumberId());
		Long productTypeId = Objects.isNull(storeNumberId)? null: UpcMasterTypeCatalog.PIM_TYPE;

		return productGroupRepository.findByProductTypeWithStock(validatedStoreNumId, productTypeId).stream()
				.map(group -> {
					ExternalProductGroupDTO groupDTO = new ExternalProductGroupDTO().parseToDTO(group);
					groupDTO.setCategoryList(productCategoryRepository.findByProductTypeWithStock(group.getProductGroupId(), validatedStoreNumId, productTypeId).stream()
							.map(category -> {
								ExternalProductCategoryDTO categoryDTO = new ExternalProductCategoryDTO().parseToDTO(category);
								categoryDTO.setSubcategoryList(productSubcategoryRepository.findByProductTypeWithStock(category.getProductCategoryId(), validatedStoreNumId, productTypeId).stream()
										.map(subcategory -> new ExternalProductSubcategoryDTO().parseToDTO(subcategory)).collect(Collectors.toUnmodifiableList())
								);
								return categoryDTO;
							}).collect(Collectors.toUnmodifiableList()));
					return groupDTO;
				})
				.collect(Collectors.toUnmodifiableList());
	}

	public List<ExternalProductGroupDTO> loadBistroCatalog(Long storeNumId) {
		
		Calendar currentTime = Calendar.getInstance();
//		System.out.println("Todays loadBistroCatalog: {0}" + currentTime);

		currentTime.add(Calendar.HOUR, -2);
//		System.out.println("Todays loadBistroCatalog: {-2}" + currentTime);

		List<Long> upcsToDisplay = recipeRepository.findByStore(
						storeNumId,
						currentTime.get(Calendar.DAY_OF_WEEK),
						DateUtils.getCurrentTimeAsInt()).stream()
				.map(RecipeModel::getRelatedUpcMaster)
				.map(UpcMasterModel::getUpcMasterId)
				.collect(Collectors.toUnmodifiableList());

		return productGroupRepository.findAll().stream()
				.peek(g -> g.getProducts().removeIf(p -> !upcsToDisplay.contains(p.getUpcMasterId())))
				.filter(g -> !g.getProducts().isEmpty())
				.map(g -> {
					ExternalProductGroupDTO groupDTO = new ExternalProductGroupDTO().parseToDTO(g);
					groupDTO.setCategoryList(g.getProductCategories().stream()
							.peek(c -> c.getProducts().removeIf(p -> !upcsToDisplay.contains(p.getUpcMasterId())))
							.filter(c -> !c.getProducts().isEmpty())
							.map(c -> {
								ExternalProductCategoryDTO categoryDTO = new ExternalProductCategoryDTO().parseToDTO(c);
								categoryDTO.setSubcategoryList(c.getProductSubcategories().stream()
										.peek(s -> s.getProducts().removeIf(p -> !upcsToDisplay.contains(p.getUpcMasterId())))
										.filter(s -> !s.getProducts().isEmpty())
										.map(s -> new ExternalProductSubcategoryDTO().parseToDTO(s))
										.collect(Collectors.toUnmodifiableList()));
								return categoryDTO;
							})
							.collect(Collectors.toUnmodifiableList()));
					return groupDTO;
				}).collect(Collectors.toUnmodifiableList());
	}
}
