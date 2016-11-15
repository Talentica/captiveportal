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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
}
