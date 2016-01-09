package hotel.controllers;


import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hotel.models.Booking;
import hotel.models.Hotel;
import hotel.models.Room;
import hotel.models.User;
import hotel.repositories.BookingRepository;
import hotel.repositories.HotelRepository;
import hotel.repositories.RoomRepository;
import hotel.repositories.UserRepository;


/*
 * Mapping
 * GET  /	- show homepage
 * 
 */

@Controller
public class AngularJSController {
	
	@Autowired
    HotelRepository hotels;
	@Autowired
	RoomRepository rooms;
	@Autowired
	UserRepository users;
	@Autowired
	BookingRepository bookings;
	
	
	@RequestMapping(value="/angularjs")	
	public String angularjs(Model model) {
	    return "angular/angular";
	}
	
	
	//Login
	@RequestMapping(value="angularjs/login", method=RequestMethod.GET)
	public String angularLogin(Model model) {
	    return "angular/login";
	}
	
	//Login
	@RequestMapping(value="/angularjs/user", method=RequestMethod.GET)
	public @ResponseBody Principal user(Principal user){
		return user;
	}
	
	//Search
	@RequestMapping(value="angularjs/search", method=RequestMethod.GET)
	public String angularSearch(Model model) {
	   	
	    return "angular/search";
	}
	
	//Booking
		@RequestMapping(value="angularjs/booking", method=RequestMethod.GET)
		public String angularBooking(Model model, @RequestHeader HttpHeaders header) {
		    return "angular/booking";
		}
		
	//Booking
		@RequestMapping(value="angularjs/bookingSuccessful", method=RequestMethod.GET)
		public String angularBookingSuccess(Model model,Principal principal, @RequestHeader HttpHeaders header) {
				String hotelName = header.get("hotel").get(0).toString();
				String startDate = header.get("initialdate").get(0).toString();
				String endDate = header.get("finaldate").get(0).toString();
				long roomId = Long.parseLong(header.get("roomID").get(0));
				System.out.println("roomId =  " + roomId);
				System.out.println(principal.getName());
				User user = users.findByUsername(principal.getName());
				Hotel hotel = hotels.findByName(hotelName);
				Room r = rooms.findOne(roomId);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//				Date start = new Date(), end = new Date();
//				try {
//					start = sdf.parse(startDate);
//					System.out.println("start = "+start);
//					end = sdf.parse(endDate);
//					System.out.println("end = "+end);
//				} catch (ParseException e) {
//					System.out.println("erro na data");
//					return "error";
//				}
				
				
				Date start1 = new Date(2016,01,01);
				Date end1 = new Date(2016,01,02);
				Booking b = new Booking(hotel, r,user , start1, end1, false);
				bookings.save(b);
				System.out.println("count = " + rooms.count());
				System.out.println("count = " + bookings.count());
			    return "angular/bookingSuccessful";
		}
	
	//Find all Hotels
	@RequestMapping(value="angularjs/allHotels", method=RequestMethod.GET)
	public @ResponseBody List<Hotel> angulargetHotels(){
	return (List<Hotel>) hotels.findAll();
    }
	
	//Find all Rooms
		@RequestMapping(value="angularjs/allRooms", method=RequestMethod.GET)
		public @ResponseBody List<Room> angularGetRooms(@RequestHeader HttpHeaders headers){
			String hotel = headers.get("hotel").get(0).toString();
			String initial = headers.get("initial").get(0).toString();
			String end  = headers.get("end").get(0).toString();
			List<Room> rooms = (List<Room>) hotels.findByName(hotel).getRooms();
			return rooms;
	    }	
	
	
		
	//Search Available Rooms
	@RequestMapping(value="angularjs/hotels" , method=RequestMethod.GET) 
    public String searchRoomsAvailable(Model model, @RequestParam("initial") @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
    	List<Hotel> hotels_available = hotels.findBookingAvailable(begin, end);
    	model.addAttribute("hotel", hotels_available);
    	return "angular/search";
    }
	

    

}