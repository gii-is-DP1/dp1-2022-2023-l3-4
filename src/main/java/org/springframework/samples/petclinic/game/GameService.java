
package org.springframework.samples.petclinic.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

	private GameRepository gameRepository;

	@Autowired
	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Transactional(readOnly = true)
	public List<Game> ListGames(){
		return gameRepository.findAll();
	}
	@Transactional(readOnly = true)
	public Game findGames(Integer i){
		return gameRepository.findById(i).get();
	}

	@Transactional
	public void save(Game game){
		gameRepository.save(game);

	}
}
