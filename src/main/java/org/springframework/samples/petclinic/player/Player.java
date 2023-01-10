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


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.samples.petclinic.achievements.Achievement;
import org.springframework.samples.petclinic.friendRequest.Friend;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.room.Room;
import org.springframework.samples.petclinic.user.User;


import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "players")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Player extends Person {

	@Column(name = "description")
	@Size(max = 100)
	private String description;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "profile_image")
	private String profileImage;
	
	@OneToOne
  @JoinColumn(name = "username")
	private User user;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "gameplayer_id", referencedColumnName = "id")
	private GamePlayer gamePlayer;

	@ManyToMany
	@JoinTable(name = "player_achievements", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "achievement_id"))
	private Set<Achievement> achievements;

	@OneToMany(mappedBy="playerSend", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<Friend> friendSend;

	@OneToMany(mappedBy="playerRec", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<Friend> friendRec;


	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="room_id",referencedColumnName = "id")
	private Room room;
	
	public void addRoom(Room room) {
		this.room=room;
    }

	// Auditing

	@CreatedBy
	private String creator; 

	@CreatedDate 
	private LocalDateTime createdDate; 

	@LastModifiedBy 
	private String modifier;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

}
