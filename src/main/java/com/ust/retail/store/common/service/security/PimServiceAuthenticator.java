package com.ust.retail.store.common.service.security;

import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.dto.security.ScreenPrivilegeDTO;
import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.service.security.UserService;

@Service
public class PimServiceAuthenticator {
	private static final Long EXTERNAL_API_ROLE_ID = 55L;
	private final String serviceUser;
	private final UserService userService;

	public PimServiceAuthenticator(@Value("${pim.fulfillment.serviceUser}") String serviceUser,
	                               UserService userService) {
		this.serviceUser = serviceUser;
		this.userService = userService;
	}

	public Authentication authenticate(String tokenOrRoleId, String userIdStr) {
		String user;
		String roles;
		if (StringUtils.isNumeric(tokenOrRoleId)) {
			Long roleId = Long.valueOf(tokenOrRoleId);

			if (Objects.equals(EXTERNAL_API_ROLE_ID, roleId)) {
				user = serviceUser;
				roles = "ROLE_EXTERNAL_APIS";
			} else {
				if (userIdStr == null) {
					throw new BadCredentialsException("User ID Missing");
				}
				Long userId = Long.valueOf(userIdStr);
				UserDTO userModel;
				try {
					userModel = userService.findById(userId);
				} catch (ResourceNotFoundException e) {
					throw new BadCredentialsException(String.format("User not found: %d", userId));
				}
				user = userModel.getUserName();
				roles = userModel.getScreenPrivilegesDTO().stream()
						.map(ScreenPrivilegeDTO::getRole)
						.collect(Collectors.joining(","));
			}
		} else {
			switch (tokenOrRoleId) {
				case "31a06284-26cd-4b55-95f3-0f891c819586":
					user = "NBsm";
					roles = "ROLE_STORE_MANAGER";
					break;
				case "31a06284-26cd-4b55-95f3-0f891c819588":
					user = "NBbm";
					roles = "ROLE_BISTRO_MANAGER";
					break;
				case "31a06284-26cd-4b55-95f3-0f891c819587":
					user = "admin@ust.com";
					roles = "ROLE_ADMIN";
					break;
				case "31a06284-26cd-4b55-95f3-0f891c819500":
					user = "NBsmvc";
					roles = "ROLE_STORE_MANAGER_VC";
					break;	
				default:
					throw new BadCredentialsException("unknown token");
			}
		}

		return new PreAuthenticatedAuthenticationToken(user, null,
				AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
	}
}
