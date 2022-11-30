package org.springframework.samples.virus.achievements;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.samples.virus.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="achievements")
public class Achievement extends NamedEntity {

  @NotBlank
  @Column(name = "description")
  private String description;
  @Column(name = "badge_image")
  private String badgeImage;
  @Column(name = "threshold")
  private double threshold;

  @ManyToOne(optional = false)
  private AchievementType achievementType;

  public String getActualDescription() {
    return description.replace("<THRESHOLD>", String.valueOf(threshold));
  }

}
