package org.springcloud.oauth2.auth.server.conf;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	//token端点配置 
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
		.authenticationManager(this.authenticationManager)					
		.tokenStore(tokenStore());	
		
	        // 配置TokenServices参数
			DefaultTokenServices tokenServices = new DefaultTokenServices();
	        tokenServices.setTokenStore(endpoints.getTokenStore());
	        tokenServices.setSupportRefreshToken(true);
	        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
	        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
	        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(1)); 
	        endpoints.tokenServices(tokenServices);
	}
//oauth 的一些权限	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer)
			throws Exception {
		oauthServer
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	}
//配置客户端权限
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient("ui1")
			.secret("ui1-secret")
			.authorities("ROLE_TRUSTED_CLIENT")
			.authorizedGrantTypes("authorization_code", "refresh_token")
			.scopes("ui1.read")
			.autoApprove(true)		
		.and()
			.withClient("ui2")
			.secret("ui2-secret")
			.authorities("ROLE_TRUSTED_CLIENT")
			.authorizedGrantTypes("authorization_code", "refresh_token")
			.scopes("ui2.read", "ui2.write")
			.autoApprove(true)
		.and()
			.withClient("mobile-app")			
			.authorities("ROLE_CLIENT")
			.authorizedGrantTypes("implicit", "refresh_token")
			.scopes("read")
			.autoApprove(true)
		.and()
			.withClient("customer-integration-system")	
			.secret("1234567890")
			.authorities("ROLE_CLIENT")
			.authorizedGrantTypes("password")
			.scopes("read")
			.autoApprove(true);
	}
	
	@Autowired
	private DataSource dataSource;
		
	@Bean// 声明TokenStore实现
	public JdbcTokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}
	
	
	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		return defaultTokenServices;
	}

}