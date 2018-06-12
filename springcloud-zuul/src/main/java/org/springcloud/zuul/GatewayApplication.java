package org.springcloud.zuul;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

	@SpringBootApplication
	@EnableZuulProxy
	@EnableOAuth2Sso
	public class GatewayApplication {

	    public static void main(String[] args) {
	        SpringApplication.run(GatewayApplication.class, args);
	    }
	    @Bean
		public SessionRegistry sessionRegistry(){    
		    return new SessionRegistryImpl();    
		}

	    
	    
	    @Configuration
		@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
		protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

			
			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http
	        	.authorizeRequests()
	        	//Allow access to all static resources without authentication
	        	.antMatchers("/","/**/*.html").permitAll()
	        	.anyRequest().authenticated()
	        	.and()
					.csrf().csrfTokenRepository(csrfTokenRepository())
				.and()
					.addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class);
				
				//http.httpBasic().disable();
			}

			private Filter csrfHeaderFilter() {
				return new OncePerRequestFilter() {
					@Override
					protected void doFilterInternal(HttpServletRequest request,
							HttpServletResponse response, FilterChain filterChain)
							throws ServletException, IOException {
						CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
								.getName());
						if (csrf != null) {
							Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
							String token = csrf.getToken();
							if (cookie == null || token != null && !token.equals(cookie.getValue())) {
								cookie = new Cookie("XSRF-TOKEN", token);
								cookie.setPath("/");
								response.addCookie(cookie);
							}
						}
						filterChain.doFilter(request, response);
					}
				};
			}

			private CsrfTokenRepository csrfTokenRepository() {
				HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
				repository.setHeaderName("X-XSRF-TOKEN");
				return repository;
			}
		}
	}

	@Component
	@Order(Ordered.HIGHEST_PRECEDENCE)
	class WorkaroundRestTemplateCustomizer implements UserInfoRestTemplateCustomizer {
		@Override
		public void customize(OAuth2RestTemplate template) {
			template.setInterceptors(new ArrayList<>(template.getInterceptors()));
		}
		
		
		/** 
	     *  
	     * attention:简单跨域就是GET，HEAD和POST请求，但是POST请求的"Content-Type"只能是application/x-www-form-urlencoded, multipart/form-data 或 text/plain 
	     * 反之，就是非简单跨域，此跨域有一个预检机制，说直白点，就是会发两次请求，一次OPTIONS请求，一次真正的请求 
	     */  
	    @Bean  
	    public CorsFilter corsFilter() {  
	        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();  
	        final CorsConfiguration config = new CorsConfiguration();  
	        config.setAllowCredentials(true); // 允许cookies跨域  
	        config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin  
	        config.addAllowedHeader("*");// #允许访问的头信息,*表示全部  
	        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了  
	        config.addAllowedMethod("OPTIONS");// 允许提交请求的方法，*表示全部允许  
	        config.addAllowedMethod("HEAD");  
	        config.addAllowedMethod("GET");// 允许Get的请求方法  
	        config.addAllowedMethod("PUT");  
	        config.addAllowedMethod("POST");  
	        config.addAllowedMethod("DELETE");  
	        config.addAllowedMethod("PATCH");  
	        source.registerCorsConfiguration("/**", config);  
	        return new CorsFilter(source);  
	    } 

	}



