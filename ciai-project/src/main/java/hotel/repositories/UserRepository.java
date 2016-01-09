package hotel.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hotel.models.Manager;
import hotel.models.User;

public interface UserRepository extends CrudRepository<User, Long>{

	@Query(value="SELECT * FROM USER u WHERE USERNAME=?",nativeQuery=true)
    public User findByUsername(String username);

	@Query(value="SELECT * FROM USER u WHERE ROLE=?",nativeQuery=true)
	List<User> findByRoles(String role);
	
	@Query(value="SELECT * FROM USER u WHERE APPROVED=?",nativeQuery=true)
	List<Manager> findByStatus(boolean approved);
}