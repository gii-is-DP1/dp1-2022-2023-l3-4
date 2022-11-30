package org.springframework.samples.virus.gamePlayer;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.samples.virus.card.Card;
import org.springframework.samples.virus.game.Game;
import org.springframework.samples.virus.model.BaseEntity;
import org.springframework.samples.virus.player.Player;

import javax.persistence.OneToOne;

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
@Table(name = "gamePlayers")
public class GamePlayer extends BaseEntity {
	private Boolean winner;
	private Boolean host;

    @OneToOne
    private Player player;
    
    

    @OneToMany
    private List<Card> cards;

}
