package org.springframework.samples.petclinic.user;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Francisco Sebastian Benitez Ruis Diaz
 * @author Jose Maria Garcia Berdejo
 */
@Controller
public class UserController {

	private static final String CREATE_OR_UPDATE_PLAYER = "player/createPlayerForm";
	private static final String USERS = "users/usersListing";
	private static final String INVALID_USER = "users/invalidUser";
	private static final String EDIT_USER = "player/createOrUpdateProfileForm";
	

	private UserService userService;
	private PlayerService playerService;
	private GamePlayerService gamePlayerService;
	private AuthoritiesService authoritiesService;

	@Autowired
	public UserController(UserService userService, PlayerService playerService, GamePlayerService gamePlayerService, AuthoritiesService authoritiesService) {
		this.userService = userService;
		this.playerService = playerService;
		this.gamePlayerService = gamePlayerService;
		this.authoritiesService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "status");
	}

	@GetMapping(value = "/user/new")
	public String initCreationForm(Map<String, Object> model) {
		Player player = new Player();
		model.put("player", player);
		return CREATE_OR_UPDATE_PLAYER;
	}

	@PostMapping(value = "/user/new")
	public String processCreationForm(@Valid Player player, BindingResult bindingResult) throws PlayerNotFoundException {
		
		if (bindingResult.hasFieldErrors("firstName")) {
			bindingResult.rejectValue("firstName", "First name cannot be empty.", "First name cannot be empty.");
			return CREATE_OR_UPDATE_PLAYER;
		} else if (bindingResult.hasFieldErrors("lastName")) {
			bindingResult.rejectValue("lastName", "Last name cannot be empty.", "Last name cannot be empty.");
			return CREATE_OR_UPDATE_PLAYER;
		}	else {
			//creating player, gamePlayer, user, and authority
			if (player.getUser().getPassword() == null) {
				bindingResult.rejectValue("user.password", "Password cannot be empty.", "Password cannot be empty.");
				return CREATE_OR_UPDATE_PLAYER;
			} else {
				try {
					this.userService.saveUser(player.getUser());
					this.playerService.savePlayer(player);
					this.gamePlayerService.saveGamePlayerForNewPlayer(player);
					this.authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");			
				} catch (DuplicatedUserException e) {
					return INVALID_USER;
				}
				return "redirect:/";
			}
		}

	}

	@GetMapping("/users")
	public String findAll(ModelMap model, @RequestParam(value = "page", required = false) Integer page) {
		Pageable pageable = null;
		if(page == null || page == 0) {
			pageable = PageRequest.of(0, 5, Sort.by(Order.asc("user.username")));
		} else {
			pageable = PageRequest.of(page, 5, Sort.by(Order.asc("user.username")));
		}
		
		Page<Player> players = playerService.findAll(pageable);
		if (players.getContent().size() >= 1) {
			model.put("players", players.getContent());
			model.put("totalPages", players.getTotalPages());
			model.put("currentPage", players.getNumber());
		} else {
			model.put("message", "There are no users registered in the system");
			model.put("messageType", "info");
		}
		return USERS;
	}

	@GetMapping("/users/{username}/edit")
	public String editUser(ModelMap model, @PathVariable("username") String username) {
		Player player = playerService.getPlayerByUsername(username);
    if (player != null) {
      model.put("player", player);
      return EDIT_USER;
    } else {
      model.put("message", "The player " + username + " doesn't exist");
      model.put("messageType", "info");
      return "redirect:/user";
    }
	}

	@PostMapping("/users/{username}/edit")
	public String saveUser(@PathVariable("username") String username, @Valid Player player, BindingResult bindingResult, ModelMap model) {
		if (bindingResult.hasFieldErrors("firstName")) {
			bindingResult.rejectValue("firstName", "First name cannot be empty.", "First name cannot be empty.");
			return EDIT_USER;
		} else if (bindingResult.hasFieldErrors("lastName")) {
			bindingResult.rejectValue("lastName", "Last name cannot be empty.", "Last name cannot be empty.");
			return EDIT_USER;
		} else {
			Player playerToUpdate = playerService.getPlayerByUsername(username);
			if (playerToUpdate != null) {
				BeanUtils.copyProperties(player, playerToUpdate, "id", "user", "friendRec", "friendSend", "friendInvitationSend", "friendInvitationRec", "achievements", "room", "gamePlayer");
				if(player.getUser().getPassword()==null || player.getUser().getPassword().equals("")) {
						bindingResult.rejectValue("user.password", "Password cannot be empty.", "Password cannot be empty.");
						return EDIT_USER;
				} else {
						User userToUpdate = playerToUpdate.getUser();
						userToUpdate.setPassword(player.getUser().getPassword());
						userService.updateUser(userToUpdate);
						playerService.savePlayer(playerToUpdate);
						model.put("message", "Your player information has been updated successfully");
						return findAll(model, null);
				}
			}
		}
		return "redirect:/users";
	}

	@GetMapping("/users/{username}/delete")
	public String deleteUser(@PathVariable("username") String username, ModelMap model) {

		String message;
    try {
      userService.deleteUser(username);
      message = "User " + username + " succesfully deleted";
    } catch (EmptyResultDataAccessException e) {
      message = "User " + username + " doesn't exist";
    }
    model.put("message", message);
    model.put("messageType", "info");
    return findAll(model, null);
	}


}
