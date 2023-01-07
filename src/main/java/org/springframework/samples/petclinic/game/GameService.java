
package org.springframework.samples.petclinic.game;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.card.GenericCardService;
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.room.Room;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;

import net.bytebuddy.asm.Advice.OffsetMapping.Target.ForArray.ReadOnly;

@Service
public class GameService {

	private GameRepository gameRepository;
	private CardService cardService;
	private GamePlayerService gamePlayerService;
	private GenericCardService genericCardService;

	@Autowired
	public GameService(GameRepository gameRepository, CardService cardService, GamePlayerService gamePlayerService, GenericCardService genericCardService) {
		this.gameRepository = gameRepository;
		this.cardService=cardService;
		this.gamePlayerService=gamePlayerService;
		this.genericCardService=genericCardService;
	}

	@Transactional(readOnly = true)
	public List<Game> listGames(){
		return gameRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Game> findGamesByGameplayer(GamePlayer gamePlayer) {
		return gameRepository.findGamesByGameplayer(gamePlayer);
	}

	@Transactional(readOnly = true)
	public Collection<Game> listRunningGames(){
		return gameRepository.findRunningGames();
	}

	
	@Transactional(readOnly = true)
	public Collection<Game> listTerminateGames(){
		return gameRepository.findTerminategGames();
	}

	@Transactional(readOnly = true)
	public Game findGame(Integer i){
		return gameRepository.findById(i).get();
	}

	@Transactional
	public void save(Game game){
		gameRepository.save(game);

	}

	@Transactional(readOnly = true)
	public String humanReadableDuration(Duration d) {
		return d.toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase();
	}

	@Transactional(readOnly = true)
	public GamePlayer findGamePlayerByPlayer(Player player){
		return gameRepository.findGamePlayerByPlayer(player.getId());
	}
	//Si la baraja se queda sin cartas, se rellena con las ya descartadas
	public void rellenaBaraja(Integer gameId){
		Optional<Game> currentGame = gameRepository.findById(gameId);
		if(currentGame.isPresent()){
			Game g = currentGame.get();
			List<Card> playedcards = g.discarted();
			cardService.shuffle(playedcards);
			g.setCards(cardService.findCards());
			gameRepository.save(g);
			}
			
		}
		
		//Barajar
		public void reparteCartas(@PathVariable("gameId") int gameId) {
			Optional<Game> currentGame = gameRepository.findById(gameId); 
			if(currentGame.isPresent()){
				Game game = currentGame.get();
				List<Card> baraja = game.baraja();
				for(GamePlayer jugador: game.getGamePlayer()) {
				List<Card> cartasJugador = jugador.getHand();
				while(cartasJugador.size()<3){
					if(baraja.size()==0) { //Si no quedan cartas en la baraja llamamos a rellenaBaraja
						rellenaBaraja(gameId);
						baraja = game.baraja();
					}
					Card card = baraja.get(0);
					cartasJugador.add(card); //Se la añadimos al jugador
					card.setGamePlayer(jugador);
					baraja.remove(0);	//La quitamos del mazo
					cardService.save(card);
				}
				jugador.setCards(cartasJugador);//Cuando ya tenga 3 cargas se guarda en el jugador
					gamePlayerService.save(jugador);
				}
			}
			
			}

			public void addOrgan(Card organ, GamePlayer gplayer1, GamePlayer gplayer2, ModelMap model){
				if(gplayer2.isThisOrganNotPresent(organ)){
					gplayer1.getCards().remove(organ);
					organ.setGamePlayer(gplayer2);
					organ.setBody(true);
					gplayer2.getCards().add(organ);
					
					
				}else{
					model.put("message", "No puede poner dos órganos del mismo color en un cuerpo");
					model.put("messageType", "info");
					throw new IllegalArgumentException();		
				}			
		}


			public void changeCards(GamePlayer g1, GamePlayer g2, Card c_organ1, Card c_organ2){
				if(c_organ1.getType().getType().toString()=="ORGAN"
				&& c_organ2.getType().getType().toString()=="ORGAN"){
				if(c_organ1.getVaccines().size()<2 && c_organ2.getVaccines().size()<2){
				if(g1.isThisOrganNotPresent(c_organ2) && g2.isThisOrganNotPresent(c_organ1)){
					cardService.changeGamePlayer(c_organ1, g1, g2);
					cardService.changeGamePlayer(c_organ2, g2, g1);
					if(c_organ1.getVaccines().size()==1){
						Card vaccine1 = c_organ1.getVaccines().get(0);
						cardService.changeGamePlayer(vaccine1, g1, g2);
					}
					else if(c_organ1.getVirus().size()==1){
						Card virus1 = c_organ1.getVirus().get(0);
						cardService.changeGamePlayer(virus1, g1, g2);
					}
					else if(c_organ2.getVaccines().size()==1){
						Card vaccine2 = c_organ2.getVaccines().get(0);
						cardService.changeGamePlayer(vaccine2, g2, g1);
					}
					else if(c_organ2.getVirus().size()==1){
						Card virus2 = c_organ2.getVirus().get(0);
						cardService.changeGamePlayer(virus2, g2, g1);
					}			
				
			}else{
				throw new IllegalArgumentException("No pueden quedar cuerpos con órganos repetidos");
			}

			}else{
				throw new IllegalArgumentException("No se pueden intercambiar órganos imnunizados");
				
			}
	} else{
		throw new IllegalArgumentException("Solo se pueden intercambiar órganos");
	}
			}


		public void changeTurn(Game game)	{
			if(game.getTurn()==game.getGamePlayer().size()-1){ //Si es el último jugador
				game.setTurn(0); //Cambiamos el turno a 0
				game.setRound(game.getRound()+1); //Añadimos una ronda
				reparteCartas(game.getId());} //Y repartimos cartas
			else{game.setTurn(game.getTurn()+1); //Sino solo incrementamos el turno en 1
			}
			gameRepository.save(game); //Guardamos los cambios de game
		}


        public void thief(Card thiefCard, GamePlayer thiefPlayer, GamePlayer victimPlayer, Card stolenCard) {
			// Verificamos que la víctima tenga la carta que se quiere robar
			if (victimPlayer.getCards().contains(stolenCard)) {
				// Realizamos el robo de la carta
				stolenCard.setGamePlayer(thiefPlayer);
				thiefCard.discard();
				victimPlayer.getCards().remove(stolenCard);
				thiefPlayer.getCards().add(stolenCard);
				thiefPlayer.getCards().remove(thiefCard);
				cardService.save(stolenCard);
				cardService.save(thiefCard);
			}
	}

	public void infection(GamePlayer gamePlayer1, GamePlayer gamePlayer2){
		List<Card> infectedCards = new ArrayList<>();
		for (Card c : gamePlayer1.getCards()) {
			if (c.getType().getType() == Type.VIRUS) {
				infectedCards.add(c);
			}
		}
		
		//Comprobar si se pueden infectar sus organos
			List<Card> body = gamePlayer2.getBody();
			for (Card c : body) {
				for (Card infectedCard : infectedCards) {
					if (c.getVaccines().size()==0) {
						if (c.getType().getColour() == infectedCard.getType().getColour() 
							&& c.getType().getType() == Type.ORGAN && c.getVirus().size()==0) {
						gamePlayer1.getCards().remove(infectedCard);
						infectedCard.setGamePlayer(gamePlayer2);
						gamePlayer2.getCards().add(infectedCard);
						c.getVirus().add(infectedCard);
						} else {
							throw new IllegalArgumentException("No se puede infectar un órgano no libre.");
						}
					} else {
						throw new IllegalArgumentException("No se puede infectar un órgano inmunizado.");
				}
			}
		}
	}

	public void glove(Card card, GamePlayer gamePlayer, Game game) {
		gamePlayer.getCards().remove(card);
		card.discard();
		cardService.save(card);
		for (GamePlayer otherGamePlayer : game.getGamePlayer()) {
			if (otherGamePlayer != gamePlayer) {  // Excluimos al jugador que ejecuta la acción
				for(Card c: otherGamePlayer.getHand()) {
					c.discard();
					cardService.save(c);
				}
				otherGamePlayer.setCards(new ArrayList<>());  // Descartamos todas las cartas del mazo del jugador
			}
		}
		for (Integer i=0; i<game.getGamePlayer().size(); i++) {
			changeTurn(game);
		}
		
	}

	public void medicalError(Card medicalError, GamePlayer gamePlayer1, GamePlayer gamePlayer2) {
		// Intercambiamos los cuerpos de los dos jugadores
		medicalError.discard();
		List<Card> player1Cards = gamePlayer1.getBody();
		List<Card> player2Cards = gamePlayer2.getBody();
		for(Card c: player1Cards) {
			c.setGamePlayer(gamePlayer2);
			cardService.save(c);
		}
		
		for(Card c: player2Cards) {
			c.setGamePlayer(gamePlayer1);
			cardService.save(c);
		}
		gamePlayer1.getBody().removeAll(player1Cards);
		gamePlayer1.getBody().addAll(player2Cards);
		gamePlayer2.getBody().removeAll(player2Cards);
		gamePlayer2.getBody().addAll(player1Cards);
		cardService.save(medicalError);
	}
		
	public Map<Integer,List<GamePlayer>> clasificate(List<GamePlayer> gamePlayers){
		Map<Integer,List<GamePlayer>> classification = new HashMap<>();
		for(GamePlayer gamePlayer : gamePlayers){
			Integer healthyOrgans = gamePlayer.getNumHealthyOrgans();
			if(healthyOrgans==4){
				gamePlayer.setWinner(true);
				classification.put(1, List.of(gamePlayer));
			} else if(healthyOrgans==3){
				if(classification.containsKey(2)){
					classification.get(2).add(gamePlayer);
				}else{
					classification.put(2, List.of(gamePlayer));
						}
					} else if(healthyOrgans==2){
						if(classification.containsKey(3)){
							classification.get(3).add(gamePlayer);
						}else{
							classification.put(3, List.of(gamePlayer));
						}
					} else if(healthyOrgans==1){
						if(classification.containsKey(4)){
							classification.get(4).add(gamePlayer);
						}else{
							classification.put(4, List.of(gamePlayer));
						}
					} else {
						if(classification.containsKey(5)){
							classification.get(5).add(gamePlayer);
						}else{
							classification.put(5, List.of(gamePlayer));
						}
					} 
				}
				return classification;
		}
	
	@Transactional(readOnly = false)
	public Game startGame(Room room) {
		Game game = new Game();
		game.setRoom(room);
		game.setIsRunning(true);
		game.setRound(0);
		game.setTurn(0);
		game.setInitialHour(LocalDateTime.now());
		game.setIsRunning(true);
		List<GamePlayer> gamePlayers = new ArrayList<>();
		game.setCards(new ArrayList<>());
		game.setClassification(new HashMap<>());
		List<Player> players = new ArrayList<>(room.getPlayers());
		
		for(Player p: players) {
			GamePlayer gp = findGamePlayerByPlayer(p);
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
			card.setVaccines(new ArrayList<>());
			card.setVirus(new ArrayList<>());
			game.getCards().add(card);
			cardService.save(card);
		}
		Collections.shuffle(game.getCards());
		save(game);
		reparteCartas(game.getId());

		return game;
	}

	@Transactional(readOnly = false)
	public void discard(List<Card> cards, GamePlayer gamePlayer) {
		if(gamePlayer.getCards().containsAll(cards)){
			for(Card card: cards){	//Recorremos las cartas que quiere descartar					
					gamePlayer.getCards().remove(card); //Cada carta la quitamos de la lista de cartas del jugador
					card.discard();
					cardService.save(card);	//Se guarda la carta	
			}  
			gamePlayerService.save(gamePlayer); //Cuando ya se han eliminado todas, se guarda el jugador
		}else{
			
		}
	}

	@Transactional(readOnly = false)
	public void finishGame(Game game) {
		game.endGame();
		Map<Integer,List<GamePlayer>> classification = clasificate(game.getGamePlayer());
		game.setClassification(classification);
		game.getCards().stream().forEach(c -> {
			c.discard();
			cardService.save(c);
		} );
		save(game);
	}

}

