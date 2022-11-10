package org.springframework.samples.petclinic.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	@Autowired
	private OwnerService ownerservice;
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public User getUser(){
		
		return (User) getAuthentication().getPrincipal();
	}
	
	public Owner getOwner() {
		Optional<Owner> owner=ownerservice.findOwnerByUsername(getUser().getUsername()).stream().findFirst();
		
		if(owner.isPresent()) {
			return owner.get();
		}else {
			return null;
		}
	}
	
}
