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

import com.talentica.sdn.common.util.Constants;

/**
 * @author NarenderK
 *
 */
@Configuration
@Controller
public class LoginController {
	
	/**
	 * 
	 * @param error
	 * @param logout
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		if (logout != null) {
			model.addObject("msg", "logged out successfully.");
		}
		if (error != null) {
			model.addObject("error", "wrong username or password.");
		}
		model.setViewName("login");
		return model;
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/captiveportal")
	public ModelAndView showHome() {
		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = Constants.USER_ROLE_ADMIN.equalsIgnoreCase(user.getAuthorities().iterator().next().getAuthority());
		boolean isGuest = Constants.USER_ROLE_GUEST.equalsIgnoreCase(user.getAuthorities().iterator().next().getAuthority());
		ModelAndView model;
		if (isAdmin){
			model = new ModelAndView("admin");
		}
		else if (isGuest){
			model = new ModelAndView("guest");
		}
		else{
			model = new ModelAndView("home");
		}
		model.addObject("isAdmin", isAdmin);
		model.addObject("isGuest", isGuest);
		return model;
	}
	
}
