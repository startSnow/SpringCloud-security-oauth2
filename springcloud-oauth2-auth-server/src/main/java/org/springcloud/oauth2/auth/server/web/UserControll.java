package org.springcloud.oauth2.auth.server.web;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("authorizationRequest")
public class UserControll {
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
}
