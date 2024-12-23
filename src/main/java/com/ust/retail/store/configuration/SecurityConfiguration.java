package com.ust.retail.store.configuration;

import com.ust.retail.store.common.filter.security.AuthenticationFilter;
import com.ust.retail.store.common.service.security.PimAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final Environment environment;
	private final PimAuthenticationProvider pimAuthenticationProvider;

	public SecurityConfiguration(Environment environment,
								 PimAuthenticationProvider pimAuthenticationProvider) {
		this.environment = environment;
		this.pimAuthenticationProvider = pimAuthenticationProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (environment.acceptsProfiles(Profiles.of("local"))) {
			http.cors(configurer -> {
				CorsConfiguration configuration = new CorsConfiguration();
				configuration.setAllowedOrigins(List.of("*"));
				configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
				configuration.setAllowedHeaders(List.of("authorization", "content-type", "enctype"));
				UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
				source.registerCorsConfiguration("/**", configuration);
				configurer.configurationSource(source);
			});
			http.authorizeRequests().antMatchers("/mock-login/**").permitAll();

		} else {
			http.cors().disable();
		}
		http
				.csrf().disable()
				.headers().frameOptions().deny().and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers("/", "/ping").permitAll()
				.anyRequest().authenticated().and()
				.exceptionHandling()
				.authenticationEntryPoint(unauthorizedEntryPoint());

		http.addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(pimAuthenticationProvider);
	}

	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
