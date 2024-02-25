package com.eazybytes.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class KeycloakSecurityUntil {

	Keycloak keycloak;

	@Value("${kc.realm}")
	private String relam;

	@Value("${kc.auth-server-url}")
	private String servletURL;

	@Value("${kc.client-id}")
	private String adminClientID;

	@Value("${kc.grant-type}")
	private String type;

	@Value("${kc.admin.username}")
	private String adminUserName;

	@Value("${kc.admin.password}")
	private String adminPassword;

	public Keycloak getKeyCloakInstance() {
		System.out.println(relam);
		if (keycloak == null) {
			keycloak = KeycloakBuilder.builder().serverUrl(servletURL).realm(relam).clientId(adminClientID)
					.grantType(type).username(adminUserName).password(adminPassword).build();
		}
		return keycloak;
	}

}
