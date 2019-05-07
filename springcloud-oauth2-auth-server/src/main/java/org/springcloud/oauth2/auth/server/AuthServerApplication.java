package org.springcloud.oauth2.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
/**
 * 配置授权服务
 * @author chixue
 *
 */
@SpringBootApplication
@EnableResourceServer

public class AuthServerApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	
}
