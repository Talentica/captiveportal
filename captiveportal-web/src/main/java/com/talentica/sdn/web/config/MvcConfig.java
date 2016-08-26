/**
 * 
 */
package com.talentica.sdn.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author NarenderK
 *
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/aboutus").setViewName("aboutus");
        registry.addViewController("/termsConditions").setViewName("termsConditions");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/guest").setViewName("guest");
        registry.addViewController("/career").setViewName("career");
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/admin/management").setViewName("management");
    }

}