package hotel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import hotel.models.User;
import hotel.repositories.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(hotel.Application.class)
@WebAppConfiguration
public class HomepageControllerTest {
	
	@Autowired
	private WebApplicationContext wac;

    @Autowired
    private UserRepository users;
    @Autowired
	private Filter springSecurityFilterChain;
    
    private MockMvc mvc;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(this.springSecurityFilterChain).build();
	}
	
	@Test
	public void testHotelIndex() throws Exception {
		mvc.perform(get("/list_hotels")).andExpect(status().isOk())
				.andExpect(view().name("homepage/index"));
	}
	
	@Test
	public void testHostelIndex() throws Exception {
		mvc.perform(get("/list_hostels")).andExpect(status().isOk())
				.andExpect(view().name("homepage/index"));
	}
	
	@Test
	public void testGetLogin() throws Exception {
		mvc.perform(get("/login")).andExpect(status().isOk())
				.andExpect(view().name("homepage/login"));
	}
	
	@Test
	public void testGetSignGuest() throws Exception {
		mvc.perform(get("/sign_up_guest")).andExpect(status().isOk())
				.andExpect(view().name("homepage/create_guest"));
	}
	
	@Test
	public void testGetSignManager() throws Exception {
		mvc.perform(get("/sign_up_manager")).andExpect(status().isOk())
				.andExpect(view().name("homepage/create_manager"));
	}
	
	@Test
	public void testLoginCheck() throws Exception {
		String username = "rui"; 
		String password = "123";
		
		RequestBuilder requestBuilder = post("/login")
	            .param("username", username)
	            .param("inputPassword3", password);
	    mvc.perform(requestBuilder)
	            .andExpect(status().isOk())
	            .andExpect(redirectedUrl("/hotels"));
	
		User user = users.findByUsername(username);
		
		Assert.assertTrue(user != null);
	}	
}
