package hotel.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class User{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long idUser;
    private String username;
    private String email;
    private String name;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Roles role;
    
    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="user", orphanRemoval=true)
    private List<Comment> comments = new ArrayList<Comment>();
    
    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="user", orphanRemoval=true)
    private List<Booking> bookings = new ArrayList<Booking>();
    
    public User() {}
    
    public User(String username, String email, String name, String password, Roles role){
		this.username = username;
		this.email = email;		
		this.name = name;
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		this.password = encoder.encode(password);
		this.role = role;
    } 
		 
    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setEmail(String email){
		this.email = email;
	}
	
	public String getEmail(){
		return email;
	}

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        
    }
    
    public void setPassword(String password){
    	PasswordEncoder encoder = new BCryptPasswordEncoder();
		this.password = encoder.encode(password);
	}
	
	public String getPassword(){
		return password;
	}
    
	 public void setRole(Roles role){
		this.role = role;
	}
	
	public Roles getRole(){
		return role;
	}

	@Override
    public String toString() {
    	return "Id: " + getIdUser() + "Username: " + getUsername() + "\nEmail: " + getEmail() + "Name: " + getName() + "\nPassword: " + getPassword();
	}

	public List<Comment> getComments() {
		return comments;
	}
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
}