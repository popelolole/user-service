package se.kthraven.userservice.Model;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.kthraven.userservice.Model.classes.CustomUserDetails;
import se.kthraven.userservice.Model.classes.Person;
import se.kthraven.userservice.Persistence.IUserPersistence;
import se.kthraven.userservice.Persistence.entities.UserDB;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    IUserPersistence userPersistence;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDB user = userPersistence.getUserByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("User not found with username: " + username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), true, authorities, Person.from(user.getPerson()));
    }

    public String getUserIdByPersonId(String personId){
        UserDB user = userPersistence.getUserByPersonId(personId);
        if(user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return user.getId();
    }

    public String login(String username, String password){
        CustomUserDetails user = (CustomUserDetails) loadUserByUsername(username);
        if(passwordEncoder.matches(password, user.getPassword())) {
            String token = user.getId() + ":" + user.getUsername();
            ArrayList<SimpleGrantedAuthority> authorities = (ArrayList<SimpleGrantedAuthority>) user.getAuthorities();
            token += ":" + authorities.get(0) + ":" + user.getPerson().getId();
            return token;
        }
        else
            throw new EntityNotFoundException("Invalid login information.");
    }

    public static Person getCurrentUserPerson(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails) authentication.getPrincipal()).getPerson();
    }
}
