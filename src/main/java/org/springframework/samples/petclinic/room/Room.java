package org.springframework.samples.petclinic.room;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Room extends BaseEntity{

    
    Integer totalGamesPlayer;

    @Min(2)
    @Max(6)
    Integer numMaxPlayers;

    @NotEmpty
    @Size(min=1,max=20)
    String roomName;

    @NotNull
    Boolean isPrivate;

    Boolean active;

    @OneToMany(mappedBy="room")
    private Collection<Player> players;

    @ManyToOne(optional = false)
    private Player host;

    String password;
}