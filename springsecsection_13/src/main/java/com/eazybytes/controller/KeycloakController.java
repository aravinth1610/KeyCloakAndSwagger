package com.eazybytes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybytes.config.KeycloakSecurityUntil;
import com.eazybytes.config.Role;
import com.eazybytes.config.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class KeycloakController {

	private KeycloakSecurityUntil keySecUntil;

	
	public KeycloakController(KeycloakSecurityUntil keySecUntil) {
		this.keySecUntil = keySecUntil;
	}

	@Value("${kc.realm}")
	private String realm;

	@SecurityRequirement(name = "keycloak")
	@GetMapping("/keycloak/users")
	private List<User> getMethodName() {

		Keycloak kc = this.keySecUntil.getKeyCloakInstance();
		List<UserRepresentation> userRep = kc.realm(realm).users().list();

		return mapUsers(userRep);
	}

	@SecurityRequirement(name = "keycloak")
	@GetMapping("/keycloak/user/{id}")
	private User getMethodUserID(@PathVariable(value = "id") String id) {

		Keycloak kc = this.keySecUntil.getKeyCloakInstance();
		UserRepresentation userRep = kc.realm(realm).users().get(id).toRepresentation();
		System.out.println(userRep);
		return mapUser(userRep);
	}

	@PostMapping("/createuser")
	private ResponseEntity<?> createUser(User User) {
		UserRepresentation userRep = mapUserRep(User);
		Keycloak kc = this.keySecUntil.getKeyCloakInstance();
		kc.realm(realm).users().create(userRep);
		return new ResponseEntity<User>(User, HttpStatus.OK);
	}

	@SecurityRequirement(name ="keycloak")
	@PutMapping("/keycloak/user")
	private ResponseEntity<?> putUser(User User) {
		UserRepresentation userRep = mapUserRep(User);
		Keycloak kc = this.keySecUntil.getKeyCloakInstance();
		kc.realm(realm).users().get(User.getId()).update(userRep);
		return new ResponseEntity<User>(User, HttpStatus.OK);
	}

	@SecurityRequirement(name = "keycloak")
	@DeleteMapping("/keycloak/user/{id}")
	private ResponseEntity<?> deleteMethodUserID(@PathVariable(value = "id") String id) {
		Keycloak kc = this.keySecUntil.getKeyCloakInstance();
		kc.realm(realm).users().delete(id);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@SecurityRequirement(name = "keycloak")
	@GetMapping("/keycloak/role")
	private List<Role> getMethodListRoles() {

		Keycloak kc = this.keySecUntil.getKeyCloakInstance();
		List<RoleRepresentation> userRep = kc.realm(realm).roles().list();

		return mapRoles(userRep);
	}
	
	@SecurityRequirement(name = "keycloak")
	@PostMapping("/keycloak/role")
	private ResponseEntity<?> createRole(Role role) {
		RoleRepresentation userRep = mapRoleRep(role);
		Keycloak kc = this.keySecUntil.getKeyCloakInstance();
		kc.realm(realm).roles().create(userRep);
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	@SecurityRequirement(name = "keycloak")
	@GetMapping("/keycloak/role/{rolename}")
	private Role getMethodByRolesName(@PathVariable(value = "rolename") String rolename) {

		Keycloak kc = this.keySecUntil.getKeyCloakInstance();
		RoleRepresentation roleRep = kc.realm(realm).roles().get(rolename).toRepresentation();
		System.out.println(roleRep);
		return mapRole(roleRep);
	}
	
	//Setting CLAIMS
	@SecurityRequirement(name = "keycloak")
	@GetMapping("/keycloak/claims/{id}")
	private ResponseEntity<?> SettingMapperAndAttribute(@PathVariable(value = "id") String id) {
		addClientUsersMapper(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private List<Role> mapRoles(List<RoleRepresentation> roleRep) {

		List<Role> roles = new ArrayList<Role>();
		if (CollectionUtil.isNotEmpty(roleRep)) {
			roleRep.forEach(role -> {
				roles.add(mapRole(role));
			});
		}
		return roles;
	}

	private Role mapRole(RoleRepresentation roleRep) {
		Role role = new Role();
		role.setId(roleRep.getId());
		role.setName(roleRep.getName());
		return role;
	}
	
	private RoleRepresentation mapRoleRep(Role role) {
		RoleRepresentation roleRep = new RoleRepresentation();
		roleRep.setName(role.getName());
        return roleRep;		
	}

	private List<User> mapUsers(List<UserRepresentation> userRep) {

		List<User> users = new ArrayList<User>();
		if (CollectionUtil.isNotEmpty(userRep)) {
			userRep.forEach(user -> {
				users.add(mapUser(user));
			});
		}
		return users;
	}

	private User mapUser(UserRepresentation userRep) {
		User user = new User();
		user.setId(userRep.getId());		
		user.setFirstName(userRep.getFirstName());
		user.setLastName(userRep.getLastName());
		user.setEmail(userRep.getEmail());
		user.setUserName(userRep.getUsername());
		return user;
	}

	private UserRepresentation mapUserRep(User user) {

		UserRepresentation userRep = new UserRepresentation();
		userRep.setFirstName(user.getFirstName());
		userRep.setLastName(user.getLastName());
		userRep.setUsername(user.getUserName());
		userRep.setEmail(user.getEmail());
		userRep.setEmailVerified(true);
		userRep.setEnabled(true); // enable the user
		List<CredentialRepresentation> Listcred = new ArrayList<CredentialRepresentation>();
		CredentialRepresentation cred = new CredentialRepresentation();
		cred.setTemporary(false);
		cred.setValue(user.getPassword());
		Listcred.add(cred);
		userRep.setCredentials(Listcred);
		return userRep;
	}

	
	private void addClientUsersMapper(String userId) {
		Keycloak kc = this.keySecUntil.getKeyCloakInstance();
		RealmResource realmResource = kc.realm(realm);
		ClientRepresentation client = realmResource.clients().findByClientId("springboot-dev").get(0);
		
		 // Define a new mapper
        ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
        
        System.out.println(mapper.getName());
        mapper.setName("adminMapper"); // Mapper name
        mapper.setProtocol("openid-connect"); // Protocol
        mapper.setProtocolMapper("oidc-usermodel-attribute-mapper"); // Mapper type
     // Set the configuration for the mapper
        Map<String, String> config = new HashMap<>();
        config.put("user.attribute", "adminF");
        config.put("claim.name", "adminF"); // Set the token claim name
        config.put("access.token.claim", "true"); // Set to add to access token
        
        mapper.setConfig(config);

        // Add the mapper to the client
        realmResource.clients().get(client.getId()).getProtocolMappers().createMapper(mapper);

        
        
        //Single User
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        // Set the custom attribute value for the user
        //We can also Add Object to this.
        userRepresentation.getAttributes().put("adminF", Collections.singletonList("what need to do here"));
        userResource.update(userRepresentation);
        
        
        /* Multiple User
        // Get the list of users
        UsersResource usersResource = realmResource.users();

        // Iterate over users and set the custom attribute
        usersResource.list().forEach(user -> {
            UserResource userResource = usersResource.get(user.getId());
            UserRepresentation userRepresentation = userResource.toRepresentation();
            // Check if attributes map is null, initialize it if necessary
            if (userRepresentation.getAttributes() == null) {
                userRepresentation.setAttributes(new HashMap<>());
            }
            userRepresentation.getAttributes().put("employeeIdF", Collections.singletonList("12345"));
            userResource.update(userRepresentation);
        });
		*/
	}
	
	
}

class att{
	String name="ram";
	String roleno="123";
}

