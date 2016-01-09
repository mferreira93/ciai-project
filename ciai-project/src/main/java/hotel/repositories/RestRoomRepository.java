package hotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hotel.models.Room;

@RepositoryRestResource(collectionResourceRel="roomsautorest", path="roomsautorest")
public interface RestRoomRepository extends CrudRepository<Room, Long> {
	
	@Query(value="Select * from Room u where hotel_id=?",nativeQuery=true)
	List<Room> findByHotelId(long hotel_id);
}