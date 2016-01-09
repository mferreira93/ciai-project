package hotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hotel.exceptions.BookingNotFoundException;
import hotel.exceptions.HotelNotFoundException;
import hotel.exceptions.RoomNotFoundException;
import hotel.models.Booking;
import hotel.models.Comment;
import hotel.models.Hotel;
import hotel.models.HotelType;
import hotel.models.Manager;
import hotel.models.Room;
import hotel.models.RoomType;
import hotel.models.User;
import hotel.repositories.BookingRepository;
import hotel.repositories.CommentRepository;
import hotel.repositories.HotelRepository;
import hotel.repositories.RoomRepository;
import hotel.repositories.UserRepository;
import hotel.security.AllowedForCommentCreation;
import hotel.security.AllowedForDeleteHotel;
import hotel.security.AllowedForDeleteRoom;
import hotel.security.AllowedForEditHotel;
import hotel.security.AllowedForHotelCreation;
import hotel.security.AllowedForRoomCreation;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

/*
 * Mapping
 * GET  	/hotels 							- the list of hotels approved
 * GET  	/hotels.json 						- the list of hotels approved
 * GET  	/hotels/new							- the form to fill the data for a new hotel 
 * POST 	/hotels         					- creates a new hotel
 * GET  	/hotels/{id} 						- the hotel with identifier {id} and its rooms
 * GET  	/hotels/{id}.json 					- the hotel with identifier {id}
 * GET  	/hotels/{id}/edit 					- the form to edit the hotel with identifier {id}
 * POST 	/hotels/{id} 	 					- update the hotel with identifier {id}
 * DELETE 	/hotels/{id} 	 					- delete the hotel with identifier {id}
 * GET  	/hotels/{id}/room 					- add room to the hotel with identifier {id}
 * POST 	/hotels/{id}/room    				- creates a new room in the hotel with identifier {id}
 * GET  	/hotels/{id}/addRoom/{idRoom}/edit	- the form to edit the room with identifier {idRoom} in hotel with identifier {id}
 * POST 	/hotel/{id}/room/{idRoom}/edit	 	- update the room with identifier {idRoom} in hotel with identifier {id}
 * DELETE 	/hotels/{id}/room/{idRoom} 			- delete room with identifier {idRoom} in hotel with identifier {id}
 * GET 		/hotels/comments					- list comments approved by hotel with identifier {id} and hotel manager with identifier {idUser}
 */

@Controller
@RequestMapping(value="/hotels")
public class HotelController {

    @Autowired
    HotelRepository hotels;
    @Autowired
    RoomRepository rooms;
    @Autowired
    UserRepository managers;
    @Autowired
    CommentRepository comments;
    @Autowired
    BookingRepository bookings;
    
    // GET  /hotels 			- the list of hotels approved
    @RequestMapping(method=RequestMethod.GET)
    public String index(Model model, Principal principal) {
    	Manager manager = (Manager) managers.findByUsername(principal.getName());
        model.addAttribute("hotel", hotels.findAllHotels(manager.getIdUser()));
        return "hotels/index";
    }
     
	// GET  /hotels.json 			- the list of hotels approved
    @RequestMapping(method=RequestMethod.GET, produces={"text/plain","application/json"})
    public @ResponseBody Iterable<Hotel> indexJSON(Model model, Principal principal) {
    	Manager manager = (Manager) managers.findByUsername(principal.getName());
        return hotels.findAllHotels(manager.getIdUser());
    }

    // GET  /hotels/new			- the form to fill the data for a new hotel
    @RequestMapping(value="/new", method=RequestMethod.GET)
    @AllowedForHotelCreation
    public String newHotel(Model model){
    	model.addAttribute("hotel", new Hotel());
    	model.addAttribute("hotelTypes", HotelType.values());
    	return "hotels/create";
    }
    
    // POST /hotels         	- creates a new hotel
    @RequestMapping(method=RequestMethod.POST)
    @AllowedForHotelCreation
    public String saveIt(@Valid @ModelAttribute Hotel hotel, BindingResult bindingResult, Principal principal, Model model, Room room) {
        if (bindingResult.hasErrors()) {
            return "hotels/create";
        }
        Manager manager = (Manager) managers.findByUsername(principal.getName());
        hotel.setManager(manager);
        hotel.setApproved(false);
        hotel.setEdited(false);
        hotels.save(hotel);
        return "redirect:/hotels";
    }
    
    // GET  /hotels/{id} 		- the hotel with identifier {id}
    @RequestMapping(value="/{id}", method=RequestMethod.GET) 
    public String show(@PathVariable("id") long id, Model model) {
    	Hotel hotel = hotels.findOne(id);
    	if( hotel == null )
    		throw new HotelNotFoundException();
    	model.addAttribute("hotel", hotel);
    	model.addAttribute("rooms", hotel.getRooms());
    	model.addAttribute("comments", comments.findByHotel(id, true));
    	return "hotels/show";
    }
    
