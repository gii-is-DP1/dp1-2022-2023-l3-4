
package org.springframework.samples.virus.game;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.virus.card.Card;
import org.springframework.samples.virus.gamePlayer.GamePlayer;
import org.springframework.samples.virus.model.BaseEntity;

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
	@Transient
	private Map<Integer,List<GamePlayer>> classification = new HashMap<>();

	@OneToMany
	private List<Card> cards;

	@Size(min=2, max=6)
	@OneToMany
    @JoinColumn(name = "gamePlayer_id")
	private List<GamePlayer> gamePlayer;

}
