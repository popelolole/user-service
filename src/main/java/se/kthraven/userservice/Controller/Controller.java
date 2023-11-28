package se.kthraven.userservice.Controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import se.kthraven.userservice.Model.CustomUserDetailsService;
import se.kthraven.userservice.Model.classes.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class Controller {

    @Autowired
    private CustomUserDetailsService userService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        String token = userService.login(username, password);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/user")
    public String getUserId(@RequestParam(value = "personId") String personId){
        return userService.getUserIdByPersonId(personId);
    }
}
