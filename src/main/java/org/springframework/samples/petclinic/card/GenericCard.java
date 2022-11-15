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

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 */
@Getter
@Setter
@Entity
@Table(name = "generic_cards")
public class GenericCard extends BaseEntity {
	
	public enum Colour {
			RED, BLUE, GREEN, YELLOW, RAINBOW;

			private static final Colour[] colours = Colour.values();
			public static Colour getColour(int i){
				return Colour.colours[i];
			}
	}

	public enum Type {
			ORGAN, VIRUS, VACCINE, TRANSPLANT, THIEF, INFECTION, GLOVES, ERROR;

		private static final Type[] types = Type.values();
		public static Type getType(int i){
			return Type.types[i];
		}
}
	@Enumerated(EnumType.STRING)
	private Colour colour;
	@Enumerated(EnumType.STRING)
	private Type type;

	public GenericCard(final Colour colour, final Type type) {
		this.colour = colour;
		this.type = type;
	}

	public GenericCard(){}

}
