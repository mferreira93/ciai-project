package hotel.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(AllowedForBookingCreation.Condition)
public @interface AllowedForBookingCreation {
    String Condition = "hasRole('ROLE_GUEST')";
}