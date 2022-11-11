package org.springframework.samples.petclinic.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

  private StatisticsService statisticsService;
  private PlayerService playerService;
  public static final String STATISTICS_LISTING = "player/playerProfile";

  @Autowired
  public StatisticsController(StatisticsService statisticsService, PlayerService playerService) {
    this.statisticsService = statisticsService;
    this.playerService = playerService;
  }

  

  
  
}
