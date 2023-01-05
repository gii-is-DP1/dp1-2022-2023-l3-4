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
package org.springframework.samples.petclinic.player;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.friendRequest.FriendService;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.statistics.Statistics;
import org.springframework.samples.petclinic.statistics.StatisticsService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@RequestMapping("/player")
public class PlayerController {


    private PlayerService playerService;
    private StatisticsService statisticsService;
    private GameService gameService;
    private AuthenticationService authenticationService;
    private FriendService friendService;

    private static final String USER_PROFILE ="player/playerProfile"; 
    private static final String EDIT_PROFILE = "player/createOrUpdateProfileForm";
    private static final String VIEW_FIND_PLAYER = "player/searchPlayer";
    private static final String VIEW_LIST_PLAYER = "player/playerListing";


    @Autowired
    public PlayerController(PlayerService ps, StatisticsService ss, AuthenticationService as, FriendService fs, GameService gs) {
        this.playerService = ps;
        this.statisticsService = ss;
        this.authenticationService = as;
        this.friendService=fs;
        this.gameService = gs;
    }


    @GetMapping("/me")
        public String listPlayerStatistics(ModelMap model) {
        Player player = authenticationService.getPlayer();
        Statistics playerStatistics = statisticsService.findPlayerStatistics(player);
        GamePlayer gp = gameService.findGamePlayerByPlayer(player);
        List<Game> games = gameService.listGames().stream().filter(x -> x.getGamePlayer().contains(gp)).collect(Collectors.toList());
        List<Duration> durations = games.stream().map(x -> x.getDuration()).collect(Collectors.toList());
        Duration totalTimePlayed = Duration.ZERO;
        for (Duration d: durations) {
            totalTimePlayed = totalTimePlayed.plus(d);
        }
        model.put("statistics", playerStatistics);
        model.put("player", player);
        model.put("gameplayer", gp);
        model.put("games", games);
        model.put("totalTimePlayed", gameService.humanReadableDuration(totalTimePlayed));
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
    public String postMethodName(@Valid Player player, BindingResult bindingResult, ModelMap model) {

        if (bindingResult.hasErrors()) {
            model.put("message", "The name cannot be empty");
            model.put("messageType", "info");
        } else {
            Player playerToUpdate = authenticationService.getPlayer();
            if (playerToUpdate != null) {
                BeanUtils.copyProperties(player, playerToUpdate, "id", "user");
                playerService.savePlayer(playerToUpdate);
                model.put("message", "Your player information has been updated successfully");
                return listPlayerStatistics(model);
            }
        }
        return "redirect:/player/me";
    }

    @GetMapping("/createSearch")
	public String findPlayer(ModelMap model) {
        Player player = new Player();
		model.put("player", player);
		return VIEW_FIND_PLAYER;
	}

    @PostMapping(value = "/createSearch")
	public String processFindRoomForm(Player player, BindingResult result, ModelMap model) {
        Player playerAuth = authenticationService.getPlayer();
		if (player.getUser().getUsername() == null) {
            User user=player.getUser();
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
