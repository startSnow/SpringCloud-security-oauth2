package org.springcloud.resource.server;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
/*
 * 配置授权资源路径
 */
@Configuration
@EnableResourceServer
@EnableWebSecurity
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resourceService1";

	@Autowired
	private DataSource dataSource;

	@Override
	public void configure(HttpSecurity http) throws Exception {				
		http
			.requestMatchers().antMatchers("/**")
		.and()
    		.authorizeRequests()		
    		.antMatchers("/test/**").access("#oauth2.isClient() or hasRole('END_USER')")	
    		.antMatchers("/demo/**").permitAll()
    		.anyRequest().authenticated();		
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {			
		resources
			.resourceId(RESOURCE_ID)
			.tokenStore(tokenStore());
		resources.tokenServices(defaultTokenServices());
	}

/*	@Bean
	public JdbcTokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}*/
	
	// ===================================================以下代码与认证服务器一致=========================================
		/**
		 * token存储,这里使用jwt方式存储
		 * 
		 * @param accessTokenConverter
		 * @return
		 */
		@Bean
		public TokenStore tokenStore() {
			TokenStore tokenStore = new JwtTokenStore(accessTokenConverter());
			return tokenStore;
		}

		/**
		 * Token转换器必须与认证服务一致
		 * 
		 * @return
		 */
		@Bean
		public JwtAccessTokenConverter accessTokenConverter() {
			JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
//				/***
//				 * 重写增强token方法,用于自定义一些token返回的信息
//				 */
//				@Override
//				public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//					String userName = authentication.getUserAuthentication().getName();
//					User user = (User) authentication.getUserAuthentication().getPrincipal();// 与登录时候放进去的UserDetail实现类一直查看link{SecurityConfiguration}
//					/** 自定义一些token属性 ***/
//					final Map<String, Object> additionalInformation = new HashMap<>();
//					additionalInformation.put("userName", userName);
//					additionalInformation.put("roles", user.getAuthorities());
//					((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
//					OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
//					return enhancedToken;
//				}

			};
			accessTokenConverter.setSigningKey("123");// 测试用,授权服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
			return accessTokenConverter;
		}

		/**
		 * 创建一个默认的资源服务token
		 * 
		 * @return
		 */
		@Bean
		public ResourceServerTokenServices defaultTokenServices() {
			final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
			defaultTokenServices.setTokenEnhancer(accessTokenConverter());
			defaultTokenServices.setTokenStore(tokenStore());
			return defaultTokenServices;
		}
		// ===================================================以上代码与认证服务器一致=========================================
}