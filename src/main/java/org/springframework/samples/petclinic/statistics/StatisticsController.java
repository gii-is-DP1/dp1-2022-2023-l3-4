package org.springframework.samples.petclinic.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

  private StatisticsService statisticsService;
  private PlayerService playerService;
  public static final String STATISTICS_LISTING = "statisctics/StatisticsListing";

  @Autowired
  public StatisticsController(StatisticsService statisticsService, PlayerService playerService) {
    this.statisticsService = statisticsService;
    this.playerService = playerService;
  }

  @GetMapping("/{username}")
  public String listPlayerStatistics(@PathVariable("usename") String username, ModelMap model) {
    Player player = playerService.getPlayerByUsername(username);
    List<Statistics> playerStatistics = statisticsService.findPlayerStatistics(player);
    model.put("statistics", playerStatistics);
    model.put("player", player);
    return STATISTICS_LISTING;
  }

  
  
}
