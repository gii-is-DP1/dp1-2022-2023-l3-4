package org.springframework.samples.petclinic.achievements;


import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "achievement_types")
public class AchievementType extends NamedEntity {

}
