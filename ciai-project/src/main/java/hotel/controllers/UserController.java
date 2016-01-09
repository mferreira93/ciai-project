package hotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hotel.models.Comment;
import hotel.models.Hotel;
import hotel.models.Manager;
import hotel.models.Roles;
import hotel.models.Room;
import hotel.repositories.CommentRepository;
import hotel.repositories.HotelRepository;
import hotel.repositories.RoomRepository;
import hotel.repositories.UserRepository;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/*
 * Mapping
 * GET  	/users							- dashboard
 * DELETE 	/users/{idUser}	 				- disapprove the hotel manager with identifier {idUser}
 * POST 	/users/{idUser}					- approve the hotel manager with identifier {idUser}
 * DELETE 	/users/{id}	 					- disapprove the hotel with identifier {id}
 * POST 	/users/{id}						- approve the hotel with identifier {id}
 * DELETE 	/users/{id}	 					- disapprove the edit made to the hotel with identifier {id}
 * POST 	/users/{id}						- approve the edit made to the hotel with identifier {id}
 * GET  	/users/guests					- list all guests
 * DELETE 	/users/guests/{idUser}	 		- delete the guest with identifier {idUser}
 */

@Controller
@RequestMapping(value="/users")
public class UserController {
	
	@Autowired
    UserRepository users;
	@Autowired
	HotelRepository hotels;
	@Autowired
	RoomRepository rooms;
	@Autowired
	CommentRepository comments;
	
	/* DASHBOARD */
	
	// GET  /users 			- dashboard
 	@RequestMapping(method=RequestMethod.GET)
 	public String dashboard(Model model, Principal principal) {
 		model.addAttribute("user", users.findByStatus(false));
 		model.addAttribute("hotels", hotels.findApproved(false));
 		model.addAttribute("edited", hotels.findEdited(true));
 	    return "users/dashboard";
 	}
 	
 	// DELETE /users/{idUser}	 	- disapprove the hotel manager with identifier {idUser}
    @RequestMapping(value = "/{idUser}", method = RequestMethod.DELETE)
    public String disapproveManager(@PathVariable("idUser") long idUser) {
    	users.delete(idUser);
        return "users/dashboard";
    }
    
    // POST /users/{idUser}			- approve the hotel manager with identifier {idUser}
    @RequestMapping(value = "/{idUser}", method = RequestMethod.POST)
    public String approveManager(@PathVariable("idUser") long idUser, Manager manager) {
    	manager = (Manager) users.findOne(idUser);
    	manager.setApproved(true);
    	users.save(manager);
        return "redirect:/users/";
    }
    
    // DELETE /users/{id}	 	- disapprove the hotel with identifier {id}
    @RequestMapping(value = "/hotels/{id}", method = RequestMethod.DELETE)
    public String disapproveHotel(@PathVariable("id") long id) {
    	hotels.delete(id);
        return "users/dashboard";
    }
    
    //POST /users/{id}			- approve the hotel with identifier {id}
    @RequestMapping(value = "/hotels/{id}", method = RequestMethod.POST)
    public String approveHotel(@PathVariable("id") long id, Hotel hotel) {
    	hotel = hotels.findOne(id);
    	hotel.setApproved(true);
    	hotels.save(hotel);
        return "users/dashboard";
    }
    
    //POST /users/edit_hotel/{id}			- approve the hotel edited with identifier {id}
    @RequestMapping(value = "/approve_edit/{id}", method = RequestMethod.POST)
    public String approveEdit(@PathVariable("id") long id, Hotel hotel) {
    	//Save the edited version of the hotel
    	hotel = hotels.findOne(id);
    	hotel.setApproved(true);
    	hotel.setEdited(false);
 
    	//Delete original version of the hotel
    	Hotel original = hotels.findByHotelEditId(id);
    	original.setHotelEdited(null);
    	List<Room> allRooms = rooms.findByHotelId(original.getId());
    	List<Comment> allComments = comments.findAllCommentsByHotel(original.getId());
    	for(int i = 0; i < allRooms.size(); i++) {
    		Room room = new Room();
    		room.setRoomType(allRooms.get(i).getRoomType());
    		room.setPrice(allRooms.get(i).getPrice());
    		room.setHotel(hotel);
    		rooms.save(room);
    	}
    	for(int i = 0; i < allComments.size(); i++) {
    		Comment comment = new Comment();
    		comment.setApproved(allComments.get(i).getApproved());
    		comment.setDescription(allComments.get(i).getDescription());
    		comment.setUser(allComments.get(i).getUser());
    		comment.setHotel(hotel);
    		comments.save(comment);
    	}
    	
    	hotels.delete(original.getId());
    	hotels.save(hotel);
        return "redirect:/users/";
    }
    
    //POST /users/edit_hotel/{id}			- approve the hotel edited with identifier {id}
    @RequestMapping(value = "/decline_update/{id}", method = RequestMethod.POST)
    public String declineEdit(@PathVariable("id") long id, Hotel hotel) {
    	//Save original version of the hotel
    	hotel = hotels.findByHotelEditId(id);
    	hotel.setHotelEdited(null);
    	hotel.setApproved(true);
    	hotel.setEdited(false);
    	
    	//Delete edited version of the hotel
    	hotels.delete(id);
    	hotels.save(hotel);
        return "redirect:/users/";
    }
    
    /* GUESTS */
    
    // GET  /users/guests			- list all guests
   	@RequestMapping(value="/guests", method=RequestMethod.GET)
   	public String listGuests(Model model) {
   		model.addAttribute("user", users.findByRoles(Roles.ROLE_GUEST.toString()));
   		return "users/guest";
   	}
   	
   	// DELETE /users/guests/{idUser}	 	- delete the guest with identifier {idUser}
    @RequestMapping(value = "/guests/{idUser}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("idUser") long idUser) {
    	users.delete(idUser);
        return "users/guest";
    }
}