    // GET  /hotels/{id}.json 		- the hotel with identifier {id}
    @RequestMapping(value="/{id}", method=RequestMethod.GET, produces={"text/plain","application/json"})
    public @ResponseBody Hotel showJSON(@PathVariable("id") long id, Model model) {
    	Hotel hotel = hotels.findOne(id);
    	if( hotel == null )
    		throw new HotelNotFoundException();
    	return hotel;
    }
    
    // GET  /hotels/{id}/edit 	- the form to edit the hotel with identifier {id}
    @RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
    @AllowedForEditHotel
    public String edit(@PathVariable("id") long id, Model model) {
    	model.addAttribute("hotel", hotels.findOne(id));
    	model.addAttribute("hotelTypes", HotelType.values());
    	return "hotels/edit";
    }
   
    // POST /hotels/{id} 	 	- update the hotel with identifier {id}
    @RequestMapping(value="/{id}/edit", method=RequestMethod.POST)
    @AllowedForEditHotel
    public String editSave(@PathVariable("id") long id, Hotel hotel, Principal principal) {
    	Manager manager = (Manager) managers.findByUsername(principal.getName());
    	
    	hotel.setManager(manager);
    	hotel.setApproved(true);
    	hotel.setEdited(true);
    	hotel.setHotelEdited(hotel);
    	
    	Hotel original = hotels.findOne(id);
    	Hotel editedHotel = new Hotel();
    	
    	editedHotel.setName(hotel.getName());
    	editedHotel.setAddress(hotel.getAddress());
    	editedHotel.setHotelType(hotel.getHotelType());
    	editedHotel.setNumStars(hotel.getNumStars());
    	editedHotel.setApproved(false);
    	editedHotel.setManager(manager);    	
    	
		if(original.getEdited()){
			Hotel editedHotel2 = original.getHotelEdited();
			original.setEdited(true);
			hotels.delete(editedHotel2);
			editedHotel2 = editedHotel;
			editedHotel2.setEdited(true);
			hotels.save(editedHotel2);
			original.setHotelEdited(editedHotel2);
			hotels.save(original);
		} else {
			original.setEdited(true);
			editedHotel.setEdited(true);
        	hotels.save(editedHotel);
        	original.setHotelEdited(editedHotel);
        	hotels.save(original);
		}

    	return "redirect:/hotels";
    }
   
    // DELETE /hotels/{id} 	 	- delete the hotel with identifier {id}
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @AllowedForDeleteHotel
    public String delete(@PathVariable("id") long id, Model model) {
        hotels.delete(id);
        return "redirect:/hotels";
    }
    
    // GET  /hotels/{id}/addRoom 		- add room to the hotel with identifier {id}
    @RequestMapping(value="/{id}/room", method=RequestMethod.GET) 
    @AllowedForRoomCreation
    public String addRoom(@PathVariable("id") long id, Model model) {
    	model.addAttribute("hotel", hotels.findOne(id));
    	model.addAttribute("room", new Room());
    	model.addAttribute("roomTypes", RoomType.values());
    	return "hotels/createRoom";
    }
    
    // POST /hotels/{id}/addRoom         	- creates a new room in the hotel with identifier {id} 
    @RequestMapping(value="/{id}/room", method=RequestMethod.POST)
    @AllowedForRoomCreation
    public String saveRoom(@Valid @ModelAttribute Room room, @PathVariable("id") long id, Principal principal, Hotel hotel, @RequestParam("n_rooms") Integer number_of_rooms, 
            @RequestParam("roomType") RoomType roomtype , @RequestParam("price") Double price, BindingResult bindingResult) {     
    	if (bindingResult.hasErrors()) {
            return "hotels/createRoom";
        }
    	
    	hotel = hotels.findOne(id);

        for(int i = 0; i < number_of_rooms; i++){   
            room = new Room();
            room.setRoomType(roomtype);
            room.setPrice(price);
            room.setHotel(hotel);
            
            rooms.save(room);
            hotel.getRooms().add(room);
        }

        Manager manager = (Manager) managers.findByUsername(principal.getName());
        hotel.setManager(manager);
        hotel.setId(id);
        hotels.save(hotel);
    
        return "redirect:/hotels/{id}";
    }
    
 	// DELETE /hotels/{id}/room/{idRoom} 	- delete room with identifier {idRoom} in hotel with identifier {id}
    @RequestMapping(value = "/{id}/room/{idRoom}", method = RequestMethod.DELETE)
    @AllowedForDeleteRoom
    public String disapproveManager(@PathVariable("id") long id, @PathVariable("idRoom") long idRoom, Hotel hotel, Room room) {
    	hotel = hotels.findOne(id);
    	room = rooms.findOne(idRoom);
    	hotel.getRooms().remove(room);
    	hotels.save(hotel);
        return "hotels/show";
    }
    
