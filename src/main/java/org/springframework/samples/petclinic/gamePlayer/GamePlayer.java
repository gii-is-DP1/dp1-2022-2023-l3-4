package org.springframework.samples.petclinic.gamePlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.model.BaseEntity;
import javax.persistence.OneToOne;
import org.springframework.samples.petclinic.player.Player;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Getter
@Setter
@Entity
@Table(name = "game_players")
public class GamePlayer extends BaseEntity {
	private Boolean winner;
	private Boolean host;

    @OneToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player player;
    

    @OneToMany(mappedBy = "gamePlayer")
    @JsonIgnore
    private List<Card> cards;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gamePlayer", orphanRemoval = true)
    private Set<Game> games;

    public GamePlayer(){
        
    }
    public GamePlayer(Integer id){
        Player p= new Player();
        p.setId(0);
        this.player=p;
        this.id = id;
        this.cards= new ArrayList<>();
        // this.host=false;
        // this.winner=false;
    }

    public List<Card> getBody(){
        return getCards().stream().filter(x->x.getBody()).collect(Collectors.toList());
    }
    public List<Card> getHand(){
        return getCards().stream().filter(x->!x.getBody() && x.getCardVaccine()==null && x.getCardVirus()==null).collect(Collectors.toList());
    }

    public Integer getNumHealthyOrgans(){
        return getBody().stream().filter(x->x.getVirus().size()==0).collect(Collectors.toList()).size();
    }

    public Boolean isWinner(){
       Integer numOrgansNeededToWin = 4; 
       return  getNumHealthyOrgans()==numOrgansNeededToWin;
    }

    public List<String> getBodyColours(){
        return getBody().stream().map(x->x.getType().getColour().toString()).collect(Collectors.toList());

    }
    public Boolean isThisOrganNotPresent(Card organ){
        Set<String> cards = new HashSet<>();
        cards.addAll(getBodyColours());
		cards.add(organ.getType().getColour().name());
		return cards.size()!=getBodyColours().size();
    }

    public List<Card> getVirusInTheBody(){
        List<Card> body=  getCards();
        body.removeAll(getHand());
        return body.stream().filter(c->c.getType().getType()==Type.VIRUS).collect(Collectors.toList());
    }

    public List<Card> getCleanOrgans(){
        return getBody().stream().filter(o-> o.getVaccines().size()==0 && o.getVirus().size()==0).collect(Collectors.toList());
    }

}
