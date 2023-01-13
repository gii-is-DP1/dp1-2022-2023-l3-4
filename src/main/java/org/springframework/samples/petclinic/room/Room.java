package org.springframework.samples.petclinic.room;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.invitation.Invitation;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Room extends BaseEntity{

    
    private Integer totalGamesPlayer;

    @Min(2)
    @Max(6)
    private Integer numMaxPlayers;

    @NotEmpty
    @Size(min=1,max=20)
    private String roomName;

    @NotNull
    private Boolean isPrivate;

    @OneToMany(mappedBy="room")
    private Collection<Player> players;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player host;

    private String password;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "room")
    private Collection<Game> games;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Invitation invitation;
}