    // GET /hotels/comments			- list comments approved by hotel with identifier {id} and hotel manager with identifier {idUser}
    @RequestMapping(value="/comments", method=RequestMethod.GET)
   	public String listComments(Model model, Principal principal) {
    	Manager manager = (Manager) managers.findByUsername(principal.getName());
   		model.addAttribute("comments",comments.findByManager(manager.getIdUser()));
   	    return "hotels/comments";
   	}
    
    // GET /hotels/comments/{id}/answer				- form to answer comment with identifier {id}
    @RequestMapping(value="/comments/{id}/answer", method=RequestMethod.GET)
    @AllowedForCommentCreation
    public String answerComment(Model model, Principal principal) { 
    	model.addAttribute("comment", new Comment());
    	return "hotels/commentAnswer";
    }
    
    // POST /hotels/comments/{id}
    @RequestMapping(value="/comments/{id}/answer", method=RequestMethod.POST)
    @AllowedForCommentCreation
    public String answerCommentSave(@Valid @ModelAttribute Comment answer, @PathVariable("id") long id, Principal principal, BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
            return "hotels/commentAnswer";
        }
    	
    	Manager manager = (Manager) managers.findByUsername(principal.getName()); 	
    	Comment comment = comments.findOne(id);
    	answer.setHotel(comment.getHotel());
    	answer.setUser(manager);
    	answer.setApproved(false);
    	comment.getAnswers().add(answer);
    	comments.save(answer);
    	return "redirect:/hotels/comments";
    }
    
    // GET /hotels/bookings					-list all bookings for the hotels of the manager
    @RequestMapping(value="/bookings", method=RequestMethod.GET)
    public String listBookings(Model model, Principal principal) {
    	Manager manager = (Manager) managers.findByUsername(principal.getName());
   		model.addAttribute("bookings",bookings.findByManagerBookings(manager.getIdUser(), true, false));
   	    return "hotels/listBookings";
    }
    
    // GET /hotels/bookings					-list all bookings approved for the hotels of the manager
    @RequestMapping(value="/bookings_wait_list", method=RequestMethod.GET)
    public String listNotApprovedBookings(Model model, Principal principal) {
    	Manager manager = (Manager) managers.findByUsername(principal.getName());
   		model.addAttribute("bookingsWaiting",bookings.findByManagerBookings(manager.getIdUser(), false, false));
   		model.addAttribute("bookingsEditWaiting",bookings.findByManagerBookings(manager.getIdUser(), false, true));
   	    return "hotels/waitlist";
    }
    
    // GET /hotels/bookings					-show bookings approved with identifier {id}
    @RequestMapping(value="/bookings_wait_list/{id}", method=RequestMethod.GET)
    public String showBooking(@PathVariable("id") long id, Model model) {
    	
    	Booking booking = bookings.findOne(id);
      	if( booking == null ) {
      		throw new BookingNotFoundException();
      	}
      	Booking original = bookings.findByBookingEditId(id);
      	model.addAttribute("booking", booking);
      	model.addAttribute("original", original);
   	    return "hotels/showBooking";
    }
    
    // DELETE /bookings/{id}	 	- disapprove the booking with identifier {id}
    @RequestMapping(value = "/bookings_wait_list/{id}", method = RequestMethod.DELETE)
    public String disapproveBooking(@PathVariable("id") long id) {
    	bookings.delete(id);
        return "users/waitlist";
    }
    
    // POST /bookings/{id}		- approve the booking with identifier {id}
    @RequestMapping(value = "/bookings_wait_list/{id}", method = RequestMethod.POST)
    public String approveBooking(@PathVariable("id") long id, Booking booking) {
    	booking = bookings.findOne(id);
    	booking.setApproved(true);
    	bookings.save(booking);
        return "redirect:/hotels/bookings/";
    }
    
    //POST /bookings/decline_update/{id}			- disapprove the booking edited with identifier {id}
    @RequestMapping(value = "/bookings_wait_list/decline_update/{id}", method = RequestMethod.POST)
    public String declineBookingEdition(@PathVariable("id") long id, Booking booking) {
    	//Save original version of the booking
    	booking = bookings.findByBookingEditId(id);
    	booking.setBookingEdited(null);
    	booking.setApproved(true);
    	booking.setEdited(false);
    	
    	//Delete edited version of the booking
    	bookings.delete(id);
    	bookings.save(booking);
        return "redirect:/hotels/bookings_wait_list/";
    }
    
    //POST /bookings_wait_list/approve_edit/{id}			- approve the booking edited with identifier {id}
    @RequestMapping(value = "/bookings_wait_list/approve_edit/{id}", method = RequestMethod.POST)
    public String approveBookingEdition(@PathVariable("id") long id, Booking booking) {
    	//Save the edited version of the booking
    	booking = bookings.findOne(id);
    	booking.setApproved(true);
    	booking.setEdited(false);
 
    	//Delete original version of the booking
    	Booking original = bookings.findByBookingEditId(id);
    	original.setBookingEdited(null);
    	User user = original.getUser();
    	List<Booking> allBookingsOfUser = user.getBookings();
    	for(int i = 0; i < allBookingsOfUser.size(); i++) {
    		Booking bk = new Booking();
    		bk.setApproved(allBookingsOfUser.get(i).getApproved());
    		bk.setStart(allBookingsOfUser.get(i).getStart());
    		bk.setEnd(allBookingsOfUser.get(i).getEnd());
    		bk.setHotel(allBookingsOfUser.get(i).getHotel());
    		bk.setEdited(allBookingsOfUser.get(i).getEdited());
    		bk.setBookingEdited(allBookingsOfUser.get(i).getBookingEdited());
    		bk.setRoom(allBookingsOfUser.get(i).getRoom());
    		bookings.save(bk);
    		managers.save(user);
    	}
    	
    	bookings.delete(original.getId());
    	bookings.save(booking);
        return "redirect:/hotels/bookings_wait_list/";
    }
    
    // DELETE /bookings/{id}	 	- delete the booking with identifier {id}
    @RequestMapping(value = "/bookings/{id}/delete", method = RequestMethod.DELETE)
    public String deleteBooking(@PathVariable("id") long id) {
    	bookings.delete(id);
        return "hotels/listBookings";
    }
    
    @RequestMapping(value="/search_available_hotels", method=RequestMethod.GET) 
    public String SearchHotels(Model model) {
    	return "hotels/searchBooking";
    }  
    
    @RequestMapping(value="/search_available_hotels/hotels_available", method=RequestMethod.GET) 
    public String showHotelsAvailable(Model model, @RequestParam("begin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Principal principal) {
    	List<Hotel> hotels_available = hotels.findBookingAvailable(begin, end);
    	List<Hotel> hotels_final = new ArrayList<Hotel>();
    	Manager manager = (Manager) managers.findByUsername(principal.getName());
    	for (int i=0; i < hotels_available.size(); i++){
    		if(hotels_available.get(i).getManager().equals(manager) && hotels_available.get(i).getApproved()){
    			hotels_final.add(hotels_available.get(i)); 
    		}
    	}
    	model.addAttribute("hotel", hotels_final);
    	return "hotels/hotelsAvailable";
    } 
    
    @RequestMapping(value="/hotelManager/{id}", method=RequestMethod.GET) 
    public String showHotelsBooking(@PathVariable("id") long id, Model model) {
    	Hotel hotel = hotels.findOne(id);
    	if( hotel == null )
    		throw new HotelNotFoundException();
    	model.addAttribute("hotel", hotel);
    	model.addAttribute("rooms", hotel.getRooms()); 
    	model.addAttribute("comment", comments.findByHotel(id, true));
    	return "hotels/showHotels";
    }
    
    @RequestMapping(value="/{id}/room/{idRoom}/booking", method=RequestMethod.GET)
   	public String CreateBooking(@PathVariable("id") long id, @PathVariable("idRoom") long idRoom, Model model, Principal principal) {
    	Manager manager = (Manager) managers.findByUsername(principal.getName());
        model.addAttribute("hotel", hotels.findAllHotels(manager.getIdUser()));
    	Room room = rooms.findOne(idRoom);
    	if( room == null )
    		throw new RoomNotFoundException();
      	model.addAttribute("room", room);
      	model.addAttribute("roomTypes", RoomType.values()); 
    	model.addAttribute("booking", new Booking());

    	return "hotels/formBooking";
   	}
    
   	// POST /hotel/{id}/room/{idRoom}/edit	 		- update the room with identifier {idRoom} in hotel with identifier {id}
    @RequestMapping(value="/{id}/room/{idRoom}/booking", method=RequestMethod.POST)
    public String CreateBookingPost(@PathVariable("id") long id, @PathVariable("idRoom") long idRoom, Principal principal, Booking booking) {
    	Hotel hotel = hotels.findOne(id);
    	Room room = rooms.findOne(idRoom);
    	User manager = managers.findByUsername(principal.getName());

		booking.setHotel(hotel);
		booking.setRoom(room);
		booking.setUser(manager);
		booking.setApproved(true);    	
		hotel.setEdited(false);
		rooms.save(room);
		bookings.save(booking);
    
    	return "redirect:/hotels/bookings";
    }
}