package org.springframework.samples.petclinic.user;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Jose Maria Garcia Berdejo
 */
@Service
public class UserService {

	private UserRepository userRepository;
	private PlayerService playerService;

	@Autowired
	public UserService(UserRepository userRepository, PlayerService playerService) {
		this.userRepository = userRepository;
		this.playerService = playerService;
	}

	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional(rollbackFor = DuplicatedUserException.class)
	public void saveUser(User user) throws DuplicatedUserException {
		List<String> listAllUsername = findAll().stream().map(x -> x.getUsername()).collect(Collectors.toList());
		if (listAllUsername.contains(user.getUsername())) {
			throw new DuplicatedUserException();
		} else {
			Authorities auth = new Authorities();
			auth.setAuthority("player");
			user.setAuthorities(Set.of(auth));
			user.setEnabled(true);
			userRepository.save(user);
		}
	}

	@Transactional
	public void updateUser(User user) {
		userRepository.save(user);
	}
	
	@Transactional(readOnly = true	)
	public User findUser(String username) {
		return userRepository.findById(username).get();
	}

	@Transactional
	public void deleteUser(String username) {
		User user = findUser(username);
		Player player = playerService.getPlayerByUsername(username);
		if (player != null) {
			player.setUser(null);
			playerService.deletePlayer(player);
		}
		userRepository.delete(user);
	}
}
