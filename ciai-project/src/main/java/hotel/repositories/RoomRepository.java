package hotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hotel.models.Room;


public interface RoomRepository extends CrudRepository<Room, Long> {
	
	@Query(value="Select * from Room u where hotel_id=?",nativeQuery=true)
	List<Room> findByHotelId(long hotel_id);
}