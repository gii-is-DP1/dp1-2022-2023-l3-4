package org.springframework.samples.petclinic.player;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.samples.petclinic.achievements.Achievement;
import org.springframework.samples.petclinic.achievements.AchievementService;
import org.springframework.samples.petclinic.friendRequest.FriendService;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Francisco Sebastian Benitez Ruis Diaz
 * @author Jose Maria Garcia Berdejo
 */
@Controller
@RequestMapping("/player")
public class PlayerController {


    private PlayerService playerService;
    private GameService gameService;
    private AuthenticationService authenticationService;
    private FriendService friendService;
    private AchievementService achievementService;
    private UserService userService;

    private static final String USER_PROFILE ="player/playerProfile"; 
    private static final String EDIT_PROFILE = "player/createOrUpdateProfileForm";
    private static final String VIEW_FIND_PLAYER = "player/searchPlayer";
    private static final String VIEW_LIST_PLAYER = "player/playerListing";


    @Autowired
    public PlayerController(PlayerService ps, AuthenticationService as, FriendService fs, GameService gs, AchievementService acs, UserService userService) {
        this.playerService = ps;
        this.authenticationService = as;
        this.friendService=fs;
        this.gameService = gs;
        this.achievementService = acs;
        this.userService = userService;
    }


    @GetMapping("/me")
        public String playerProfile(ModelMap model, @RequestParam(value = "page", required = false) Integer page) {
        Player player = authenticationService.getPlayer();
        Pageable pageable = null;
        if(page == null || page == 0) {
            pageable = PageRequest.of(0, 10, Sort.by(Order.desc("initialHour")));
        } else {
            pageable = PageRequest.of(page, 10, Sort.by(Order.desc("initialHour")));
        }

        GamePlayer gp = authenticationService.getGamePlayer();
        Integer numGamesPlayed = gameService.getNumGamesPlayed(gp);
        Integer numGamesWon = gameService.getNumGamesWon(gp);
        Page<Game> games = gameService.findGamesByGameplayerPaged(gp, pageable);
        Duration totalTimePlayed = games.stream().map(x -> x.getDuration()).reduce((x,y) -> x.plus(y)).orElse(Duration.ZERO);
        List<Achievement> achievements = achievementService.getPlayerAchievements(player);
       
        model.put("numGamesPlayed",numGamesPlayed);
        model.put("numGamesWon",numGamesWon);
        model.put("player", player);
        model.put("gameplayer", gp);
        model.put("games", games.getContent());
        model.put("totalPages", games.getTotalPages());
        model.put("currentPage", games.getNumber());
        model.put("totalTimePlayed", gameService.humanReadableDuration(totalTimePlayed));
        model.put("achievements", achievements);
        return USER_PROFILE;
  }

    @GetMapping("/me/edit")
    public String getPlayer(ModelMap model) {
        Player player = authenticationService.getPlayer();
        if (player != null) {
            model.put("player", player);
        }
        return EDIT_PROFILE;
    }

    @PostMapping(value="/me/edit")
    public String savePlayer(@Valid Player player, BindingResult bindingResult, ModelMap model) {

        if (bindingResult.hasFieldErrors("firstName")) {
            bindingResult.rejectValue("firstName", "first name cannot be empty.", "first name cannot be empty.");
            return EDIT_PROFILE;
        } else if (bindingResult.hasFieldErrors("lastName")) {
            bindingResult.rejectValue("lastName", "Last name cannot be empty.", "first name cannot be empty.");
            return EDIT_PROFILE;
        } else {
            Player playerToUpdate = authenticationService.getPlayer();
            if (playerToUpdate != null) {
                BeanUtils.copyProperties(player, playerToUpdate, "id", "user", "friendRec", "friendSend", "friendInvitationSend", "friendInvitationRec", "achievements", "room", "gamePlayer");
                if(player.getUser() == null || player.getUser().getPassword()==null || player.getUser().getPassword().equals("")) {
                    bindingResult.rejectValue("user.password", "Password cannot be empty.", "Password cannot be empty.");
                    return EDIT_PROFILE;
                } else {
                    User userToUpdate = playerToUpdate.getUser();
                    userToUpdate.setPassword(player.getUser().getPassword());
                    userService.updateUser(userToUpdate);
                    playerService.savePlayer(playerToUpdate);
                    model.put("message", "Your player information has been updated successfully");
                    model.put("messageType", "info");
                    return playerProfile(model, null);
                }
                
            } else {
                model.put("message", "You need to be logged in to change your player information.");
                model.put("messageType", "info");
                return "welcome";
            }
        }
    }

    @GetMapping("/search")
	public String findPlayer(ModelMap model) {
        Player player = new Player();
		model.put("player", player);
		return VIEW_FIND_PLAYER;
	}

    @PostMapping(value = "/search")
	public String processFindPlayerForm(Player player, BindingResult result, ModelMap model) {
        Player playerAuth = authenticationService.getPlayer();
		if (player.getUser() == null || player.getUser().getUsername() == null) {
            User user= new User();
            user.setUsername("");
			player.setUser(user);
		}
		// find player by userName
		Collection<Player> players = playerService.getPlayersByUsername(player.getUser().getUsername());
        players.remove(playerAuth);
		if (players.isEmpty()) {
			// no player found
			result.rejectValue("User.username", "notFound", "not found");
			return VIEW_FIND_PLAYER;
		}

		else {
			// multiple player found
            Collection<Player> myFriends=friendService.findFriendsById(playerAuth.getId());
            players.removeAll(myFriends);
			model.put("players", players);
			return VIEW_LIST_PLAYER;
		}
	}
	
}
