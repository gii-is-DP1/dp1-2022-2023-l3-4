package org.springframework.samples.petclinic.invitation;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.room.Room;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invitation")
@Getter
@Setter
public class Invitation extends BaseEntity {
    @Column(name = "isViewer")
    private Boolean isViewer;
    
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="PlayerInvitationSend_id",referencedColumnName = "id")
	private Player playerInvitationSend;

    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="PlayerInvitationRec_id",referencedColumnName = "id")
	private Player playerInvitationRec;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_id",referencedColumnName = "id")
    private Room room;
}

