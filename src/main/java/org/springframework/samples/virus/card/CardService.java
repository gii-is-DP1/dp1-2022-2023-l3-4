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
package org.springframework.samples.virus.card;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.virus.gamePlayer.GamePlayer;
import org.springframework.samples.virus.gamePlayer.GamePlayerRepository;

import java.util.Optional;


/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */


@Service 
public class CardService {

	private CardRepository cardRepository;
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	public CardService(CardRepository cardRepository, GamePlayerRepository gamePlayerRepository) {
		this.cardRepository = cardRepository;
		this.gamePlayerRepository = gamePlayerRepository;
	}

	@Transactional(readOnly = true)	
	public List<Card> findCards() throws DataAccessException {
		return cardRepository.findAll();
	}	

	@Transactional(readOnly = true)
	public List<Card> findPlayed(){
		return cardRepository.findAll().stream().filter(x->x.getPlayed()).collect(Collectors.toList());

	}

  @Transactional(readOnly = true)
		public Optional<Card> findCard(Integer i){
			return cardRepository.findById(i);
		}

	@Transactional
		public Card save(Card card){
			return cardRepository.save(card);	
		}
	
	@Transactional(readOnly = true)
	public List<Card> getBodyFromAGamePlayer(Integer gamePlayerId){
		List<Card> result = new ArrayList<>();
		Optional<GamePlayer> gp = gamePlayerRepository.findById(gamePlayerId);
		if(gp.isPresent()){
			result = gp.get().getCards().stream().filter(x->x.getBody()).collect(Collectors.toList());
		}
		return result;
	}
}
