package hotel.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(AllowedForCommentCreation.Condition)
public @interface AllowedForCommentCreation {

	String Condition = "@mySecurityService.canAnswerCreate(principal, #vars.comment.id) or " + AllowedForHotelCreation.Condition;
	String Condition2 = "@mySecurityService.canCommentCreate(principal,#vars.hotel.id) or " + AllowedForBookingCreation.Condition;
}
