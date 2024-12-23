package com.ust.retail.store.bistro.service.recipes;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ust.retail.store.bistro.dto.recipes.BistroNewUpcResponseDTO;
import com.ust.retail.store.bistro.dto.recipes.BistroUpcMasterDTO;
import com.ust.retail.store.bistro.dto.recipes.RecipeDTO;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;

@Service
public class RecipeUpcMasterService {
	private final RecipeRepository recipeRepository;
	private final UpcMasterRepository upcMasterRepository;
	private final AuthenticationFacade authenticationFacade;

	public RecipeUpcMasterService(RecipeRepository recipeRepository,
								  UpcMasterRepository upcMasterRepository,
								  AuthenticationFacade authenticationFacade) {
		this.recipeRepository = recipeRepository;
		this.upcMasterRepository = upcMasterRepository;
		this.authenticationFacade = authenticationFacade;
	}

	@Transactional
	public BistroNewUpcResponseDTO save(BistroUpcMasterDTO bistroUpcMasterDTO) {

		UpcMasterModel upcMaster = upcMasterRepository.save(bistroUpcMasterDTO.createModel(this.authenticationFacade.getCurrentUserId()));
		upcMaster.updateSku();
		upcMasterRepository.save(upcMaster);

		RecipeModel recipe = recipeRepository.save(new RecipeDTO().createModel(upcMaster));

		return new BistroNewUpcResponseDTO(upcMaster, recipe);
	}

	@Transactional
	public BistroUpcMasterDTO update(BistroUpcMasterDTO bistroUpcMasterDTO) {

		UpcMasterModel model = bistroUpcMasterDTO.createModel(this.authenticationFacade.getCurrentUserId());
		model.updateSku();

		upcMasterRepository.findById(bistroUpcMasterDTO.getUpcMasterId())
				.ifPresent(currentModel -> {
					model.updateMenuBoardImageUrl(currentModel.getMenuBoardImageUrl());
					model.updateWebsiteImage(currentModel.getWebsiteImageUrl());
					model.updateKioskImageUrl(currentModel.getKioskImageUrl());
					model.updateAppImageUrl(currentModel.getAppImageUrl());
					
					currentModel.getStorePrices().forEach(sp ->
							model.addStorePrice(sp)
					);
					
				});

		UpcMasterModel upcMaster = upcMasterRepository.save(model);

		return bistroUpcMasterDTO.parseToDTO(upcMaster);
	}

}
