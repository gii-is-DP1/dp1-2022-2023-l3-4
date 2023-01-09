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
package org.springframework.samples.petclinic.game;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
// import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {

    List<Game> findAll(); 
	Optional<Game> findById(int id);
    @Query("SELECT gp FROM GamePlayer gp WHERE gp.player.id = :playerId")
    GamePlayer findGamePlayerByPlayer(@Param(value = "playerId") Integer playerId);

    @Query("SELECT g FROM Game g WHERE g.isRunning = true")
    public Collection<Game> findRunningGames();
    
    @Query("SELECT g FROM Game g WHERE g.isRunning = false")
    public Collection<Game> findTerminategGames();


    @Query("SELECT g FROM Game g WHERE g.room.id = :roomId")
    Collection<Game> findGameByRoomId(@Param(value = "roomId") Integer roomId);

    @Query("SELECT g FROM Game g WHERE :gamePlayer MEMBER OF g.gamePlayer")
    List<Game> findGamesByGameplayer(@Param(value = "gamePlayer") GamePlayer gamePlayer);

    @Query("SELECT g FROM Game g WHERE :gamePlayer MEMBER OF g.gamePlayer AND g.isRunning = false")
    Page<Game> findGamesByGameplayerPaged(@Param(value = "gamePlayer") GamePlayer gamePlayer, Pageable page);

}
