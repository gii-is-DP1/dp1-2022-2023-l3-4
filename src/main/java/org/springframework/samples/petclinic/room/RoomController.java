package org.springframework.samples.petclinic.room;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerService;
import org.springframework.samples.petclinic.room.exceptions.DuplicatedNameRoomException;
import org.springframework.stereotype.Controller;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


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
    private OwnerService ownerService;

    @Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;        
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Owner owner, ModelMap model) {
		Room room = new Room();
		
		model.put("room", room);
		model.put("owner", owner);
		return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Room room, BindingResult result, ModelMap model) {	
		Owner owner = authService.getOwner();
		if (result.hasErrors()) {
			model.put("room", room);
			return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
		}
		else {
                    try{
						if(room.isPrivate==null){
							room.setIsPrivate(false);
						}
						room.setTotalGamesPlayer(0);
						this.roomService.saveRoom(room);
                    	owner.setRoom(room);
						ownerService.saveOwner(owner);
                    }catch(DuplicatedNameRoomException ex){
                        result.rejectValue("roomName", "duplicate", "already exists");
                        return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
                    }
                    return "redirect:/owners/1";
		}
	}

	@GetMapping("/room/{roomId}")
	public String showOwner(@PathVariable("roomId") int roomId) {

		return "redirect:/owners/1";
	}

}
