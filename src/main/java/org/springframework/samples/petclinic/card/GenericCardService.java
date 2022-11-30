package org.springframework.samples.petclinic.card;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class GenericCardService {
    
    private GenericCardRepository gCardRepository;

	@Autowired
	public GenericCardService(GenericCardRepository gCardRepository) {
		this.gCardRepository = gCardRepository;
	}

	@Transactional(readOnly = true)
	public List<GenericCard> listGCards(){
		return gCardRepository.findAll();
	}

    @Transactional
	public GenericCard save(GenericCard gcard){
		return gCardRepository.save(gcard);	
	}

	@Transactional(readOnly = true)	
	public List<GenericCard> findGCards() throws DataAccessException {
		return gCardRepository.findAll();
	}	

	List<GenericCard> cards = new ArrayList<GenericCard>(68);
	@PostConstruct
	//Generar baraja
	public void reset() {
		GenericCard.Colour[] colours = GenericCard.Colour.values();

		for(int i = 0; i < 4; i++) {
			GenericCard.Colour colour = colours[i];
			//ORGANOS
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0), colour.toString() + "organ"));
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0), colour.toString() + "organ"));
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0), colour.toString() + "organ"));
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0), colour.toString() + "organ"));
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0), colour.toString() + "organ"));
			//VIRUS Y VACUNAS
			for(int j = 1; j<3; j++) {
				cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(j), colour.toString() + GenericCard.Type.getType(j).toString()));
				cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(j), colour.toString() + GenericCard.Type.getType(j).toString()));
				cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(j), colour.toString() + GenericCard.Type.getType(j).toString()));
				cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(j), colour.toString() + GenericCard.Type.getType(j).toString()));
			}
		}
		//ORGANOS Y VIRUS ARCOIRIS
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.ORGAN, "RainbowOrgan"));
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.VIRUS, "RainbowVirus"));
		for(int i = 0; i < 4; i++) {
			cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.VACCINE, "RainbowVaccine"));
		}
		//ESPECIALES(1)
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.GLOVES, "Gloves"));
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.ERROR, "Error"));
		
		//ESPECIALES(3)
		for(int i = 0; i < 3; i++) {
			cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.THIEF, "Thief"));
			cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.TRANSPLANT, "Transplant"));
		}
		
		//ESPECIAL(2)
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.INFECTION, "Infection"));
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.INFECTION, "Infection"));

		for(GenericCard c : cards) {
			gCardRepository.save(c);
		}
	}
}
