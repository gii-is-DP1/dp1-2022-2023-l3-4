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
package org.springframework.samples.virus.game;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.virus.card.Card;
import org.springframework.samples.virus.card.CardService;
import org.springframework.samples.virus.card.GenericCard;
import org.springframework.samples.virus.card.GenericCardService;
import org.springframework.samples.virus.gamePlayer.GamePlayer;
import org.springframework.samples.virus.gamePlayer.GamePlayerService;
import org.springframework.samples.virus.player.Player;
import org.springframework.samples.virus.room.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private final GenericCardService genericCardService;
	private final RoomService roomService;


	@Autowired
	public GameController(GameService gameService,GamePlayerService gamePlayerService, 
	CardService cardService, GenericCardService genericCardService, RoomService roomService) {
		this.gameService = gameService;
		this.gamePlayerService= gamePlayerService;
		this.cardService=cardService;
		this.genericCardService=genericCardService;
		this.roomService = roomService;
	}

	@GetMapping(value = "/game/start/{roomId}")
	public String init(@PathVariable("roomId") Integer roomId) {

		Game game = new Game();
		game.setRound(0);
		game.setTurn(0);
		List<GamePlayer> gamePlayers = new ArrayList<>();
		game.setCards(new ArrayList<>());
		game.setClassification(new HashMap<>());
		List<Player> players = new ArrayList<>(roomService.findRoomById(roomId).getPlayers());
		
		for(Player p: players) {
			GamePlayer gp = new GamePlayer();
			gp.setPlayer(p);
			gp.setCards(new ArrayList<>());
			gamePlayers.add(gp);
			gamePlayerService.save(gp);
		}

		game.setGamePlayer(gamePlayers);
		List<GenericCard> deck = genericCardService.findGCards();
		for(GenericCard c: deck){
			Card card = new Card();
			card.setType(c);
			card.setBody(false);
			card.setPlayed(false);
			game.getCards().add(card);
			cardService.save(card);
		}
		gameService.save(game);
		reparteCartas(game.getId());


		return "redirect:/games/"+ game.getId() +"/gamePlayer/"+gamePlayers.get(0).getId();
		
	}
	
	//Listar juegos
	@GetMapping(value="/games")
	public String ListGames(ModelMap model){
		List<Game> allGames=  gameService.ListGames();
		model.put("games", allGames);
		return "games/listing";
	}

	//Si la baraja se queda sin cartas, se rellena con las ya descartadas
	public void rellenaBaraja(@PathVariable("gameId") int gameId){
		Game currentGame = gameService.findGames(gameId);
		List<Card> playedcards = currentGame.getCards().stream().filter(x->x.getPlayed()).collect(Collectors.toList());
		Collections.shuffle(playedcards);
		for(Card c: playedcards){
			c.setPlayed(false);
			cardService.save(c);
		}
		currentGame.setCards(cardService.findCards());
		gameService.save(currentGame);
	}
	
	//Barajar
	public void reparteCartas(int gameId) {
		Game currentGame = gameService.findGames(gameId); 
		List<Card> baraja = currentGame.getCards().stream().filter(x->!x.getBody() && !x.getPlayed() && x.getGamePlayer()==null).collect(Collectors.toList());
		if(baraja.size()==0) { //Si no quedan cartas en la baraja llamamos a shuffle
			log.info("Rellenando baraja");
			rellenaBaraja(gameId);
		}
		log.info("Repartiendo cartas");
		for(GamePlayer jugador: currentGame.getGamePlayer()) {
			List<Card> cartasJugador = jugador.getCards().stream().filter(x->!x.getBody()).collect(Collectors.toList());
			while(cartasJugador.size()<3){
				Card card = baraja.get(0);
				cartasJugador.add(card); //Se la añadimos al jugador
				card.setGamePlayer(jugador);
				baraja.remove(0);	//La quitamos del mazo
				cardService.save(card);
			}
			jugador.setCards(cartasJugador);//Cuando ya tenga 3 cargas se guarda en el jugador
				gamePlayerService.save(jugador);
			}

			log.info("Cartas repartidas correctamente");
		}

	//Muestra vista Individual de cada jugador
	@GetMapping(value="/games/{gameId}/gamePlayer/{gamePlayerId}")
	public String muestraVista(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, ModelMap model){
		GamePlayer gp_vista= gamePlayerService.findById(gamePlayerId).get();
		Game game = gameService.findGames(gameId);
		model = generaTablero(model, gp_vista, game);
		return "games/game";
	}

	private ModelMap generaTablero(ModelMap model, GamePlayer gamePlayer, Game game) {
		Map<GamePlayer, List<Card>> bodies = new HashMap<>(); 
		for(GamePlayer gp: game.getGamePlayer()){
			if(gp.equals(gamePlayer)){
				model.put("hand", gp.getCards().stream().filter(x->!x.getBody()).collect(Collectors.toList()));
			}
			bodies.put(gp, gp.getCards().stream().filter(x->x.getBody()).collect(Collectors.toList()));
		}
		model.put("bodies", bodies);
		return model;
	}

	//Cambio de turno
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
				return "redirect:/games/" + gameId + "/gamePlayer/" + gamePlayerId;
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
	@GetMapping(value="/games/{gameId}/gamePlayer/{gamePlayerId}/play/{cardId}")
	public String play(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, @PathVariable("cardId") Integer cardId, ModelMap model){
		Optional<Card> c = cardService.findCard(cardId);
		Optional<GamePlayer> gp = gamePlayerService.findById(gamePlayerId);
		Game game = gameService.findGames(gameId);
		
		if(c.isPresent() && gp.isPresent()){
			if(gp.get().getCards().contains(c.get())){
				model = generaTablero(model, gp.get(), game);
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
				return "/games/selecciona";
			}else{
				log.error("Debes jugar una carta que esté en tu mano");
				return muestraVista(gameId, gamePlayerId, model);
			}
		} else{
			log.error("Movimiento no válido");
			return muestraVista(gameId, gamePlayerId, model);
		}
	}

	@GetMapping("/games/{gameId}/gamePlayer/{sourceGamePlayerId}/play/{cardId}/toPlayer/{targetGamePlayerid}")
	public String playOnBody(@PathVariable("gameId") Integer gameId, @PathVariable("sourceGamePlayerId") Integer sourceGP, @PathVariable("cardId") Integer cardId,
			@PathVariable("targetGamePlayerid") Integer targetGP, ModelMap model) {
		Card card = cardService.findCard(cardId).get();
		if(card.getType().getType().equals(GenericCard.Type.ORGAN)){
			return playOrgan(gameId, targetGP, sourceGP, cardId);
		}
		
		return muestraVista(gameId, sourceGP, model);
	}

	@GetMapping("/games/{gameId}/gamePlayer/{sourceGamePlayerId}/play/{cardId}/toCard/{targetCardId}")
	public String playOnCard(@PathVariable("gameId") Integer gameId, @PathVariable("sourceGamePlayerId") Integer sourceGP, @PathVariable("cardId") Integer cardId,
			@PathVariable("targetCardId") Integer targetC, ModelMap model) {
		Card card = cardService.findCard(cardId).get();
		if(card.getType().getType().equals(GenericCard.Type.VACCINE)){
			return playVaccine(gameId, sourceGP, cardId, targetC);
		} else if(card.getType().getType().equals(GenericCard.Type.VIRUS)) {
			return playVirus(gameId, sourceGP, cardId, targetC);
		}
		
		return muestraVista(gameId, sourceGP, model);
	}

	//Jugar un órgano
	public String playOrgan(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, Integer g_id, Integer c_id){
		Optional<Card> c = cardService.findCard(c_id);
		Optional<GamePlayer> gp1 = gamePlayerService.findById(gamePlayerId);
		Optional<GamePlayer> gp2 = gamePlayerService.findById(g_id);
		Set<Card> sc = new HashSet<>();
		Set<String> cards = new HashSet<>();
		ModelMap model = new ModelMap();
		if(c.isPresent() && gp2.isPresent() && gp1.isPresent()){
				Card organ = c.get();
				GamePlayer gplayer1 = gp1.get();
				GamePlayer gplayer2 = gp2.get();
				if(organ.getType().getType().toString()=="ORGAN"){
					cards.addAll(cardService.getBodyFromAGamePlayer(gplayer2.getId()).stream().map(x->x.getType().getColour().toString()).collect(Collectors.toSet()));
					cards.add(organ.getType().getColour().name());
					if(cards.size()!=cardService.getBodyFromAGamePlayer(gplayer2.getId()).size()){
						sc = gplayer1.getCards().stream().collect(Collectors.toSet());
						sc.remove(organ);
						gplayer1.setCards(sc.stream().collect(Collectors.toList()));
						gamePlayerService.save(gplayer1);
						organ.setGamePlayer(gplayer2);
						organ.setBody(true);
						cardService.save(organ);
						sc = gplayer2.getCards().stream().collect(Collectors.toSet());
						sc.add(organ);
						gplayer2.setCards(sc.stream().collect(Collectors.toList()));
						gamePlayerService.save(gplayer2);
						return turn(gameId,gamePlayerId);
					}else{
						log.error("No puede poner dos órganos del mismo color en un cuerpo");
						
						model.put("message", "No puede poner dos órganos del mismo color en un cuerpo");
						model.put("messageType", "info");
						return muestraVista(gameId, gamePlayerId, model);
					}	
				}else{
					log.error("Esta carta no es un órgano");
					model.put("message", "Esta carta no es un órgano");
						model.put("messageType", "info");
						return muestraVista(gameId, gamePlayerId, model);
				}			
		}else{
			log.error("Movimiento inválido");
			model.put("message", "Movimiento inválido");
			model.put("messageType", "info");
			return muestraVista(gameId, gamePlayerId, model);
		}
	}	

	//Jugar un virus
	public String playVirus(@PathVariable("gameId") int gameId, @PathVariable("gamePlayerId") int gamePlayerId, Integer c1_id, Integer c2_id){
		Optional<Card> c1 = cardService.findCard(c1_id);
		Optional<Card> c2 = cardService.findCard(c2_id);
		if(c1.isPresent() && c2.isPresent()){
			Card c_virus = c1.get();
			Card c_organ = c2.get();
			if(c_organ.getType().getType().name()=="ORGAN" && 
			(c_virus.getType().getColour()==c_organ.getType().getColour() || c_organ.getType().getColour().name()=="RAINBOW" || c_virus.getType().getColour().name()=="RAINBOW")){
				
				if(c_organ.getVaccines().size()==0){
					if(c_organ.getVirus().size()==0){
						c_organ.getVirus().add(c_virus);
						c_virus.setBody(true);
						c_virus.setCardVirus(c_organ);	

					}else{
						Card virus1 = c_organ.getVirus().get(0);
						c_organ.setVirus(new ArrayList<>());
						virus1.setBody(false);
						virus1.setPlayed(true);
						virus1.setCardVirus(null);
						c_virus.setPlayed(true);
						cardService.save(virus1);
					
					}
					cardService.save(c_virus);
					cardService.save(c_organ);
					return turn(gameId, gamePlayerId);			
				} else if(c_organ.getVaccines().size()==1){
					Card vaccine = c_organ.getVaccines().get(0);
					vaccine.setBody(false);
					vaccine.setPlayed(true);
					vaccine.setCardVaccine(null);
					c_virus.setPlayed(true);
					c_organ.setVaccines(new ArrayList<>());
					cardService.save(c_virus);
					cardService.save(c_organ);
					cardService.save(vaccine);
					return turn(gameId, gamePlayerId);

				}else{
					log.error("No puedes infectar un órgano inmunizado");
					return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
				}
			}else{
				log.error("No puedes infectar un ógano que no sea ni arcoirís ni de tu color");
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
			if(c_organ.getType().getType().name()=="ORGAN" && 
			(c_vaccine.getType().getColour()==c_organ.getType().getColour() || c_organ.getType().getColour().name()=="RAINBOW")){
				
				if(c_organ.getVirus().size()==0){
					if(c_organ.getVaccines().size()<2){
						c_organ.getVaccines().add(c_vaccine);
						c_vaccine.setBody(true);
						c_vaccine.setCardVaccine(c_organ);
						cardService.save(c_vaccine);
						cardService.save(c_organ);
						return turn(gameId, gamePlayerId);
					}else{
						log.error("Este órgano ya está inmunizado");
						return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
					}
			
				}else{
					Card virus = c_organ.getVirus().get(0);
					virus.setBody(false);
					virus.setPlayed(true);
					virus.setCardVirus(null);
					c_vaccine.setPlayed(true);
					c_organ.setVirus(new ArrayList<>());;
					cardService.save(c_vaccine);
					cardService.save(c_organ);
					cardService.save(virus);
					return turn(gameId, gamePlayerId);

				}
				}else{
					log.error("No puedes vacunar un órgano que no sea ni arcoirís ni de tu color");
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
		Set<Card> sc = new HashSet<>();
		Set<Card> sc1 = new HashSet<>();
		if(c1.isPresent() && c2.isPresent()){
			Card c_organ1 = c1.get();
			Card c_organ2 = c2.get();
			if(c_organ1.getVaccines().size()<2 && c_organ2.getVaccines().size()<2){
				GamePlayer g1 = c_organ1.getGamePlayer();
				GamePlayer g2 = c_organ2.getGamePlayer();
				sc= g1.getCards().stream().collect(Collectors.toSet());
				sc.remove(c_organ1);
				if(c_organ1.getVaccines().size()==1){
					sc.remove(c_organ1.getVaccines().get(0));
					sc1= g2.getCards().stream().collect(Collectors.toSet());
					sc1.add(c_organ1.getVaccines().get(0));
					g2.setCards(sc1.stream().collect(Collectors.toList()));
				}
				else if(c_organ1.getVirus().size()==1){
					sc.remove(c_organ1.getVirus().get(0));
					sc1= g2.getCards().stream().collect(Collectors.toSet());
					sc1.add(c_organ1.getVirus().get(0));
					g2.setCards(sc1.stream().collect(Collectors.toList()));
				}
				g1.setCards(sc.stream().collect(Collectors.toList()));
				sc= g2.getCards().stream().collect(Collectors.toSet());
				sc.remove(c_organ2);
				if(c_organ2.getVaccines().size()==1){
					sc.remove(c_organ2.getVaccines().get(0));
					sc1= g1.getCards().stream().collect(Collectors.toSet());
					sc1.add(c_organ2.getVaccines().get(0));
					g1.setCards(sc1.stream().collect(Collectors.toList()));
				}
				else if(c_organ2.getVirus().size()==1){
					sc.remove(c_organ2.getVirus().get(0));
					sc1= g1.getCards().stream().collect(Collectors.toSet());
					sc1.add(c_organ2.getVirus().get(0));
					g1.setCards(sc1.stream().collect(Collectors.toList()));
				}
				g2.setCards(sc.stream().collect(Collectors.toList()));
				g1.getCards().add(c_organ2);
				g2.getCards().add(c_organ1);
				c_organ1.setGamePlayer(g2);
				c_organ2.setGamePlayer(g1);
				gamePlayerService.save(g1);
				gamePlayerService.save(g2);
				cardService.save(c_organ1);
				cardService.save(c_organ2);
				return turn(gameId, gamePlayerId);
			}else{
				log.error("No se pueden intercambiar órganos imnunizados");
			return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
			}
	} else{
		log.error("Movimiento inválido");
			return "/games/"+gameId+"/gamePlayer/"+gamePlayerId+"/decision";
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