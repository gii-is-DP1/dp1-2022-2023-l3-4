package org.springframework.samples.petclinic.room;

import java.util.Collection;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.room.exceptions.DuplicatedNameRoomException;
import org.springframework.samples.petclinic.room.exceptions.PlayerHostsExistingRoomException;
import org.springframework.stereotype.Controller;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/room")
public class RoomController {

    private static final String VIEWS_ROOM_CREATE_OR_UPDATE_FORM = "rooms/createOrUpdateRoomForm";
	private static final String VIEWS_ROOM_SEARCH = "rooms/createOrSearch";
	private static final String VIEWS_WAITING_ROOM = "rooms/waitingRoom";

    
	private final RoomService roomService;



	@Autowired
    private AuthenticationService authService;

	@Autowired
    private PlayerService playerService;

	@Autowired
    private GameService gameService;

    @Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;        
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Player player, ModelMap model) {
		Room room = new Room();
		
		model.put("room", room);
		model.put("player", player);
		return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Room room, BindingResult result, ModelMap model) {	
		Player player = authService.getPlayer();
		if (result.hasErrors()) {
			model.put("room", room);
			return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
		}
		else {
				try{
					if(room.getPassword()==null){
						room.setPassword("");
					}
					room.setTotalGamesPlayer(0);
					room.setHost(player);
					this.roomService.saveRoom(room);

				}catch(DuplicatedNameRoomException | DataAccessException | PlayerHostsExistingRoomException ex){
					if(ex.getClass().equals(DuplicatedNameRoomException.class)){
						result.rejectValue("roomName", "duplicate", "already exists");
					} else {
						model.put("message", "You are already host of another room.");
						model.put("messageType", "warning");
					}
					return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
				}
				player.setRoom(room);
				this.playerService.savePlayer(player);
				return "redirect:/room/" + room.getId();
		}
	}
	@GetMapping(value = "/edit")
	public String initEditForm( ModelMap model) {
		Player player = authService.getPlayer();
		Room room =roomService.findRoomByHost(player);
		model.put("room", room);
		model.put("player", player);
		return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/edit")
	public String processEditForm(@Valid Room room, BindingResult result, ModelMap model) throws PlayerHostsExistingRoomException {	
		Player player = authService.getPlayer();
		Room roomOld = roomService.findRoomByHost(player);
		if (result.hasErrors()) {
			model.put("room", room);
			return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
		}
		else {
				try{
					if(room.getPassword()==null){
						room.setPassword("");
					}
					room.setId(roomOld.getId());
					room.setPlayers(roomOld.getPlayers());
					room.setTotalGamesPlayer(roomOld.getTotalGamesPlayer());
					room.setHost(roomOld.getHost());
					roomService.updateRoom(room);

				}catch(DuplicatedNameRoomException ex){
					
						result.rejectValue("roomName", "duplicate", "already exists");
					
					return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
				}
				return "redirect:/room/" + roomOld.getId();
		}
	}

	@GetMapping("/createSearch")
	public String createSearch(ModelMap model) {
		Player player = authService.getPlayer();
		model.put("player", player);
		model.put("room", new Room());
		return VIEWS_ROOM_SEARCH;
	}

	@GetMapping(value = "/find")
	public String processFindRoomForm(Room room, BindingResult result, ModelMap model) {
		// allow parameterless GET request for /find to return all records
		if (room.getRoomName() == null) {
			room.setRoomName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		Collection<Room> results = roomService.findRoomsByRoomName(room.getRoomName());
		if (results.isEmpty()) {
			// no rooms found
			result.rejectValue("roomName", "notFound", "not found");
			return "rooms/createOrSearch";
		}

		else {
			// multiple roomss found
			model.put("rooms", results);
			return "rooms/roomsList";
		}
	}

@GetMapping("/{roomId}")
	public String showRoom(@PathVariable("roomId") int roomId,ModelMap model) {
		Player player = authService.getPlayer();
		Room roomPlayer=player.getRoom();
		Room room=this.roomService.findRoomById(roomId);
		//Para cuando se borra una room que te redirect a room Search
		if(room==null){
			model.put("message", "the room you were in was deleted");
			model.put("messageType", "warning");
			return createSearch(model);
		}

		Game game = gameService.getRunningGame(room);
		if(game!=null){
			return "redirect:/games/"+game.getId();
		}
		//Si no eres host de una room o ya perteneces a esa sala
		if(roomService.findRoomByHost(player)==null||room.getId()==player.getRoom().getId()){
			if (room.getPlayers().size()>= room.getNumMaxPlayers()&&(player.getRoom()==null||!(room.getId()==roomPlayer.getId()))) {
				model.put("player", player);
				model.put("room", new Room());
				model.put("message", "The room is full of players");
				model.put("messageType", "warning");
				return VIEWS_ROOM_SEARCH;
			} else {
				player.setRoom(room);
				this.playerService.savePlayer(player);
				Collection<Player> players=room.getPlayers();
				if(!(players.contains(player))){
					players.add(player);
				}
				model.put("room", room);
				model.put("players",players);
				model.put("countPlayer",players.size());
				model.put("host", player.equals(room.getHost()));
				return VIEWS_WAITING_ROOM;
				} 
		} else {
			model.put("player", player);
			model.put("room", new Room());
			model.put("message", "First delete your room.");
			model.put("messageType", "warning");
			return VIEWS_ROOM_SEARCH;
			}

	}
	

	
	@GetMapping("/delete/{roomId}")
	public String deleteRoom(@PathVariable("roomId") int roomId, ModelMap model){
		Player player = authService.getPlayer();
		Room room =this.roomService.findRoomById(roomId);
		if(room.getHost().equals(player)){
			Collection<Player> players=room.getPlayers();
			players.forEach(p->{
				p.setRoom(null);
				playerService.savePlayer(p);
			});

			Collection<Game> gamesByRoom=gameService.findGameByRoomId(room.getId());
			gamesByRoom.forEach(g->{
				g.setRoom(null);
				gameService.save(g);
			});
			roomService.deleteRoom(room.getId());

			return "redirect:/";
		} else {
			return "redirect:/";
		}


	}

	//redirect hacia my room
	@GetMapping("/myRoom")
        public String showMyRoom() {
			Player player = authService.getPlayer();
			Room room=roomService.findRoomByHost(player);
			if(room==null){
				return "redirect:/room/new";
			}else{
				return "redirect:/room/"+room.getId();
			}
      	  
  }

  //permita salir de una room
  @GetMapping("/exit/{roomId}")
  	public String exitRoom(@PathVariable("roomId") int roomId){
		Player player = authService.getPlayer();
		player.setRoom(null);
		playerService.savePlayer(player);
		return "redirect:/";
	}

}