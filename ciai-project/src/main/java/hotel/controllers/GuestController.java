package hotel.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hotel.exceptions.HotelNotFoundException;
import hotel.exceptions.RoomNotFoundException;
import hotel.models.Booking;
import hotel.models.Comment;
import hotel.models.Hotel;
import hotel.models.Room;
import hotel.models.RoomType;
import hotel.models.User;
import hotel.repositories.BookingRepository;
import hotel.repositories.CommentRepository;
import hotel.repositories.UserRepository;
import hotel.security.AllowedForCommentCreation;
import hotel.repositories.HotelRepository;
import hotel.repositories.RoomRepository;

/*
 * Mapping
 * GET /guests				- list all bookings(edited, approved or waiting for approval) from guest with identifier {id}
 */

@Controller
@RequestMapping(value="/guests")
public class GuestController {
	
	@Autowired
    UserRepository guests;
	@Autowired
    HotelRepository hotels;
	@Autowired
    RoomRepository rooms;
	@Autowired
    BookingRepository bookings;
	@Autowired
    CommentRepository comments;
 	
	
	//GET /guests				- list all bookings(edited, approved or waiting for approval) from guest with identifier {id}
	@RequestMapping(method=RequestMethod.GET)
    public String ShowUserBookings(Model model, Principal principal) {
    	User guest = guests.findByUsername(principal.getName());    	
    	model.addAttribute("bookings", bookings.findByUserBooking(guest.getIdUser(), true, false));
    	model.addAttribute("waintingApproval", bookings.findByUserBooking(guest.getIdUser(), false, false));
    	model.addAttribute("waintingEditApproval", bookings.findByUserBooking(guest.getIdUser(), false, true));
    	return "guests/guestBookings";
    }	
	

    // DELETE /bookings/{id}	 	- delete the booking with identifier {id}
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String cancelBooking(@PathVariable("id") long id) {
    	bookings.delete(id);
        return "guests/guestBookings";
    }

	// GET  /users/moderators 			- list all comment moderators
   	@RequestMapping(value="/hotels", method=RequestMethod.GET)
   	public String GetHotels(Model model, Principal principal) {
   	 model.addAttribute("hotel", hotels.findApproved(true));
     return "guests/index";
   	}
   	
    @RequestMapping(value="/hotels/{id}", method=RequestMethod.GET) 
    public String show(@PathVariable("id") long id, Model model) {
    	Hotel hotel = hotels.findOne(id);
    	if( hotel == null )
    		throw new HotelNotFoundException();
    	model.addAttribute("hotel", hotel);
    	model.addAttribute("rooms", hotel.getRooms()); 
    	model.addAttribute("comment", comments.findByHotel(id, true));
    	return "guests/show";
    }
   	
	// GET  /users/moderators 			- list all comment moderators
   	@RequestMapping(value="/hotels/{id}/room/{idRoom}/booking", method=RequestMethod.GET)
   	public String CreateBooking(@PathVariable("id") long id, @PathVariable("idRoom") long idRoom, Model model, Principal principal) {
   		model.addAttribute("hotel", hotels.findOne(id));
    	Room room = rooms.findOne(idRoom);
    	if( room == null )
    		throw new RoomNotFoundException();
      	model.addAttribute("room", room);
      	model.addAttribute("roomTypes", RoomType.values()); 
    	model.addAttribute("booking", new Booking());

    	return "guests/formBooking";
   	}
   	
   	// POST /hotel/{id}/room/{idRoom}/edit	 		- update the room with identifier {idRoom} in hotel with identifier {id}
    @RequestMapping(value="/hotels/{id}/room/{idRoom}/booking", method=RequestMethod.POST)
    public String CreateBookingPost(@PathVariable("id") long id, @PathVariable("idRoom") long idRoom, Principal principal, Booking booking) {
    	Hotel hotel = hotels.findOne(id);
    	Room room = rooms.findOne(idRoom);
    	User guest = guests.findByUsername(principal.getName());

		booking.setHotel(hotel);
		booking.setRoom(room);
		booking.setUser(guest);
		booking.setApproved(false);    	
		hotel.setEdited(false);
		rooms.save(room);
		bookings.save(booking);
    
    	return "redirect:/guests";
    }
    
