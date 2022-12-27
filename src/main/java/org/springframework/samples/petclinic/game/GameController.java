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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class GameController {
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	private final GameService gameService;
	private final GamePlayerService gamePlayerService;
	private final CardService cardService;

	//Constructor
	@Autowired
	public GameController(GameService gameService,GamePlayerService gamePlayerService, 
	CardService cardService) {
		this.gameService = gameService;
		this.gamePlayerService= gamePlayerService;
		this.cardService=cardService;

	}

	//Listar juegos
	@GetMapping(value="/games")
	public String ListGames(ModelMap model){
		List<Game> allGames=  gameService.ListGames();
		model.put("games", allGames);
		return "games/listing";
	}

	/* 
	public void init(Integer gameId) {
		Game game = new Game();
		game.setRound(0);
		game.setTurn(0);
		reset();
		List<Card> deck = new ArrayList<Card>(68);
		Game currentGame = gameService.findGames(gameId);
			int i = 0;
		for(Card c: deck){
			c.setId(i + gameId*100);
			c.setType(cards.get(i));
			i++;
		}
		currentGame.setCards(deck);
	}
	*/

	//Muestra vista Individual de cada jugador
	@GetMapping(value="/games/{gameId}/gamePlayer/{gamePlayerId}")
	public void muestraVista(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, ModelMap model){
		GamePlayer gp_vista= gamePlayerService.findById(gamePlayerId).get();		
		for(GamePlayer gp: gameService.findGames(gameId).getGamePlayer()){
			if(gp== gp_vista){
				model.put("hand", gp.getHand());
			}
			model.put("body"+gp.getId(), gp.getBody());
		}
	}

	//Cambio de turno
	public String turn(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId){
			Game game = gameService.findGames(gameId); //Encontramos el juego
			GamePlayer gamePlayer = gamePlayerService.findById(gamePlayerId).get();
			if(gamePlayer.isWinner()){ //Si ya alguien ganó, finalizar la partida
				ModelMap model = new ModelMap();
				return clasification(gameId, model);
			}else{
				return gameService.changeTurn(game);
			}

		}

	//Decidir entre jugar o descartar
	@GetMapping(value="/games/{gameId}/gamePlayer/{currentPlayer}/decision/{decision}")
	public String decision(@PathVariable("gameId") int gameId, @PathVariable("currentPlayer") int currentPlayer, @PathVariable("decision") int decision){
			if(decision==0){ //Desición de jugar
				return "/games/"+gameId+"/gamePlayer"+currentPlayer+"/play";
			} else{ //Desición de descartar
				return "/games/"+gameId+"/gamePlayer"+currentPlayer+"/discard";
			}
		}

	//Jugar
	@GetMapping(value="/games/{gameId}/gamePlayer/play/{cardId}")
	public String play(@PathVariable("gameId") int gameId, @PathVariable("currentPlayer") int currentPlayer, Integer cardId){
		Optional<Card> c = cardService.findCard(cardId);
		Optional<GamePlayer> gp = gamePlayerService.findById(gameId);
		if(c.isPresent() && gp.isPresent()){
			if(gp.get().getCards().contains(c.get())){
				switch(c.get().getType().toString()){
					case("ORGAN"):
					log.info("Elige el jugador a quien quieras dar este órgano");
					case("VIRUS"):
					log.info("Elige el órgano que quieras infectar");
					case("VACCINE"):
					log.info("Elige el órgano que quieras vacunar");
					case("TRANSPLANT"):
					log.info("Selecciona qué dos organos quieres intercambiar");
				}
				return "/games/{gameId}/gamePlayer/play/selecciona";
			}else{
				log.error("Debes jugar una carta que esté en tu mano");
				return "/games/"+gameId+"/gamePlayer/"+currentPlayer+"/decision";
			}
		} else{
			log.error("Movimiento no válido");
			return "/games/"+gameId+"/gamePlayer/"+currentPlayer+"/decision";
		}
	}

	//Jugar un órgano
	public String playOrgan(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, Integer g_id, Integer c_id){
		Optional<Card> c = cardService.findCard(c_id);
		Optional<GamePlayer> gp1 = gamePlayerService.findById(gamePlayerId);
		Optional<GamePlayer> gp2 = gamePlayerService.findById(g_id);		
		if(c.isPresent() && gp2.isPresent() && gp1.isPresent()){
				Card organ = c.get();
				GamePlayer gplayer1 = gp1.get();
				GamePlayer gplayer2 = gp2.get();
					if(gplayer2.isThisOrganNotPresent(organ)){
						gplayer1.getCards().remove(organ);
						gamePlayerService.save(gplayer1);
						organ.setGamePlayer(gplayer2);
						organ.setBody(true);
						cardService.save(organ);
						gplayer2.getCards().add(organ);
						gamePlayerService.save(gplayer2);
						return turn(gameId,gamePlayerId);
					}else{
						log.error("No puede poner dos órganos del mismo color en un cuerpo");
						return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
					}	
	
		}else{
			log.error("Movimiento inválido");
			return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
		}
	}	

	//Jugar un virus
	public String playVirus(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, Integer c1_id, Integer c2_id){
		Optional<Card> c1 = cardService.findCard(c1_id);
		Optional<Card> c2 = cardService.findCard(c2_id);
		if(c1.isPresent() && c2.isPresent()){
			Card c_virus = c1.get();
			Card c_organ = c2.get();
			try{
				cardService.infect(c_organ, c_virus);
				return turn(gameId, gamePlayerId);
			}catch(IllegalArgumentException e){
				return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
			}		
		} else {
			log.error("Movimiento inválido");
			return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
		}
		
	}

	//Jugar una vacuna
	public String playVaccine(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, Integer c1_id, Integer c2_id){
		Optional<Card> c1 = cardService.findCard(c1_id);
		Optional<Card> c2 = cardService.findCard(c2_id);
		if(c1.isPresent() && c2.isPresent()){
			Card c_vaccine = c1.get();
			Card c_organ = c2.get();
			try{
				cardService.vaccinate(c_organ, c_vaccine);
				return turn(gameId, gamePlayerId);
			}catch(IllegalArgumentException e){
				return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
			}		
		}else{
			log.error("Movimiento inválido");
			return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
		}
		
	}

	//Jugar transplante (Añadir restricción de no quedarse con dos órganos iguales en el cuerpo)
	public String playTransplant(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, Integer c1_id, Integer c2_id){
		Optional<Card> c1 = cardService.findCard(c1_id);
		Optional<Card> c2 = cardService.findCard(c2_id);
		if(c1.isPresent() && c2.isPresent()){
			Card c_organ1 = c1.get();
			Card c_organ2 = c2.get();
			GamePlayer g1 = c_organ1.getGamePlayer();
			GamePlayer g2 = c_organ2.getGamePlayer();
				try{
					gameService.changeCards(g1,g2,c_organ1,c_organ2);
					return turn(gameId, gamePlayerId);
				}catch(IllegalArgumentException e){
					return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
				}
				
	} else{
		log.error("Movimiento inválido");
			return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
	}
}

	// Método para descartar cartas
	@GetMapping(value="/games/{gameId}/gamePlayer/discard/{cards}")
    public String discard(@PathVariable List<Card> cards, @PathVariable Integer gamePlayerId, @PathVariable Integer gameId) {
        if(gamePlayerService.findById(gamePlayerId).isPresent()){
			GamePlayer gamePlayer = gamePlayerService.findById(gamePlayerId).get();
			if(gamePlayer.getCards().containsAll(cards)){
				for(Card card: cards){	//Recorremos las cartas que quiere descartar					
						gamePlayer.getCards().remove(card); //Cada carta la quitamos de la lista de cartas del jugador
						card.setPlayed(true); //Se cambia el estado de la carta a jugada
						cardService.save(card);	//Se guarda la carta	
			}  
				gamePlayerService.save(gamePlayer); //Cuando ya se han eliminado todas, se guarda el jugador
				return turn(gameId, gamePlayerId); //Volvemos al método del turno
			}else{
				log.error("you can't discard those cards");
				return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
			}				
		}else{
			log.error("this player is not available");
			return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
		} 
					
    }

	//Clasificación tras la finalización de la partida
	@GetMapping(value= "/games/{gameId}/clasificacion")
	public String clasification(@PathVariable("gameId") int gameId, ModelMap model) {
		Game game = this.gameService.findGames(gameId);
		game.endGame();
		log.info("Clasificando");
		Map<Integer,List<GamePlayer>> classification = gameService.clasificate(game.getGamePlayer());
				game.setClassification(classification);
				gameService.save(game);
				model.put("clasificacion", classification);
				return "rondas/clasificacion";
			}
}