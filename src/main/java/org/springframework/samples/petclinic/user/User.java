package org.springframework.samples.petclinic.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
	@Id
	String username;

	@NotEmpty
	String password;
	
	boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private Set<Authorities> authorities;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private Player player;

}
