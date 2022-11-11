package org.springframework.samples.petclinic.user;

import org.ehcache.core.spi.service.StatisticsService;
import org.springframework.samples.petclinic.player.PlayerController;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.statistics.StatisticsController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/profile")
public class ProfileController {

  private StatisticsService statisticsService;
  private PlayerService playerService;

  @GetMapping(value="/")
  public String getPlayer() {

      return "redirect:/statistics/{username}";
  }
  
  
}
