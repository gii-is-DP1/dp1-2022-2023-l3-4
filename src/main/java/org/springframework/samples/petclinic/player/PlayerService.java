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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PlayerService {

    private PlayerRepository playerRepository;
    private GamePlayerService gamePlayerService;
    private GameService gameService;
    

    @Autowired
    public PlayerService(PlayerRepository playerRepository, GamePlayerService gamePlayerService, GameService gameService){
        this.playerRepository = playerRepository;
        this.gamePlayerService = gamePlayerService;
        this.gameService = gameService;
    }

    @Transactional
    public void savePlayer(Player player) throws DataAccessException {
        if(player.getStatus() == null) {
            player.setStatus(false);
        }
        //creating player
        playerRepository.save(player);
    }

    public Player getPlayerByUsername(String username) {
        return playerRepository.findPlayerByUsername(username);
    }

    @Transactional
    public Collection<Player> getPlayersByUsername(String username) {
        return playerRepository.findPlayersByUsername(username);
    }

    @Transactional
    public Collection<Player> getAllPlayers() {
        return (Collection<Player>) playerRepository.findAll();
    }

    @Transactional
    public Player findPlayerById(Integer id) {
        return playerRepository.findById(id).get();
    }

    @Transactional
    public void deletePlayer(Player player) {
        GamePlayer gp = gameService.findGamePlayerByPlayer(player);
        if (gp != null) {
            gp.setPlayer(null);
            gamePlayerService.deleteGamePlayer(gp);
        }
        playerRepository.delete(player);
    }


}
