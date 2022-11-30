package org.springframework.samples.virus.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.springframework.samples.virus.model.BaseEntity;
import org.springframework.samples.virus.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "players_statistics")
public class Statistics extends BaseEntity {
  
  @Column(name = "num_played_games")
  @Min(0)
  private Integer numPlayedGames;
  
  @Min(0)
  @Column(name = "num_won_games")
  private Integer numWonGames;

  @Min(0)
  private Integer points;
  
  @OneToOne(optional = false)
  @JoinColumn(name = "player_id", referencedColumnName = "id")
  private Player player;
  
}
