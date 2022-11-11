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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/games")
public class GameController {

	private final GameService gameService;

	@Autowired
	public GameController(GameService gameService) {
		this.gameService = gameService;
  }
  private static final Logger log = LoggerFactory.getLogger(GameController.class);
	private final GameService gameService;
	private final GamePlayerService gamePlayerService;

	@Autowired
	public GameController(GameService gameService,GamePlayerService gamePlayerService) {
		this.gameService = gameService;
		this.gamePlayerService= gamePlayerService;
	}
	
	@GetMapping
	public List<Game> ListGames(){
		return gameService.ListGames();
	}
}
	//Si la baraja se queda sin cartas, se rellena con las ya descartadas

	public void rellenaBaraja(@PathVariable("gameId") int gameId){
		Game currentGame = gameService.findGames(gameId);
		List<Card> playedcards = currentGame.getCards().stream().filter(x->x.getPlayed()).collect(Collectors.toList());
		Collections.shuffle(playedcards);
		currentGame.setCards(playedcards);
		gameService.save(currentGame);
	}
	public String reparteCartas(@PathVariable("gameId") int gameId) {
		Game currentGame = gameService.findGames(gameId); 
		List<Card> baraja = currentGame.getCards();
		if(baraja.size()==0) { //Si no quedan cartas en la baraja llamamos a shuffle
			log.info("Rellenando baraja");
			rellenaBaraja(gameId);
		}
		log.info("Repartiendo cartas");
		for(GamePlayer jugador: currentGame.getGamePlayer()) {
			List<Card> cartasJugador = jugador.getCards();
			while(cartasJugador.size()<3){
				Card card = baraja.get(0);
				cartasJugador.add(card); //Se la añadimos al jugador
				baraja.remove(0);	//La quitamos del mazo
			}
			jugador.setCards(cartasJugador);//Cuando ya tenga 3 cargas se guarda en el jugador
				gamePlayerService.save(jugador);

			}
			currentGame.setCards(baraja); //Cuando ya se han repartido a todos los jugadores guardamos el mazo resultante
			currentGame.setRound(currentGame.getRound()+1); //Añadimos una ronda al juego y guardamos
			gameService.save(currentGame);	
			log.info("Cartas repartidas correctamente");
			return "redirect:/games/{gameId}/play";
		}

		@GetMapping(value= "/games/{gameId}/clasificacion")
		public String clasificacion(@PathVariable("gameId") int gameId,Map<String, Map<Integer, List<GamePlayer>>> model) {
			
			Game game = this.gameService.findGames(gameId);
			game.setIsRunning(false);
			this.gameService.save(game);
			Map<Integer,List<GamePlayer>> classification = new HashMap<>();
			log.info("Clasificando");
			List<GamePlayer> gamePlayers = game.getGamePlayer();
			for(GamePlayer gamePlayer : gamePlayers){
				List<Card> body = gamePlayer.getCards().stream().filter(x->x.getBody()).collect(Collectors.toList());
					if(body.size()==4 && body.stream().allMatch(x->x.getVirus().size()==0)){
						gamePlayer.setWinner(true);
						classification.put(1, List.of(gamePlayer));
					} else if(body.size()==3 && body.stream().allMatch(x->x.getVirus().size()==0) || 
					body.size()==4 && body.stream().filter(x->x.getVirus().size()==0).collect(Collectors.toList()).size()==3){
						if(classification.containsKey(2)){
							classification.get(2).add(gamePlayer);
						}else{
							classification.put(2, List.of(gamePlayer));
						}
					} else if(body.size()==2 && body.stream().allMatch(x->x.getVirus().size()==0) || 
					body.size()==4 && body.stream().filter(x->x.getVirus().size()==0).collect(Collectors.toList()).size()==2 ||
					body.size()==3 && body.stream().filter(x->x.getVirus().size()==0).collect(Collectors.toList()).size()==2){
						if(classification.containsKey(3)){
							classification.get(3).add(gamePlayer);
						}else{
							classification.put(3, List.of(gamePlayer));
						}
					}
				}
				game.setClassification(classification);
				gameService.save(game);
				model.put("clasificacion", classification);
				return "rondas/clasificacion";
				}
	
		
		@GetMapping(value="/games/{gameId}/play")
		public String play(@PathVariable("gameId") int gameId){
			Game game = gameService.findGames(gameId);
			List<GamePlayer> gamePlayers = game.getGamePlayer();
			Integer currentPlayer = game.getRound();
			for(Integer i=0; i<gamePlayers.size();i++){
				if(i==currentPlayer){
					return "redirect:/game/{gameId}/gamePlayer/{currentPlayer}";
				}
			}

			return "ha surgido un error";

		}
	
	}
