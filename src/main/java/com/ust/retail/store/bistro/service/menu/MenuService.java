package com.ust.retail.store.bistro.service.menu;

import com.ust.retail.store.bistro.dto.menu.MenuEntryCopyDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryFiltersDTO;
import com.ust.retail.store.bistro.dto.menu.MenuEntryFiltersResponseDTO;
import com.ust.retail.store.bistro.model.menu.MenuEntryModel;
import com.ust.retail.store.bistro.repository.menu.MenuEntryRepository;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.repository.catalog.StoreNumberRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MenuService {
	private final MenuEntryRepository menuEntryRepository;
	private final StoreNumberRepository storeNumberRepository;
	private final AuthenticationFacade authenticationFacade;

	public MenuService(MenuEntryRepository menuEntryRepository,
					   StoreNumberRepository storeNumberRepository,
					   AuthenticationFacade authenticationFacade) {
		this.menuEntryRepository = menuEntryRepository;
		this.storeNumberRepository = storeNumberRepository;
		this.authenticationFacade = authenticationFacade;
	}

	@Transactional
	public MenuEntryDTO save(MenuEntryDTO menuEntryDTO) {
		MenuEntryModel menuEntry = menuEntryDTO.createModel(authenticationFacade.getCurrentUserId());
		menuEntryRepository.save(menuEntry);

		return menuEntryDTO.parseToDTO(menuEntry);
	}

	@Transactional
	public MenuEntryDTO update(MenuEntryDTO menuEntryDTO) {

		MenuEntryModel existingModel = menuEntryRepository.findById(menuEntryDTO.getMenuEntryId())
				.orElseThrow(() -> new ResourceNotFoundException("MenuEntry ", "id", menuEntryDTO.getMenuEntryId()));

		existingModel.clearWeekDays();
		menuEntryRepository.save(existingModel);

		MenuEntryModel model = menuEntryDTO.createModel(authenticationFacade.getCurrentUserId());

		model.getWeekDays().forEach(existingModel::addWeekDay);

		menuEntryRepository.save(existingModel);

		return menuEntryDTO.parseToDTO(existingModel);
	}

	public MenuEntryDTO findById(Long menuEntryId) {
		return menuEntryRepository.findById(menuEntryId)
				.map(m -> new MenuEntryDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("MenuEntry", "id", menuEntryId));
	}

	public Page<MenuEntryFiltersResponseDTO> findByFilters(MenuEntryFiltersDTO filtersDTO) {
		return menuEntryRepository.findByFilters(
				filtersDTO.getStoreNumId(),
				filtersDTO.getRecipeName(),
				filtersDTO.getWeekDay(),
				filtersDTO.getStart(),
				filtersDTO.createPageable()
		).map(m -> new MenuEntryFiltersResponseDTO().parseToDTO(m));
	}

	@Transactional
	public MenuEntryDTO copy(MenuEntryCopyDTO copyDTO) {
		MenuEntryModel existingModel = menuEntryRepository.findById(copyDTO.getMenuEntryId())
				.orElseThrow(() -> new ResourceNotFoundException("MenuEntry ", "id", copyDTO.getMenuEntryId()));

		storeNumberRepository.findById(copyDTO.getStoreNumId())
				.orElseThrow(() -> new ResourceNotFoundException("StoreNumber ", "id", copyDTO.getStoreNumId()));

		MenuEntryModel model = menuEntryRepository.findByRecipeRecipeIdAndStoreStoreNumId(existingModel.getRecipe().getRecipeId(), copyDTO.getStoreNumId())
				.orElseGet(() -> new MenuEntryModel(copyDTO.getStoreNumId(), existingModel.getRecipe().getRecipeId(), authenticationFacade.getCurrentUserId()));

		model.clearWeekDays();
		existingModel.getWeekDays()
				.forEach(weekDay -> model.addWeekDay(weekDay.copy(authenticationFacade.getCurrentUserId())));

		menuEntryRepository.save(model);

		return new MenuEntryDTO().parseToDTO(existingModel);
	}

	@Transactional
	public void delete(Long menuEntryId) {
		MenuEntryModel menuEntryModel = menuEntryRepository.findById(menuEntryId)
				.orElseThrow(() -> new ResourceNotFoundException("MenuEntry", "id", menuEntryId));

		menuEntryRepository.delete(menuEntryModel);
	}
}
