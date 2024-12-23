package com.ust.retail.store.bistro.service.recipes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.bistro.commons.catalogs.DrinkSizeFlavourDiaryStatusCatalog;
import com.ust.retail.store.bistro.dto.recipes.DrinksConfExternalApiDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeDrinkConfDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeDrinkConfDTO.DrinkDiary;
import com.ust.retail.store.bistro.dto.recipes.RecipeDrinkConfDTO.DrinkFlavour;
import com.ust.retail.store.bistro.dto.recipes.RecipeDrinkConfDTO.DrinkSize;
import com.ust.retail.store.bistro.model.recipes.DrinkConfMasterDetailDiaryModel;
import com.ust.retail.store.bistro.model.recipes.DrinkConfMasterDetailFlavourModel;
import com.ust.retail.store.bistro.model.recipes.DrinkConfMasterDetailSizeModel;
import com.ust.retail.store.bistro.model.recipes.DrinkConfMasterModel;
import com.ust.retail.store.bistro.model.recipes.DrinkDiaryModel;
import com.ust.retail.store.bistro.model.recipes.DrinkFlavourModel;
import com.ust.retail.store.bistro.model.recipes.DrinkSizeModel;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.bistro.repository.recipes.DrinkConfMasterRepository;
import com.ust.retail.store.bistro.repository.recipes.DrinkDiaryRepository;
import com.ust.retail.store.bistro.repository.recipes.DrinkFlavourRepository;
import com.ust.retail.store.bistro.repository.recipes.DrinkSizeRepository;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcStorePriceRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
@Transactional
public class RecipeDrinkConfService {

    private final RecipeRepository recipeRepository;

    private final DrinkConfMasterRepository drinkConfMasterRepository;
    private final DrinkSizeRepository drinkSizeRepository;
    private final DrinkFlavourRepository drinkFlavourRepository;
    private final DrinkDiaryRepository drinkDiaryRepository;
	private final UpcStorePriceRepository upcStorePriceRepository;

