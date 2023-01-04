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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;

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
	private GamePlayerService gamePlayerService;

	@Autowired
	public CardService(CardRepository cardRepository, GamePlayerService gamePlayerService) {
		this.cardRepository = cardRepository;
		this.gamePlayerService = gamePlayerService;
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
	public void shuffle(List<Card> cards){
		Collections.shuffle(cards);
			for(Card c: cards){
				c.setPlayed(false);
				cardRepository.save(c);
			}
	}

	@Transactional
	public void changeGamePlayer(Card card, GamePlayer gamePlayer1, GamePlayer gamePlayer2){
		gamePlayer1.getCards().remove(card);
		gamePlayer2.getCards().add(card);
		card.setGamePlayer(gamePlayer2);
		cardRepository.save(card);
	}
	private void infectOrVaccinate(Card organ, Card virus_vaccine){		
		virus_vaccine.setGamePlayer(organ.getGamePlayer());
		if(virus_vaccine.getType().getType().toString()=="VIRUS"){
			organ.getVirus().add(virus_vaccine);
			virus_vaccine.setCardVirus(organ);
		}else{
			organ.getVaccines().add(virus_vaccine);
			virus_vaccine.setCardVaccine(organ);
		}		
	}

  @Transactional
	public void vaccinate(Card organ, Card vaccine){
		if(organ.areCompatible(vaccine)){
			if(organ.getVirus().size()==0){
				if(organ.getVaccines().size()<2){
					infectOrVaccinate(organ, vaccine);
					cardRepository.save(vaccine);
					cardRepository.save(organ);
				}else{
					throw new IllegalArgumentException("Este órgano ya está inmunizado");
				}
		
			}else{
				Card virus = organ.getVirus().get(0);
				virus.discard();
				vaccine.discard();
				cardRepository.save(vaccine);
				cardRepository.save(organ);
				cardRepository.save(virus);
			}
			}else{
				throw new IllegalArgumentException("No puedes vacunar un órgano que no sea ni arcoirís ni de tu color");
			}
	}

	@Transactional
	public void infect(Card organ, Card virus){
		if(organ.areCompatible(virus)){
			if(organ.getVaccines().size()==0){
				if(organ.getVirus().size()==0){
					infectOrVaccinate(organ, virus);
				}else{
					Card virus1 = organ.getVirus().get(0);
					organ.discard();
					virus1.discard();
					virus.discard();
					cardRepository.save(virus1);
				
				}
				cardRepository.save(virus);
				cardRepository.save(organ);			
			} else if(organ.getVaccines().size()==1){
				Card vaccine = organ.getVaccines().get(0);
				vaccine.discard();
				virus.discard();
				organ.setVaccines(new ArrayList<>());
				cardRepository.save(virus);
				cardRepository.save(organ);
				cardRepository.save(vaccine);
	  }else{
		  throw new IllegalArgumentException("No puedes infectar un órgano inmunizado");
	  }
  }else{
	  throw new IllegalArgumentException("No puedes infectar un ógano que no sea ni arcoirís ni de tu color si tu virus no es arcoíris");
  }
 }
 public List<Card> findAllCardsByIds(List<Integer> cardIds) {
	return cardRepository.findCardsByIds(cardIds);
}

	@Transactional(readOnly = false)
	public void addOrgan(Card organ, GamePlayer gplayer1, GamePlayer gplayer2) {
		if(gplayer2.isThisOrganNotPresent(organ)){
			gplayer1.getCards().remove(organ);
			gamePlayerService.save(gplayer1);
			organ.setGamePlayer(gplayer2);
			organ.setBody(true);
			save(organ);
			gplayer2.getCards().add(organ);
			gamePlayerService.save(gplayer2);
		}
	}
}