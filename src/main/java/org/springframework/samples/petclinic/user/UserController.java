/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.user;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.statistics.StatisticsService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class UserController {

	private static final String VIEWS_PLAYER_CREATE_FORM = "users/createPlayerForm";
	private static final String USERS = "users/listPageableUsers";

	private UserService userService;
	private PlayerService playerService;
	private StatisticsService statisticsService;
	private AuthoritiesService authoritiesService;

	@Autowired
	public UserController(UserService userService, PlayerService playerService, StatisticsService statisticsService, AuthoritiesService authoritiesService) {
		this.userService = userService;
		this.playerService = playerService;
		this.statisticsService = statisticsService;
		this.authoritiesService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "status");
	}

	@GetMapping(value = "/users/new")
	public String initCreationForm(Map<String, Object> model) {
		Player player = new Player();
		model.put("player", player);
		return VIEWS_PLAYER_CREATE_FORM;
	}

	@PostMapping(value = "/users/new")
	public String processCreationForm(@Valid Player player, BindingResult result) throws PlayerNotFoundException {
		if (result.hasErrors()) {
			return VIEWS_PLAYER_CREATE_FORM;
		}
		else {
			//creating player, user, and authority
			this.playerService.savePlayer(player);
			this.userService.saveUser(player.getUser());
			this.authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");
			statisticsService.saveStatisticsForNewPlayer(player);
			return "redirect:/";
		}
	}

	@GetMapping("/users")
	public String findAll(ModelMap model) {
		Page<User> pg = userService.findAll(PageRequest.of(0, 5));
		model.put("pages", pg);
		return USERS;
	} 


}
