package org.springframework.samples.petclinic.room;


import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.apache.catalina.User;
import org.hibernate.validator.constraints.Range;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.owner.Owner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "room")
public class Room {
    
	@NotEmpty
	Integer totalGamesPlayed;

    @NotEmpty
    Integer maxNumbersPlayed;

    @NotEmpty
    Boolean isPrivate;

    @Id
    @Range(min=0,max=999999)
    //Generar automaticamente
    Integer code;

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Owner> owners;


	


}
