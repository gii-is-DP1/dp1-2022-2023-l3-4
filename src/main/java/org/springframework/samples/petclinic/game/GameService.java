
package org.springframework.samples.petclinic.game;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class GameService {

	private GameRepository gameRepository;
	private CardService cardService;
	private GamePlayerService gamePlayerService;
	private CardService cardService;
	private GamePlayerService gamePlayerService;

	@Autowired
	public GameService(GameRepository gameRepository, CardService cardService, GamePlayerService gamePlayerService) {
	public GameService(GameRepository gameRepository, CardService cardService, GamePlayerService gamePlayerService) {
		this.gameRepository = gameRepository;
		this.cardService=cardService;
		this.gamePlayerService=gamePlayerService;
		this.cardService=cardService;
		this.gamePlayerService=gamePlayerService;
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
					gamePlayerService.save(g1);
					gamePlayerService.save(g2);				
				
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


		public String changeTurn(Game game)	{
			if(game.getTurn()==game.getGamePlayer().size()-1){ //Si es el último jugador
				game.setTurn(0); //Cambiamos el turno a 0
				game.setRound(game.getRound()+1); //Añadimos una ronda
				reparteCartas(game.getId());} //Y repartimos cartas
			else{game.setTurn(game.getTurn()+1); //Sino solo incrementamos el turno en 1
			}
			gameRepository.save(game); //Guardamos los cambios de game
			return "/games/"+game.getId()+"/gamePlayer/"+game.getCurrentGamePlayerId()+"/decision";
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
					}
				}
				return classification;
		}
}
