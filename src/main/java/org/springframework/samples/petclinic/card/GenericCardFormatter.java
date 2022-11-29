package org.springframework.samples.petclinic.card;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class GenericCardFormatter implements Formatter<GenericCard>{

    @Override
    public String print(GenericCard card, Locale locale) {
        return card.getColour().toString() + "-" + card.getType().toString();
    }

    @Override
    public GenericCard parse(String text, Locale locale) throws ParseException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
