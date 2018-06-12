package org.springcloud.oauth2.auth.server.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomController {
	
	static final Logger LOGGER = LoggerFactory.getLogger(CustomController.class);
	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private DefaultTokenServices defaultTokenServices;

	@RequestMapping("/rediect")
	public String rediect(HttpServletResponse responsel, String clientId, String token) {
		OAuth2Authentication authentication = tokenStore.readAuthentication(token);
		if (authentication == null) {
			throw new InvalidTokenException("Invalid access token: " + token);
		}
		OAuth2Request request = authentication.getOAuth2Request();
		Map map = new HashMap();
		map.put("code", request.getRequestParameters().get("code"));
		map.put("grant_type", request.getRequestParameters().get("grant_type"));
		map.put("response_type", request.getRequestParameters().get("response_type"));
		//TODO 需要查询一下要跳转的Client_id配置的回调地址
		map.put("redirect_uri", "http://127.0.0.1:8080");
		map.put("client_id", clientId);
		map.put("state", request.getRequestParameters().get("state"));
		request = new OAuth2Request(map, clientId, request.getAuthorities(), request.isApproved(), request.getScope(),
				request.getResourceIds(), map.get("redirect_uri").toString(), request.getResponseTypes(),request.getExtensions()); // 模拟用户登录
		Authentication t = tokenStore.readAuthentication(token);
		OAuth2Authentication auth = new OAuth2Authentication(request, t);
		OAuth2AccessToken new_token = defaultTokenServices.createAccessToken(auth);
		return "redirect:/user_info?access_token=" + new_token.getValue();
	}
}
