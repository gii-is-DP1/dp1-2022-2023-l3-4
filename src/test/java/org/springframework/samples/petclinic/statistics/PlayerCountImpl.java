package org.springframework.samples.petclinic.statistics;

import org.springframework.samples.petclinic.player.Player;

public class PlayerCountImpl implements PlayerCount{
    public Player player;
    public Integer numWonGames;

    PlayerCountImpl(Player p, Integer n) {
        this.player=p;
        this.numWonGames=n;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Integer getNumWonGames() {
        return numWonGames;
    }
    
}
