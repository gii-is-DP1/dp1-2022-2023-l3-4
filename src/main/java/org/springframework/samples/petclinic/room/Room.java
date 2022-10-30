package org.springframework.samples.petclinic.room;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CascadeType;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.owner.Owner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Room extends BaseEntity{

    @NotEmpty
    Integer totalGamesPlayer;

    @NotEmpty
    Integer numMaxPlayers;

    @NotEmpty
    String roomName;

    @NotEmpty
    Boolean isPrivate;

    @OneToMany(mappedBy="room")
    private Collection<Owner> owners;

    
}