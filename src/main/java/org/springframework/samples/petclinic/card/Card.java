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
package org.springframework.samples.petclinic.card;

import java.io.Serializable;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */

@Getter
@Setter
@Entity
@Table(name = "cards")
public class Card extends BaseEntity implements Serializable {
 
	private Boolean played;
	private Boolean body;

	// @ManyToOne
    // @JoinColumn(name="card_id")
    // private Card cards;

	// @ElementCollection
	// @Size(min=0, max=2)
    // @CollectionTable(name = "vaccines", joinColumns = @JoinColumn(name = "card_id")) // 2
    // @Column(name = "vaccine_set") // 3
    // private Set<Card> vaccineSet;	
	
	@ManyToOne
	@JoinColumn(name="cardVaccine_id")
	private Card cardVaccine;

	@Size(min=0, max=2)
	@OneToMany(mappedBy="cardVaccine", cascade=CascadeType.ALL)
	private List<Card> vaccines;

	@ManyToOne
	@JoinColumn(name="cardVirus_id")
	private Card cardVirus;

	@Size(min=0, max=2)
	@OneToMany(mappedBy="cardVirus", cascade=CascadeType.ALL)
	private List<Card> virus = new ArrayList<>();

	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "type_id")
	private GenericCard type;

	@ManyToOne(optional = true)
	private GamePlayer gamePlayer;

	public Card(int id, boolean body, boolean played, List<Card> vaccines, List<Card> virus, GamePlayer game_player_id, GenericCard type) {
		this.id = id;
		this.body = body;
		this.played = played;
		this.vaccines = vaccines;
		this.virus = virus;
		this.gamePlayer = game_player_id;
		this.type = type;
	}

	public Card() {}

	public Card(Integer id, Boolean body, GamePlayer gp, GenericCard type){
		// Card organ = new Card();
		// organ.setId(id);
		// organ.setBody(body);
		// organ.setPlayed(false);
		// organ.setVaccines(new ArrayList<>());
		// organ.setVirus(new ArrayList<>());
		// organ.setGamePlayer(gp);
		// organ.setType(type);
		this.id = id;
		this.body = body;
		this.played = false;
		this.vaccines = new ArrayList<>();
		this.virus = new ArrayList<>();
		this.type = type;
	}
	public Card createVirus(Integer id, GamePlayer gp, GenericCard type, Card cardVirus){
		Card virus = new Card();
		virus.setId(id);
		virus.setBody(false);
		virus.setPlayed(false);
		virus.setCardVirus(cardVirus);
		virus.setGamePlayer(gp);
		virus.setType(type);
		return virus;
	}
	public Card createVaccine(Integer id, GamePlayer gp, GenericCard type, Card cardVaccine){
		Card vaccine = new Card();
		vaccine.setId(id);
		vaccine.setBody(false);
		vaccine.setPlayed(false);
		vaccine.setCardVaccine(cardVirus);
		vaccine.setGamePlayer(gp);
		vaccine.setType(type);
		return vaccine;
	}

	public void discard(){
		if(getType().toString()=="ORGAN"){
			setVaccines(new ArrayList<>());
			setVirus(new ArrayList<>());
		}
		else if(getType().toString()=="VIRUS") setCardVirus(null);
		
		else if(getType().toString()=="VACCINE") setCardVaccine(null);
		
		setPlayed(true);
		setBody(false);
		setGamePlayer(null);
		
	}

	public Boolean areCompatible(Card virus_or_vaccine){
		return (getType().getType().name()=="ORGAN") && 
		(virus_or_vaccine.getType().getColour().name()==getType().getColour().name() || getType().getColour().name()=="RAINBOW" ||
		virus_or_vaccine.getType().getColour().name()=="RAINBOW");

	}


}
