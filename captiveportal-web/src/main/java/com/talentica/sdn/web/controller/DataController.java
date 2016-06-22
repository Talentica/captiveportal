/**
 * 
 */
package com.talentica.sdn.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talentica.sdn.persistence.entities.User;
import com.talentica.sdn.service.base.UserRoleService;
import com.talentica.sdn.service.base.UserService;
import com.talentica.sdn.web.util.Response;

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

	@RequestMapping(value = "/active", method = RequestMethod.GET)
	public @ResponseBody Response isMacRegistered(String srcMac) {
		Response response = new Response();
		User user = userService.findUserByMacAddress(srcMac);
		if (user != null && user.isActivated()) {
			response.setExist(true);
			response.setMacAddress(user.getMacAddress());
		} else {
			response.setExist(false);
			response.setMacAddress(null);
		}
		return response;
	}

	@RequestMapping(value = "/newUser", method = RequestMethod.GET)
	public @ResponseBody Response addUser(@RequestParam String srcIp, @RequestParam String srcMac) {
		Response response = new Response();
		User user = new User();
		user.setMacAddress(srcMac);
		user.setIpAddress(srcIp);
		user.setUserRole(userRoleService.findUserRole("user"));
		user.setActivated(false);
		user.setCreatedDate(new DateTime());
		user.setLastModifiedDate(new DateTime());
		User savedUser = userService.createUser(user);
		if (savedUser != null) {
			response.setExist(true);
			response.setMacAddress(savedUser.getMacAddress());
		} else {
			response.setExist(false);
			response.setMacAddress(null);
		}
		return response;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public @ResponseBody List<String> getRegisteredMacs(String srcMac) {
		List<String> users = new ArrayList<>();
		users = userService.findRegisteredMacs();
		return users;
	}

	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	private ModelAndView isRequestValid(HttpServletRequest request, HttpServletResponse res) {
		ModelAndView model = new ModelAndView();
		boolean activated = false;
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		System.out.println("****\n***\n***"+ipAddress);
		List<User> users = userService.findUserByIpAddressAndActivated(ipAddress, activated);
		for(User user : users){
			String mac = user.getMacAddress();
			try {
				URL url = new URL("http://localhost:8181/restconf/operations/hello:hello-world");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				String username = "admin";
				String password = "admin";
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				String authString = username + ":" + password;
		        String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
		        conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
				String input = "{\"input\": {\n\"mac\": \""+mac+"\",\n\"ipAddress\": \""+ipAddress+"\"\n}\n}";
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					if(output.contains("true")){
						userService.setUserActivatedByMac(mac);
					}
				}
				conn.disconnect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		model.setViewName("welcome");
		return model;
	}
	
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
}
