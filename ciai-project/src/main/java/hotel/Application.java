package hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import hotel.models.*;
import hotel.repositories.*;


@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	/**
	 * The main() method uses Spring Boot’s SpringApplication.run() method to launch an application.
	 * The run() method returns an ApplicationContext where all the beans that were created 
	 * either by your app or automatically added thanks to Spring Boot are.
	 * @param args
	 */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Autowired
    UserRepository users;
    @Autowired
    HotelRepository hotels;
    @Autowired
    CommentRepository comments;
    @Autowired
    BookingRepository bookings;
    
    @Override
    @Transactional
    public void run(String... strings) {
    	    
    	log.info("Setting up seed data");
    	 	
    	users.deleteAll();
    	
    	User myUsers[] = {	new User("miguel","miguel@mail.com","Miguel","123", Roles.ROLE_ADMIN ),
							new User("kduarte","kduarte@mail.com","kduarte","123", Roles.ROLE_MODERATOR),
							new User("jtomas","jtomas@mail.com","jtomas","123", Roles.ROLE_GUEST)};
		for(User user : myUsers) users.save(user);
		
		Manager myManagers[] = { new Manager("tomas","tomas@mail.com","Tomas","123", true),
								 new Manager("rui","rui@mail.com","rui","123", true),
								 new Manager("sara","sara@mail.com","sara","123", false)};
		for(Manager manager : myManagers) users.save(manager);
    	
    	hotels.deleteAll();
    	Hotel myHotels[] = {new Hotel("Marriot","Lisboa",HotelType.Hotel,5,true,myManagers[0]), 
    						new Hotel("Intercontinental","Lisboa",HotelType.Hostel,5,false,myManagers[1]), 
    						new Hotel("Trip","Lisboa",HotelType.Hotel,5,true,myManagers[0]), 
    						new Hotel("Holiday Inn","Lisboa",HotelType.Hostel,5,true,myManagers[1]), 
    						new Hotel("Tulip","Lisboa",HotelType.Hotel,5,false,myManagers[0]), 
    						new Hotel("Hostel da Costa","Lisboa",HotelType.Hostel,5,true,myManagers[1])};
    	for(Hotel hotel : myHotels) hotels.save(hotel);
    	
		@SuppressWarnings("unused")
		Room[] roomArray = {new Room(RoomType.Single, myHotels[0], 50.00), 
				new Room(RoomType.Double, myHotels[0],40.00)};	
    	hotels.save(myHotels[0]);
    	
    	comments.deleteAll();
    	Comment myComments[] = { new Comment("This is amazing!", myUsers[2], myHotels[0], false),
    							 new Comment("Blablabla", myUsers[2], myHotels[3], true),
    							 new Comment("sup", myUsers[2], myHotels[0], true),
    							 new Comment("sup!!", myUsers[2], myHotels[2], true)};
    	for(Comment comment : myComments) comments.save(comment);
    }
}