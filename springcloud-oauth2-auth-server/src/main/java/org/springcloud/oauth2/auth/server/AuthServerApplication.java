package org.springcloud.oauth2.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
/**
 * 配置授权服务
 * @author chixue
 *
 */
@SpringBootApplication
@EnableResourceServer
@Controller
@SessionAttributes("authorizationRequest")
public class AuthServerApplication {

	@RequestMapping("/user")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	
}
