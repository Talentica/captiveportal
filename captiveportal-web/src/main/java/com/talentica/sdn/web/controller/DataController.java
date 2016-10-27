/**
 * 
 */
package com.talentica.sdn.web.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talentica.sdn.common.util.Constants;
import com.talentica.sdn.persistence.entities.User;
import com.talentica.sdn.service.base.UserRoleService;
import com.talentica.sdn.service.base.UserService;

/**
 * @author NarenderK
 *
 */
@Controller
public class DataController {

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
	@RequestMapping(value = "/getRole", method = RequestMethod.GET)
	public @ResponseBody String getRole(@RequestParam String srcMac) {
		User user = userService.findUserByMacAddress(srcMac);
		if(user == null){
			return "GUEST";
		}		
		return user.getUserRole().getRole();
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
	
	/**
	 * 
	 * @param request
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	private ModelAndView isRequestValid(HttpServletRequest request, HttpServletResponse res) throws Exception {
		ModelAndView model = new ModelAndView();
		boolean activated = false;
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		List<User> users = userService.findUserByIpAddressAndActivated(ipAddress, activated);
		for(User userToValidate : users){
			String mac = userToValidate.getMacAddress();
			try {
				URL url = new URL("http://localhost:8181/restconf/operations/connect:connection");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				String username = Constants.ODL_USERNAME_ADMIN;
				String password = Constants.ODL_PASSWORD_ADMIN;
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				String authString = username + ":" + password;
		        String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
		        conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
				String input = "{\"input\": {\n\"mac\": \""+mac+"\",\n\"ipAddress\": \""+ipAddress+"\"\n}\n}";
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				while ((output = br.readLine()) != null) {
					if(output.contains("true")){
						Authentication user = SecurityContextHolder.getContext().getAuthentication();
						boolean isEmployee = user.getAuthorities().iterator().next().getAuthority().equalsIgnoreCase("ROLE_USER");
						if(isEmployee){
							userService.setUserRoleByMac(userRoleService.findUserRole("user"), mac);
						}
						userService.setUserActivatedByMac(mac);
					}
				}
				conn.disconnect();
			} catch (Exception e) {
				throw e;
			}
		}
		model.setViewName("welcome");
		return model;
	}
	
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
	 * @param dpid
	 * @param flowId
	 * @param input
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/updateFlow", method = RequestMethod.POST)
	public ModelAndView updateFlow(@RequestParam String dpid, @RequestParam String flowId, @RequestParam String input) throws Exception{
		Integer code = 0;
		ModelAndView model = new ModelAndView();
		String path = "http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/";
		String dpidPath = path + dpid + "/";
		String pathToFlow = dpidPath + "table/0/flow/" + flowId;
		try {
			URL url = new URL(pathToFlow);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			String username = Constants.ODL_USERNAME_ADMIN;
			String password = Constants.ODL_PASSWORD_ADMIN;
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/xml");
			String authString = username + ":" + password;
	        String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
	        conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			code = conn.getResponseCode();
			conn.disconnect();
		} catch (Exception e) {
			throw e;
		}
		if(code==200){
			model.addObject("msg", "Flow added or updated successfully!");
		}else{
			model.addObject("error", "Error!! HTTP response code : "+code);
		}
		model.setViewName("flow");
		return model;
	}
	
	/**
	 * 
	 * @param dpid
	 * @param meterId
	 * @param input
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/updateMeter", method = RequestMethod.POST)
	public ModelAndView updateMeter(@RequestParam String dpid, @RequestParam String meterId, @RequestParam String input) throws Exception{
		Integer code = 0;
		ModelAndView model = new ModelAndView();
		String path = "http://localhost:8181/restconf/config/opendaylight-inventory:nodes/node/";
		String dpidPath = path + dpid + "/";
		String pathToFlow = dpidPath + "meter/" + meterId;
		try {
			URL url = new URL(pathToFlow);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			String username = Constants.ODL_USERNAME_ADMIN;
			String password = Constants.ODL_PASSWORD_ADMIN;
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/xml");
			String authString = username + ":" + password;
	        String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
	        conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			code = conn.getResponseCode();
			conn.disconnect();
		} catch (Exception e) {
			throw e;
		}
		if(code==200){
			model.addObject("msg", "Meter added or updated successfully!");
		}else{
			model.addObject("error", "Error!! HTTP response code : "+code);
		}
		model.setViewName("flow");
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
