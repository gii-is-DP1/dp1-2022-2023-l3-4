package org.springframework.samples.petclinic.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

  private StatisticsService statisticsService;
  private PlayerService playerService;
  private AuthenticationService authenticationService;
  public static final String STATISTICS_LISTING = "player/playerProfile";
  public static final String EDIT_STATISTICS = "player/updatePlayerStatistics";

  @Autowired
  public StatisticsController(StatisticsService ss, PlayerService ps, AuthenticationService as) {
    this.statisticsService = ss;
    this.playerService = ps;
    this.authenticationService = as;
  }

  @GetMapping("/player/edit")
  public String editPlayerStatistics(ModelMap model) {

    Player player = authenticationService.getPlayer();
    if (player != null) {
      model.put("player", player);
      return EDIT_STATISTICS;
    } else {
      model.put("message", "Cannot find player statistics, because the player does not exist");
      model.put("messageType", "info");
    }

    return "redirect:/";

  }
  
}
