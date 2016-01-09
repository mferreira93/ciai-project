package hotel.security;

import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	 @Autowired
	 private UserDetailsService customUserDetailsService;

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder);
	}
//	
//	protected void configure(HttpSecurity http) throws Exception {
//		   http
//		       .httpBasic().and()
//		       .authorizeRequests()
//		         .antMatchers("/app", "/").permitAll()
//		         //.anyRequest().authenticated()
//		         .and()
//		         .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
//		         .csrf().csrfTokenRepository(csrfTokenRepository());
//		 }
//	 
//	 private CsrfTokenRepository csrfTokenRepository() {
//		    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//		    repository.setHeaderName("X-XSRF-TOKEN");
//		    return repository;
//		 }

	protected void configure(HttpSecurity http) throws Exception {
			    http
			    	.csrf().disable()
			        .authorizeRequests()
			        .antMatchers("/images/**").permitAll()
			        .antMatchers("/css/**").permitAll()
			        .antMatchers("/js/**").permitAll()
			        .antMatchers("/**").permitAll()
			        .antMatchers("/users/**").hasRole("ADMIN")
			        .antMatchers("/hotels/**").hasRole("MANAGER")
			        .antMatchers("/comments/**").hasRole("MODERATOR")
			        .antMatchers("/guests/**").hasRole("GUEST")
			        	.anyRequest().authenticated()
			        	.and()	        	
			        .formLogin()
			            .loginPage("/login")
			            .defaultSuccessUrl("/default")
			            .permitAll()
			            .and()
			        .logout()
			        	.logoutSuccessUrl("/")
			        	.permitAll();  
	}
}
