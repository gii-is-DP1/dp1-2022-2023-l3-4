package org.springframework.samples.petclinic.statistics;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {
  private GameService gameService;
  private GamePlayerService gamePlayerService;
  public static final String STATISTICS_LISTING = "player/playerProfile";
  public static final String EDIT_STATISTICS = "player/updatePlayerStatistics";
  public static final String RANKING = "statistics/ranking";

  @Autowired
  public StatisticsController(GameService gs, GamePlayerService gps) {
    this.gameService = gs;
    this.gamePlayerService = gps;
  }

  @GetMapping("/ranking/global")
  public String getPlayersRanking(ModelMap model) {
    List<PlayerCount> stats = gameService.getRanking();
    List<PlayerCount> top3 = stats.subList(0, 3);
    List<PlayerCount> restOfPlayers = stats.subList(3, stats.size());
    model.put("top3", top3);
    model.put("rops", restOfPlayers);

    // Total Duration
    List<Duration> durations = gameService.listTerminateGames().stream().map(x -> x.getDuration()).collect(Collectors.toList());
    Duration totalTimePlayed = Duration.ZERO;
    for (Duration d: durations) {
      totalTimePlayed = totalTimePlayed.plus(d);
    }

    model.put("duration", gameService.humanReadableDuration(totalTimePlayed));

    // Average duration
    Double average = durations.stream().mapToLong(x -> x.toSeconds()).average().getAsDouble();
    Duration avgDuration = Duration.ofSeconds(Long.valueOf(average.toString().replace(".0", "")));
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
    List<LocalDateTime> gamesDay = gameService.listTerminateGames().stream().map(x -> x.getInitialHour()).collect(Collectors.toList());
    Map<LocalDateTime, Integer> map = new HashMap<>();
    for (LocalDateTime l: gamesDay) {
      if (map.containsKey(l)) {
        map.put(l, map.get(l) + 1);
      } else {
        map.put(l, 1);
      }
    }

    Entry<LocalDateTime, Integer> maxGamesPlayed = map.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
    model.put("maxGames", maxGamesPlayed);
    
    // Minimum games played
    Entry<LocalDateTime, Integer> minGamesPlayed = map.entrySet().stream().min(Map.Entry.comparingByValue()).orElse(null);
    model.put("minGames", minGamesPlayed);

    return RANKING;
  }
  
}
