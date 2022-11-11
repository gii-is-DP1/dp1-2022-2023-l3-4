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

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.statistics.Statistics;
import org.springframework.samples.petclinic.statistics.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private static final String USER_PROFILE ="player/playerProfile"; 


    @Autowired
    public PlayerController(PlayerService ps, StatisticsService ss) {
        this.playerService = ps;
        this.statisticsService = ss;
    }


    @GetMapping("/me")
        public String listPlayerStatistics(ModelMap model) {
        Player player = authenticationService.getPlayer();
        List<Statistics> playerStatistics = statisticsService.findPlayerStatistics(player);
        model.put("statistics", playerStatistics);
        model.put("player", player);
        return USER_PROFILE;
  }
	
}
