package org.springframework.samples.petclinic.gamePlayer;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.model.BaseEntity;
import javax.persistence.OneToOne;
import org.springframework.samples.petclinic.player.Player;

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
	private Integer turn;

    @OneToOne
    private Player player;
    
    @ManyToOne
    @JoinColumn(name = "game_id")
	private Game game;

    @Size(min=0, max=3)
    @OneToMany
    private List<Card> cards;

}
