
package org.springframework.samples.petclinic.gamePlayer;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Transactional
	public GamePlayer save(GamePlayer gamePlayer){
		return gamePlayerRepository.save(gamePlayer);
	}
	
}
