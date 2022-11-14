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
package org.springframework.samples.petclinic.card;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/games/{gameId}/")
public class CardController {

	private final CardService cardService;
	private final GameService gameService;

	@Autowired
	public CardController(CardService cardService, GameService gameService) {
		this.cardService = cardService;
		this.gameService=gameService;
	}
//cuando no quedan más cartas en la baraja del juego, se recogen todas las jugadas y pasan a ser la nueva baraja
	@GetMapping("/cards")
	public void Shuffle(@Valid Game game, BindingResult result,	@PathVariable("gameId") int gameId){
		List<Card> playedcards = cardService.findPlayed();
		Game currentGame = gameService.findGames(gameId);
		currentGame.setCards(playedcards);
		gameService.save(currentGame);
	}
	
	public void imprimir(){
		Card card = cardService.findCard(1).get();
		System.out.println(card.getVirus());
	}

}
