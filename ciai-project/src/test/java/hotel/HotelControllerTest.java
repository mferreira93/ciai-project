package hotel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import hotel.models.Hotel;
import hotel.repositories.HotelRepository;
import hotel.repositories.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(hotel.Application.class)
@WebAppConfiguration
public class HotelControllerTest {
	
	@Autowired
	private WebApplicationContext wac;

    @Autowired
    private UserRepository users;
    @Autowired
	private Filter springSecurityFilterChain;
    private MockMvc mvc;
	@Autowired
	HotelRepository hotels;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(this.springSecurityFilterChain).build();
	}
	
	
	@Test
	public void testHotelIndex() throws Exception {
		mvc.perform(get("/hotels")).andExpect(status().isOk())
				.andExpect(view().name("hotels/index"));
	}
	
	@Test
	public void testLogout() throws Exception {	
		mvc.perform(post("/hotels/logout"))
				.andExpect(view().name("homepage/homepage"));
	}


//	@Test
//	public void testAddHotel() throws Exception {
//		String hotelName = "Salgados";
//		String username = "rui";
//		String password = "123";
//		mvc.perform(post("/login")
//				.param("username", username)
//				.param("password", password)
//				.
//				
//				.param("id", Integer.toString(0))
//                .param("name", hotelName))
//				.andExpect(status().is3xxRedirection())
//				.andExpect(redirectedUrl("/list_hotels"));
//				
//		Hotel hotel = hotels.findByName(hotelName);
//		
//		Assert.assertTrue(hotel != null);
//	}
	
	@Test
	public void testAddHotelWithoutPermission() throws Exception {
		String hotelName = "Salgados"; 
		mvc.perform(post("/hotels/new")
				.param("id", Integer.toString(0))
                .param("name", hotelName))
				.andExpect(redirectedUrl(null));
				
				
		Hotel hotel = hotels.findByName(hotelName);
		
		Assert.assertTrue(hotel == null);
	}
	
	@Test
	public void testGetOne() throws Exception {
		String hotelName = "Marriot"; 

		Hotel hotel = hotels.findByName(hotelName);
		
		mvc.perform(get("/hotels/"+hotel.getId()))
				.andExpect(view().name("hotels/show"));
	}
	
	
}
