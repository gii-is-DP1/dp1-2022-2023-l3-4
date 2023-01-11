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
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;

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

	@Autowired
	public CardService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
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

	@Transactional(readOnly=true)
		public List<Card> getBodyFromAGamePlayer(Integer gamePlayerId){
			return cardRepository.getBodyFromAGamePlayer(gamePlayerId);
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
		if((gamePlayer2.isThisOrganNotPresent(card) && card.getVaccines().size()<2) || card.getType().getType()==Type.VACCINE){
		gamePlayer1.getCards().remove(card);
		gamePlayer2.getCards().add(card);
		card.setGamePlayer(gamePlayer2);
		} else {
			throw new IllegalArgumentException("No puedes robar un órgano que ya tienes o que esté inmunizado.");
		}
	}
	private void infectOrVaccinate(Card organ, Card virus_vaccine){		
		changeGamePlayer(virus_vaccine,virus_vaccine.getGamePlayer(),organ.getGamePlayer());
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
				}else{
					throw new IllegalArgumentException("This organ is already immunized");
				}
		
			}else{
				Card virus = organ.getVirus().get(0);
				organ.setVirus(new ArrayList<>());
				virus.discard();
				vaccine.discard();
			}
			}else{
				throw new IllegalArgumentException("You can only vaccinate a " + vaccine.getType().getColour() + " or RAINBOW organ with a " +vaccine.getType().getColour() + " vaccine.");
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
				
				}		
			} else if(organ.getVaccines().size()==1){
				Card vaccine = organ.getVaccines().get(0);
				vaccine.discard();
				virus.discard();
				organ.setVaccines(new ArrayList<>());
	  }else{
		  throw new IllegalArgumentException("You can't infect an immunized organ.");
	  }
  }else{
	  throw new IllegalArgumentException("You can only infect a " + virus.getType().getColour() + " or RAINBOW organ with a " +virus.getType().getColour() + " virus.");
  }
 }
 public List<Card> findAllCardsByIds(List<Integer> cardIds) {
	return cardRepository.findCardsByIds(cardIds);
}

public List<String> getColours(List<Card> cards){
	return cards.stream().map(c->c.getType().getColour().toString()).collect(Collectors.toList());
}
public Card getARainBow(List<Card> cards){
	return cards.stream().filter(v->v.getType().getColour().toString()=="RAINBOW").findAny().get();
}

}