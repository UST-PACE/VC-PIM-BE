package com.ust.retail.store.pim.service.catalog;

import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;
import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.repository.catalog.StoreLocationRepository;
import com.ust.retail.store.pim.repository.catalog.StoreNumberRepository;
import com.ust.retail.store.pim.repository.security.UserRepository;
import com.ust.retail.store.pim.service.security.RoleService;
import com.ust.retail.store.pim.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StoreNumberService extends BaseService {

	private final StoreNumberRepository storeNumberRepository;
	private final StoreLocationRepository storeLocationRepository;
	private final UserRepository userRepository;

	@Autowired
	public StoreNumberService(StoreNumberRepository storeNumberRepository,
	                          StoreLocationRepository storeLocationRepository,
	                          UserRepository userRepository) {
		this.storeNumberRepository = storeNumberRepository;
		this.storeLocationRepository = storeLocationRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public StoreNumberDTO create(StoreNumberDTO dto) {
		StoreNumberModel storeNumberModel = this.storeNumberRepository.save(dto.createModel());

		storeLocationRepository.save(new StoreLocationModel(
					null,
					storeNumberModel.getStoreNumId(),
					"Front Store",
					1L)
				.enableStoreLocationForSales());

		return new StoreNumberDTO().parseToDTO(storeNumberModel);
	}

	@Transactional
	public StoreNumberDTO update(StoreNumberDTO dto) {
		StoreNumberModel model = storeNumberRepository.findById(dto.getStoreNumId())
				.orElseThrow(() -> new ResourceNotFoundException("StoreNumber", "id", dto.getStoreNumId()));

		return new StoreNumberDTO().parseToDTO(this.storeNumberRepository.save(model.updateValues(dto)));
	}

	public StoreNumberDTO findById(Long storeNumberId) {
		return storeNumberRepository.findById(storeNumberId)
				.map(m -> new StoreNumberDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("StoreNumber", "id", storeNumberId));
	}

	public Page<StoreNumberDTO> getStoreNumbersByFilters(@Valid StoreNumberDTO storeNumberDTO) {
		return storeNumberRepository.findByFilters(storeNumberDTO.getStoreName(), storeNumberDTO.createPageable())
				.map(model -> new StoreNumberDTO().parseToDTO(model));
	}

	public List<StoreNumberDTO> getAutocomplete(@Valid String storeName) {
		return storeNumberRepository.getAutocompleteList(storeName).stream()
				.map(model -> new StoreNumberDTO(model.getStoreNumId(), model.getStoreName()))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<StoreNumberDTO> load() {
		return storeNumberRepository.findAll().stream()
				.map(m -> new StoreNumberDTO().parseToDTO(m))
				.collect(Collectors.toList());
	}

	public List<StoreNumberDTO> loadByVendorMasterId(Long vendorMasterId) {
		return storeNumberRepository.findByVendorMasterId(vendorMasterId).stream()
				.map(m -> new StoreNumberDTO().parseToDTO(m))
				.collect(Collectors.toList());
	}

	public List<String> getStoreManagerEmailList(Long storeNumberId) {
		return userRepository.findUsersWithRoleByStoreNumId(storeNumberId, RoleService.STORE_MANAGER_ROLE_ID).stream()
				.filter(u -> !Objects.equals(u.getUserId(), UserService.SERVICE_USER_ID))
				.map(UserModel::getEmail)
				.distinct()
				.collect(Collectors.toUnmodifiableList());
	}

	public List<String> getStoreManagerNameList(Long storeNumberId) {
		return userRepository.findUsersWithRoleByStoreNumId(storeNumberId, RoleService.STORE_MANAGER_ROLE_ID).stream()
				.filter(u -> !Objects.equals(u.getUserId(), UserService.SERVICE_USER_ID))
				.map(UserModel::getNameDesc)
				.distinct()
				.collect(Collectors.toUnmodifiableList());
	}

	public List<String> getPoApproverEmailList(Long storeNumberId) {
		List<String> approverStoreManagerEmails = userRepository.findUsersWithRoleByStoreNumId(storeNumberId, RoleService.STORE_MANAGER_ROLE_ID).stream()
				.filter(UserModel::isApprover)
				.map(UserModel::getEmail)
				.distinct()
				.collect(Collectors.toUnmodifiableList());

		List<String> storeAdminEmails = userRepository.findUsersWithRoleByStoreNumId(storeNumberId, RoleService.ADMIN_ROLE_ID).stream()
				.map(UserModel::getEmail)
				.distinct()
				.collect(Collectors.toUnmodifiableList());

		List<String> result = new ArrayList<>(approverStoreManagerEmails);
		result.addAll(storeAdminEmails);

		return result;
	}
}
