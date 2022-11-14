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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import net.bytebuddy.description.type.TypeDescription.Generic;

import net.bytebuddy.description.type.TypeDescription.Generic;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class GameController {
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	private static final String DECISION = "/games/{gameId}/gamePlayer/{currentPlayer}/decision";
	private static final String PLAY = "/games/{gameId}/gamePlayer/{currentPlayer}/play/selecciona";
	private static final String DISCARD = "/games/{gameId}/gamePlayer/{currentPlayer}/discard/selecciona";
	private final GameService gameService;
	private final GamePlayerService gamePlayerService;
	private int cardsInDeck;
	private final CardService cardService;

	@Autowired
	public GameController(GameService gameService,GamePlayerService gamePlayerService, CardService cardService) {
		this.gameService = gameService;
		this.gamePlayerService= gamePlayerService;
		this.cardService=cardService;
	}

	@GetMapping(value="/games")
	public String ListGames(ModelMap model){
		List<Game> allGames=  gameService.ListGames();
		model.put("games", allGames);
		return "games/listing";
	}


	GenericCard[] cards = new GenericCard[68];

	public void reset() {
		GenericCard.Colour[] colours = GenericCard.Colour.values();
		cardsInDeck = 0;

		for(int i = 0; i< colours.length-1; i++) {
			GenericCard.Colour colour = colours[i];
			cards[cardsInDeck++] = new GenericCard(colour, GenericCard.Type.getType(0));
			cards[cardsInDeck++] = new GenericCard(colour, GenericCard.Type.getType(0));
			cards[cardsInDeck++] = new GenericCard(colour, GenericCard.Type.getType(0));
			cards[cardsInDeck++] = new GenericCard(colour, GenericCard.Type.getType(0));
			cards[cardsInDeck++] = new GenericCard(colour, GenericCard.Type.getType(0));

			for(int j = 1; j<3; j++) {
				cards[cardsInDeck++] = new GenericCard(colour, GenericCard.Type.getType(j));
				cards[cardsInDeck++] = new GenericCard(colour, GenericCard.Type.getType(j));
				cards[cardsInDeck++] = new GenericCard(colour, GenericCard.Type.getType(j));
				cards[cardsInDeck++] = new GenericCard(colour, GenericCard.Type.getType(j));
			}
		}

		GenericCard.Type[] types = new GenericCard.Type[]{GenericCard.Type.TRANSPLANT, 
			GenericCard.Type.THIEF, GenericCard.Type.INFECTION, GenericCard.Type.GLOVES, GenericCard.Type.ERROR};
		cards[cardsInDeck++] = new GenericCard(GenericCard.Colour.RAINBOW, GenericCard.Type.ORGAN);
		cards[cardsInDeck++] = new GenericCard(GenericCard.Colour.RAINBOW, GenericCard.Type.VIRUS);
		for(int i = 0; i < 5; i++) {
			cards[cardsInDeck++] = new GenericCard(GenericCard.Colour.RAINBOW, GenericCard.Type.VACCINE);
		}
		
		cards[cardsInDeck++] = new GenericCard(GenericCard.Colour.RAINBOW, GenericCard.Type.GLOVES);
		cards[cardsInDeck++] = new GenericCard(GenericCard.Colour.RAINBOW, GenericCard.Type.ERROR);
		
		for(int i = 0; i < 4; i++) {
			cards[cardsInDeck++] = new GenericCard(GenericCard.Colour.RAINBOW, GenericCard.Type.THIEF);
			cards[cardsInDeck++] = new GenericCard(GenericCard.Colour.RAINBOW, GenericCard.Type.TRANSPLANT);
		}
		
		cards[cardsInDeck++] = new GenericCard(GenericCard.Colour.RAINBOW, GenericCard.Type.INFECTION);
		cards[cardsInDeck++] = new GenericCard(GenericCard.Colour.RAINBOW, GenericCard.Type.INFECTION);
	}



	//Si la baraja se queda sin cartas, se rellena con las ya descartadas
	
	public void rellenaBaraja(@PathVariable("gameId") int gameId){
		Game currentGame = gameService.findGames(gameId);
		List<Card> playedcards = currentGame.getCards().stream().filter(x->x.getPlayed()).collect(Collectors.toList());
		Collections.shuffle(playedcards);
		currentGame.setCards(playedcards);
		gameService.save(currentGame);
	}
	public void reparteCartas(@PathVariable("gameId") int gameId) {
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
			gameService.save(currentGame);	
			log.info("Cartas repartidas correctamente");
		}
		@GetMapping()
		public String turn(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId){
			Game game = gameService.findGames(gameId); //Encontramos el juego
			GamePlayer gamePlayer = gamePlayerService.findById(gamePlayerId).get();
			List<Card> body = gamePlayer.getCards().stream().filter(x->x.getBody()).collect(Collectors.toList());
			ModelMap model = new ModelMap();
			if((body.size()==4 || body.size()==5) && body.stream().filter(x->x.getVirus().size()==0).collect(Collectors.toList()).size()==4)
			 { //Si ya alguien ganó, finalizar la partida
				return clasificacion(gameId, model);
			}else{
				if(game.getTurn()==game.getGamePlayer().size()-1){ //Si es el último jugador
					game.setTurn(0); //Cambiamos el turno a 0
					game.setRound(game.getRound()+1); //Añadimos una ronda
					reparteCartas(gameId);} //Y repartimos cartas
				else{game.setTurn(game.getTurn()+1); //Sino solo incrementamos el turno en 1
				}
				gameService.save(game); //Guardamos los cambios de game
				gamePlayerId = game.getGamePlayer().get(game.getTurn()).getId();
				return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
			}
	 

		}

		@GetMapping(value="/games/{gameId}/gamePlayer/{currentPlayer}/decision/{decision}")
		public String decision(@PathVariable("gameId") int gameId, @PathVariable("currentPlayer") int currentPlayer, @PathVariable("decision") int decision){
			if(decision==0){ //Desición de jugar
				return "/games/"+gameId+"/gamePlayer"+currentPlayer+"/play";
			} else{ //Desición de descartar
				return "/games/"+gameId+"/gamePlayer"+currentPlayer+"/discard";
			}
		}


		@GetMapping(value="/games/{gameId}/gamePlayer/play")
		public String play(@PathVariable("gameId") int gameId, @PathVariable("currentPlayer") int currentPlayer, Integer cardId){
			Optional<Card> c = cardService.findCard(cardId);
			Optional<GamePlayer> gp = gamePlayerService.findById(gameId);
			if(c.isPresent() && gp.isPresent()){
				if(gp.get().getCards().contains(c.get())){
					switch(c.get().getType().toString()){
						case("ORGAN"):
						log.info("Elige el jugador a quien quieras dar este órgano");
						case("VIRUS"):
						log.info("Elige el jugador y el órgano que quieras infectar");
						case("VACCINE"):
						log.info("Elige el jugador y el órgano que quieras vacunar");
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


	@GetMapping(value="/game/{gameId}/gamePlayer/{gamePlayerId}")
	public void playOrgan(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, Integer g_id, Integer c_id){
		Optional<Card> c = cardService.findCard(c_id);
		Optional<GamePlayer> gp = gamePlayerService.findById(g_id);
		Set<String> cards = new HashSet<>();
		if(c.isPresent() && gp.isPresent()){
				cards.addAll(gp.get().getCards().stream().filter(x->x.getBody()).map(x->x.getType().getColour().name()).collect(Collectors.toSet()));
				cards.add(c.get().getType().getColour().name());
				if(cards.size()!=gp.get().getCards().size()){
					gp.get().getCards().add(c.get());
				}else{
					log.error("No puede poner dos órganos del mismo color en un cuerpo");
				}		
		}else{
			log.error("Movimiento inválido");
		}
	}	

		@GetMapping(value="/games/{gameId}/gamePlayer/{gamePlayerId}")
		public void muestraVista(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, ModelMap model){
			GamePlayer gp_vista= gamePlayerService.findById(gamePlayerId).get();
			
			for(GamePlayer gp: gameService.findGames(gameId).getGamePlayer()){
				if(gp== gp_vista){
					model.put("hand", gp.getCards().stream().filter(x->!x.getBody()).collect(Collectors.toList()));
				}
				model.put("body"+gp.getId(), gp.getCards().stream().filter(x->x.getBody()).collect(Collectors.toList()));
			}

		}

	// Método para descartar cartas
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
				return "you can't discard those cards";}		
		}else{
			return "this player is not available";} 		
    }

	
	@GetMapping(value= "/games/{gameId}/clasificacion")
	public String clasificacion(@PathVariable("gameId") int gameId, ModelMap model) {
		
		Game game = this.gameService.findGames(gameId);
		game.setIsRunning(false);
		this.gameService.save(game);
		Map<Integer,List<GamePlayer>> classification = new HashMap<>();
		log.info("Clasificando");
		List<GamePlayer> gamePlayers = game.getGamePlayer();
		for(GamePlayer gamePlayer : gamePlayers){
			List<Card> body = gamePlayer.getCards().stream().filter(x->x.getBody()).collect(Collectors.toList());
			Integer numNoVirus = body.stream().filter(x->x.getVirus().size()==0).collect(Collectors.toList()).size();
				if(numNoVirus==4){
					gamePlayer.setWinner(true);
					classification.put(1, List.of(gamePlayer));
				} else if(numNoVirus==3){
					if(classification.containsKey(2)){
						classification.get(2).add(gamePlayer);
					}else{
						classification.put(2, List.of(gamePlayer));
					}
				} else if(numNoVirus==2){
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

}