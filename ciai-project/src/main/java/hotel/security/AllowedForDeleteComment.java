package hotel.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(AllowedForDeleteComment.Condition)
public @interface AllowedForDeleteComment {
	
	String Condition = "@mySecurityService.canDeleteRoom(principal, #vars.comment.id) or hasRole('ROLE_MODERATOR')";

}
