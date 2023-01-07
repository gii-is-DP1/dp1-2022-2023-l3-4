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
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.card.Hand;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.room.Room;
import org.springframework.samples.petclinic.room.RoomService;
import org.springframework.samples.petclinic.statistics.WonPlayedGamesException;
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
	
	private final GameService gameService;
	private final RoomService roomService;
	private final GamePlayerService gamePlayerService;
	private final CardService cardService;
	private final AuthenticationService authenticationService;

	public static final String RUNNING_GAMES_LISTING = "games/runningGameListing";
	public static final String TERMINATE_GAMES_LISTING = "games/terminateGameListing";

	@Autowired
	public GameController(GameService gameService,GamePlayerService gamePlayerService, 
	CardService cardService, RoomService roomService, AuthenticationService authenticationService) {
		this.gameService = gameService;
		this.gamePlayerService= gamePlayerService;
		this.cardService=cardService;
		this.roomService = roomService;
		this.authenticationService = authenticationService;
	}

	@GetMapping(value = "/game/start/{roomId}")
	public String init(@PathVariable("roomId") Integer roomId, ModelMap model) {
		Room room = roomService.findRoomById(roomId);
		Game runningGame = gameService.getRunningGame(room);
		//comprobamos que no haya ninguna partida ya iniciada en la room
		if(runningGame == null){
			Player currentPlayer = authenticationService.getPlayer();
			if(room.getHost().equals(currentPlayer)) {
				Game game = gameService.startGame(room);
				return "redirect:/games/"+ game.getId();
			} else {
				model.put("message", "Only the host of the room can start a game.");
				model.put("messageType", "info");
				return "welcome";
			}
		} else {
			model.put("message", "There is already a game running.");
			model.put("messageType", "info");
			return muestraVista(runningGame.getId(), model);
		}
		
	}
	
	//Listados de partidas para el administrador
	@GetMapping(value="/runningGames")
	public String listRunningGames(ModelMap model){
		Collection<Game> runningGames=  gameService.listRunningGames();
		model.put("games", runningGames);
		return RUNNING_GAMES_LISTING;
	}

	@GetMapping(value="/terminateGames")
	public String terminateRunningGames(ModelMap model){
		Collection<Game> terminateGames=  gameService.listTerminateGames();
		model.put("games", terminateGames);
		return TERMINATE_GAMES_LISTING;
	}
	
	//Muestra vista Individual de cada jugador
	@GetMapping(value="/games/{gameId}")
	public String muestraVista(@PathVariable("gameId") int gameId, ModelMap model){
		GamePlayer gp_vista= authenticationService.getGamePlayer();
		Game game = gameService.findGame(gameId);
		if(game.getWinner()==null){
			model = generaTablero(model, gp_vista, game);
			GamePlayer currentTurnGamePlayer = game.getGamePlayer().get(game.getTurn());
			Boolean isYourTurn = currentTurnGamePlayer.equals(gp_vista);
			model.put("isYourTurn", isYourTurn);
			model.put("currentTurnGamePlayer", currentTurnGamePlayer);

			return "games/game";
		} else {
			return "redirect:/games/" + game.getId() + "/classification";
		}
		
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
				if (c.get().getType().getType().equals(GenericCard.Type.GLOVES)) {
					return playGlove(gameId, cardId);
				} else {
					model = generaTablero(model, gp, game);
					return "/games/selecciona";
				}
			}else{
				model.put("message", "You must play with a card in your hand.");
				model.put("messageType", "info");
				return muestraVista(gameId, model);
			}
		} else{
			model.put("message", "That card does not belong to this game.");
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
	}

	@GetMapping("/games/{gameId}/play/{cardId}/toPlayer/{targetGamePlayerid}")
	public String playOnBody(@PathVariable("gameId") Integer gameId, @PathVariable("cardId") Integer cardId,
			@PathVariable("targetGamePlayerid") Integer targetGP, ModelMap model) {
		Card card = cardService.findCard(cardId).get();
		if(card.getType().getType().equals(GenericCard.Type.ORGAN)){
			return playOrgan(gameId, targetGP, cardId, model);
		} else if(card.getType().getType().equals(GenericCard.Type.ERROR)) {
			return playMedicalError(gameId, targetGP, cardId, model);
		} else if(card.getType().getType().equals(GenericCard.Type.INFECTION)) {
			return playInfect(gameId, targetGP, cardId, model);
		}
		model.put("message", "Invalid action, a " +  card.getType().getType().toString() + " card can only be played on another card.");
		model.put("messageType", "info");
		return muestraVista(gameId, model);
	}

	@GetMapping("/games/{gameId}/play/{cardId}/toCard/{targetCardId}")
	public String playOnCard(@PathVariable("gameId") Integer gameId, @PathVariable("cardId") Integer cardId,
			@PathVariable("targetCardId") Integer targetC, ModelMap model) {
		Card card = cardService.findCard(cardId).get();
		if(card.getType().getType().equals(GenericCard.Type.VACCINE)){
			return playVaccine(gameId, cardId, targetC, model);
		} else if(card.getType().getType().equals(GenericCard.Type.VIRUS)) {
			return playVirus(gameId, cardId, targetC, model);
		} else if(card.getType().getType().equals(GenericCard.Type.THIEF)) {
			return playThief(gameId, cardId, targetC, model);
		} else if(card.getType().getType().equals(GenericCard.Type.TRANSPLANT)) {
			GamePlayer currentGamePlayer = authenticationService.getGamePlayer();
			Game game = gameService.findGame(gameId);
			model.put("selectAgain", true);
			model.put("target1", targetC);
			model = generaTablero(model, currentGamePlayer, game);
			return "games/selecciona";
		}
		
		model.put("message", "Invalid action, a " +  card.getType().getType().toString() + " card can only be played on another body.");
		model.put("messageType", "info");
		return muestraVista(gameId, model);
	}

	@GetMapping("/games/{gameId}/play/{cardId}/toCard/{target1}/and/{target2}")
	public String playOnAnotherCard(@PathVariable("gameId") Integer gameId, @PathVariable("cardId") Integer cardId,
			@PathVariable("target1") Integer targetC, @PathVariable("target2") Integer targetC2, ModelMap model) {
		Card card = cardService.findCard(cardId).get();
		if(card.getType().getType().equals(GenericCard.Type.TRANSPLANT)) {
			return playTransplant(gameId, targetC, targetC2, cardId, model);
		}
		model.put("message", "Invalid action, " +  card.getType().getType().toString() + " card can only be played on another card.");
		model.put("messageType", "info");
		
		return muestraVista(gameId, model);
	}

	//Jugar un órgano
	public String playOrgan(@PathVariable("gameId") int gameId, Integer g_id, Integer c_id, ModelMap model){
		Optional<Card> c = cardService.findCard(c_id);
		GamePlayer gp1 = authenticationService.getGamePlayer();
		Optional<GamePlayer> gp2 = gamePlayerService.findById(g_id);
		if(c.isPresent() && gp2.isPresent()){
				Card organ = c.get();
				GamePlayer gplayer2 = gp2.get();
				try{
					gameService.addOrgan(organ, gplayer2, gplayer2, model);
					cardService.save(organ);
					gamePlayerService.save(gp1);
					gamePlayerService.save(gplayer2);
					return turn(gameId);
					}catch(IllegalArgumentException e){
					model.put("message", e.getMessage());
					model.put("messageType", "info");
					return muestraVista(gameId, model);
				}			
		}else{
			model.put("message", "Movimiento inválido");
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}

				
	}	

	//Jugar un virus
	public String playVirus(@PathVariable("gameId") int gameId, Integer c1_id, Integer c2_id, ModelMap model){
		Optional<Card> c1 = cardService.findCard(c1_id);
		Optional<Card> c2 = cardService.findCard(c2_id);
		Card old_card = null;
		if(c1.isPresent() && c2.isPresent()){
			Card c_virus = c1.get();
			Card c_organ = c2.get();
			try{
				if(c_organ.getVirus().size()==1 || c_organ.getVaccines().size()==1){
					old_card = (c_organ.getVirus().size()==1)?c_organ.getVirus().get(0): c_organ.getVaccines().get(0);
				}

				cardService.infect(c_organ, c_virus);
				cardService.save(c_virus);
				cardService.save(c_organ);
				if(old_card!=null)cardService.save(old_card);
				return turn(gameId);
			}catch(IllegalArgumentException e){
				model.put("message", e.getMessage());
				model.put("messageType", "info");
				return muestraVista(gameId, model);
			}			
		} else {
			model.put("message", "Movimiento inválido");
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
		
	}

	//Jugar una vacuna
	public String playVaccine(@PathVariable("gameId") int gameId, Integer c1_id, Integer c2_id, ModelMap model){
		Optional<Card> c1 = cardService.findCard(c1_id);
		Optional<Card> c2 = cardService.findCard(c2_id);
		Card old_virus = null;
		if(c1.isPresent() && c2.isPresent()){
			Card c_vaccine = c1.get();
			Card c_organ = c2.get();
			try{
				if(c_organ.getVirus().size()==1){
					old_virus=c_organ.getVirus().get(0);
				}
				cardService.vaccinate(c_organ, c_vaccine);
				cardService.save(c_vaccine);
				cardService.save(c_organ);
				if(old_virus!=null) cardService.save(old_virus);
				return turn(gameId);
			}catch(IllegalArgumentException e){
				model.put("message", e.getMessage());
				model.put("messageType", "info");
				return muestraVista(gameId, model);
			}			
		}else{
			model.put("message", "Movimiento inválido");
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
		
	}

	//Jugar transplante (Añadir restricción de no quedarse con dos órganos iguales en el cuerpo)
	public String playTransplant(int gameId, Integer c1_id, Integer c2_id, Integer transplantId, ModelMap model){
		Optional<Card> c1 = cardService.findCard(c1_id);
		Optional<Card> c2 = cardService.findCard(c2_id);
		Optional<Card> transplantCard = cardService.findCard(transplantId);
		if(c1.isPresent() && c2.isPresent() && transplantCard.isPresent()){
			Card c_organ1 = c1.get();
			Card c_organ2 = c2.get();
			Card transplant = transplantCard.get();
			GamePlayer g1 = c_organ1.getGamePlayer();
			GamePlayer g2 = c_organ2.getGamePlayer();
				try{
					gameService.changeCards(g1,g2,c_organ1,c_organ2);
					gamePlayerService.save(g1);
					gamePlayerService.save(g2);	
					cardService.save(c_organ1);	
					cardService.save(c_organ2);
					transplant.discard();
					cardService.save(transplant);
					return turn(gameId);
				}catch(IllegalArgumentException e){
					model.put("message", e.getMessage());
					model.put("messageType", "info");
					return muestraVista(gameId, model);
				}
		} else{
			model.put("message", "Movimiento inválido");
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
	}


    public String playThief(int gameId, Integer c_id, Integer stolenCardId, ModelMap model) {
		// Obtenemos las cartas y jugadores involucrados en la acción
		Card thiefCard = getCard(c_id);
		GamePlayer thiefPlayer = authenticationService.getGamePlayer();
		Card stolenCard = getCard(stolenCardId);
		GamePlayer victimPlayer = stolenCard.getGamePlayer();
		// Verificamos que todas las cartas y jugadores existan
		if (thiefCard != null && thiefPlayer != null && victimPlayer != null && stolenCard != null) {
			gameService.thief(thiefCard, thiefPlayer, victimPlayer, stolenCard);
			gamePlayerService.save(thiefPlayer);
			gamePlayerService.save(victimPlayer);
			return turn(gameId);
		} else {
			model.put("message", "Movimiento inválido");
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
	}
	
	Card getCard(Integer cardId) {
		Optional<Card> optionalCard = cardService.findCard(cardId);
		return optionalCard.orElse(null);
	}
	
	public String playInfect(int gameId, Integer g_id, Integer c_id, ModelMap model) {
		try {
			Card card = cardService.findCard(c_id).orElseThrow(() -> new Exception("Card not found."));
			cardService.findCard(c_id).orElseThrow(() -> new Exception("Carta no encontrada"));
			GamePlayer gamePlayer1 = authenticationService.getGamePlayer();
			GamePlayer gamePlayer2 = gamePlayerService.findById(g_id).orElseThrow(() -> new Exception("Player not found."));
			gameService.infection(gamePlayer1, gamePlayer2);
			gamePlayerService.save(gamePlayer1);
			gamePlayerService.save(gamePlayer2);
			card.discard();
			cardService.save(card);
			return turn(gameId);
		} catch(Exception E) {
			model.put("message", E.getMessage());
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
	}
	
	public String playGlove(int gameId, Integer c_id) {
		try {
			Card card = cardService.findCard(c_id).orElseThrow(() -> new Exception("Card not found."));
			GamePlayer gamePlayer = authenticationService.getGamePlayer();
			Game game = gameService.findGame(gameId);
			gameService.glove(card, gamePlayer, game);
			for (GamePlayer otherGamePlayer : game.getGamePlayer()) {
				gamePlayerService.save(otherGamePlayer);
			}
			return "redirect:/games/" + gameId;
		} catch (Exception e) {
			ModelMap model = new ModelMap();
			model.put("message", e.getMessage());
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
	}
	
	public String playMedicalError(int gameId, Integer g_id, Integer c_id, ModelMap model) {
		try {
			GamePlayer gamePlayer1 = authenticationService.getGamePlayer();
			GamePlayer gamePlayer2 = gamePlayerService.findById(g_id).orElseThrow(() -> new Exception("Player not found."));
			Card medicalError = cardService.findCard(c_id).orElseThrow(() -> new Exception("Card not found."));
			gameService.medicalError(medicalError, gamePlayer1, gamePlayer2);
			// Finalizamos el turno
			// Actualizamos los cambios en la base de datos
			gamePlayerService.save(gamePlayer1);
			gamePlayerService.save(gamePlayer2);
			return turn(gameId);
		} catch(Exception E) {
			model.put("message", E.getMessage());
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
	}
	

	// Método para descartar cartas
	@GetMapping(value = "/games/{gameId}/discard")
	public String discardView(@PathVariable("gameId") Integer gameId, ModelMap model){
		GamePlayer currentGamePlayer = authenticationService.getGamePlayer();
		if(gameService.isYourTurn(currentGamePlayer, gameId)) {
			List<Card> hand = currentGamePlayer.getHand();
			Hand emptyForm = new Hand();
			model.put("hand", hand);
			model.put("cardsForm", emptyForm);
			return "games/discard";
		} else {
			model.put("message", "Wait for your turn to discard.");
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}
		
		
	}

	@PostMapping(value = "/games/{gameId}/discard")
    public String discard(Hand cardIds, @PathVariable("gameId") Integer gameId, ModelMap model, BindingResult br) {
		GamePlayer currentGamePlayer = authenticationService.getGamePlayer();
		if(gameService.isYourTurn(currentGamePlayer, gameId)) {
			
			List<Card> cards = cardService.findAllCardsByIds(cardIds.getCards());
			if(currentGamePlayer.getCards().containsAll(cards)){
				gameService.discard(cards, currentGamePlayer);
				return turn(gameId); //Volvemos al método del turno
			}else{
				model.put("message", "You can't discard those cards.");
				model.put("messageType", "info");
				return muestraVista(gameId, model);
			}
		} else {
			model.put("message", "Wait for your turn to discard.");
			model.put("messageType", "info");
			return muestraVista(gameId, model);
		}			
    }

	//Clasificación tras la finalización de la partida
	@GetMapping(value= "/games/{gameId}/classification")
	public String classification(@PathVariable("gameId") int gameId, ModelMap model) throws WonPlayedGamesException {
		Game game = this.gameService.findGame(gameId);
		if(game.getIsRunning()) {
			try {
				gameService.finishGame(game);
			} catch (Exception e) {
				throw new WonPlayedGamesException();
			}
		}
		model.put("classification", game.getClassification());
		return "games/classification";
	}
}