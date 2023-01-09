package org.springframework.samples.petclinic.statistics;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {
  private AuthenticationService authenticationService;
  private GameService gameService;
  private GamePlayerService gamePlayerService;
  public static final String STATISTICS_LISTING = "player/playerProfile";
  public static final String EDIT_STATISTICS = "player/updatePlayerStatistics";
  public static final String RANKING = "statistics/globalStatistics";

  @Autowired
  public StatisticsController(AuthenticationService as, GameService gs, GamePlayerService gps) {
    this.authenticationService = as;
    this.gameService = gs;
    this.gamePlayerService = gps;
  }

  @GetMapping("/statistics/player/edit")
  public String editPlayerStatistics(ModelMap model) {

    Player player = authenticationService.getPlayer();
    if (player != null) {
      model.put("player", player);
      return EDIT_STATISTICS;
    } else {
      model.put("message", "Cannot find player statistics, because the player does not exist");
      model.put("messageType", "info");
      return "redirect:/";
    }
  }

  @GetMapping("/statistics/global")
  public String getGameStatistics(ModelMap model) {

    // Ranking
    List<PlayerCount> stats = gameService.getRanking();
    List<PlayerCount> top3 = stats.subList(0, 3);
    List<PlayerCount> restOfPlayers = stats.subList(3, stats.size());
    model.put("top3", top3);
    model.put("rops", restOfPlayers);

    // Total Duration
    List<Duration> durations = gameService.listGames().stream().map(x -> x.getDuration()).collect(Collectors.toList());
    Duration totalTimePlayed = Duration.ZERO;
    for (Duration d: durations) {
      totalTimePlayed = totalTimePlayed.plus(d);
    }

    model.put("duration", gameService.humanReadableDuration(totalTimePlayed));

    // Average duration
    Double average = durations.stream().mapToLong(x -> x.toMinutes()).average().getAsDouble();
    Duration avgDuration = Duration.ofMinutes(Long.valueOf(average.toString().replace(".0", "")));
    model.put("avgDuration", gameService.humanReadableDuration(avgDuration));

    // Maximum duration
    Duration maxDuration = durations.stream().max(Comparator.comparing(x -> x)).get();
    model.put("maxDuration", gameService.humanReadableDuration(maxDuration));

    // Minimum duration
    Duration minDuration = durations.stream().min(Comparator.comparing(x -> x)).get();
    model.put("minDuration", gameService.humanReadableDuration(minDuration));

    // Total Games Played
    Integer totalGamesPlayed = gameService.getTotalGamesPlayed();
    model.put("games", totalGamesPlayed);

    // Average games played
    Double avgGamesPlayed = gamePlayerService.findAll().stream().mapToInt(x -> x.getGames().size()).average().getAsDouble();
    model.put("avgGames", avgGamesPlayed.toString().replace(".0", ""));

    // Maximum games played
    List<LocalDateTime> gamesDay = gameService.listGames().stream().map(x -> x.getInitialHour()).collect(Collectors.toList());
    Map<LocalDateTime, Integer> map = new HashMap<>();
    for (LocalDateTime l: gamesDay) {
      if (map.containsKey(l)) {
        map.put(l, map.get(l) + 1);
      } else {
        map.put(l, 1);
      }
    }

    Entry<LocalDateTime, Integer> maxGamesPlayed = map.entrySet().stream().max(Map.Entry.comparingByValue()).get();
    model.put("maxGames", maxGamesPlayed);
    
    // Minimum games played
    Entry<LocalDateTime, Integer> minGamesPlayed = map.entrySet().stream().min(Map.Entry.comparingByValue()).get();
    model.put("minGames", minGamesPlayed);

    return RANKING;
  }
  
}
