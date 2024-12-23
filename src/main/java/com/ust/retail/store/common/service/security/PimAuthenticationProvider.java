package com.ust.retail.store.common.service.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class PimAuthenticationProvider implements AuthenticationProvider {

	private final PimServiceAuthenticator pimServiceAuthenticator;

	public PimAuthenticationProvider(PimServiceAuthenticator pimServiceAuthenticator) {
		this.pimServiceAuthenticator = pimServiceAuthenticator;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String tokenOrRole = (String) authentication.getPrincipal();
		String userId = (String) authentication.getCredentials();

		if (StringUtils.isBlank(tokenOrRole)) {
			throw new BadCredentialsException("Invalid token/roleId");
		}

		Authentication authResult = pimServiceAuthenticator.authenticate(tokenOrRole, userId);

		if (!authResult.isAuthenticated()) {
			throw new BadCredentialsException("Invalid token");
		}

		return authResult;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(PreAuthenticatedAuthenticationToken.class);
	}
}
