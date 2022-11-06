package org.springframework.samples.petclinic.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

  private StatisticsService statisticsService;
  public static final String STATISTICS_LISTING = "statisctics/StatisticsListing";

  @Autowired
  public StatisticsController(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping("/")
  public String listAllStatistics(ModelMap model) {
    List<Statistics> allStatistics = statisticsService.getAllStatistics();
    model.put("statistics", allStatistics);
    return STATISTICS_LISTING;
  }

  
  
}
