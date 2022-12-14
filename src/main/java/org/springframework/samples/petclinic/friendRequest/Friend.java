package org.springframework.samples.petclinic.friendRequest;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.player.Player;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "friend")
@Getter
@Setter
public class Friend extends BaseEntity{

    @Column(name = "status")
	private Boolean status;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="PlayerSend_id",referencedColumnName = "id")
	private Player playerSend;

    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="PlayerRec_id",referencedColumnName = "id")
	private Player playerRec;



    
}
