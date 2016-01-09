package hotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import hotel.repositories.HotelRepository;

/*
 * Mapping
 * GET  /						- show homepage
 * GET  /list_hotels			- the list of hotels
 * GET  /list_hostels			- the list of hostels
 */

@Controller
public class ApplicationController {
	
	@Autowired
    HotelRepository hotels;
	
	// GET  /				- show homepage
	@RequestMapping(value="/")	
	public String root(Model model) {
	    return "homepage/homepage";
	}	
	
	// GET  /list_hotels			- the list of hotels
    @RequestMapping(value="/list_hotels", method=RequestMethod.GET)
    public String listHotels(Model model) {
        model.addAttribute("hotel", hotels.findByType("Hotel"));
        return "homepage/index";
    }
    
    // GET  /list_hostels			- the list of hostels
    @RequestMapping(value="/list_hostels", method=RequestMethod.GET)
    public String listHostels(Model model) {
        model.addAttribute("hotel", hotels.findByType("Hostel"));
        return "homepage/index";
    }      
}