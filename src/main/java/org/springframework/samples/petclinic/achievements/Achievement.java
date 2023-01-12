package org.springframework.samples.petclinic.achievements;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="achievements")
public class Achievement extends BaseEntity {

  @Size(min = 3, max = 50)
	@Column(name = "name", unique = true)
	private String name;

  @NotBlank
  @Column(name = "description")
  private String description;

  @Column(name = "badge_image")
  private String badgeImage;

  @Column(name = "threshold")
  @NotNull
  private Integer threshold;

  @ManyToOne(optional = false)
  @JoinColumn(name = "type", referencedColumnName = "id")
  private AchievementType type;

  public String getActualDescription() {
    return description.replace("<THRESHOLD>", String.valueOf(threshold));
  }

}
