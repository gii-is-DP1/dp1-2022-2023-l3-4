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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.model.BaseEntity;

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
	private Set<Card> vaccines;

	@ManyToOne
	@JoinColumn(name="cardVirus_id")
	private Card cardVirus;

	@Size(min=0, max=2)
	@OneToMany(mappedBy="cardVirus", cascade=CascadeType.ALL)
	private List<Card> virus = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "type_id")
	private GenericCard type;

	@ManyToOne(optional = true)
	@JoinColumn(name = "gamePlayer_id")
	private GamePlayer gamePlayer;

}
