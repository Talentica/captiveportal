/**
 * 
 */
package com.talentica.sdn.web.util;

/**
 * @author NarenderK
 *
 */
public class Response {
	
	private boolean exist;
	private String macAddress;
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
}
