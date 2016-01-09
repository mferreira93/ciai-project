package hotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hotel.models.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

	@Query(value="SELECT * FROM COMMENT m WHERE APPROVED=? ORDER BY hotel_id",nativeQuery=true)
	List<Comment> findByStatus(boolean approved);

	@Query(value="SELECT * FROM COMMENT u WHERE hotel_id=? AND approved=?",nativeQuery=true)
	List<Comment> findByHotel(long id, boolean approved);

	@Query(value="SELECT * FROM COMMENT u WHERE hotel_id=?",nativeQuery=true)
	List<Comment> findAllCommentsByHotel(long id);
	
	@Query(value="Select * FROM COMMENT u where approved=true and hotel_id in (SELECT id FROM HOTEL u WHERE manager_idUser=?)",nativeQuery=true)
	List<Comment> findByManager(long idUser);
}