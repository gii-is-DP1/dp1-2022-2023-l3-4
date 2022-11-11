
package org.springframework.samples.petclinic.card;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
}
