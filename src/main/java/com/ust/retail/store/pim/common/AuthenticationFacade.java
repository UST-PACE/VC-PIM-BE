package com.ust.retail.store.pim.common;

import com.ust.retail.store.pim.dto.security.StoreNumberInfoDTO;
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthenticationFacade {

	private final UserService userService;
	
	@Autowired
	public AuthenticationFacade(UserService userService) {
		super();
		this.userService = userService;
	}
	
	public StoreNumberInfoDTO getUserStoreNumber() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		UserDTO userDto = this.userService.findUserByUserName(authentication.getName());
		return new StoreNumberInfoDTO(userDto.getStoreNumberId(), userDto.getStoreNumberDesc());
	}

	public Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.userService.findUserByUserName(authentication.getName()).getUserId();
	}

	public UserModel getCurrentUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.userService.findUserModelByUserName(authentication.getName());
	}

	public String getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
	
	public String getAuthority() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> ls = authentication.getAuthorities();
		
		return ls.iterator().next().getAuthority();
	}

	public void logInvalidCredentials(String username) {
		userService.logInvalidCredentialsFor(username);
	}

	public void clearLoginAttempts(String username) {
		userService.clearLoginAttempts(username);
	}
}
