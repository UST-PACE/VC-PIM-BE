package com.ust.retail.store.pim.service.security;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.common.catalogs.UserStatusCatalog;
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.dto.security.UserSearchResponseDTO;
import com.ust.retail.store.pim.exceptions.EmailExistsException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UserNameExistsException;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.repository.security.UserRepository;

@Service
public class UserService extends BaseService {

	private static final int MAX_LOGIN_ATTEMPTS = 5;
	public static final long SERVICE_USER_ID = 3L;

	private final UserRepository userRepository;
	private final StoreNumberService storeNumberService;
	private final RoleService roleService;

	public UserService(UserRepository userRepository,
					   StoreNumberService storeNumberService,
					   RoleService roleService) {
		this.userRepository = userRepository;
		this.storeNumberService = storeNumberService;
		this.roleService = roleService;
	}


	@Transactional(readOnly = true)
	public UserDTO findById(Long userId) {
		Optional<UserModel> byId = userRepository.findById(userId);
		Optional<UserModel> byExternalId = userRepository.findByExternalUserId(userId);

		if (byId.isEmpty() && byExternalId.isEmpty()) {
			throw new ResourceNotFoundException("User", "id", userId);
		}

		return new UserDTO().parseToDTO(byExternalId.orElseGet(byId::get));
	}

	@Transactional
	public UserDTO createUser(UserDTO userDTO) {

		userDTO.setStoreNumberId(storeNumberService.findById(userDTO.getStoreNumberId()).getStoreNumId());
		UserModel user = userDTO.parseToModel(UserStatusCatalog.USER_STATUS_ACTIVE);

		user.addPrivilege(roleService.getMappedRoleId(userDTO.getRoleId()).orElse(userDTO.getRoleId()));
		user.setApprover(userDTO.isApprover());

		checkExistsUserName(user);

		return new UserDTO().parseToDTO(userRepository.save(user));
	}

	@Transactional
	public UserDTO updateUser(UserDTO userDTO) {

		Optional<UserModel> byId = userRepository.findById(userDTO.getUserId());
		Optional<UserModel> byExternalId = userRepository.findByExternalUserId(userDTO.getUserId());

		if (byId.isEmpty() && byExternalId.isEmpty()) {
			throw new ResourceNotFoundException("User", "id", userDTO.getUserId());
		}

		userDTO.setStoreNumberId(storeNumberService.findById(userDTO.getStoreNumberId()).getStoreNumId());

		UserModel currentUser = byExternalId.orElseGet(byId::get);
		currentUser.updateValues(userDTO);

		checkExistsUserName(currentUser);

		currentUser.clearPrivileges();

		this.userRepository.saveAndFlush(currentUser);

		currentUser.addPrivilege(roleService.getMappedRoleId(userDTO.getRoleId()).orElse(userDTO.getRoleId()));
		currentUser.setApprover(userDTO.isApprover());

		this.userRepository.save(currentUser);

		return new UserDTO().parseToDTO(currentUser);
	}

	@Transactional(readOnly = true)
	public List<UserSearchResponseDTO> getAutocomplete(String nameDesc) {
		return userRepository.findUserAutocomplete(nameDesc);
	}

	public UserDTO findUserByUserName(String userName) {
		UserModel user = userRepository.findByUserName(userName)
				.orElseThrow(() -> new ResourceNotFoundException("User", "UserName", userName));

		return new UserDTO().parseToDTO(user);
	}

	public UserModel findUserModelByUserName(String userName) {
		return userRepository.findByUserName(userName)
				.orElseThrow(() -> new ResourceNotFoundException("User", "UserName", userName));
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> getUsersByFilters(UserDTO userDTO) {
		return this.userRepository.getUsersByFilters(userDTO.getNameDesc(), userDTO.getUserName(), userDTO.getStatusId(),
				userDTO.getStoreNumberId(), userDTO.createPageable());
	}

	@Transactional
	public void changeUserPassword(UserDTO userDTO) {

		UserModel currentUser = userRepository.getById(userDTO.getUserId());
		currentUser.changePassword(userDTO.getPassword());

		this.userRepository.save(currentUser);
	}

	private void checkExistsUserName(UserModel user) {
		if (isEmailTaken(user)) {
			throw new EmailExistsException(user.getEmail());
		}
		if (isUserNameTaken(user)) {
			throw new UserNameExistsException(user.getUserName());
		}
	}

	private boolean isEmailTaken(UserModel user) {
		return this.userRepository.findByEmail(user.getEmail()).filter(u -> !Objects.equals(u.getUserId(), user.getUserId())).isPresent();
	}

	private boolean isUserNameTaken(UserModel user) {
		return this.userRepository.findByUserName(user.getUserName()).filter(u -> !Objects.equals(u.getUserId(), user.getUserId())).isPresent();
	}

	public void logInvalidCredentialsFor(String username) {
		userRepository.findByUserName(username).ifPresent(userModel -> {
			int loginAttempts = Optional.ofNullable(userModel.getLoginAttempts()).orElse(0) + 1;
			userModel.setLoginAttempts(loginAttempts);
			if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
				userModel.setStatus(new CatalogModel(UserStatusCatalog.USER_STATUS_INACTIVE));
			}
			userRepository.save(userModel);
		});
	}

	public void clearLoginAttempts(String username) {
		userRepository.findByUserName(username).ifPresent(userModel -> {
			userModel.setLoginAttempts(0);
			userRepository.save(userModel);
		});
	}

	public boolean userHasRole(Long userId, Long roleId) {
		return !userRepository.findUserWithRole(userId, roleId).isEmpty();
	}

	public GenericResponse deleteUser(Long userId) {
		UserModel userModel = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		userModel.setDeleted(true);

		userRepository.save(userModel);

		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "User deleted successfully");
	}

	public GenericResponse undeleteUser(Long userId) {
		UserModel userModel = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		userModel.setDeleted(false);
		userModel.setLoginAttempts(0);
		userModel.setStatus(new CatalogModel(UserStatusCatalog.USER_STATUS_ACTIVE));

		userRepository.save(userModel);

		return new GenericResponse(GenericResponse.OP_TYPE_ACCEPT, GenericResponse.SUCCESS_CODE, "User restored successfully");
	}
}
