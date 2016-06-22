/**
 * 
 */
package com.talentica.sdn.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author NarenderK
 *
 */
@Configuration
@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		if (error != null) {
			model.addObject("error", "You've entered wrong username or password.");
		}
		model.setViewName("login");
		return model;
	}
	
	@RequestMapping("/captiveportal")
	public ModelAndView showHome() {
		Authentication user = SecurityContextHolder.getContext()
				.getAuthentication();
		boolean isAdmin = user.getAuthorities().iterator().next()
				.getAuthority().equalsIgnoreCase("ROLE_ADMIN");
		ModelAndView model;
		if (isAdmin)
			model = new ModelAndView("admin");
		else
			model = new ModelAndView("home");
		model.addObject("isAdmin", isAdmin);
		return model;
	}
}
