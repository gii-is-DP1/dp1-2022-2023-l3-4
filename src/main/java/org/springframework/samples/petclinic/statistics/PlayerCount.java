package org.springframework.samples.petclinic.statistics;

import org.springframework.samples.petclinic.player.Player;

public interface PlayerCount {
    Player getPlayer();
    Integer getNumWonGames();
}