	// GET  /users/moderators 			- list all comment moderators
   	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
   	public String EditBooking(@PathVariable("id") long id, Model model) {
    	model.addAttribute("booking", bookings.findOne(id));

    	return "guests/editBooking";
   	}
    
	// POST /hotel/{id}/room/{idRoom}/edit	 		- update the room with identifier {idRoom} in hotel with identifier {id}
    @RequestMapping(value="/{id}/edit", method=RequestMethod.POST)
    public String EditBookingPost(@PathVariable("id") long id, Principal principal, Booking booking) {
    	User guest = guests.findByUsername(principal.getName());
    	Booking the_booking = bookings.findOne(id);
    	Hotel hotel = the_booking.getHotel();
    	Room room = the_booking.getRoom();
    	
		booking.setHotel(hotel);
    	booking.setRoom(booking.getRoom());
    	booking.setUser(guest);
    	booking.setApproved(true);
    	booking.setEdited(true);
    	booking.setBookingEdited(booking);
    	rooms.save(room);
    	
    	Booking original = bookings.findOne(id);
    	Booking editedBooking = new Booking();
    	
    	editedBooking.setApproved(false);
    	editedBooking.setHotel(hotel);
    	editedBooking.setRoom(room);
    	editedBooking.setUser(guest);
    	editedBooking.setStart(booking.getStart());
    	editedBooking.setEnd(booking.getEnd());
    	rooms.save(room);
    	
		if(original.getEdited()){
			Booking editedBooking2 = original.getBookingEdited();
			original.setEdited(true);
			bookings.delete(editedBooking2);
			editedBooking2 = editedBooking;
			editedBooking2.setEdited(true);
			rooms.save(room);
			bookings.save(editedBooking2);
			original.setBookingEdited(editedBooking2);
			rooms.save(room);
			bookings.save(original);
		} else {
			original.setEdited(true);
			editedBooking.setEdited(true);
			rooms.save(room);
        	bookings.save(editedBooking);
        	original.setBookingEdited(editedBooking);
        	rooms.save(room);
        	bookings.save(original);
		}
    
    	return "redirect:/guests";
    }
   	
    // GET /hotels/comments/{id}/answer
    @RequestMapping(value="/hotels/{id}/comment", method=RequestMethod.GET)
    @AllowedForCommentCreation
    public String comment(Model model) { 
    	model.addAttribute("comment", new Comment());
    	return "guests/comment";
    }
    
    // POST /hotels/comments/{id}
    @RequestMapping(value="/hotels/{id}/comment", method=RequestMethod.POST)
    @AllowedForCommentCreation
    public String answerCommentSave(@Valid @ModelAttribute Comment comment, @PathVariable("id") long id, BindingResult bindingResult, Principal principal) {
    	if (bindingResult.hasErrors()) {
            return "guests/comment";
        }
    	User user = guests.findByUsername(principal.getName()); 
      	comment.setHotel(hotels.findOne(id));
      	comment.setUser(user);
      	comment.setApproved(false);
      	comments.save(comment);
    	return "redirect:/guests/hotels/{id}";
    }	
    
    @RequestMapping(value="/search_available_hotels", method=RequestMethod.GET) 
    public String SearchHotels(Model model) {
    	return "guests/searchBooking";
    }  
    
    @RequestMapping(value="/search_available_hotels/hotels_available", method=RequestMethod.GET) 
    public String showHotelsAvailable(Model model, @RequestParam("begin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
    	List<Hotel> hotels_available = hotels.findBookingAvailable(begin, end);
    	model.addAttribute("hotel", hotels_available);
    	return "guests/hotelsAvailable";
    }    
}
