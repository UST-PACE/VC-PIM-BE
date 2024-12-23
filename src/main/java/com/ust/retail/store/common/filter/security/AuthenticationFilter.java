package com.ust.retail.store.common.filter.security;


import com.beust.jcommander.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class AuthenticationFilter extends GenericFilterBean {

	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		Optional<String> userIdValue = Optional.ofNullable(httpRequest.getHeader("AUTH_USERID"));
		Optional<String> roleIdValue = Optional.ofNullable(httpRequest.getHeader("ROLE_ID"));
		Optional<String> tokenValue = Optional.ofNullable(httpRequest.getHeader(HttpHeaders.AUTHORIZATION))
				.filter(header -> Strings.startsWith(header, "Bearer", false))
				.map(header -> StringUtils.removeStartIgnoreCase(header, "Bearer").trim());

		try {
			if (roleIdValue.isPresent()) {
				log.info("Trying to authenticate user by RoleId/UserId method. Value: {}/{}", roleIdValue.get(), userIdValue);
				processRoleIdAuthentication(roleIdValue.get(), userIdValue.orElse(null));
			}

			if (tokenValue.isPresent()) {
				log.info("Trying to authenticate user by Bearer token method. Value: {}", tokenValue.get());
				processTokenAuthentication(tokenValue.get());
			}

			log.trace("AuthenticationFilter is passing request down the filter chain");
			chain.doFilter(request, response);
		} catch (InternalAuthenticationServiceException e) {
			SecurityContextHolder.clearContext();
			log.error("Internal authentication service exception", e);
			chain.doFilter(request, response);
		} catch (AuthenticationException e) {
			SecurityContextHolder.clearContext();
			log.error("Authentication exception", e);
			chain.doFilter(request, response);
		}
	}

	private void processTokenAuthentication(String token) {
		Authentication responseAuthentication = authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(token, null));
		if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
			throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials [token]");
		}
		log.trace("User successfully authenticated");
		SecurityContextHolder.getContext().setAuthentication(responseAuthentication);
	}

	private void processRoleIdAuthentication(String roleId, String userId) {
		Authentication responseAuthentication = authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(roleId, userId));
		if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
			throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials [role]");
		}
		log.trace("User successfully authenticated");
		SecurityContextHolder.getContext().setAuthentication(responseAuthentication);
	}

}
