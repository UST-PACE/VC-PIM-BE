package com.ust.retail.store.pim.service.upcmaster;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.upcmaster.ComboDTO;
import com.ust.retail.store.pim.dto.upcmaster.ComboProductCategoryDTO;
import com.ust.retail.store.pim.exceptions.ComboException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.upcmaster.ComboModel;
import com.ust.retail.store.pim.repository.upcmaster.ComboRepository;

@Service
public class ComboService {

	private final ComboRepository comboRepository;

	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public ComboService(ComboRepository comboRepository,
			AuthenticationFacade authenticationFacade) {
		super();
		this.comboRepository = comboRepository;
		this.authenticationFacade = authenticationFacade;
	}

	public ComboDTO saveOrUpdate(ComboDTO dto) {

		validateComboCategory(dto.getComboCategories());
		
		List<String> existData = comboRepository.existsByUpcOrComboName(dto.getPrincipalUPC(), dto.getComboName(),dto.getComboId());
		if (!existData.isEmpty()) {
			throw new ComboException("Combo", existData.get(0));
		}
		validComboCombination(dto);
		if (dto.getComboId() != null)
			comboRepository.findById(dto.getComboId()).orElseThrow(() -> new ResourceNotFoundException("Combo", "id", dto.getComboId()));

		dto.setCreatedUserId(this.authenticationFacade.getCurrentUserId());
		ComboModel comboModel = comboRepository.save(new ComboModel(dto));
		return new ComboDTO(comboModel);
	}

	public Page<ComboDTO> getComboByFilters(ComboDTO dto) {
		return comboRepository.findByFilter(dto.getComboName(), dto.getComboStatusId(), dto.createPageable())
				.map(c -> new ComboDTO(c));
	}

	public List<ComboDTO> load() {
		return comboRepository.findAllByDeletedFalse().stream().map(c -> new ComboDTO(c))
				.collect(Collectors.toUnmodifiableList());
	}

	public ComboDTO findById(Long comboId) {
		return comboRepository.findById(comboId).map(c -> new ComboDTO(c))
				.orElseThrow(() -> new ResourceNotFoundException("Combo", "id", comboId));
	}

	public List<ComboDTO> getAutocomplete(@Valid String comboName) {
		return comboRepository.getAutocompleteList(comboName);
	}

	private void validComboCombination(ComboDTO dto) {
		Map<Long, Integer> categoryIdAndQuantity = dto.getComboCategories().stream().collect(Collectors.toMap(ComboProductCategoryDTO::getProductCategoryId, ComboProductCategoryDTO::getQuantity));

		if (comboRepository.findByCategoryIds(dto.getProductCategoryIds(),dto.getComboId()).stream().filter(c -> c.getCategoryIdAndQuantity().equals(categoryIdAndQuantity)).findAny().isPresent())
			throw new ComboException("This Combo is already on the system");
	}

	private void validateComboCategory(Set<ComboProductCategoryDTO> comboCategories) {

		if (comboCategories.size() == 1 && comboCategories.stream().noneMatch(c -> c.getQuantity() > 1)) {
			throw new ComboException("Combo should contain atleast two category or recurrences count");
		}

		boolean categoryIsDublicate = comboCategories.stream()
				.collect(Collectors.groupingBy(ComboProductCategoryDTO::getProductCategoryId, Collectors.counting()))
				.entrySet().stream().anyMatch(i -> i.getValue() > 1);
		if (categoryIsDublicate)
			throw new ComboException("Combo Category must be unique");
	}
}
