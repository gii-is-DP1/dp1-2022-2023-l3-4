package org.springframework.samples.petclinic.card;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

public class GenericCardController {
    
	private final GenericCardService gCardService;
	
	@Autowired
	public GenericCardController(GenericCardService gCardService) {
		this.gCardService=gCardService;
	}

    List<GenericCard> cards = new ArrayList<GenericCard>(68);
	//Generar baraja
	public void reset() {
		GenericCard.Colour[] colours = GenericCard.Colour.values();

		for(int i = 0; i < 4; i++) {
			GenericCard.Colour colour = colours[i];
			//ORGANOS
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0)));
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0)));
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0)));
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0)));
			cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(0)));
			//VIRUS Y VACUNAS
			for(int j = 1; j<3; j++) {
				cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(j)));
				cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(j)));
				cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(j)));
				cards.add(new GenericCard(cards.size(), colour, GenericCard.Type.getType(j)));
			}
		}
		//ORGANOS Y VIRUS ARCOIRIS
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.ORGAN));
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.VIRUS));
		for(int i = 0; i < 4; i++) {
			cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.VACCINE));
		}
		//ESPECIALES(1)
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.GLOVES));
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.ERROR));
		
		//ESPECIALES(3)
		for(int i = 0; i < 3; i++) {
			cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.THIEF));
			cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.TRANSPLANT));
		}
		
		//ESPECIAL(2)
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.INFECTION));
		cards.add(new GenericCard(cards.size(), GenericCard.Colour.RAINBOW, GenericCard.Type.INFECTION));

		for(GenericCard c : cards) {
			gCardService.save(c);
		}
	}

    //Listar juegos
	@GetMapping(value="/genericCards")
	public String listGCards(ModelMap model){
		List<GenericCard> allGCards =  gCardService.listGCards();
		model.put("genericCards", allGCards);
		return "genericCards/listing";
	}
}

