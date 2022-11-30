package org.springframework.samples.virus.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.virus.player.Player;
import org.springframework.samples.virus.player.PlayerService;
import org.springframework.samples.virus.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	@Autowired
	private PlayerService playerService;
	private UserService userService;
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public User getUser(){
		
		return (User) getAuthentication().getPrincipal();
	}
	
	public Player getPlayer() {
		return playerService.getPlayerByUsername(getUser().getUsername());	
	}
	
}