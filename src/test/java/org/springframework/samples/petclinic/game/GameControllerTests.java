package org.springframework.samples.petclinic.game;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.card.Hand;
import org.springframework.samples.petclinic.card.GenericCard.Colour;
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.room.Room;
import org.springframework.samples.petclinic.room.RoomService;
import org.springframework.samples.petclinic.statistics.WonPlayedGamesException;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.ui.ModelMap;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class GameControllerTests {

	private static final Integer TEST_GAME_ID = 1;

    @Autowired
    private MockMvc mockMvc; 
	
		@MockBean
		private GameService gameService;
		
		@MockBean
		private RoomService roomService;
		
		@MockBean
		private GamePlayerService gamePlayerService;
		
		@MockBean
		private CardService cardService;
		
		@MockBean
		private AuthenticationService authenticationService;
		
		
		@Autowired
		private GameController gameController;
		

		@BeforeEach
		public void init() {
			MockitoAnnotations.initMocks(this);
		}
		
		@Test
		public void testInit() {
			//given
			Integer roomId = 1;
			Integer gameId = 0;
			Room room = new Room();
			Game runningGame = new Game();
			Player currentPlayer = new Player();
			GamePlayer gp_vista = new GamePlayer();
			ModelMap model = new ModelMap();
			runningGame.setId(0);
			runningGame.setWinner(null);
			Card card = new Card();
			card.setBody(false);
			gp_vista.setCards(List.of(card));
			card.setGamePlayer(gp_vista);
			runningGame.setGamePlayer(List.of(gp_vista));
			runningGame.setTurn(0);
			room.setHost(currentPlayer);
			
			//when
			when(authenticationService.getGamePlayer()).thenReturn(gp_vista);
			when(roomService.findRoomById(roomId)).thenReturn(room);
			when(gameService.getRunningGame(room)).thenReturn(null);
			when(gameService.findGame(gameId)).thenReturn(runningGame);
			when(gameService.startGame(room)).thenReturn(runningGame);
			when(authenticationService.getPlayer()).thenReturn(currentPlayer);
			
			//then
			String result = gameController.init(roomId, model);
			assertThat(result).isEqualTo("redirect:/games/"+ runningGame.getId());
		}

		@Test
		public void testInitNoHost() {
			//given
			Integer roomId = 1;
			Integer gameId = 0;
			Room room = new Room();
			Game runningGame = new Game();
			Player currentPlayer = new Player();
			GamePlayer gp_vista = new GamePlayer();
			ModelMap model = new ModelMap();
			runningGame.setId(0);
			runningGame.setWinner(null);
			Card card = new Card();
			card.setBody(false);
			gp_vista.setCards(List.of(card));
			card.setGamePlayer(gp_vista);
			runningGame.setGamePlayer(List.of(gp_vista));
			runningGame.setTurn(0);
			room.setHost(new Player());

			//when
			when(authenticationService.getGamePlayer()).thenReturn(gp_vista);
			when(roomService.findRoomById(roomId)).thenReturn(room);
			when(gameService.getRunningGame(room)).thenReturn(null);
			when(gameService.findGame(gameId)).thenReturn(runningGame);
			when(gameService.startGame(room)).thenReturn(runningGame);
			when(authenticationService.getPlayer()).thenReturn(currentPlayer);
			
			//then
			String result = gameController.init(roomId, model);
			assertThat(result).isEqualTo("welcome");
		}

		@Test
		public void testInitRunningGame() {
			//given
			Integer roomId = 1;
			Integer gameId = 0;
			Room room = new Room();
			Game runningGame = new Game();
			Player currentPlayer = new Player();
			GamePlayer gp_vista = new GamePlayer();
			ModelMap model = new ModelMap();
			runningGame.setId(0);
			runningGame.setWinner(null);
			Card card = new Card();
			card.setBody(false);
			gp_vista.setCards(List.of(card));
			card.setGamePlayer(gp_vista);
			runningGame.setGamePlayer(List.of(gp_vista));
			runningGame.setTurn(0);
			room.setHost(new Player());

			//when
			when(authenticationService.getGamePlayer()).thenReturn(gp_vista);
			when(roomService.findRoomById(roomId)).thenReturn(room);
			when(gameService.getRunningGame(room)).thenReturn(runningGame);
			when(gameService.findGame(gameId)).thenReturn(runningGame);
			when(gameService.startGame(room)).thenReturn(runningGame);
			when(authenticationService.getPlayer()).thenReturn(currentPlayer);
			
			//then
			String result = gameController.init(roomId, model);
			assertThat(result).isEqualTo("games/game");
		}
		
		@Test
		public void testListRunningGames() {
			//given
			Collection<Game> runningGames = new ArrayList<>();
			ModelMap model = new ModelMap();
			
			//when
			when(gameService.listRunningGames()).thenReturn(runningGames);
			
			//then
			String result = gameController.listRunningGames(model);
			assertThat(result).isEqualTo(GameController.RUNNING_GAMES_LISTING);
			assertThat(model.get("games")).isEqualTo(runningGames);
		}
		
		@Test
		public void testTerminateRunningGames() {
			//given
			Collection<Game> terminateGames = new ArrayList<>();
			ModelMap model = new ModelMap();
			
			//when
			when(gameService.listTerminateGames()).thenReturn(terminateGames);
			
			//then
			String result = gameController.terminateRunningGames(model);
			assertThat(result).isEqualTo(GameController.TERMINATE_GAMES_LISTING);
			assertThat(model.get("games")).isEqualTo(terminateGames);
		}

		@WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    	@Test
    	public void testDeleteTerminatedGames() throws Exception {
        Game mockGame = new Game();
        mockGame.setId(TEST_GAME_ID);
        mockGame.setRoom(new Room());
        mockGame.setGamePlayer(new ArrayList<>());
        mockGame.setCards(new ArrayList<>());
        mockGame.setWinner(new GamePlayer());
        
        mockMvc.perform(get("/games/{gameId}/delete", TEST_GAME_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("games/terminateGameListing"))
            .andExpect(model().attributeExists("message"));
    }
		
		@Test
		public void testMuestraVista() {
			//given
			int gameId = 1;
			GamePlayer gp_vista = new GamePlayer();
			Game game = new Game();
			ModelMap model = new ModelMap();
			Card card = new Card();
			card.setBody(false);
			gp_vista.setCards(List.of(card));
			card.setGamePlayer(gp_vista);
			game.setGamePlayer(List.of(gp_vista));
			game.setTurn(0);

			//when
			when(gameService.findGame(gameId)).thenReturn(game);
			when(authenticationService.getGamePlayer()).thenReturn(gp_vista);
			
			//then
			String result = gameController.muestraVista(gameId, model);
			assertThat(result).isEqualTo("games/game");
			assertThat(model.get("hand")).isEqualTo(gp_vista.getHand());
			assertThat(model.get("bodies")).isInstanceOf(Map.class);
			assertThat(model.get("isYourTurn")).isInstanceOf(Boolean.class);
			assertThat(model.get("currentTurnGamePlayer")).isInstanceOf(GamePlayer.class);
		}
		
    @WithMockUser
    @Test
	public void testGeneraTablero() {
		ModelMap model = new ModelMap();
		GamePlayer gamePlayer = new GamePlayer();
		Game game = new Game();
        Card card = new Card();
		card.setBody(false);
        gamePlayer.setCards(List.of(card));
        card.setGamePlayer(gamePlayer);
        game.setGamePlayer(List.of(gamePlayer));
		
		ModelMap result = gameController.generaTablero(model, gamePlayer, game);
		
		assertTrue(result.containsKey("hand"));
		assertTrue(result.containsKey("bodies"));
		assertEquals(gamePlayer.getHand(), result.get("hand"));
	}
	
	@Test
	public void testTurn() {
		//given
		int gameId = 1;
		Game game = new Game();
		game.setId(1);
		GamePlayer gamePlayer = new GamePlayer();
        game.setGamePlayer(List.of(gamePlayer));
		Card card = new Card();
        GenericCard genericCard = new GenericCard();
        genericCard.setId(1);
        genericCard.setType(Type.ORGAN);
        genericCard.setColour(Colour.RED);
		card.setType(genericCard);
		card.setBody(true);
		gamePlayer.setCards(List.of(card));
		card.setGamePlayer(gamePlayer);
		
		//when
		when(gameService.findGame(gameId)).thenReturn(game);
		
		//then
		String result = gameController.turn(gameId);
		assertThat(result).isEqualTo("redirect:/games/" + game.getId());
	}

	@Test
	public void testTurnWinner() {
		int gameId = 1;
		Game game = new Game();
		game.setId(gameId);
		GamePlayer gamePlayer = new GamePlayer();
		GenericCard generic_heart =new GenericCard(1,Colour.RED, Type.ORGAN);
    	GenericCard generic_stomach =new GenericCard(2,Colour.GREEN, Type.ORGAN);
    	GenericCard generic_rainbow = new GenericCard(3, Colour.RAINBOW, Type.ORGAN);
		GenericCard generic_bone = new GenericCard(12,Colour.YELLOW, Type.ORGAN);
		Card organ_heart = new Card(1, true, gamePlayer, generic_heart);
    	Card organ_rainbow = new Card(3, true, gamePlayer, generic_rainbow);
    	Card organ_stomach = new Card(2, true, gamePlayer, generic_stomach);
		Card organ_bone = new Card(13,true,gamePlayer,generic_bone);
		game.setWinner(gamePlayer);
		gamePlayer.setWinner(true);
		List<Card> cards = new ArrayList<>();
		cards.add(organ_bone);
		cards.add(organ_heart);
		cards.add(organ_rainbow);
		cards.add(organ_stomach);
		gamePlayer.setCards(cards);
		List<GamePlayer> gps = new ArrayList<>();
		gps.add(gamePlayer);
		game.setGamePlayer(gps);
		
		//when
		when(gameService.findGame(gameId)).thenReturn(game);
		
		//then
		String result = gameController.turn(gameId);
		assertThat(result).isEqualTo("redirect:/games/"+gameId+"/classification");
	}

    @Test
	public void testPlay() {
		//given
		int gameId = 1;
		Integer cardId = 1;
		Card card = new Card();
		GamePlayer gp = new GamePlayer();
		Game game = new Game();
		ModelMap model = new ModelMap();
        GenericCard genericCard = new GenericCard();
        genericCard.setId(1);
        genericCard.setType(Type.ORGAN);
        genericCard.setColour(Colour.RED);
		card.setType(genericCard);
		card.setBody(false);
		gp.setCards(List.of(card));
		card.setGamePlayer(gp);
		game.setGamePlayer(List.of(gp));
		
		//when
		when(cardService.findCard(cardId)).thenReturn(Optional.of(card));
		when(authenticationService.getGamePlayer()).thenReturn(gp);
		when(gameService.findGame(gameId)).thenReturn(game);
		
		//then
		assertEquals("/games/selecciona", gameController.play(gameId, cardId, model));
	}

	@Test
	public void testPlayGloves() {
		//given
		int gameId = 1;
		Integer cardId = 1;
		Card card = new Card();
		GamePlayer gp = new GamePlayer();
		Game game = new Game();
		ModelMap model = new ModelMap();
        GenericCard genericCard = new GenericCard();
        genericCard.setId(1);
        genericCard.setType(Type.GLOVES);
        genericCard.setColour(Colour.RED);
		card.setType(genericCard);
		card.setBody(false);
		gp.setCards(List.of(card));
		card.setGamePlayer(gp);
		game.setGamePlayer(List.of(gp));
		
		//when
		when(cardService.findCard(cardId)).thenReturn(Optional.of(card));
		when(authenticationService.getGamePlayer()).thenReturn(gp);
		when(gameService.findGame(gameId)).thenReturn(game);
		
		//then
		assertEquals("redirect:/games/1", gameController.play(gameId, cardId, model));
	}

	@Test
	public void testPlayNoCard() {
		//given
		int gameId = 1;
		Integer cardId = 1;
		Card card = new Card();
		GamePlayer gp = new GamePlayer();
		Game game = new Game();
		ModelMap model = new ModelMap();
        GenericCard genericCard = new GenericCard();
        genericCard.setId(1);
        genericCard.setType(Type.GLOVES);
        genericCard.setColour(Colour.RED);
		card.setType(genericCard);
		card.setBody(false);
		game.setTurn(0);
		gp.setCards(List.of(card));
		card.setGamePlayer(gp);
		game.setGamePlayer(List.of(gp));
		Optional<Card> cardO = Optional.of(card);
		//when
		when(cardService.findCard(4)).thenReturn(cardO);
		when(authenticationService.getGamePlayer()).thenReturn(gp);
		when(gameService.findGame(gameId)).thenReturn(game);
		
		//then
		assertEquals("games/game", gameController.play(gameId, cardId, model));
	}

	@Test
	public void testPlayCardNoGameplayer() {
		//given
		int gameId = 1;
		Integer cardId = 1;
		Card card = new Card();
		GamePlayer gp = new GamePlayer();
		Game game = new Game();
		ModelMap model = new ModelMap();
        GenericCard genericCard = new GenericCard();
        genericCard.setId(1);
        genericCard.setType(Type.GLOVES);
        genericCard.setColour(Colour.RED);
		card.setType(genericCard);
		card.setBody(false);
		game.setTurn(0);
		gp.setCards(new ArrayList<>());
		card.setGamePlayer(gp);
		game.setGamePlayer(List.of(gp));
		//when
		when(cardService.findCard(cardId)).thenReturn(Optional.of(card));
		when(authenticationService.getGamePlayer()).thenReturn(gp);
		when(gameService.findGame(gameId)).thenReturn(game);
		
		//then
		assertEquals("games/game", gameController.play(gameId, cardId, model));
	}
	
	@Test
	public void testPlayOnBody() {
		//given
		Integer gameId = 1;
		Integer cardId = 1;
		Integer g_id = 1;
		GamePlayer gp = new GamePlayer();
		Card card = new Card();
		Game game = new Game();
		game.setId(gameId);
        GenericCard genericCard = new GenericCard();
        genericCard.setId(1);
        genericCard.setType(Type.ORGAN);
        genericCard.setColour(Colour.RED);
		card.setType(genericCard);
		card.setGamePlayer(gp);
		card.setBody(false);
		List<Card> cards = new ArrayList<>();
		cards.add(card);
		gp.setCards(cards);
		gp.setId(g_id);
		List<GamePlayer> gps = new ArrayList<>();
		gps.add(gp);
		game.setGamePlayer(gps);
		ModelMap model = new ModelMap();
		
		//when
		when(authenticationService.getGamePlayer()).thenReturn(gp);
		when(gamePlayerService.findById(g_id)).thenReturn(Optional.of(gp));
		when(cardService.findCard(cardId)).thenReturn(Optional.of(card));
		when(gameService.findGame(gameId)).thenReturn(game);
		
		//then
		assertEquals("redirect:/games/1", gameController.playOnBody(gameId, cardId, gp.getId(), model));
	}
	
	@Test
	public void testPlayOnCard() {
		//given
		Game game = new Game();
		game.setId(1);
		Integer cardId = 1;
		Integer targetC = 1;
		Card card = new Card();
		GamePlayer gp = new GamePlayer();
        GenericCard genericCard = new GenericCard();
        genericCard.setId(1);
        genericCard.setType(Type.VACCINE);
        genericCard.setColour(Colour.RED);
		card.setType(genericCard);
		card.setBody(false);
		List<Card> cards = new ArrayList<>();
		cards.add(card);
		gp.setCards(cards);
		List<GamePlayer> gps = new ArrayList<>();
		gps.add(gp);
		game.setGamePlayer(gps);
		ModelMap model = new ModelMap();
		
		//when
		when(gameService.findGame(1)).thenReturn(game);
		when(cardService.findCard(cardId)).thenReturn(Optional.of(card));
		
		//then
		assertEquals("redirect:/games/1", gameController.playOnCard(game.getId(), cardId, targetC, model));
	}
	
	@Test
	public void testPlayOnAnotherCard() {
		//given
		Integer gameId = 1;
		Integer cardId = 1;
		Integer targetC = 2;
		Integer targetC2 = 3;
		GamePlayer gp = new GamePlayer();
		Card card = new Card();
        GenericCard genericCard = new GenericCard();
        genericCard.setId(1);
        genericCard.setType(Type.TRANSPLANT);
        genericCard.setColour(Colour.RED);
		GenericCard generic_heart =new GenericCard(1,Colour.RED, Type.ORGAN);
    	GenericCard generic_rainbow = new GenericCard(3, Colour.RAINBOW, Type.ORGAN);
		Card organ_heart = new Card(1, false, gp, generic_heart);
    	Card organ_rainbow = new Card(3, false, null, generic_rainbow);
		organ_heart.setId(targetC);
		organ_rainbow.setId(targetC2);
		card.setType(genericCard);
		ModelMap model = new ModelMap();
		Game game = new Game();
		game.setId(gameId);
		card.setType(genericCard);
		card.setBody(false);
		List<Card> cards = new ArrayList<>();
		cards.add(card);
		cards.add(organ_heart);
		gp.setCards(cards);
		List<GamePlayer> gps = new ArrayList<>();
		gps.add(gp);
		game.setGamePlayer(gps);
		
		//when
		when(gameService.findGame(gameId)).thenReturn(game);
		when(cardService.findCard(cardId)).thenReturn(Optional.of(card));
		when(cardService.findCard(targetC)).thenReturn(Optional.of(organ_heart));
		when(cardService.findCard(targetC2)).thenReturn(Optional.of(organ_rainbow));
		
		//then
		assertEquals("redirect:/games/1", gameController.playOnAnotherCard(gameId, cardId, targetC, targetC2, model));
	}

        @Test
		public void playVirus_validInput_returnsTurn() {
			// Arrange
			int gameId = 1;
			Game game = new Game();
			game.setId(gameId);
			Integer c1_id = 2;
			Integer c2_id = 3;
			ModelMap model = new ModelMap();
			game.setGamePlayer(new ArrayList<>());
			Optional<Card> c1 = Optional.of(new Card());
			Optional<Card> c2 = Optional.of(new Card());
			Card old_card = new Card();
			c1.get().setVaccines(new ArrayList<>());
			c2.get().setVaccines(new ArrayList<>());

			when(gameService.findGame(gameId)).thenReturn(game);
			when(cardService.findCard(c1_id)).thenReturn(c1);
			when(cardService.findCard(c2_id)).thenReturn(c2);
			
			// Act
			String result = gameController.playVirus(gameId, c1_id, c2_id, model);
			
			// Assert
			assertEquals("redirect:/games/1", result);
			verify(cardService).infect(c2.get(), c1.get());
			verify(cardService).save(c1.get());
			verify(cardService).save(c2.get());
		}
		
        @Test
		public void playThief_validInput_returnsTurn() {
			// Arrange
			int gameId = 1;
			Integer c_id = 2;
			Integer stolenCardId = 3;
			Game game = new Game();
			game.setId(gameId);
			ModelMap model = new ModelMap();
			GamePlayer gp2 = new GamePlayer();
			GamePlayer gp = new GamePlayer();
			List<Card> cards = new ArrayList<>();
			Card card = new Card();
			Card card1 = new Card();
			card.setGamePlayer(gp2);
			card.setBody(true);
			card1.setBody(false);
			card.setId(stolenCardId);
			cards.add(card);
			gp2.setCards(cards);
			List<GamePlayer> gps = new ArrayList<>();
			gps.add(gp);
			gps.add(gp2);
			gp.setCards(cards);
			game.setGamePlayer(gps);
			when(authenticationService.getGamePlayer()).thenReturn(gp);
			when(cardService.findCard(stolenCardId)).thenReturn(Optional.of(card));
			when(cardService.findCard(c_id)).thenReturn(Optional.of(card1));
			when(gameService.findGame(gameId)).thenReturn(game);
			// Act
			String result = gameController.playThief(gameId, c_id, stolenCardId, model);
			
			// Assert
			assertEquals("redirect:/games/1", result);
		}
		
		@Test
		public void playInfect_validInput_returnsTurn() {
			// Arrange
			int gameId = 1;
			Integer g_id = 2;
			Integer c_id = 3;
			ModelMap model = new ModelMap();
			
			// Act
			String result = gameController.playInfect(gameId, g_id, c_id, model);
			
			// Assert
			assertEquals("turn", result);
		}
		
		@Test
		public void playInfect_invalidInput_returnsMuestraVista() {
			// Arrange
			int gameId = 1;
			Integer g_id = null;
			Integer c_id = 3;
			ModelMap model = new ModelMap();
			
			// Act
			String result = gameController.playInfect(gameId, g_id, c_id, model);
			
			// Assert
			assertEquals("muestraVista", result);
			assertEquals("Jugador no encontrado", model.get("message"));
			assertEquals("info", model.get("messageType"));
		}
		
		@Test
		public void playGlove_validInput_returnsTurn() {
			// Arrange
			int gameId = 1;
			Integer c_id = 2;
			
			// Act
			String result = gameController.playGlove(gameId, c_id);
			
			// Assert
			assertEquals("turn", result);
		}
		
		@Test
		public void playGlove_invalidInput_returnsMuestraVista() {
			// Arrange
			int gameId = 1;
			Integer c_id = null;
			ModelMap model = new ModelMap();
			
			// Act
			String result = gameController.playGlove(gameId, c_id);
			
			// Assert
			assertEquals("muestraVista", result);
			assertEquals("Card not found.", model.get("message"));
			assertEquals("info", model.get("messageType"));
		}
	
	@Test
	public void testPlayMedicalError_GamePlayerNotFound() {
		// Arrange
		int gameId = 1;
		Integer g_id = 2;
		Integer c_id = 3;
		ModelMap model = new ModelMap();
		GamePlayer gamePlayer1 = mock(GamePlayer.class);
		
		when(authenticationService.getGamePlayer()).thenReturn(gamePlayer1);
		when(gamePlayerService.findById(g_id)).thenReturn(Optional.empty());
		
		// Act
		String result = gameController.playMedicalError(gameId, g_id, c_id, model);
		
		// Assert
		assertEquals("muestraVista", result);
		assertEquals("Jugador no encontrado", model.get("message"));
		assertEquals("info", model.get("messageType"));
		verify(gameService, never()).medicalError(any(), any());
		verify(cardService, never()).save(any());
		verify(gamePlayerService, never()).save(any());
	}
	
	@Test
	public void testPlayMedicalError_CardNotFound() {
		// Arrange
		int gameId = 1;
		Integer g_id = 2;
		Integer c_id = 3;
		ModelMap model = new ModelMap();
		GamePlayer gamePlayer1 = mock(GamePlayer.class);
		GamePlayer gamePlayer2 = mock(GamePlayer.class);
		
		when(authenticationService.getGamePlayer()).thenReturn(gamePlayer1);
		when(gamePlayerService.findById(g_id)).thenReturn(Optional.of(gamePlayer2));
		when(cardService.findCard(c_id)).thenReturn(Optional.empty());
		
		// Act
		String result = gameController.playMedicalError(gameId, g_id, c_id, model);
		
		// Assert
		assertEquals("muestraVista", result);
		assertEquals("Carta no encontrada", model.get("message"));
		assertEquals("info", model.get("messageType"));
		verify(gameService, never()).medicalError(any(), any());
		verify(cardService, never()).save(any());
		verify(gamePlayerService, never()).save(any());
	}

    //Test method for discard
	@Test
	public void discardTestIsYourTurnAndContainsAllCards(){
		//Arrange
        Game game = new Game();
        GamePlayer gamePlayer = new GamePlayer(0);
        List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(gamePlayer);
        game.setGamePlayer(gamePlayers);
        game.setTurn(0);
		Integer gameId = 1;
		ModelMap model = new ModelMap();
        Hand cardIds = new Hand();
        when(authenticationService.getGamePlayer()).thenReturn(gamePlayer);
		when(gameService.isYourTurn(gamePlayer, gameId)).thenReturn(true);
        when(gameService.findGame(anyInt())).thenReturn(game);
		assertEquals("redirect:/games/1", gameController.discard(cardIds, gameId, model, null));
	}

    @Test
	public void discardTestIsYourTurnAndNotContainsAllCards(){
		//Arrange
        List<Card> cards = new ArrayList<>();
        GenericCard generic_heart =new GenericCard(1,Colour.RED, Type.ORGAN);
		Card organ_heart = new Card(1, true, gamePlayer, generic_heart);
        cards.add(organ_heart);
        Game game = new Game();
        GamePlayer gamePlayer = new GamePlayer(0);
        List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(gamePlayer);
        game.setGamePlayer(gamePlayers);
        game.setTurn(0);
		Integer gameId = 1;
		ModelMap model = new ModelMap();
        Hand cardIds = new Hand();
        cardIds.getCards().add(1);
        when(cardService.findAllCardsByIds(cardIds.getCards())).thenReturn(cards);
        when(authenticationService.getGamePlayer()).thenReturn(gamePlayer);
		when(gameService.isYourTurn(gamePlayer, gameId)).thenReturn(true);
        when(gameService.findGame(anyInt())).thenReturn(game);
		assertEquals("games/game", gameController.discard(cardIds, gameId, model, null));
	}

    @Test
	public void discardTestIsNotYourTurn(){
		//Arrange
        Game game = new Game();
        GamePlayer gamePlayer = new GamePlayer(0);
        List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(gamePlayer);
        game.setGamePlayer(gamePlayers);
        game.setTurn(0);
        GamePlayer currentGamePlayer = authenticationService.getGamePlayer();
        currentGamePlayer.setCards(new ArrayList<>());
        Hand cardIds = new Hand();
        Integer gameId = 1;
		ModelMap model = new ModelMap();
		when(gameService.isYourTurn(currentGamePlayer, gameId)).thenReturn(false);
        when(gameService.findGame(anyInt())).thenReturn(game); 
		assertEquals("games/game", gameController.discard(cardIds, gameId, model, null));
	}

    //Elementos comunes a classification
    int gameId = 1;
    Game game = new Game();
    List<GamePlayer> gamePlayers = new ArrayList<>();
    GamePlayer gamePlayer = new GamePlayer(0);
    ModelMap model = new ModelMap();

    public void classificationTestCommonMethod(){
		game.setId(gameId);
        game.setIsRunning(true);

		GenericCard generic_heart =new GenericCard(1,Colour.RED, Type.ORGAN);
    	GenericCard generic_stomach =new GenericCard(2,Colour.GREEN, Type.ORGAN);
    	GenericCard generic_rainbow = new GenericCard(3, Colour.RAINBOW, Type.ORGAN);
		GenericCard generic_bone = new GenericCard(12,Colour.YELLOW, Type.ORGAN);
		Card organ_heart = new Card(1, true, gamePlayer, generic_heart);
    	Card organ_rainbow = new Card(3, true, gamePlayer, generic_rainbow);
    	Card organ_stomach = new Card(2, true, gamePlayer, generic_stomach);
		Card organ_bone = new Card(13,true,gamePlayer,generic_bone);
		game.setWinner(gamePlayer);
		gamePlayer.setWinner(true);
        gamePlayer.getCards().add(organ_heart);
        gamePlayer.getCards().add(organ_rainbow);
        gamePlayer.getCards().add(organ_stomach);
        gamePlayer.getCards().add(organ_bone);
    }
	
	@Test 
	public void classificationTestHasWinnerAndIsRunning(){
        classificationTestCommonMethod();
        gamePlayers.add(gamePlayer);
        game.setGamePlayer(gamePlayers);
		when(gameService.findGame(anyInt())).thenReturn(game);
		//test
		String result = gameController.classification(gameId, model);		
		assertEquals(result, "games/classification");
		assertEquals(model.get("classification"), gamePlayers);     
	}
    
    @Test
    public void classificationTestHasWinnerAndIsNotRunning(){
        classificationTestCommonMethod();
        gamePlayers.add(gamePlayer);
        game.setGamePlayer(gamePlayers);
        game.setIsRunning(false);
        when(gameService.findGame(anyInt())).thenReturn(game);
        String result2 = gameController.classification(gameId, model);
        assertEquals("games/classification", result2 );
    }

    @Test
    public void classificationTestHasNotWinnerAndIsRunning(){
        classificationTestCommonMethod();
        gamePlayer.getCards().remove(gamePlayer.getCards().get(0));
        gamePlayers.add(gamePlayer);
        game.setGamePlayer(gamePlayers);
        game.setWinner(null);
        game.setIsRunning(false);
        game.setTurn(0);
        when(gameService.findGame(anyInt())).thenReturn(game);
        String result2 = gameController.classification(gameId, model);
        assertEquals("games/game", result2 );
    }

}
