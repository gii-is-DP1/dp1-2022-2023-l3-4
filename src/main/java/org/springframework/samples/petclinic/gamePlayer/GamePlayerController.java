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
package org.springframework.samples.petclinic.gamePlayer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/games/{gameId}/players")
public class GamePlayerController {


	private final GamePlayerService gamePlayerService;
	private final CardService cardService;

	@Autowired
	public GamePlayerController(GamePlayerService gamePlayerService, CardService cardService) {
		this.gamePlayerService = gamePlayerService;
		this.cardService=cardService;
	}

	@GetMapping
	public List<GamePlayer> listGamePlayers(){
		return gamePlayerService.findAll();
	}
// Método para descartar cartas
	@DeleteMapping(value = "/gamePlayer/{gamePlayerId}/cards/{cards}")
    public @ResponseBody String discardCards(@PathVariable List<Card> cards, @PathVariable Integer gamePlayerId) {
        if(gamePlayerService.findById(gamePlayerId).isPresent()){
			GamePlayer gamePlayer = gamePlayerService.findById(gamePlayerId).get();
			if(gamePlayer.getCards().containsAll(cards)){
				for(Card card: cards){					
						gamePlayer.getCards().remove(card);
						card.setPlayed(true);
						gamePlayerService.save(gamePlayer);
						cardService.save(card);		
			} return "correct move";
			}else{
				return "you can't discard those cards";}		
		}else{
			return "this player is not available";} 		
    }
}