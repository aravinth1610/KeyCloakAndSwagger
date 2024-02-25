package com.eazybytes.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter  implements Converter<Jwt, Collection<GrantedAuthority>> {

	//This is used to ready form scope access
	
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
//        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
//       
//        if (realmAccess == null || realmAccess.isEmpty()) {
//            return new ArrayList<>();
//        }

        Collection<GrantedAuthority> returnValue = ((List<String>) jwt.getClaims().get("roles"))
                .stream().map(roleName -> roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return returnValue;
    }

}
