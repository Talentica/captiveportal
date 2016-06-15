/**
 * 
 */
package com.talentica.sdn.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author NarenderK
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.talentica"})
public class CaptivePortalApplication extends SpringBootServletInitializer{
	public static void main(String[] args){
		SpringApplication.run(CaptivePortalApplication.class, args);
	}
}
