/**
 * 
 */
package com.talentica.sdn.persistence.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author NarenderK
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = { "com.talentica.sdn.persistence.repositories" })
@EntityScan(basePackages = { "com.talentica.sdn.persistence.entities" })
public class PersistentConfig {

}
