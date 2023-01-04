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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.card.Hand;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.room.Room;
import org.springframework.samples.petclinic.room.RoomService;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class GameController {
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	private final GameService gameService;
	private final RoomService roomService;
	private final GamePlayerService gamePlayerService;
	private final CardService cardService;
	private final AuthenticationService authenticationService;

	public static final String RANKING = "statistics/ranking";


	@Autowired
	public GameController(GameService gameService,GamePlayerService gamePlayerService, 
	CardService cardService, RoomService roomService, AuthenticationService authenticationService) {
		this.gameService = gameService;
		this.gamePlayerService= gamePlayerService;
		this.cardService=cardService;
		this.roomService = roomService;
		this.authenticationService = authenticationService;
	}

	@GetMapping("/ranking/global")
  public String getPlayersRanking(ModelMap model) {
    Map<GamePlayer, Integer> gps = gameService.getRanking();
    model.put("topGamers", gps);
    return RANKING;
  }

	@GetMapping(value = "/game/start/{roomId}")
	public String init(@PathVariable("roomId") Integer roomId, ModelMap model) {
		Room room = roomService.findRoomById(roomId);
		Player currentPlayer = authenticationService.getPlayer();
		if(room.getHost().equals(currentPlayer)) {
			Game game = gameService.startGame(room);
			return "redirect:/games/"+ game.getId();
		} else {
			model.put("message", "Only the host of the room can start a game.");
			model.put("messageType", "info");
			return "welcome";
		}
	}
	
	//Listar juegos
	@GetMapping(value="/games")
	public String ListGames(ModelMap model){
		List<Game> allGames=  gameService.listGames();
		model.put("games", allGames);
		return "games/listing";
	}
	
	//Muestra vista Individual de cada jugador
	@GetMapping(value="/games/{gameId}")
	public String muestraVista(@PathVariable("gameId") int gameId, ModelMap model){
		GamePlayer gp_vista= authenticationService.getGamePlayer();
		Game game = gameService.findGame(gameId);
		model = generaTablero(model, gp_vista, game);
		GamePlayer currentTurnGamePlayer = game.getGamePlayer().get(game.getTurn());
		Boolean isYourTurn = currentTurnGamePlayer.equals(gp_vista);
		model.put("isYourTurn", isYourTurn);
		model.put("currentTurnGamePlayer", currentTurnGamePlayer);

		return "games/game";
	}

	private ModelMap generaTablero(ModelMap model, GamePlayer gamePlayer, Game game) {
		Map<GamePlayer, List<Card>> bodies = new HashMap<>(); 
		for(GamePlayer gp: game.getGamePlayer()){
			if(gp.equals(gamePlayer)){
				model.put("hand", gp.getHand());
			}
			bodies.put(gp, gp.getCards().stream().filter(x->x.getBody()).collect(Collectors.toList()));
		}
		model.put("bodies", bodies);
		return model;
	}

	//Cambio de turno
	public String turn(int gameId){
			Game game = gameService.findGame(gameId); //Encontramos el juego
			if(game.hasAnyWinners())
			 { //Si ya alguien ganó, finalizar la partida
				return "redirect:/games/"+gameId+"/classification";
			}else{
				gameService.changeTurn(game);
				return "redirect:/games/" + gameId;
			}
		}

	//Jugar
	@GetMapping(value="/games/{gameId}/play/{cardId}")
	public String play(@PathVariable("gameId") int gameId, @PathVariable("cardId") Integer cardId, ModelMap model){
		Optional<Card> c = cardService.findCard(cardId);
		GamePlayer gp = authenticationService.getGamePlayer();
		Game game = gameService.findGame(gameId);
		
		if(c.isPresent()){
			if(gp.getCards().contains(c.get())){
				model = generaTablero(model, gp, game);
				return "/games/selecciona";
			}else{
				log.error("Debes jugar una carta que esté en tu mano");
				return muestraVista(gameId, model);
			}
		} else{
			log.error("Movimiento no válido");
			return muestraVista(gameId, model);
		}
	}

	@GetMapping("/games/{gameId}/play/{cardId}/toPlayer/{targetGamePlayerid}")
	public String playOnBody(@PathVariable("gameId") Integer gameId, @PathVariable("cardId") Integer cardId,
			@PathVariable("targetGamePlayerid") Integer targetGP, ModelMap model) {
		Card card = cardService.findCard(cardId).get();
		if(card.getType().getType().equals(GenericCard.Type.ORGAN)){
			return playOrgan(gameId, targetGP, cardId);
		}
		
		return muestraVista(gameId, model);
	}

	@GetMapping("/games/{gameId}/play/{cardId}/toCard/{targetCardId}")
	public String playOnCard(@PathVariable("gameId") Integer gameId, @PathVariable("cardId") Integer cardId,
			@PathVariable("targetCardId") Integer targetC, ModelMap model) {
		Card card = cardService.findCard(cardId).get();
		if(card.getType().getType().equals(GenericCard.Type.VACCINE)){
			return playVaccine(gameId, cardId, targetC);
		} else if(card.getType().getType().equals(GenericCard.Type.VIRUS)) {
			return playVirus(gameId, cardId, targetC);
		}
		
		return muestraVista(gameId, model);
	}

	//Jugar un órgano
	public String playOrgan(@PathVariable("gameId") int gameId, Integer g_id, Integer c_id){
		Optional<Card> c = cardService.findCard(c_id);
		GamePlayer gp1 = authenticationService.getGamePlayer();
		Optional<GamePlayer> gp2 = gamePlayerService.findById(g_id);
		ModelMap model = new ModelMap();
		if(c.isPresent() && gp2.isPresent()){
			Card organ = c.get();
			GamePlayer gplayer1 = gp1;
			GamePlayer gplayer2 = gp2.get();
			try {
				cardService.addOrgan(organ, gplayer1, gplayer2);
				return turn(gameId);
			} catch (Exception e) {
				log.error("No puede poner dos órganos del mismo color en un cuerpo");
				model.put("message", "No puede poner dos órganos del mismo color en un cuerpo");
				model.put("messageType", "info");
				return muestraVista(gameId, model);
			}
		} else {
			log.error("Movimiento inválido");
			model.put("message", "Movimiento inválido");
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
	}	

	//Jugar un virus
	public String playVirus(@PathVariable("gameId") int gameId, Integer c1_id, Integer c2_id){
		Optional<Card> c1 = cardService.findCard(c1_id);
		Optional<Card> c2 = cardService.findCard(c2_id);
		if(c1.isPresent() && c2.isPresent()){
			Card c_virus = c1.get();
			Card c_organ = c2.get();
			try{
				cardService.infect(c_organ, c_virus);
				return turn(gameId);
			}catch(IllegalArgumentException e){
				return "TODO";
			}
			
		} else {
			log.error("Movimiento inválido");
			return "/games/"+gameId;
		}
		
	}

	//Jugar una vacuna
	public String playVaccine(@PathVariable("gameId") int gameId, Integer c1_id, Integer c2_id){
		Optional<Card> c1 = cardService.findCard(c1_id);
		Optional<Card> c2 = cardService.findCard(c2_id);
		if(c1.isPresent() && c2.isPresent()){
			Card c_vaccine = c1.get();
			Card c_organ = c2.get();
			try{
				cardService.vaccinate(c_organ, c_vaccine);
				return turn(gameId);
			}catch(IllegalArgumentException e){
				return "todo";
			}
			
		}else{
			log.error("Movimiento inválido");
			return "/games/"+gameId;
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
					return turn(gameId);
				}catch(IllegalArgumentException e){
					return "todo";
				}
		} else{
			log.error("Movimiento inválido");
			return "/games/"+gameId;
		}
	}
	
	// Método para descartar cartas
	@GetMapping(value = "/games/{gameId}/discard")
	public String discardView(@PathVariable("gameId") Integer gameId, ModelMap model){
		Player player = authenticationService.getPlayer();
		GamePlayer currentGamePlayer = gameService.findGamePlayerByPlayer(player);
		Game game = gameService.findGame(gameId);
		List<Card> hand = currentGamePlayer.getHand();
		Hand emptyForm = new Hand();
		
		GamePlayer currentTurnGamePlayer = game.getGamePlayer().get(game.getTurn());
		Boolean isYourTurn = currentTurnGamePlayer.equals(currentGamePlayer);
		model.put("isYourTurn", isYourTurn);
		model.put("hand", hand);
		model.put("cardsForm", emptyForm);
		return "games/discard";
	}

	@PostMapping(value = "/games/{gameId}/discard")
    public String discard(Hand cardIds, @PathVariable("gameId") Integer gameId, ModelMap model, BindingResult br) {
		GamePlayer currentGamePlayer = authenticationService.getGamePlayer();
		List<Card> cards = cardService.findAllCardsByIds(cardIds.getCards());
		if(currentGamePlayer.getCards().containsAll(cards)){
			gameService.discard(cards, currentGamePlayer);
			return turn(gameId); //Volvemos al método del turno
		}else{
			log.error("you can't discard those cards");
			return muestraVista(gameId, model);
		}				
    }

	//Clasificación tras la finalización de la partida
	@GetMapping(value= "/games/{gameId}/classification")
	public String classification(@PathVariable("gameId") int gameId, ModelMap model) {
		Game game = this.gameService.findGame(gameId);
		if(game.getIsRunning()) gameService.finishGame(game);
		model.put("classification", game.getClassification());
		return "games/classification";
	}
}