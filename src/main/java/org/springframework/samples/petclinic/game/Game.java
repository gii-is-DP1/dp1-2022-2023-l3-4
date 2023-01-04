
package org.springframework.samples.petclinic.game;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.model.BaseEntity;


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
	@Transient
	private Map<Integer,List<GamePlayer>> classification = new HashMap<>();

	@OneToMany
	private List<Card> cards;

	@Size(min=2, max=6)
	@OneToMany
	private List<GamePlayer> gamePlayer;

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

	public static String humanReadableDuration(Duration d) {
		return d.toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase();
	}

	public Boolean hasAnyWinners() {
		return gamePlayer.stream().anyMatch(g -> g.isWinner());
	}

}
