
package org.springframework.samples.petclinic.gamePlayer;


import java.util.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class GamePlayerService {

	private GamePlayerRepository gamePlayerRepository;


	@Autowired
	public GamePlayerService(GamePlayerRepository gamePlayerRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
	}
	@Transactional(readOnly = true)
	public List<GamePlayer> findAll(){
		return gamePlayerRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<GamePlayer> findById(Integer i){
		return gamePlayerRepository.findById(i);
	}

	@Transactional(readOnly = true)
	public GamePlayer getGamePlayerByPlayer(Player player) {
		return gamePlayerRepository.getGamePlayerByPlayer(player);
	}

	@Transactional
	public GamePlayer save(GamePlayer gamePlayer){
		return gamePlayerRepository.save(gamePlayer);
	}

	public GamePlayerService() {}

	public GamePlayer saveGamePlayerForNewPlayer(@Valid Player player) {
		GamePlayer gamePlayer = new GamePlayer();
		gamePlayer.setPlayer(player);
		return save(gamePlayer);
	}

	
}
