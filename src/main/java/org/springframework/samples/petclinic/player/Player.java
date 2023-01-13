package org.springframework.samples.petclinic.player;


import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
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

	@OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = false, mappedBy = "player")
	@JoinColumn(name = "gameplayer_id", referencedColumnName = "id")
	private GamePlayer gamePlayer;

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
