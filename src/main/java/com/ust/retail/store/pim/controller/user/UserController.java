package com.ust.retail.store.pim.controller.user;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnRecover;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.catalogs.UserStatusCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.security.StoreNumberInfoDTO;
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.dto.security.UserSearchResponseDTO;
import com.ust.retail.store.pim.exceptions.InvalidUserModificationException;
import com.ust.retail.store.pim.model.security.RoleModel;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.service.security.RoleService;
import com.ust.retail.store.pim.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/users/p")
@Validated
public class UserController extends BaseController {

	private final UserService userService;
	private final UserStatusCatalog userStatusCatalog;
	private final RoleService roleService;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public UserController(UserService userService, UserStatusCatalog userStatusCatalog,
						  RoleService roleService,
						  AuthenticationFacade authenticationFacade) {
		super();
		this.userService = userService;
		this.userStatusCatalog = userStatusCatalog;
		this.roleService = roleService;
		this.authenticationFacade = authenticationFacade;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/find/id/{id}")
	public UserDTO findById(@PathVariable(value = "id") Long userId) {
		return userService.findById(userId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/find/username/{username:.+}")
	public UserDTO findByUserName(@PathVariable(value = "username") String username) {
		return userService.findUserByUserName(username);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public UserDTO create(@Valid @RequestBody UserDTO userDTO) {
		return userService.createUser(userDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public UserDTO updateUser(@Valid @RequestBody UserDTO userDTO) {
		return userService.updateUser(userDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER')")
	@PutMapping("/updateprofile")
	@Validated(OnUpdate.class)
	public UserDTO updateUserProfile(@Valid @RequestBody UserDTO userDTO) {
		UserModel currentUser = authenticationFacade.getCurrentUserDetails();

		if (!Objects.equals(userDTO.getUserId(), currentUser.getUserId())) {
			throw new InvalidUserModificationException();
		}

		return userService.updateUser(userDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PutMapping("/updatepassword")
	@Validated(OnRecover.class)
	public ResponseEntity<Void> updatePassword(@Valid @RequestBody UserDTO userDTO) {
		this.userService.changeUserPassword(userDTO);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@PutMapping("/updateuserpassword")
	@Validated(OnRecover.class)
	public ResponseEntity<Void> updateUserPassword(@Valid @RequestBody UserDTO userDTO) {
		UserModel currentUser = authenticationFacade.getCurrentUserDetails();

		if (!Objects.equals(userDTO.getUserId(), currentUser.getUserId())) {
			throw new InvalidUserModificationException();
		}

		this.userService.changeUserPassword(userDTO);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/list")
	public Page<UserDTO> listUserByFilters(@Valid @RequestBody UserDTO userDTO) {
		return this.userService.getUsersByFilters(userDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{userId}")
	public GenericResponse deleteUser(@PathVariable Long userId) {
		return this.userService.deleteUser(userId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/undelete/{userId}")
	public GenericResponse undeleteUser(@PathVariable Long userId) {
		return this.userService.undeleteUser(userId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/autocomplete/{name}")
	public List<UserSearchResponseDTO> findAutoCompleteOptions(@PathVariable(value = "name") String nameDesc) {
		return userService.getAutocomplete(nameDesc);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/catalog/user/status")
	public List<CatalogDTO> getUserStatusCatalog() {
		return this.userStatusCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/security/store/number")
	public StoreNumberInfoDTO getStoreNumberByUser() {
		return this.authenticationFacade.getUserStoreNumber();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/load/roles")
	public List<RoleModel> loadRoles() {
		return this.roleService.findAllRoles();
	}

}
