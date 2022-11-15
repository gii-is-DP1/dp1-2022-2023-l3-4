/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.player;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.achievements.Achievement;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.user.User;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "players")
@Getter
@Setter
public class Player extends Person {

	@Column(name = "description")
	private String description;

	@Column(name = "status")
	private Boolean status;
	
	@OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;

	@ManyToMany
	@JoinTable(name = "player_achievements", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "achievement_id"))
	private Set<Achievement> achievements;
	
}
