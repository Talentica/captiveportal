/**
 * 
 */
package com.talentica.sdn.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.talentica.sdn.persistence.entities.User;
import com.talentica.sdn.service.base.UserRoleService;
import com.talentica.sdn.service.base.UserService;

/**
 * @author NarenderK
 *
 */
@Controller
public class ViewController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRoleService userRoleService;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/admin/flow")
	public ModelAndView flow() {
		ModelAndView model = new ModelAndView();
		model.setViewName("flow");
		return model;
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/admin/qos")
	public ModelAndView qos() {
		ModelAndView model = new ModelAndView();
		model.setViewName("qos");
		return model;
	}
	
	/**
	 * 
	 * @param request
	 * @param res
	 * @return
	 */
	@RequestMapping("/admin/userdetails")
	public ModelAndView showUserDetails(HttpServletRequest request, HttpServletResponse res) {
		List<User> users = userService.findUserDetails();
		ModelAndView model = new ModelAndView();
		if (users != null) {
			users = users.isEmpty() ? null : users;
		}
		model.addObject("users", users);
		model.setViewName("userDetails");
		return model;
	}
	
	/**
	 * 
	 * @param macAddress
	 * @param ipAddress
	 * @return
	 */
	@RequestMapping(value = "/admin/createUser", method = RequestMethod.POST)
	public ModelAndView createUser(@RequestParam String macAddress, @RequestParam String ipAddress) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		user.setMacAddress(macAddress);
		user.setIpAddress(ipAddress);
		user.setUserRole(userRoleService.findUserRole("user"));
		user.setActivated(true);
		user.setCreatedDate(new DateTime());
		user.setLastModifiedDate(new DateTime());
		userService.createUser(user);
		List<User> users = userService.findUserDetails();
		if (users != null) {
			users = users.isEmpty() ? null : users;
		}
		model.addObject("users", users);
		model.setViewName("userDetails");
		return model;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/admin/deleteUser", method = RequestMethod.POST)
	public ModelAndView deleteUser(@RequestParam String userId) {
		ModelAndView model = new ModelAndView();
		
		userService.deleteUser(Long.parseLong(userId));
		List<User> users = userService.findUserDetails();
		if (users != null) {
			users = users.isEmpty() ? null : users;
		}
		model.addObject("users", users);
		model.setViewName("userDetails");
		return model;
	}

}
