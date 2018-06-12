package org.springcloud.oauth2.auth.server;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
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
	@Autowired
	private TokenStore tokenStore;
	/**
	 * 返回用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping("/user")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}
	/**
	 * 为单点登录准备返回用户信息
	 * @param access_token
	 * @param response
	 */
	@RequestMapping({ "/user_info" })
	public void user(String access_token,HttpServletResponse response) {
		OAuth2Authentication auth=tokenStore.readAuthentication(access_token);
		OAuth2Request request=auth.getOAuth2Request();
	  Map<String, String> map = new LinkedHashMap<>();
	  map.put("loginName", auth.getUserAuthentication().getName());
	  map.put("password", auth.getUserAuthentication().getName());
	  map.put("id", auth.getUserAuthentication().getName());
	  try {
		response.sendRedirect(request.getRedirectUri()+"?name="+auth.getUserAuthentication().getName());
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	
}
