package com.eazybytes.config;

import com.eazybytes.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import javax.swing.AbstractAction;

@Configuration
public class ProjectSecurityConfig {
	
	
	//Keycloak Role Base Authentication for API's
	private ServletPolicyEnforcerFilter CreatePolicyEnforce() {
		  return new ServletPolicyEnforcerFilter(new CreatePolicyEnforceFilter());
		}
		

	@Bean
	protected SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setExposedHeaders(Arrays.asList("Authorization", "XSRF-TOKEN", "Content-Type"));
						config.setMaxAge(3600L);
						return config;
					}
				})).csrf(csrf -> csrf.disable())
				.addFilterAfter(CreatePolicyEnforce(),BearerTokenAuthenticationFilter.class);
				
	
				
			
				
				
/*	
 *
 *                ------------------- This all are used for oauth2 Liberies ---------------------- 
  
                 //Role Base Authentication for API's
//				.authorizeHttpRequests((requests) -> requests.requestMatchers("/myCards").hasRole("owner")
//						.requestMatchers("/myBalance").hasRole("manager").requestMatchers("/user", "/myAccount")
//						.authenticated().requestMatchers("/swagger-ui/**").permitAll().requestMatchers("/v3/**")
//						.permitAll().requestMatchers("/notices", "/contact", "/register").permitAll().anyRequest()
//						.authenticated())

				// This is JWT Default Token generated
//				 .oauth2ResourceServer(oauth2ResourceServerCustomizer ->
//				 oauth2ResourceServerCustomizer.jwt(Customizer.withDefaults()));

				// This is opaque Token generated
//				 .oauth2ResourceServer(oauth2ResourceServerCustomizer ->
//				 oauth2ResourceServerCustomizer.opaqueToken(Customizer.withDefaults()));

				// This is used to customize the JWT Token and This is used to ready form scope access
//				.oauth2ResourceServer(oauth2ResourceServerCustomizer -> oauth2ResourceServerCustomizer
//						.jwt(jwtCustomizer -> jwtCustomizer.jwtAuthenticationConverter(jwtAuthenticationConverter)));

		       // JWT Default Token form Keycloak and it will not work (No use just for ref)
//				.oauth2ResourceServer(oauth2ResourceServerCustomizer -> oauth2ResourceServerCustomizer
//						.jwt(Customizer.withDefaults()));
 * 
 
*/
		return http.build();
	}
	
//	@Bean
//	GrantedAuthorityDefaults grantedAuthorityDefaults() {
//		return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
//	}
	
	//----------------No use just for Ref: -----------------------------------

	// Default Web Security Role Prefix and it will not work (No use just for ref)
//	@Bean
//	protected DefaultWebSecurityExpressionHandler wSecurity() {
//		DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler	 = new DefaultWebSecurityExpressionHandler();
//		defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");
//		return defaultWebSecurityExpressionHandler;
//	}
//	
//	//Default Method Security Role Prefix and it will not work (No use just for ref)
//	@Bean
//    DefaultMethodSecurityExpressionHandler mSecurity() {
//		DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
//		defaultMethodSecurityExpressionHandler.setDefaultRolePrefix("");
//		return defaultMethodSecurityExpressionHandler;
//	}

	// JWT Default Authentication Filter and It is used to get form token and it
	// will not work (No use just for ref)
//	@Bean
//	protected JwtAuthenticationConverter jwtAuthenticationConverter() {
//		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//		JwtGrantedAuthoritiesConverter jwtGrantedAuthenticationConverter = new JwtGrantedAuthoritiesConverter();
//		jwtGrantedAuthenticationConverter.setAuthorityPrefix("");
//		jwtGrantedAuthenticationConverter.setAuthoritiesClaimName("roles");
//		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthenticationConverter);
//		return jwtAuthenticationConverter;
//
//	}

}
