package com.ust.retail.store.common.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ust.retail.store.pim.util.FixtureLoader;

import lombok.Getter;
import lombok.Setter;

@Controller
@Profile("local")
@RequestMapping("/mock-login")
public class MockLoginController {

	private final FixtureLoader fixtureLoader;

	public MockLoginController() throws Exception {
		fixtureLoader = new FixtureLoader(MockLoginController.class);
	}

	@GetMapping
	public ModelAndView getLoginOptions(@RequestParam String path, @RequestParam String origin) {
		ModelAndView modelAndView = new ModelAndView("mockLogin");
		modelAndView.addObject("loginOptions", List.of(
				"storeManager",
				"admin",
				"bistroManager",
				"storeManagerVc"
		));
		modelAndView.addObject("path", path);
		modelAndView.addObject("origin", origin);

		return modelAndView;
	}

	@GetMapping("/{role}")
	@ResponseBody
	public void getLogin(@PathVariable String role,
	                     @RequestParam String path,
	                     @RequestParam String origin,
	                     HttpServletResponse response) throws IOException {
		Root cookieContent = fixtureLoader.getObject(role, Root.class).orElse(new Root());
		Cookie cookie = new Cookie("access_token", cookieContent.access_token);
		cookie.setPath("/");
		response.addCookie(cookie);
		cookie = new Cookie("refresh_token", cookieContent.refresh_token);
		cookie.setPath("/");
		response.addCookie(cookie);
		cookie = new Cookie("customerRoleId", String.valueOf(cookieContent.customer.roleId));
		cookie.setPath("/");
		response.addCookie(cookie);
		cookie = new Cookie("customerEmailId", cookieContent.customer.emailId);
		cookie.setPath("/");
		response.addCookie(cookie);
		response.sendRedirect(String.format("%s/%s",
				new String(Base64.getDecoder().decode(origin)),
				new String(Base64.getDecoder().decode(path))));
	}

	@Getter
	@Setter
	private static class Customer {
		private String firstName;
		private String lastName;
		private String mobileNumber;
		private String emailId;
		private boolean active;
		private boolean smsOtpVerified;
		private boolean enablePushNotification;
		private int roleId;
		private boolean poApprover;
	}

	@Getter
	@Setter
	private static class Root {
		private String supportRedirectMail;
		private String qrCode;
		private String customerId;
		private Customer customer;
		private String access_token;
		private String refresh_token;
		private String token_type;
		private int expires_in;
	}
}
