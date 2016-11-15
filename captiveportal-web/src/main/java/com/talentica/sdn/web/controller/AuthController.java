/**
 * 
 */
package com.talentica.sdn.web.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talentica.sdn.common.util.Constants;
import com.talentica.sdn.persistence.entities.User;
import com.talentica.sdn.service.base.UserRoleService;
import com.talentica.sdn.service.base.UserService;
import com.talentica.sdn.web.to.UserTO;

/**
 * @author NarenderK
 *
 */
@Controller
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRoleService userRoleService;
	
	/**
	 * 
	 * @param srcMac
	 * @return
	 */
	@RequestMapping(value = "/active", method = RequestMethod.GET)
	public @ResponseBody String isMacRegistered(String srcMac) {
		User user = userService.findUserByMacAddress(srcMac);
		if (user != null && user.isActivated()) {
			return user.getMacAddress();
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param srcIp
	 * @param srcMac
	 * @return
	 */
	@RequestMapping(value = "/newUser", method = RequestMethod.GET)
	public @ResponseBody String addUser(@RequestParam String srcIp, @RequestParam String srcMac) {
		User user = new User();
		user.setMacAddress(srcMac);
		user.setIpAddress(srcIp);
		user.setUserRole(userRoleService.findUserRole("guest"));
		user.setActivated(false);
		user.setCreatedDate(new DateTime());
		user.setLastModifiedDate(new DateTime());
		User savedUser = userService.createUser(user);
		if (savedUser != null) {
			return savedUser.getMacAddress();
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param srcMac
	 * @return
	 */
	@RequestMapping(value = "/getUserDetials", method = RequestMethod.GET)
	public @ResponseBody UserTO getUserDetails(@RequestParam String srcMac) {
		UserTO userTo = new UserTO();
		User user = userService.findUserByMacAddress(srcMac);
		if (user != null) {
			userTo.setMacAddress(user.getMacAddress());
			userTo.setIpAddress(user.getIpAddress());
			userTo.setActivated(user.isActivated());
			userTo.setUserRole(user.getUserRole().getRole());
			userTo.setExist(true);
		} else {
			userTo.setMacAddress(null);
			userTo.setIpAddress(null);
			userTo.setActivated(false);
			userTo.setUserRole(Constants.USER_ROLE_GUEST);
			userTo.setExist(false);
		}
		return userTo;
	}
	
	/**
	 * 
	 * @param srcMac
	 * @return
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public @ResponseBody List<String> getRegisteredMacs(String srcMac) {
		return userService.findRegisteredMacs();
	}
}