    public void updateDrinkConf(RecipeDrinkConfDTO drinkConfRequestDTO) {

        RecipeModel recipe = recipeRepository.findById(drinkConfRequestDTO.getRecipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe","id", drinkConfRequestDTO.getRecipeId()));

        Long upcMasterId = recipe.getRelatedUpcMaster().getUpcMasterId();


        drinkConfMasterRepository.findByRelatedUpcMasterUpcMasterIdAndStoreNumberStoreNumId(upcMasterId, drinkConfRequestDTO.getStoreNumberId())
                .ifPresentOrElse(masterConf -> {

                    masterConf.setEnabledDrinkConf(drinkConfRequestDTO.getEnabledDrinkConf());

                    if(drinkConfRequestDTO.getEnabledDrinkConf()) {

                        if(drinkConfRequestDTO.getSizes().stream().anyMatch(RecipeDrinkConfDTO.DrinkSize::getHasEnabled)) {
                            updateSizes(masterConf, drinkConfRequestDTO.getSizes());
                        }
                        else { masterConf.clearSizes(); }


                        if(drinkConfRequestDTO.getFlavours().stream().anyMatch(RecipeDrinkConfDTO.DrinkFlavour::getHasEnabled)) {

                            updateFlavours(masterConf, drinkConfRequestDTO.getFlavours());
                        }
                        else { masterConf.clearFlavours(); }


                        if(drinkConfRequestDTO.getDiaries().stream().anyMatch(RecipeDrinkConfDTO.DrinkDiary::getHasEnabled)) {

                            updateDiaries(masterConf, drinkConfRequestDTO.getDiaries());
                        }
                        else { masterConf.clearDiaries(); }


                    }
                    else {
                        masterConf.clearSizes();
                        masterConf.clearFlavours();
                        masterConf.clearDiaries();
                    }
                }, () -> {

                    DrinkConfMasterModel newDrinkConfMaster = DrinkConfMasterModel.builder()
                            .storeNumber(new StoreNumberModel(drinkConfRequestDTO.getStoreNumberId()))
                            .relatedUpcMaster(new UpcMasterModel(upcMasterId))
                            .enabledDrinkConf(drinkConfRequestDTO.getEnabledDrinkConf())
                            .build();

                    if(drinkConfRequestDTO.getEnabledDrinkConf()) {
                        addSizes(newDrinkConfMaster, drinkConfRequestDTO.getSizes());
                        addFlavour(newDrinkConfMaster, drinkConfRequestDTO.getFlavours());
                        addDiary(newDrinkConfMaster, drinkConfRequestDTO.getDiaries());

                    }

                    drinkConfMasterRepository.save(newDrinkConfMaster);
                });
        
        if(drinkConfRequestDTO.getEnabledDrinkConf())
        	upcStorePriceRepository.resetAllStorePrices(upcMasterId,drinkConfRequestDTO.getStoreNumberId());
    }
    
    public void disableDrinksConfiguration(Long storeNumId, Long upcMasterId) {
    	drinkConfMasterRepository.disableDrinksConfiguration(upcMasterId,storeNumId);
    }


    public RecipeDrinkConfDTO loadInfoRecipeDrinkConf(Long recipeId, Long storeNumberId) {

        RecipeModel recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe","id", recipeId));

        Long upcMasterId = recipe.getRelatedUpcMaster().getUpcMasterId();

        RecipeDrinkConfDTO.RecipeDrinkConfDTOBuilder confDTOBuilder = RecipeDrinkConfDTO.builder()
                .storeNumberId(storeNumberId)
                .recipeId(recipeId)
                .upcMasterId(upcMasterId)
                .enabledDrinkConf(false);


        List<DrinkSizeModel> drinkSizes = drinkSizeRepository.findByStatusCatalogId(DrinkSizeFlavourDiaryStatusCatalog.ENABLED);
        List<DrinkFlavourModel> drinkFlavours = drinkFlavourRepository.findByStatusCatalogId(DrinkSizeFlavourDiaryStatusCatalog.ENABLED);
        List<DrinkDiaryModel> drinkDiaries = drinkDiaryRepository.findByStatusCatalogId(DrinkSizeFlavourDiaryStatusCatalog.ENABLED);

        drinkConfMasterRepository.findByRelatedUpcMasterUpcMasterIdAndStoreNumberStoreNumId(upcMasterId, storeNumberId)
                .ifPresentOrElse(drinkConfMaster -> {

                    confDTOBuilder.enabledDrinkConf(drinkConfMaster.getEnabledDrinkConf());

                    loadSizesInfo(confDTOBuilder, drinkSizes, drinkConfMaster);
                    loadFlavoursInfo(confDTOBuilder, drinkFlavours, drinkConfMaster);
                    loadDiariesInfo(confDTOBuilder, drinkDiaries, drinkConfMaster);

                }, () -> {

                    loadDefaultConf(confDTOBuilder, drinkSizes, drinkFlavours, drinkDiaries);

                });

        return confDTOBuilder.build();
    }
    
    public DrinksConfExternalApiDTO loadDrinksInfoExternalApi(Long recipeId, Long storeNumberId) {
    	
    	RecipeDrinkConfDTO d = loadInfoRecipeDrinkConf(recipeId,storeNumberId);
    	
    	List<DrinkDiary> diaries = null;
    	List<DrinkSize> sizes =  null;
    	List<DrinkFlavour> flavours =  null;

    	if(d.getEnabledDrinkConf()) {
    		
    		diaries = new ArrayList<>();
        	sizes =  new ArrayList<>();
        	flavours =  new ArrayList<>();
        	
    		for(DrinkDiary currentConf : d.getDiaries()) {
        		if(currentConf.getHasEnabled()) {
        			currentConf.setHasEnabled(null);
        			currentConf.setDrinkConfMasterDetailDiaryId(null);
        			
        			if(currentConf.getIsDefault() == null)
        				currentConf.setIsDefault(false);
        			
        			diaries.add(currentConf);
        		}
        	}
        	
        	for(DrinkSize currentConf : d.getSizes()) {
        		if(currentConf.getHasEnabled()) {
        			currentConf.setHasEnabled(null);
        			currentConf.setDrinkConfMasterDetailSizeId(null);
        			
        			if(currentConf.getIsDefault() == null)
        				currentConf.setIsDefault(false);
        			
        			sizes.add(currentConf);
        		}
        	}
        	
        	for(DrinkFlavour currentConf : d.getFlavours()) {
        		if(currentConf.getHasEnabled()) {
        			currentConf.setHasEnabled(null);
        			currentConf.setDrinkConfMasterDetailFlavourId(null);
        			flavours.add(currentConf);
        		}
        	}
    	}
    	
    	
    	return new DrinksConfExternalApiDTO(d.getEnabledDrinkConf(),sizes, flavours,diaries);
    }
    

    private static void loadDefaultConf(RecipeDrinkConfDTO.RecipeDrinkConfDTOBuilder confDTOBuilder, List<DrinkSizeModel> drinkSizes,
                                        List<DrinkFlavourModel> drinkFlavours, List<DrinkDiaryModel> drinkDiaries) {

        NumberFormat nf = new DecimalFormat("##.###");

        confDTOBuilder.sizes(drinkSizes.stream().map(size -> RecipeDrinkConfDTO.DrinkSize.builder()
                        .price(0d)
                        .isDefault(false)
                        .drinkSizeId(size.getDrinkSizeId())
                        .sizeName(String.format("%s (%s Oz)", size.getSizeName(), nf.format(size.getOunces())))
                        .hasEnabled(false)
                        .build())
                .collect(Collectors.toList()));

        confDTOBuilder.flavours(drinkFlavours.stream().map(flavour -> RecipeDrinkConfDTO.DrinkFlavour.builder()
                        .price(0d)
                        .flavourName(flavour.getFlavourName())
                        .drinkFlavourId(flavour.getDrinkFlavourId())
                        .hasEnabled(false)
                        .build())
                .collect(Collectors.toList()));

        confDTOBuilder.diaries(drinkDiaries.stream().map(diary -> RecipeDrinkConfDTO.DrinkDiary.builder()
                        .price(0d)
                        .drinkDiaryId(diary.getDrinkDiaryId())
                        .diaryName(diary.getDiaryName())
                        .hasEnabled(false)
                    .build())
                .collect(Collectors.toList()));
    }


    private void addSizes(DrinkConfMasterModel drinkConfMaster, List<RecipeDrinkConfDTO.DrinkSize> sizes) {

        Optional.ofNullable(sizes).ifPresent(list -> {

            for (RecipeDrinkConfDTO.DrinkSize confSize : list.stream().filter(RecipeDrinkConfDTO.DrinkSize::getHasEnabled).collect(Collectors.toList())) {
                drinkConfMaster.addSize(DrinkConfMasterDetailSizeModel.builder()
                        .drinkConfMaster(drinkConfMaster)
                        .price(confSize.getPrice())
                        .isDefault(confSize.getIsDefault())
                        .drinkSize(DrinkSizeModel.builder().drinkSizeId(confSize.getDrinkSizeId()).build())
                        .build());
            }
        });

    }

    private void addFlavour(DrinkConfMasterModel drinkConfMaster, List<RecipeDrinkConfDTO.DrinkFlavour> flavours) {

        Optional.ofNullable(flavours).ifPresent(list -> {

            for(RecipeDrinkConfDTO.DrinkFlavour confFlavour: list.stream().filter(RecipeDrinkConfDTO.DrinkFlavour::getHasEnabled).collect(Collectors.toList())) {
                drinkConfMaster.addFlavour(DrinkConfMasterDetailFlavourModel.builder()
                        .drinkConfMaster(drinkConfMaster)
                        .price(confFlavour.getPrice())
                        .drinkFlavour(DrinkFlavourModel.builder().drinkFlavourId(confFlavour.getDrinkFlavourId()).build())
                        .build());
            }

        });
    }

    private void addDiary(DrinkConfMasterModel drinkConfMaster, List<RecipeDrinkConfDTO.DrinkDiary> diaries) {

        Optional.ofNullable(diaries).ifPresent(list -> {

            for(RecipeDrinkConfDTO.DrinkDiary confDiary: list.stream().filter(RecipeDrinkConfDTO.DrinkDiary::getHasEnabled).collect(Collectors.toList())) {
                drinkConfMaster.addDiary(DrinkConfMasterDetailDiaryModel.builder()
                        .drinkConfMaster(drinkConfMaster)
                        .price(confDiary.getPrice())
                        .drinkDiary(DrinkDiaryModel.builder().drinkDiaryId(confDiary.getDrinkDiaryId()).build())
                        .isDefault(confDiary.getIsDefault())
                        .build());
            }

        });
    }



    private void updateSizes(DrinkConfMasterModel drinkConfMaster, List<RecipeDrinkConfDTO.DrinkSize> sizes) {

        List<RecipeDrinkConfDTO.DrinkSize> enabledSizes = sizes.stream()
                .filter(RecipeDrinkConfDTO.DrinkSize::getHasEnabled).collect(Collectors.toList());

        for(RecipeDrinkConfDTO.DrinkSize confSize: enabledSizes) {

            drinkConfMaster.getAvailableSizes().stream()
                    .filter(item -> Optional.ofNullable(item.getDrinkConfMasterDetailSizeId()).orElse(-1L).equals(confSize.getDrinkConfMasterDetailSizeId()))
                    .findAny().ifPresentOrElse( currentConf -> {

                        currentConf.update(confSize.getPrice(), confSize.getIsDefault());

                    }, () -> {

                        drinkConfMaster.addSize(DrinkConfMasterDetailSizeModel.builder()
                                .drinkConfMaster(drinkConfMaster)
                                .price(confSize.getPrice())
                                .isDefault(confSize.getIsDefault())
                                .drinkSize(DrinkSizeModel.builder().drinkSizeId(confSize.getDrinkSizeId()).build())
                                .build());
                    });
        }

        List<Long> disabledSizeIds = sizes.stream()
                .filter(r -> !r.getHasEnabled())
                .map(i->Optional.ofNullable(i.getDrinkConfMasterDetailSizeId()).orElse(-1L))
                .collect(Collectors.toList());

        if(disabledSizeIds.size() > 0) {
            drinkConfMasterRepository.deleteDisabledSizesConf(disabledSizeIds);
        }


    }

    private void updateFlavours(DrinkConfMasterModel drinkConfMaster, List<RecipeDrinkConfDTO.DrinkFlavour> flavours) {

        List<RecipeDrinkConfDTO.DrinkFlavour> enabledFlavours = flavours.stream()
                .filter(RecipeDrinkConfDTO.DrinkFlavour::getHasEnabled).collect(Collectors.toList());

        for(RecipeDrinkConfDTO.DrinkFlavour confFlavour: enabledFlavours) {

            drinkConfMaster.getAvailableFlavours().stream()
                    .filter(item -> Optional.ofNullable(item.getDrinkConfMasterDetailFlavourId()).orElse(-1L).equals(confFlavour.getDrinkConfMasterDetailFlavourId()))
                    .findAny().ifPresentOrElse( currentConf -> {

                        currentConf.setPrice(confFlavour.getPrice());

                    }, () -> {


                        drinkConfMaster.addFlavour(DrinkConfMasterDetailFlavourModel.builder()
                                .drinkConfMaster(drinkConfMaster)
                                .price(confFlavour.getPrice())
                                .drinkFlavour(DrinkFlavourModel.builder().drinkFlavourId(confFlavour.getDrinkFlavourId()).build())
                                .build());


                    });

        }

        List<Long> disabledIds = flavours.stream()
                .filter(r -> !r.getHasEnabled())
                .map(i->Optional.ofNullable(i.getDrinkConfMasterDetailFlavourId()).orElse(-1L))
                .collect(Collectors.toList());

        if(disabledIds.size() > 0) {
            drinkConfMasterRepository.deleteDisabledFlavourConf(disabledIds);
        }
    }

    private void updateDiaries(DrinkConfMasterModel drinkConfMaster, List<RecipeDrinkConfDTO.DrinkDiary> diaries) {

        List<RecipeDrinkConfDTO.DrinkDiary> enabledDiaries = diaries.stream()
                .filter(RecipeDrinkConfDTO.DrinkDiary::getHasEnabled).collect(Collectors.toList());

        for(RecipeDrinkConfDTO.DrinkDiary confDiary: enabledDiaries) {

            drinkConfMaster.getAvailableDiaries().stream()
                    .filter(item -> Optional.ofNullable(item.getDrinkConfMasterDetailDiaryId()).orElse(-1L).equals(confDiary.getDrinkConfMasterDetailDiaryId()))
                    .findAny().ifPresentOrElse( currentConf -> {

                        currentConf.update(confDiary.getPrice(), confDiary.getIsDefault());

                    }, () -> {

                        drinkConfMaster.addDiary(DrinkConfMasterDetailDiaryModel.builder()
                                .drinkConfMaster(drinkConfMaster)
                                .price(confDiary.getPrice())
                                .isDefault(confDiary.getIsDefault())
                                .drinkDiary(DrinkDiaryModel.builder().drinkDiaryId(confDiary.getDrinkDiaryId()).build())
                                .build());
                    });

        }

        List<Long> disabledIds = diaries.stream()
                .filter(r -> !r.getHasEnabled())
                .map(i->Optional.ofNullable(i.getDrinkConfMasterDetailDiaryId()).orElse(-1L))
                .collect(Collectors.toList());

        if(disabledIds.size() > 0) {
            drinkConfMasterRepository.deleteDisabledDiaryConf(disabledIds);
        }
    }

    private static void loadSizesInfo(RecipeDrinkConfDTO.RecipeDrinkConfDTOBuilder confDTOBuilder, List<DrinkSizeModel> drinkSizes,
                                      DrinkConfMasterModel drinkConfMaster) {

        List<RecipeDrinkConfDTO.DrinkSize> sizes = new ArrayList<>();

        NumberFormat nf = new DecimalFormat("##.###");

        for (DrinkSizeModel drinkSize: drinkSizes) {

            drinkConfMaster.getAvailableSizes().stream()
                    .filter(item -> item.getDrinkSize().getDrinkSizeId().equals(drinkSize.getDrinkSizeId()))
                    .findAny().ifPresentOrElse( currentConf -> {
                        sizes.add(RecipeDrinkConfDTO.DrinkSize.builder()
                                .drinkConfMasterDetailSizeId(currentConf.getDrinkConfMasterDetailSizeId())
                                .price(currentConf.getPrice())
                                .isDefault(currentConf.getIsDefault())
                                .drinkSizeId(drinkSize.getDrinkSizeId())
                                .sizeName(String.format("%s (%s Oz)", drinkSize.getSizeName(), nf.format(drinkSize.getOunces())))
                                .hasEnabled(true)
                                .build());
                    }, () -> {
                        sizes.add(RecipeDrinkConfDTO.DrinkSize.builder()
                                .price(0d)
                                .isDefault(false)
                                .hasEnabled(false)
                                .drinkSizeId(drinkSize.getDrinkSizeId())
                                .sizeName(String.format("%s (%s Oz)", drinkSize.getSizeName(), nf.format(drinkSize.getOunces())))
                                .build());
                    });
        }

        confDTOBuilder.sizes(sizes);
    }

    private static void loadFlavoursInfo(RecipeDrinkConfDTO.RecipeDrinkConfDTOBuilder confDTOBuilder, List<DrinkFlavourModel> drinkFlavours,
                                         DrinkConfMasterModel drinkConfMaster) {

        List<RecipeDrinkConfDTO.DrinkFlavour> flavours = new ArrayList<>();

        for (DrinkFlavourModel drinkFlavour: drinkFlavours) {

            drinkConfMaster.getAvailableFlavours().stream()
                    .filter(item -> item.getDrinkFlavour().getDrinkFlavourId().equals(drinkFlavour.getDrinkFlavourId()))
                    .findAny().ifPresentOrElse( currentConf -> {
                        flavours.add(RecipeDrinkConfDTO.DrinkFlavour.builder()
                                .drinkConfMasterDetailFlavourId(currentConf.getDrinkConfMasterDetailFlavourId())
                                .flavourName(drinkFlavour.getFlavourName())
                                .drinkFlavourId(drinkFlavour.getDrinkFlavourId())
                                .price(currentConf.getPrice())
                                .hasEnabled(true)
                                .build());
                    }, () -> {
                        flavours.add(RecipeDrinkConfDTO.DrinkFlavour.builder()
                                .flavourName(drinkFlavour.getFlavourName())
                                .drinkFlavourId(drinkFlavour.getDrinkFlavourId())
                                .price(0d)
                                .hasEnabled(false)
                                .build());
                    });
        }

        confDTOBuilder.flavours(flavours);
    }

    private static void loadDiariesInfo(RecipeDrinkConfDTO.RecipeDrinkConfDTOBuilder confDTOBuilder, List<DrinkDiaryModel> drinkDiaries,
                                        DrinkConfMasterModel drinkConfMaster) {

        List<RecipeDrinkConfDTO.DrinkDiary> diaries = new ArrayList<>();

        for (DrinkDiaryModel drinkDiary: drinkDiaries) {

            drinkConfMaster.getAvailableDiaries().stream()
                    .filter(item -> item.getDrinkDiary().getDrinkDiaryId().equals(drinkDiary.getDrinkDiaryId()))
                    .findAny().ifPresentOrElse( currentConf -> {
                        diaries.add(RecipeDrinkConfDTO.DrinkDiary.builder()
                                .drinkConfMasterDetailDiaryId(currentConf.getDrinkConfMasterDetailDiaryId())
                                .drinkDiaryId(drinkDiary.getDrinkDiaryId())
                                .diaryName(drinkDiary.getDiaryName())
                                .price(currentConf.getPrice())
                                .hasEnabled(true)
                                .isDefault(currentConf.getIsDefault())
                                .build());
                    }, () -> {
                        diaries.add(RecipeDrinkConfDTO.DrinkDiary.builder()
                                .drinkDiaryId(drinkDiary.getDrinkDiaryId())
                                .diaryName(drinkDiary.getDiaryName())
                                .price(0d)
                                .hasEnabled(false)
                                .isDefault(false)
                                .build());
                    });
        }

        confDTOBuilder.diaries(diaries);
    }
}
