package org.springframework.samples.petclinic.card;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Integer> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }
    public List<Integer> getCards(){
        return cards;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }
    
}
