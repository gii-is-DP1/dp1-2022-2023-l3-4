
package org.springframework.samples.petclinic.game;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.room.Room;

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
@Table(name = "games")
public class Game extends BaseEntity {

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime initialHour;
	private Boolean isRunning;
	private Integer round;
	private Integer turn;
	private Duration duration;
	
	@ManyToOne(cascade = CascadeType.PERSIST, optional = true)
	private Room room;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Card> cards;

	@Size(min=2, max=6)
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<GamePlayer> gamePlayer;

	@ManyToOne(cascade = CascadeType.PERSIST, optional = true)
	private GamePlayer winner;

	public List<Card> baraja(){
		return getCards().stream().filter(x->!x.getBody() && !x.getPlayed() && x.getGamePlayer()==null).collect(Collectors.toList());
	}
	public List<Card> discarted(){
		return getCards().stream().filter(x->x.getPlayed()).collect(Collectors.toList());
	}

	public Integer getCurrentGamePlayerId(){
		return getGamePlayer().get(getTurn()).getId();
	}
	public void endGame(){
		setIsRunning(false);
		getDuration();
		setDuration(Duration.between(getInitialHour(), LocalDateTime.now()));
	}

	public String humanReadableDuration() {
		return getDuration().toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase();
	}

	public Boolean hasAnyWinners() {
		return gamePlayer.stream().anyMatch(g -> g.isWinner());
	}

	public Integer numOrgansPlayed() {
		if(getCards()==null || getCards().size()==0){
			return 0;
		} else {
			return (int) getCards().stream()
			.filter(c -> c.getBody())
			.filter(c -> c.getType().getType().equals(GenericCard.Type.ORGAN))
			.count();
		}
	}

	public Integer numVaccinesPlayed() {
		if(getCards()==null || getCards().size()==0) {
			return 0;
		} else {
			return (int) getCards().stream()
			.filter(c -> c.getPlayed())
			.filter(c -> c.getType().getType().equals(GenericCard.Type.ORGAN))
			.flatMap(c -> c.getVaccines().stream())
			.count();
		}
	}

	public Integer numVirusPlayed() {
		if(getCards()==null || getCards().size()==0) {
			return 0;
		} else {
			return (int) getCards().stream()
			.filter(c -> c.getPlayed())
			.filter(c -> c.getType().getType().equals(GenericCard.Type.ORGAN))
			.flatMap(c -> c.getVirus().stream())
			.count();
		}
	}

}
